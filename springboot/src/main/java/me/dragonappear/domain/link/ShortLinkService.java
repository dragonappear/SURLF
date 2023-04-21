package me.dragonappear.domain.link;

import lombok.RequiredArgsConstructor;
import me.dragonappear.domain.main.exception.Custom4xxException;
import me.dragonappear.domain.main.exception.Custom5xxException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static me.dragonappear.domain.main.exception.CustomExceptionError.EXHAUSTED_SHORT_LINK;
import static me.dragonappear.domain.main.exception.CustomExceptionError.NOT_EXIST_SHORT_ID;


/**
 * This is responsible for the service layer, and transaction takes place in here.
 * <p>
 * <p>
 * - createShortUrl: query counts O(1)
 * <p>
 * - createRandomShortId: query counts O(1)
 */

@Service
@Transactional
@RequiredArgsConstructor
public class ShortLinkService {

    private final ShortLinkRepository shortLinkRepository;

    // TODO test about exception
    public ShortLinkEntity createShortLink(String url, String clientIp, String userAgent) {
        for (int i = 0; i < 5; i++) {
            String randomShortId = createRandomShortId();
            ShortLinkEntity newShortLinkEntity = ShortLinkEntity.createShortUrlEntity(url, randomShortId, clientIp, userAgent);
            Exception exceptionHolder = null;
            try {
                return shortLinkRepository.save(newShortLinkEntity);
            } catch (Exception e) {
                exceptionHolder = e;
            }
        }
        throw new Custom5xxException(EXHAUSTED_SHORT_LINK);
    }


    @Cacheable(value = "ShortLinkEntity", key = "#shortId", cacheManager = "cacheManager")
    public ShortLinkEntity getShortLink(String shortId) {
        return shortLinkRepository.findByShortId(shortId).orElseThrow(() -> new Custom4xxException(NOT_EXIST_SHORT_ID));
    }

    public String createRandomShortId() {

        for (int i = 3; i < 14; i++) {
            String shortId = UUID.randomUUID().toString().substring(0, i);

            if (shortId.contains("-")) {
                shortId.replace("-", "");
            }

            if (!shortLinkRepository.existsByShortId(shortId)) {
                return shortId;
            }
        }

        throw new Custom5xxException(EXHAUSTED_SHORT_LINK);
    }
}

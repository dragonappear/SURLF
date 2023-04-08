package me.dragonappear.domain.link;

import lombok.RequiredArgsConstructor;
import me.dragonappear.domain.main.exception.Custom5xxException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static me.dragonappear.domain.main.exception.CustomExceptionError.EXHAUSTED_SHORT_LINK;


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

    public ShortLinkEntity createShortUrl(String url) {

        Optional<ShortLinkEntity> byUrl = shortLinkRepository.findByOriginalUrl(url);

        if (byUrl.isEmpty()) {
            String randomShortId = createRandomShortId();
            ShortLinkEntity newShortLinkEntity = ShortLinkEntity.createShortUrlEntity(url, randomShortId);
            return shortLinkRepository.save(newShortLinkEntity);
        }

        return byUrl.get();
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

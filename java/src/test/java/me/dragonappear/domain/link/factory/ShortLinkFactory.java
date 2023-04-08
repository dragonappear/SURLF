package me.dragonappear.domain.link.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.dragonappear.domain.link.ShortLinkEntity;
import me.dragonappear.domain.link.ShortLinkRepository;
import me.dragonappear.domain.link.ShortLinkService;
import me.dragonappear.domain.link.request.ShortLinkCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShortLinkFactory {

    public static final String HOST = "https://www.airbridge.io";
    public static final String PATH = "/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8NPM%EC%9C%BC%EB%A1%9C%ED%94%84%EB%A1%A0%ED%8A%B8%EC%97%94%EB%93%9C%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC%EA%B4%80%EB%A6%AC";
    public static final String CLIENT_IP = "https://www.localhost.com";
    public static final String USER_AGENT = "LocalHost";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ShortLinkRepository shortLinkRepository;

    @Autowired
    ShortLinkService shortLinkService;

    public ShortLinkCreateRequest createShortLinkCreateRequest(String url) {
        ShortLinkCreateRequest request = new ShortLinkCreateRequest();
        request.setUrl(url);
        return request;
    }

    public String createShortLinkRequestToJson(String url) throws JsonProcessingException {
        ShortLinkCreateRequest request = createShortLinkCreateRequest(url);
        return objectMapper.writeValueAsString(request);
    }

    public ShortLinkEntity createShortLinkEntity(String url) {
        String shortId = UUID.randomUUID().toString().substring(0, 3);
        ShortLinkEntity shortUrlEntity = ShortLinkEntity.createShortUrlEntity(url, shortId, CLIENT_IP, USER_AGENT);
        return shortLinkRepository.save(shortUrlEntity);
    }

}

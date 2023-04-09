package me.dragonappear.domain.link.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Async
@Component
@Profile(value = {"local", "default"})
@RequiredArgsConstructor
public class ShortLinkKafkaEventListener {

    private final KafkaTemplate<Integer, String> template;
    private final ObjectMapper objectMapper;

    @EventListener
    public void handleShortLinkKafkaEvent(ShortLinkKafkaEvent shortLinkKafkaEvent) {
        try {
            String json = objectMapper.writeValueAsString(shortLinkKafkaEvent.shortLinkEntity());
            template.send("short-link.json", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}

package me.dragonappear.domain.link.event;

import me.dragonappear.domain.link.ShortLinkEntity;

public record ShortLinkKafkaEvent(ShortLinkEntity shortLinkEntity) {
}

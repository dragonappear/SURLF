package me.dragonappear.domain.link.dto;

import lombok.Data;
import me.dragonappear.domain.link.ShortLinkEntity;

import static me.dragonappear.domain.link.util.DateTimeFormat.getDateTime;


@Data
public class ShortLinkDto {

    private String shortId;
    private String url;
    private String createdAt;

    public ShortLinkDto(ShortLinkEntity shortLinkEntity) {
        this.shortId = shortLinkEntity.getShortId();
        this.url = shortLinkEntity.getOriginalUrl();
        this.createdAt = getDateTime(shortLinkEntity.getCreatedAt());
    }

}

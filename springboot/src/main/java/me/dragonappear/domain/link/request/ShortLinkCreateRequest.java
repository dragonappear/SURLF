package me.dragonappear.domain.link.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ShortLinkCreateRequest {

    @NotBlank(message = "url cannot be empty")
    private String url;

}

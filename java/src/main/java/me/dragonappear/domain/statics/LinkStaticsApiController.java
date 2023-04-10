package me.dragonappear.domain.statics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.dragonappear.domain.link.ShortLinkService;
import me.dragonappear.domain.main.response.ApiResponse;
import me.dragonappear.domain.statics.dto.LinkStaticsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LinkStaticsApiController {

    private final LinkStaticsService linkStaticsService;
    private final ShortLinkService shortLinkService;

    @GetMapping("/statics/{shortId}")
    public ResponseEntity<ApiResponse> search(@PathVariable("shortId") String shortId) throws IOException {
        shortLinkService.getShortLink(shortId);

        List<LinkStaticsDto> dtos = linkStaticsService.getStaticsPerDay(shortId);
        ApiResponse apiResponse = new ApiResponse(dtos);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}

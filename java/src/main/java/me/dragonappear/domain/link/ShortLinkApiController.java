package me.dragonappear.domain.link;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.dragonappear.domain.link.dto.ShortLinkDto;
import me.dragonappear.domain.link.request.ShortLinkCreateRequest;
import me.dragonappear.domain.link.validator.UrlValidator;
import me.dragonappear.domain.main.response.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * This is MVC pattern.
 * <p>
 * This controller is responsible for API entry points.
 * <p>
 * <p>
 * - POST /short-links
 * <p>
 * - GET /short-links/{short_id}
 * <p>
 * - GET /r/{short_id}
 */

@RestController
@RequiredArgsConstructor
public class ShortLinkApiController {

    private final ShortLinkService shortLinkService;
    private final UrlValidator urlValidator;

    @PostMapping("/short-links")
    public ResponseEntity<ApiResponse> createShortLink(@RequestBody @Valid ShortLinkCreateRequest shortLinkCreateRequest, HttpServletRequest httpServletRequest) {
        urlValidator.validate(shortLinkCreateRequest.getUrl());

        String ipAddress = httpServletRequest.getRemoteAddr();
        String userAgent = httpServletRequest.getHeader("User-Agent");

        ShortLinkEntity shortLinkEntity = shortLinkService.createShortLink(shortLinkCreateRequest.getUrl(), ipAddress, userAgent);
        ApiResponse apiResponse = new ApiResponse(new ShortLinkDto(shortLinkEntity));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/short-links/{short_id}")
    public ResponseEntity<ApiResponse> getShortLink(@PathVariable(name = "short_id") String shortId) {
        ShortLinkEntity shortLinkEntity = shortLinkService.getShortLink(shortId);
        ApiResponse apiResponse = new ApiResponse(new ShortLinkDto(shortLinkEntity));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/r/{short_id}")
    public ResponseEntity<ApiResponse> redirectToOriginalUrl(@PathVariable(name = "short_id") String shortId) {
        ShortLinkEntity shortLinkEntity = shortLinkService.getShortLink(shortId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", shortLinkEntity.getOriginalUrl());
        return new ResponseEntity<>(headers, HttpStatus.MOVED_TEMPORARILY);
    }

}

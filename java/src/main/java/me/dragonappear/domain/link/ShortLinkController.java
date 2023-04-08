package me.dragonappear.domain.link;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.dragonappear.domain.link.dto.ShortLinkDto;
import me.dragonappear.domain.link.request.ShortLinkCreateRequest;
import me.dragonappear.domain.link.validator.UrlValidator;
import me.dragonappear.domain.main.exception.Custom4xxException;
import me.dragonappear.domain.main.response.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static me.dragonappear.domain.main.exception.CustomExceptionError.NOT_EXIST_SHORT_ID;


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
public class ShortLinkController {

    private final ShortLinkService shortLinkService;
    private final ShortLinkRepository shortLinkRepository;
    private final UrlValidator urlValidator;

    @PostMapping("/short-links")
    public ResponseEntity<ApiResponse> createShortLink(@RequestBody @Valid ShortLinkCreateRequest request) {
        urlValidator.validate(request.getUrl());
        ShortLinkEntity shortLinkEntity = shortLinkService.createShortUrl(request.getUrl());
        ApiResponse apiResponse = new ApiResponse(new ShortLinkDto(shortLinkEntity));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/short-links/{short_id}")
    public ResponseEntity<ApiResponse> getShortLink(@PathVariable(name = "short_id") String shortId) {
        ShortLinkEntity shortLinkEntity = shortLinkRepository.findByShortId(shortId).orElseThrow(() -> new Custom4xxException(NOT_EXIST_SHORT_ID));
        ApiResponse apiResponse = new ApiResponse(new ShortLinkDto(shortLinkEntity));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/r/{short_id}")
    public ResponseEntity<ApiResponse> redirectToOriginalUrl(@PathVariable(name = "short_id") String shortId) {
        ShortLinkEntity shortLinkEntity = shortLinkRepository.findByShortId(shortId).orElseThrow(() -> new Custom4xxException(NOT_EXIST_SHORT_ID));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", shortLinkEntity.getOriginalUrl());
        return new ResponseEntity<>(headers, HttpStatus.MOVED_TEMPORARILY);
    }

}

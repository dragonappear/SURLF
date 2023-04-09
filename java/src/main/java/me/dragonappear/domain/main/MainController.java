package me.dragonappear.domain.main;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.dragonappear.domain.link.ShortLinkRepository;
import me.dragonappear.domain.link.dto.ShortLinkDto;
import me.dragonappear.domain.main.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final ShortLinkRepository shortLinkRepository;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> index(HttpServletRequest httpServletRequest) {

        String ipAddress = httpServletRequest.getRemoteAddr();
        String userAgent = httpServletRequest.getHeader("User-Agent");

        List<ShortLinkDto> shortLinks = shortLinkRepository.findByClientIpAndUserAgent(ipAddress, userAgent)
                .stream()
                .map(ShortLinkDto::new)
                .toList();

        ApiResponse apiResponse = new ApiResponse(shortLinks);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}

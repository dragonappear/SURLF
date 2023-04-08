package me.dragonappear.domain.main;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.dragonappear.domain.link.ShortLinkRepository;
import me.dragonappear.domain.link.ShortLinkService;
import me.dragonappear.domain.link.dto.ShortLinkDto;
import me.dragonappear.domain.link.request.ShortLinkCreateRequest;
import me.dragonappear.domain.link.validator.UrlValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final ShortLinkService shortLinkService;
    private final ShortLinkRepository shortLinkRepository;
    private final UrlValidator urlValidator;

    @GetMapping("/")
    public String index(Model model, HttpServletRequest httpServletRequest) {

        String ipAddress = httpServletRequest.getRemoteAddr();
        String userAgent = httpServletRequest.getHeader("User-Agent");

        List<ShortLinkDto> shortLinks = shortLinkRepository.findByClientIpAndUserAgent(ipAddress, userAgent)
                .stream()
                .map(ShortLinkDto::new)
                .toList();

        model.addAttribute("shortLinkCreateRequest", new ShortLinkCreateRequest());
        model.addAttribute("shortLinks", shortLinks);

        return "index";
    }

    @PostMapping("/")
    public String shortLinkRequest(ShortLinkCreateRequest shortLinkCreateRequest, HttpServletRequest httpServletRequest) {
        urlValidator.validate(shortLinkCreateRequest.getUrl());

        String ipAddress = httpServletRequest.getRemoteAddr();
        String userAgent = httpServletRequest.getHeader("User-Agent");

        shortLinkService.createShortUrl(shortLinkCreateRequest.getUrl(), ipAddress, userAgent);
        return "redirect:/";
    }

}

package me.dragonappear.domain.link.validator;

import me.dragonappear.domain.main.exception.Custom4xxException;
import org.springframework.stereotype.Component;

import static me.dragonappear.domain.main.exception.CustomExceptionError.INVALID_URL_FORMAT;


@Component
public class UrlValidator {

    private static final String URL_REGEX = "(https?:\\/\\/)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";

    public void validate(String url) {
        if (!url.matches(URL_REGEX)) {
            throw new Custom4xxException(INVALID_URL_FORMAT);
        }
    }

}

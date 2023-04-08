package me.dragonappear.domain.link;

import me.dragonappear.domain.link.factory.ShortLinkFactory;
import me.dragonappear.domain.link.request.ShortLinkCreateRequest;
import me.dragonappear.domain.link.validator.UrlValidator;
import me.dragonappear.domain.main.exception.Custom5xxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static me.dragonappear.domain.main.exception.CustomExceptionError.EXHAUSTED_SHORT_LINK;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ShortLinkApiControllerMockTest {

    @InjectMocks
    private ShortLinkApiController shortLinkApiController;

    @Mock
    private ShortLinkService shortLinkService;

    @Mock
    private UrlValidator urlValidator;

    @Spy
    private ShortLinkFactory shortLinkFactory;

    @Test
    @DisplayName("Random shortId가 고갈되면 예외 발생")
    void when_random_shortId_exhausted_throws_Custom5xxException() throws Exception {

        //given
        ShortLinkCreateRequest request = shortLinkFactory.createShortLinkCreateRequest(ShortLinkFactory.HOST);
        Mockito.doNothing().when(urlValidator).validate(any());
        Mockito.doThrow(new Custom5xxException(EXHAUSTED_SHORT_LINK)).when(shortLinkService).createShortUrl(any(), any(), any());

        //when
        assertThrows(Custom5xxException.class, () -> shortLinkApiController.createShortLink(request, new MockHttpServletRequest()));
    }


}

package me.dragonappear.domain.link;

import me.dragonappear.domain.link.ShortLinkRepository;
import me.dragonappear.domain.link.ShortLinkService;
import me.dragonappear.domain.main.exception.Custom5xxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ShortLinkServiceMockTest {

    @InjectMocks
    private ShortLinkService shortLinkService;

    @Mock
    private ShortLinkRepository shortLinkRepository;

    @Test
    @DisplayName("Random shortId가 고갈되면 예외 발생")
    void when_random_shortId_exhausted_throws_Custom5xxException() throws Exception {

        //given
        Mockito.doReturn(true).when(shortLinkRepository).existsByShortId(any());

        //when
        assertThrows(Custom5xxException.class, () -> shortLinkService.createRandomShortId());
    }
}

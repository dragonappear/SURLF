package me.dragonappear.domain.link;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.dragonappear.domain.link.factory.ShortLinkFactory;
import me.dragonappear.infra.MockMvcTest;
import lombok.extern.slf4j.Slf4j;
import me.dragonappear.domain.main.exception.Custom4xxException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;
import java.util.stream.Stream;


import static me.dragonappear.domain.link.util.DateTimeFormat.getDateTime;
import static me.dragonappear.domain.main.exception.CustomExceptionError.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This test is for integration test for ShortLinkController
 */

@Slf4j
@MockMvcTest
class ShortLinkControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ShortLinkRepository shortLinkRepository;

    @Autowired
    ShortLinkFactory shortLinkFactory;

    static Stream<String> validUrlsProvider() throws IOException {
        Resource resource = new ClassPathResource("url_valid.csv");
        return Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8).stream();
    }

    static Stream<String> invalidUrlsProvider() throws IOException {
        Resource resource = new ClassPathResource("url_invalid.csv");
        return Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8).stream();
    }

    @ParameterizedTest
    @MethodSource("validUrlsProvider")
    @DisplayName("Short Link 생성 - 정상 URL")
    void create_short_link_with_correct_request(String url) throws Exception {

        //given
        String json = shortLinkFactory.createShortLinkRequestToJson(url);

        //when
        mockMvc.perform(post("/short-links")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.url").value(url))
                .andExpect(jsonPath("$.data.shortId").exists())
                .andExpect(jsonPath("$.data.createdAt").exists())
                .andDo(print());

        //then
        assertThatShortLinkCreated(url);
    }

    @ParameterizedTest
    @MethodSource("invalidUrlsProvider")
    @DisplayName("Short Link 생성 - 오류 URL")
    void create_short_link_with_wrong_request(String url) throws Exception {

        //given
        String json = shortLinkFactory.createShortLinkRequestToJson(url);

        //when
        mockMvc.perform(post("/short-links")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception ex = result.getResolvedException();
                    assertNotNull(ex);
                    assertEquals(Custom4xxException.class, ex.getClass());
                })
                .andExpect(jsonPath("$.errorCode").value(INVALID_URL_FORMAT.getErrorCode()))
                .andExpect(jsonPath("$.errorMsg").value(INVALID_URL_FORMAT.getErrorMsg()))
                .andDo(print());
    }

    @Test
    @DisplayName("Short Link 생성 - url 없이 요청")
    void create_short_link_with_empty_request() throws Exception {

        //when
        mockMvc.perform(post("/short-links"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception ex = result.getResolvedException();
                    assertNotNull(ex);
                    assertEquals(HttpMessageNotReadableException.class, ex.getClass());
                })
                .andExpect(jsonPath("$.errorCode").value(NOT_VALID_ARGUMENT.getErrorCode()))
                .andExpect(jsonPath("$.errorMsg").value(NOT_VALID_ARGUMENT.getErrorMsg()))
                .andDo(print());
    }

    @Test
    @DisplayName("Short Link 조회 - 존재하는 ShortId")
    void get_short_link_with_correct_path_variable() throws Exception {

        //given
        ShortLinkEntity shortLinkEntity = shortLinkFactory.createShortLinkEntity(ShortLinkFactory.HOST + ShortLinkFactory.PATH);

        //when
        mockMvc.perform(get("/short-links/{short_id}", shortLinkEntity.getShortId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.url").value(ShortLinkFactory.HOST + ShortLinkFactory.PATH))
                .andExpect(jsonPath("$.data.shortId").value(shortLinkEntity.getShortId()))
                .andExpect(jsonPath("$.data.createdAt").value(getDateTime(shortLinkEntity.getCreatedAt())))
                .andDo(print());
    }

    @Test
    @DisplayName("Short Link 조회 - 존재하지 않는 ShortId")
    void get_short_link_with_wrong_path_variable() throws Exception {

        //when
        mockMvc.perform(get("/short-links/{short_id}", String.valueOf(-1)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception ex = result.getResolvedException();
                    assertNotNull(ex);
                    assertEquals(Custom4xxException.class, ex.getClass());
                })
                .andExpect(jsonPath("$.errorCode").value(NOT_EXIST_SHORT_ID.getErrorCode()))
                .andExpect(jsonPath("$.errorMsg").value(NOT_EXIST_SHORT_ID.getErrorMsg()))
                .andDo(print());
    }

    @Test
    @DisplayName("리다이렉트 to origin URL - 존재하는 ShortId")
    void redirect_to_original_url_with_correct_path_variable() throws Exception {

        //given
        ShortLinkEntity shortLinkEntity = shortLinkFactory.createShortLinkEntity(ShortLinkFactory.HOST + ShortLinkFactory.PATH);

        //when
        mockMvc.perform(get("/r/{short_id}", shortLinkEntity.getShortId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", shortLinkEntity.getOriginalUrl()))
                .andExpect(redirectedUrl(shortLinkEntity.getOriginalUrl()))
                .andDo(print());
    }

    @Test
    @DisplayName("리다이렉트 to origin URL - 존재하지 않는 ShortId")
    void redirect_to_original_url_with_wrong_path_variable() throws Exception {

        //when
        mockMvc.perform(get("/r/{short_id}", String.valueOf(-1)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception ex = result.getResolvedException();
                    assertNotNull(ex);
                    assertEquals(Custom4xxException.class, ex.getClass());
                })
                .andExpect(jsonPath("$.errorCode").value(NOT_EXIST_SHORT_ID.getErrorCode()))
                .andExpect(jsonPath("$.errorMsg").value(NOT_EXIST_SHORT_ID.getErrorMsg()))
                .andDo(print());
    }

    private void assertThatShortLinkCreated(String url) {
        Optional<ShortLinkEntity> byOriginalUrl = shortLinkRepository.findByOriginalUrl(url);
        Assertions.assertThat(byOriginalUrl).isPresent();

        ShortLinkEntity shortLinkEntity = byOriginalUrl.get();

        Assertions.assertThat(shortLinkEntity.getOriginalUrl()).isEqualTo(url);
        Assertions.assertThat(shortLinkEntity.getShortId().length()).isBetween(2, 13);
    }

}
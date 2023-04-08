package me.dragonappear.domain.link.docs;

import me.dragonappear.domain.link.ShortLinkEntity;
import me.dragonappear.domain.link.ShortLinkRepository;
import me.dragonappear.domain.link.factory.ShortLinkFactory;
import me.dragonappear.infra.RestDocs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RestDocs
public class ShortLinkApiControllerRestDocs {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ShortLinkFactory shortLinkFactory;

    @Autowired
    private ShortLinkRepository shortLinkRepository;

    @AfterEach
    void tearDown() {
        shortLinkRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /short-links DOCS")
    void post_short_links_docs() throws Exception {

        String json = shortLinkFactory.createShortLinkRequestToJson(ShortLinkFactory.HOST);

        mockMvc.perform(post("/short-links")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("post-short-links-success",
                        requestFields(
                                fieldWithPath("url").description("URL to compress").attributes(key("constraint").value("Enter a valid URL"))),
                        responseFields(
                                fieldWithPath("data.url").description("URL in the request"),
                                fieldWithPath("data.shortId").description("Short link id"),
                                fieldWithPath("data.createdAt").description("Time the short link created"))));

        json = shortLinkFactory.createShortLinkRequestToJson("https://www.airbr▩idge.io");

        mockMvc.perform(post("/short-links")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("post-short-links-failure",
                        responseFields(
                                fieldWithPath("errorCode").description("4002"),
                                fieldWithPath("errorMsg").description("INVALID_URL_FORMAT"))));
    }

    @Test
    @DisplayName("GET /short-links/{short_id} DOCS")
    void get_short_links_short_id_docs() throws Exception {

        ShortLinkEntity shortLinkEntity = shortLinkFactory.createShortLinkEntity(ShortLinkFactory.HOST);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/short-links/{short_id}", shortLinkEntity.getShortId())
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-short-links-success",
                        pathParameters(
                                parameterWithName("short_id").description("shortId")
                        ),
                        responseFields(
                                fieldWithPath("data.url").description("ShortLink's url"),
                                fieldWithPath("data.shortId").description("ShortLink's shortId"),
                                fieldWithPath("data.createdAt").description("Time ShortLink created"))));

        mockMvc.perform(get("/short-links/{short_id}", String.valueOf(-1)))
                .andExpect(status().isBadRequest())
                .andDo(document("get-short-links-failure",
                        responseFields(
                                fieldWithPath("errorCode").description("4001"),
                                fieldWithPath("errorMsg").description("NOT_EXIST_SHORT_ID"))));
    }

    @Test
    @DisplayName("GET /r/{short_id} DOCS")
    void get_r_short_id_docs() throws Exception {

        ShortLinkEntity shortLinkEntity = shortLinkFactory.createShortLinkEntity(ShortLinkFactory.HOST);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/r/{short_id}", shortLinkEntity.getShortId())
                        .accept(APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(shortLinkEntity.getOriginalUrl()))
                .andDo(document("redirect-short-links-success"));

        mockMvc.perform(get("/short-links/{short_id}", String.valueOf(-1)))
                .andExpect(status().isBadRequest())
                .andDo(document("redirect-short-links-failure",
                        responseFields(
                                fieldWithPath("errorCode").description("4001"),
                                fieldWithPath("errorMsg").description("NOT_EXIST_SHORT_ID"))));
    }

}

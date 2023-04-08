package me.dragonappear.domain.link;


import me.dragonappear.domain.link.factory.ShortLinkFactory;
import lombok.extern.slf4j.Slf4j;
import me.dragonappear.domain.link.request.ShortLinkCreateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;
import java.util.regex.Pattern;


/**
 * This test is for ShortLinkService which is responsible for service layer.
 * <p>
 * ShortLinkService creates ShortLinkEntity and Random ShortId.
 * <p>
 * <p>
 * Test: ShortLinkService.createShortUrl with diverse request
 * <p>
 * Test: ShortLinkService.createRandomShortId with 1_000 times
 */

@Slf4j
@Rollback
@SpringBootTest
class ShortLinkServiceTest {

    @Autowired
    ShortLinkFactory shortLinkFactory;

    @Autowired
    ShortLinkRepository shortLinkRepository;

    @Autowired
    ShortLinkService shortLinkService;

    @AfterEach
    void tearDown() {
        shortLinkRepository.deleteAll();
    }

    @Test
    @DisplayName("Short Link Entity 생성 - 새로운 URL")
    void create_short_link_with_correct_request() throws Exception {

        // given
        ShortLinkCreateRequest request = shortLinkFactory.createShortLinkCreateRequest(ShortLinkFactory.HOST + ShortLinkFactory.PATH);

        // when
        ShortLinkEntity newShortLinkEntity = shortLinkService.createShortUrl(request.getUrl());

        // then
        assertThatShortLinkCreated(newShortLinkEntity);
    }

    @ParameterizedTest
    @ValueSource(strings = {ShortLinkFactory.HOST + ShortLinkFactory.PATH, "https://www.google.com" + ShortLinkFactory.PATH, "https://www.naver.com" + ShortLinkFactory.PATH, "https://chat.openai.com" + ShortLinkFactory.PATH})
    @DisplayName("Short Link Entity 조회만, 새롭게 생성X - 존재하는 URL")
    void create_short_link_with_wrong_request(String url) throws Exception {

        // given
        ShortLinkCreateRequest request = shortLinkFactory.createShortLinkCreateRequest(url);
        ShortLinkEntity prevShortLinkEntity = shortLinkFactory.createShortLinkEntity(url);

        // when
        ShortLinkEntity newShortLinkEntity = shortLinkService.createShortUrl(request.getUrl());

        // then
        assertThatShortLinkNotCreated(prevShortLinkEntity, newShortLinkEntity);
    }

    @RepeatedTest(1_000)
    @DisplayName("Random Short Id(숫자,문자만 포함 + length 3이상 13이하) 생성 - 1000번 반복 수행")
    void create_random_short_id() throws Exception {

        //when
        String randomShortId = shortLinkService.createRandomShortId();
        log.info("Created shortId : {}", randomShortId);

        //then
        Assertions.assertThat(randomShortId.length()).isBetween(3, 13);
        Assertions.assertThat(randomShortId).containsPattern(Pattern.compile("^[0-9a-f]+")); // Check to include only numeric characters.
        Assertions.assertThat(randomShortId).doesNotContainPattern(Pattern.compile("^[^0-9a-f]+")); // Check to include only numeric characters.
    }

    private void assertThatShortLinkNotCreated(ShortLinkEntity prevShortLinkEntity, ShortLinkEntity newShortLinkEntity) {
        Assertions.assertThat(prevShortLinkEntity).isEqualTo(newShortLinkEntity);
        Assertions.assertThat(prevShortLinkEntity.getShortId()).isEqualTo(newShortLinkEntity.getShortId());
        Assertions.assertThat(prevShortLinkEntity.getOriginalUrl()).isEqualTo(newShortLinkEntity.getOriginalUrl());
    }

    private void assertThatShortLinkCreated(ShortLinkEntity newShortLinkEntity) {
        Optional<ShortLinkEntity> byShortId = shortLinkRepository.findByShortId(newShortLinkEntity.getShortId());
        Assertions.assertThat(byShortId).isPresent();
        ShortLinkEntity shortLinkEntity = byShortId.get();
        Assertions.assertThat(shortLinkEntity).isEqualTo(newShortLinkEntity);
        Assertions.assertThat(shortLinkEntity.getOriginalUrl()).isEqualTo(newShortLinkEntity.getOriginalUrl());
        Assertions.assertThat(shortLinkEntity.getShortId()).isEqualTo(newShortLinkEntity.getShortId());
    }

}
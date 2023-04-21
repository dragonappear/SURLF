package me.dragonappear.domain.link;

import me.dragonappear.domain.link.factory.ShortLinkFactory;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;


/**
 * This test is for ShortLinkRepository which is responsible for repository layer.
 * <p>
 * Test only for method name query.
 * <p>
 * <p>
 * - Test: ShortLinkRepository.findByOriginalUrl(String url)
 * <p>
 * - Test: ShortLinkRepository.findByShortId(String shortId)
 * <p>
 * - Test: ShortLinkRepository.existsByShortId(String shortId)
 */


@Slf4j
@Rollback
@SpringBootTest
class ShortLinkRepositoryTest {

    @Autowired
    ShortLinkRepository shortLinkRepository;

    @Autowired
    ShortLinkFactory shortLinkFactory;

    @AfterEach
    void tearDown() {
        shortLinkRepository.deleteAll();
    }

    @Test
    @DisplayName("findByOriginalUrl 쿼리 테스트 - 존재하는 URL로 조회")
    void find_by_original_url_with_existing_url() throws Exception {

        //given
        ShortLinkEntity newShortLinkEntity = shortLinkFactory.createShortLinkEntity(ShortLinkFactory.HOST + ShortLinkFactory.PATH);

        //when
        Optional<ShortLinkEntity> byOriginalUrl = shortLinkRepository.findByOriginalUrl(newShortLinkEntity.getOriginalUrl());

        //then
        assertThatEqualEntity(newShortLinkEntity, byOriginalUrl);
    }

    @Test
    @DisplayName("findByOriginalUrl 쿼리 테스트 - 존재하지 않는 URL로 조회")
    void find_by_original_url_with_non_existing_url() throws Exception {

        //when
        Optional<ShortLinkEntity> byOriginalUrl = shortLinkRepository.findByOriginalUrl(ShortLinkFactory.HOST + ShortLinkFactory.PATH);

        //then
        Assertions.assertThat(byOriginalUrl).isEmpty();
    }

    @Test
    @DisplayName("findByShortId 쿼리 테스트 - 존재하는 shortId로 조회")
    void find_by_short_id_with_existing_short_id() throws Exception {

        //given
        ShortLinkEntity newShortLinkEntity = shortLinkFactory.createShortLinkEntity(ShortLinkFactory.HOST + ShortLinkFactory.PATH);

        //when
        Optional<ShortLinkEntity> byOriginalUrl = shortLinkRepository.findByShortId(newShortLinkEntity.getShortId());

        //then
        assertThatEqualEntity(newShortLinkEntity, byOriginalUrl);
    }

    @Test
    @DisplayName("findByShortId 쿼리 테스트 - 존재하지 않는 shortId로 조회")
    void find_by_short_id_with_non_existing_short_id() throws Exception {

        //when
        Optional<ShortLinkEntity> byShortId = shortLinkRepository.findByShortId(String.valueOf(-1));

        //then
        Assertions.assertThat(byShortId).isEmpty();
    }

    @Test
    @DisplayName("existsByShortId 쿼리 테스트 - 존재하는 shortId로 조회")
    void exists_by_short_id_with_existing_short_id() throws Exception {

        //given
        ShortLinkEntity newShortLinkEntity = shortLinkFactory.createShortLinkEntity(ShortLinkFactory.HOST + ShortLinkFactory.PATH);

        //then
        Assertions.assertThat(shortLinkRepository.existsByShortId(newShortLinkEntity.getShortId())).isTrue();
    }

    @Test
    @DisplayName("existsByShortId 쿼리 테스트 - 존재하지 않는 shortId로 조회")
    void exists_by_short_id_with_non_existing_short_id() throws Exception {

        //then
        Assertions.assertThat(shortLinkRepository.existsByShortId(String.valueOf(-1))).isFalse();

    }

    private void assertThatEqualEntity(ShortLinkEntity newShortLinkEntity, Optional<ShortLinkEntity> byOriginalUrl) {
        Assertions.assertThat(byOriginalUrl).isPresent();

        ShortLinkEntity shortLinkEntity = byOriginalUrl.get();

        Assertions.assertThat(shortLinkEntity).isEqualTo(newShortLinkEntity);
        Assertions.assertThat(shortLinkEntity.getShortId()).isEqualTo(newShortLinkEntity.getShortId());
        Assertions.assertThat(shortLinkEntity.getOriginalUrl()).isEqualTo(newShortLinkEntity.getOriginalUrl());
    }
}
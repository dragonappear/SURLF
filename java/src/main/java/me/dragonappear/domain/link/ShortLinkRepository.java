package me.dragonappear.domain.link;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * This layer is responsible for the repository layer and for data CRUD.
 */

@Transactional(readOnly = true)
public interface ShortLinkRepository extends JpaRepository<ShortLinkEntity, Long> {

    Optional<ShortLinkEntity> findByOriginalUrl(String url);

    @QueryHints(value = @QueryHint(name = "org.hibernate.annotations.QueryHints.USE_INDEXES", value = "uk_short_id"))
    Optional<ShortLinkEntity> findByShortId(String shortId);

    @QueryHints(value = @QueryHint(name = "org.hibernate.annotations.QueryHints.USE_INDEXES", value = "idx_client_info"))
    List<ShortLinkEntity> findByClientIpAndUserAgent(String clientIp, String userAgent);

    @QueryHints(value = @QueryHint(name = "org.hibernate.annotations.QueryHints.USE_INDEXES", value = "uk_short_id"))
    boolean existsByShortId(String shortId);
}

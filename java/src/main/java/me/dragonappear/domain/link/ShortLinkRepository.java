package me.dragonappear.domain.link;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * This layer is responsible for the repository layer and for data CRUD.
 */

@Transactional(readOnly = true)
public interface ShortLinkRepository extends JpaRepository<ShortLinkEntity, Long> {

    Optional<ShortLinkEntity> findByOriginalUrl(String url);

    Optional<ShortLinkEntity> findByShortId(String shortId);

    boolean existsByShortId(String shortId);
}

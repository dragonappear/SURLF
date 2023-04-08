package me.dragonappear.domain.link;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * This is an entity layer corresponding to the domain.
 */

@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "short_link")
public class ShortLinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_short_link", nullable = false)
    private Long id;

    @Size(min = 3)
    @Column(name = "short_id", nullable = false, unique = true)
    private String shortId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "original_url", nullable = false, columnDefinition = "TEXT")
    private String originalUrl;

    public static ShortLinkEntity createShortUrlEntity(String originalUrl, String shortId) {
        return ShortLinkEntity.builder()
                .shortId(shortId)
                .originalUrl(originalUrl)
                .createdAt(LocalDateTime.now())
                .build();
    }

}

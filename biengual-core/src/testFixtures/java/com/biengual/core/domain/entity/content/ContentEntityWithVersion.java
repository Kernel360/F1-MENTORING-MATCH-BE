package com.biengual.core.domain.entity.content;

import com.biengual.core.domain.entity.BaseEntity;
import com.biengual.core.domain.entity.category.CategoryEntity;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.enums.ContentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "content_with_version")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentEntityWithVersion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String url;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String title;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int hits;

    @Column(columnDefinition = "varchar(255)")
    private String thumbnailUrl;

    @Size(max = 255)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String preScripts;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentStatus contentStatus = ContentStatus.ACTIVATED;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(255)")
    private String mongoContentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private CategoryEntity category;

    @Column(name = "num_of_quiz", nullable = false, columnDefinition = "bigint")
    private Integer numOfQuiz;

    @Version
    private Integer version;

    public void updateHits() {
        this.hits += 1;
    }
}

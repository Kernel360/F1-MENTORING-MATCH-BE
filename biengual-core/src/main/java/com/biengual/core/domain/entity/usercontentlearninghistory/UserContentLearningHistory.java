package com.biengual.core.domain.entity.usercontentlearninghistory;

import com.biengual.core.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_content_learning_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserContentLearningHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long userId;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long contentId;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal learningRate;

    @Column(nullable = false)
    private LocalDateTime learningStartTime;
}

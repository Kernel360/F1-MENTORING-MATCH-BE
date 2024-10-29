package com.biengual.userapi.core.domain.entity.bookmark;

import com.biengual.userapi.core.domain.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "bookmark")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, columnDefinition = "bigint")
	private Long scriptIndex;

	@Column(columnDefinition = "bigint")
	private Long sentenceIndex;

	@Column(columnDefinition = "varchar(512)")
	private String detail;

	@Column(columnDefinition = "varchar(255)")
	private String description;

	@Column(nullable = true, columnDefinition = "double")
	private Double startTimeInSecond;

	@Column(name = "user_id", nullable = false, columnDefinition = "bigint")
	private Long userId;

	@Builder
	public BookmarkEntity(
		@NotNull Long scriptIndex, Long sentenceIndex,
		String detail, String description, Double startTimeInSecond, Long userId
	) {
		this.scriptIndex = scriptIndex;
		this.sentenceIndex = sentenceIndex;
		this.detail = detail;
		this.description = description;
		this.startTimeInSecond = startTimeInSecond;
		this.userId = userId;
	}

	public void updateDescription(String description) {
		this.description = description;
	}
}

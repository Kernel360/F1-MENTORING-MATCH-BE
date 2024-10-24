package com.biengual.userapi.bookmark.domain;

import com.biengual.userapi.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

	@Builder
	public BookmarkEntity(
		@NotNull Long scriptIndex, Long sentenceIndex,
		String detail, String description, Double startTimeInSecond
	) {
		this.scriptIndex = scriptIndex;
		this.sentenceIndex = sentenceIndex;
		this.detail = detail;
		this.description = description;
		this.startTimeInSecond = startTimeInSecond;
	}

	public void updateDescription(String description) {
		this.description = description;
	}
}

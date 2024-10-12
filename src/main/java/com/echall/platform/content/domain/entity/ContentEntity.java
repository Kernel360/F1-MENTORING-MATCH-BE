package com.echall.platform.content.domain.entity;

import com.echall.platform.category.domain.entity.CategoryEntity;
import com.echall.platform.content.domain.dto.ContentRequestDto;
import com.echall.platform.content.domain.enums.ContentStatus;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.user.domain.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "content")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentEntity extends BaseEntity {
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

	@Builder
	public ContentEntity(
		String url, String title, String thumbnailUrl,
		ContentType contentType, String mongoContentId,
		List<Script> preScripts, CategoryEntity category
	) {
		this.url = url;
		this.title = title;
		this.thumbnailUrl = thumbnailUrl;
		this.contentType = contentType;
		this.mongoContentId = mongoContentId;
		this.preScripts = truncate(
			preScripts.subList(0, 5)
				.stream()
				.map(Script::getEnScript)
				.toList().toString()
			, 255
		);
		this.category = category;
	}

	public void update(ContentRequestDto.ContentUpdateRequestDto dto) {
		this.url = dto.url();
		this.title = dto.title();
		this.contentStatus = dto.contentStatus() == null ? ContentStatus.ACTIVATED : dto.contentStatus();
		this.preScripts = truncate(
			dto.script().subList(0, 5)
				.stream()
				.map(Script::getEnScript)
				.toList().toString()
			, 255);
	}

	public void updateStatus(ContentStatus contentStatus) {
		this.contentStatus = contentStatus == null ? ContentStatus.ACTIVATED : contentStatus;
	}

	private String truncate(String content, int maxLength) {
		return content.length() > maxLength ? content.substring(0, maxLength) : content;
	}
}

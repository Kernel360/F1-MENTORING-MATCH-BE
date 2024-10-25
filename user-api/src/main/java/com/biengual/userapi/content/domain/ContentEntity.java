package com.biengual.userapi.content.domain;

import com.biengual.userapi.category.domain.entity.CategoryEntity;
import com.biengual.userapi.content.presentation.ContentRequestDto;
import com.biengual.userapi.script.domain.entity.Script;
import com.biengual.userapi.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "content")
@Getter
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
			preScripts.subList(0, Math.min(preScripts.size(), 5))
				.stream()
				.map(Script::getEnScript)
				.toList().toString()
			, 255
		);
		this.category = category;
	}

	public void update(ContentCommand.Modify command) {
		this.url = command.url();
		this.title = command.title();
		this.contentStatus = command.contentStatus() == null ? ContentStatus.ACTIVATED : command.contentStatus();
		this.preScripts = truncate(
			command.script().subList(0, Math.min(command.script().size(), 5))
				.stream()
				.map(Script::getEnScript)
				.toList().toString()
			, 255);
	}

	public void updateStatus(ContentStatus contentStatus) {
		this.contentStatus = contentStatus == null ? ContentStatus.ACTIVATED : contentStatus;
	}

	private String truncate(String content, int maxLength) {
		if(content.startsWith("[")){
			content = content.substring(1);
		}
		if(content.endsWith("]")){
			content = content.substring(0, content.length()-1);
		}
		return (content.length() > maxLength) ? content.substring(0, maxLength) : content;
	}
}

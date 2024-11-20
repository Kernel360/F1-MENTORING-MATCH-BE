package com.biengual.core.domain.entity.content;

import static com.biengual.core.constant.RestrictionConstant.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.biengual.core.enums.ContentLevel;
import org.hibernate.annotations.DynamicUpdate;

import com.biengual.core.domain.document.content.script.Script;
import com.biengual.core.domain.entity.BaseEntity;
import com.biengual.core.domain.entity.category.CategoryEntity;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.enums.ContentType;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "content")
@Getter
@DynamicUpdate
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

	@Column(nullable = false, columnDefinition = "bigint")
	private Integer numOfQuiz;

	@Column(columnDefinition = "smallint")
	private Integer videoDuration;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ContentLevel contentLevel;

	@Builder
	public ContentEntity(
		String url, String title, String thumbnailUrl,
		ContentType contentType, String mongoContentId,
		List<Script> preScripts, CategoryEntity category, Integer videoDuration
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
		this.numOfQuiz = 0;
		this.videoDuration = videoDuration;
	}

	public void updateStatus(ContentStatus contentStatus) {
		this.contentStatus = contentStatus == null ? ContentStatus.ACTIVATED : contentStatus;
	}

	public void updateNumOfQuiz(Integer numOfQuiz) {
		this.numOfQuiz += numOfQuiz;
	}

	public void updateContentLevel(ContentLevel contentLevel) {
		this.contentLevel = contentLevel;
	}

	public boolean isRecentContent() {
		LocalDateTime createdAt = getCreatedAt();
		LocalDate recentThreshold = LocalDate.now().minusDays(PERIOD_FOR_POINT_CONTENT_ACCESS);

		if (createdAt != null) {
			return recentThreshold.isBefore(createdAt.toLocalDate());
		}

		return false;
	}

	public boolean isDeactivated() {
		return Objects.equals(this.contentStatus, ContentStatus.DEACTIVATED);
	}

	// Internal Method =================================================================================================

	private String truncate(String content, int maxLength) {
		if (content.startsWith("[")) {
			content = content.substring(1);
		}
		if (content.endsWith("]")) {
			content = content.substring(0, content.length() - 1);
		}
		return (content.length() > maxLength) ? content.substring(0, maxLength) : content;
	}
}

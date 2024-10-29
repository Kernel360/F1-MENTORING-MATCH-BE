package com.biengual.core.domain.entity.scrap;

import com.biengual.core.domain.entity.BaseEntity;
import com.biengual.core.domain.entity.content.ContentEntity;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scrap")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private ContentEntity content;

	@Column(name = "user_id", nullable = false, columnDefinition = "bigint")
	private Long userId;

	@Builder
	public ScrapEntity(ContentEntity content, Long userId) {
		this.content = content;
		this.userId = userId;
	}
}

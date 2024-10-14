package com.echall.platform.scrap.domain.entity;

import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.user.domain.entity.BaseEntity;

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

	@ManyToOne
	@JoinColumn(name = "content_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private ContentEntity content;

	@Builder
	public ScrapEntity(Long id, ContentEntity content) {
		this.id = id;
		this.content = content;
	}

	@Builder
	public ScrapEntity(ContentEntity content) {
		this.content = content;
	}
}

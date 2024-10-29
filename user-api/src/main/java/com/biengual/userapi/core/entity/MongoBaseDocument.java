package com.biengual.userapi.core.entity;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.PersistenceConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MongoBaseDocument {

	@Id
	protected ObjectId id;

	@CreatedDate
	protected LocalDateTime createdAt = LocalDateTime.now();

	@LastModifiedDate
	protected LocalDateTime updatedAt = LocalDateTime.now();

	@PersistenceConstructor
	public MongoBaseDocument(ObjectId id, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
		this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
	}

	public String getStringId(){
		return id.toString();
	}
}
package com.biengual.core.domain.document.content;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "content_access")
@Getter
@TypeAlias("content_access")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentAccessDocument {
    @Indexed(expireAfterSeconds = 30) // 5일 = 5 * 24 * 60 * 60 초
    protected Date createdAt;

    @LastModifiedDate
    protected LocalDateTime updatedAt = LocalDateTime.now();

    private Long contentId;

    private Set<Long> accessedUsers = new HashSet<>();

    @Builder
    public ContentAccessDocument(Long contentId) {
        this.contentId = contentId;
        this.accessedUsers = new HashSet<>();
        this.createdAt = Date.from(Instant.now().plusSeconds(30));
    }

    public void addAccessedUser(Long userId) {
        this.accessedUsers.add(userId);
    }
}

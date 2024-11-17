package com.biengual.userapi.domain.user;

import com.biengual.core.domain.entity.bookmark.BookmarkEntity;
import com.biengual.core.domain.entity.scrap.ScrapEntity;
import com.biengual.core.domain.entity.user.UserEntity;
import com.biengual.core.enums.Gender;
import com.biengual.core.enums.Role;
import com.biengual.core.enums.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Test에 사용할 User 도메인의 객체 생성을 위한 TestFixture 클래스
 *
 * @author 문찬욱
 */
public class UserTestFixture {

    private UserTestFixture() {
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestUserEntity {
        private @Builder.Default Long id = 123L;
        private @Builder.Default String username = "testUsername";
        private @Builder.Default String nickname = "testNickname";
        private @Builder.Default String password = null;
        private @Builder.Default String email = "test@test.com";
        private @Builder.Default String phoneNumber = null;
        private @Builder.Default LocalDate birth = null;
        private @Builder.Default Gender gender = null;
        private @Builder.Default Role role = Role.ROLE_USER;
        private @Builder.Default UserStatus userStatus = UserStatus.USER_STATUS_CREATED;
        private @Builder.Default String provider = "kakao";
        private @Builder.Default String providerId = "1234567890";
        private @Builder.Default LocalDateTime lastLoginTime = LocalDateTime.now();
        private @Builder.Default Long currentPoint = 100L;
        private @Builder.Default List<BookmarkEntity> bookmarks = new ArrayList<>();
        private @Builder.Default List<ScrapEntity> scraps = new ArrayList<>();
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestUserEntity.TestUserEntityBuilder createUserEntity() {
            return TestUserEntity.builder();
        }

        public UserEntity get() {
            return mapper.convertValue(this, UserEntity.class);
        }
    }
}

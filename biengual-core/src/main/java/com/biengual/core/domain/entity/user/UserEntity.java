package com.biengual.core.domain.entity.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

import com.biengual.core.domain.entity.BaseEntity;
import com.biengual.core.domain.entity.bookmark.BookmarkEntity;
import com.biengual.core.domain.entity.scrap.ScrapEntity;
import com.biengual.core.enums.Gender;
import com.biengual.core.enums.Role;
import com.biengual.core.enums.UserStatus;
import com.biengual.core.util.RandomNicknameGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user")
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String username;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String nickname;

    @Column(columnDefinition = "varchar(255)")
    private String password;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(255)")
    private String email;

    @Column(name = "phone_number", columnDefinition = "varchar(255)")
    private String phoneNumber;

    @Column(columnDefinition = "date")
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "user_status")
    private UserStatus userStatus;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String provider;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String providerId;

    @Column(name = "last_login_time", nullable = false)
    private LocalDateTime lastLoginTime;

    @Column(name = "current_point", nullable = false, columnDefinition = "bigint")
    private Long currentPoint;

    @OneToMany
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private List<BookmarkEntity> bookmarks = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private List<ScrapEntity> scraps = new ArrayList<>();

    // For Spring Security==============================================================================================
    @Builder
    public UserEntity(String username, String nickname, String password, String email, String phoneNumber,
        LocalDate birth, Gender gender, Role role, UserStatus userStatus, String provider, String providerId) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.gender = gender;
        this.role = role;
        this.userStatus = userStatus;
        this.provider = provider;
        this.providerId = providerId;
        this.currentPoint = 100L;
        this.lastLoginTime = LocalDateTime.now();
    }

    public static UserEntity createByOAuthUser(
        String username, String email, String provider, String providerId
    ) {
        return UserEntity.builder()
            .username(username)
            .nickname(RandomNicknameGenerator.setRandomNickname())
            .email(email)
            .role(Role.ROLE_USER)
            .userStatus(UserStatus.USER_STATUS_CREATED)
            .provider(provider)
            .providerId(providerId)
            .build();
    }

    // 본인 정보 수정
    public void updateMyInfo(
        String username, String nickname, String phoneNumber, LocalDate birth, Gender gender
    ) {
        this.username = StringUtils.defaultIfBlank(username, this.username);
        this.nickname = StringUtils.defaultIfBlank(nickname, this.nickname);
        this.phoneNumber = StringUtils.defaultIfBlank(phoneNumber, this.phoneNumber);
        this.birth = Optional.ofNullable(birth).orElse(this.birth);
        this.gender = Optional.ofNullable(gender).orElse(this.gender);
        this.userStatus = this.userStatus ==
            UserStatus.USER_STATUS_CREATED ? UserStatus.USER_STATUS_ACTIVATE : this.userStatus;
    }

    public void updateAfterOAuth2Login(String username, String provider, String providerId) {
        this.username = username;
        this.provider = provider;
        this.providerId = providerId;
    }
}

package com.biengual.userapi.user.domain;

import com.biengual.userapi.bookmark.domain.BookmarkEntity;
import com.biengual.userapi.common.entity.BaseEntity;
import com.biengual.userapi.oauth2.domain.info.OAuth2UserPrincipal;
import com.biengual.userapi.scrap.domain.entity.ScrapEntity;
import com.biengual.userapi.user.domain.enums.Gender;
import com.biengual.userapi.user.domain.enums.Role;
import com.biengual.userapi.user.domain.enums.UserStatus;
import com.biengual.userapi.util.RandomNicknameGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
	}

	public static UserEntity createByOAuthUser(OAuth2UserPrincipal oAuthUser) {
		return UserEntity.builder()
			.username(oAuthUser.getUsername())
			.nickname(RandomNicknameGenerator.setRandomNickname())
			.email(oAuthUser.getEmail())
			.role(Role.ROLE_USER)
			.userStatus(UserStatus.USER_STATUS_CREATED)
			.provider(oAuthUser.getProvider())
			.providerId(oAuthUser.getProviderId())
			.build();
	}

	// 본인 정보 수정
	public void updateMyInfo(UserCommand.UpdateMyInfo command) {
		this.username = command.username() == null ? this.username : command.username();
		this.nickname = command.nickname() == null ? this.nickname : command.nickname();
		this.phoneNumber = command.phoneNumber() == null ? this.phoneNumber : command.phoneNumber();
		this.birth = command.birth() == null ? this.birth : command.birth();
		this.gender = command.gender() == null ? this.gender : command.gender();
	}

	public void updateUserBookmark(BookmarkEntity bookmark) {
		this.bookmarks.add(bookmark);
	}

	public void updateAfterOAuth2Login(OAuth2UserPrincipal oAuthUser) {
		this.username = oAuthUser.getUsername();
		this.provider = oAuthUser.getProvider();
		this.providerId = oAuthUser.getProviderId();
	}

	public boolean hasContent(Long contentId){
		return this.scraps.stream()
			.anyMatch(scrap -> scrap.getContent().getId().equals(contentId));
	}
}

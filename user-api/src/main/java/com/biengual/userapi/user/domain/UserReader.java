package com.biengual.userapi.user.domain;

import java.time.LocalDateTime;

import com.biengual.core.domain.entity.user.UserEntity;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;

/**
 * User 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface UserReader {
    UserEntity findUser(Long userId, String email);

    UserEntity findUser(OAuth2UserPrincipal principal);

    UserInfo.MyInfo findMyInfo(Long userId);

    UserInfo.MySignUpTime findMySignUpTime(Long userId);

    LocalDateTime findLastLoginTime(Long userId);
}

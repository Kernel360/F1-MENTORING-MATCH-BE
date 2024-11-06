package com.biengual.userapi.user.domain;

import com.biengual.core.domain.entity.user.UserEntity;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * User 도메인의 Service 계층의 인터페이스
 *
 * @author 김영래
 */
public interface UserService {
    UserInfo.MyInfo getMyInfo(Long userId);

    void updateMyInfo(UserCommand.UpdateMyInfo command);

    UserInfo.MySignUpTime getMySignUpTime(Long userId);

    UserEntity getUserByOAuthUser(OAuth2UserPrincipal oAuthUser);

    void logout(HttpServletRequest request, HttpServletResponse response, Long userId);

    Boolean getLoginStatus(OAuth2UserPrincipal principal);
}

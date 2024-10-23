package com.biengual.userapi.user.domain;

import com.biengual.userapi.oauth2.domain.info.OAuth2UserPrincipal;
import com.biengual.userapi.user.domain.entity.UserEntity;
import com.biengual.userapi.user.presentation.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    void updateMyInfo(UserCommand.UpdateMyInfo command);

    UserResponseDto.UserMyPageResponse getMyPage(String email);

    UserResponseDto.UserMyTimeResponse getMySignUpTime(Long userId);

    UserEntity getUserById(Long userId);

    UserEntity getUserByOAuthUser(OAuth2UserPrincipal oAuthUser);

    void logout(HttpServletRequest request, HttpServletResponse response, Long userId);

    Boolean getUserStatus(HttpServletRequest request);
}

package com.biengual.userapi.user.infrastructure;

import com.biengual.userapi.core.annotation.DataProvider;
import com.biengual.userapi.core.response.error.exception.CommonException;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import com.biengual.userapi.user.domain.UserInfo;
import com.biengual.userapi.user.domain.UserReader;
import com.biengual.userapi.core.domain.entity.user.UserEntity;
import com.biengual.userapi.user.presentation.UserDtoMapper;
import com.biengual.userapi.user.domain.UserCategoryCustomRepository;
import com.biengual.userapi.user.domain.UserCustomRepository;
import com.biengual.userapi.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.biengual.userapi.core.response.error.code.UserErrorCode.USER_NOT_FOUND;

@DataProvider
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {
    private final UserDtoMapper userDtoMapper;
    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;
    private final UserCategoryCustomRepository userCategoryCustomRepository;

    // userId와 email로 UserEntity 조회
    @Override
    public UserEntity findUser(Long userId, String email) {
        return userRepository.findByIdAndEmail(userId, email)
            .orElseThrow(() -> new CommonException(USER_NOT_FOUND));
    }

    // OAuth2UserPrincipal을 이용한 회원가입 및 로그인 처리와 함께 UserEntity 조회
    @Override
    public UserEntity findUser(OAuth2UserPrincipal principal) {
        UserEntity user = userRepository.findByEmail(principal.getEmail())
            .orElseGet(() -> {
                UserEntity newUser = UserEntity.createByOAuthUser(principal);

                return userRepository.save(newUser);
            });

        user.updateAfterOAuth2Login(principal);

        return user;
    }

    // userId로 유저 정보 조회
    @Override
    public UserInfo.MyInfo findMyInfo(Long userId) {
        UserInfo.MyInfoExceptMyCategories myInfoExceptMyCategories =
            userCustomRepository.findMyInfoExceptMyCategories(userId)
                .orElseThrow(() -> new CommonException(USER_NOT_FOUND));

        List<UserInfo.MyCategory> myCategories = userCategoryCustomRepository.findAllMyCategories(userId);

        return userDtoMapper.buildMyInfo(myInfoExceptMyCategories, myCategories);
    }

    // userId로 유저 회원가입 날짜 조회
    @Override
    public UserInfo.MySignUpTime findMySignUpTime(Long userId) {
        return userCustomRepository.findMySignUpTime(userId)
            .orElseThrow(() -> new CommonException(USER_NOT_FOUND));
    }
}

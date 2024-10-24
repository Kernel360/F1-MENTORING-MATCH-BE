package com.biengual.userapi.user.infrastructure;

import com.biengual.userapi.annotation.DataProvider;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.user.domain.UserInfo;
import com.biengual.userapi.user.domain.UserReader;
import com.biengual.userapi.user.domain.UserEntity;
import com.biengual.userapi.user.presentation.UserDtoMapper;
import com.biengual.userapi.user.repository.UserCategoryCustomRepository;
import com.biengual.userapi.user.repository.UserCustomRepository;
import com.biengual.userapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.biengual.userapi.message.error.code.UserErrorCode.USER_NOT_FOUND;

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

    // userId로 유저 정보 조회
    @Override
    public UserInfo.MyInfo findMyInfo(Long userId) {
        UserInfo.MyInfoExceptMyCategories myInfoExceptMyCategories =
            userCustomRepository.findMyInfoExceptMyCategories(userId)
                .orElseThrow(() -> new CommonException(USER_NOT_FOUND));

        List<UserInfo.MyCategory> myCategories = userCategoryCustomRepository.findAllMyCategories(userId);

        return userDtoMapper.buildMyInfo(myInfoExceptMyCategories, myCategories);
    }
}

package com.biengual.userapi.user.infrastructure;

import com.biengual.userapi.annotation.DataProvider;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.user.domain.UserCommand;
import com.biengual.userapi.user.domain.UserStore;
import com.biengual.userapi.user.domain.UserEntity;
import com.biengual.userapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import static com.biengual.userapi.message.error.code.UserErrorCode.USER_NOT_FOUND;

@DataProvider
@RequiredArgsConstructor
public class UserStoreImpl implements UserStore {
    private final UserRepository userRepository;

    // 본인 정보를 수정 - Dirty Checking
    @Override
    public void updateMyInfo(UserCommand.UpdateMyInfo command) {
        UserEntity user = userRepository.findByIdAndEmail(command.userId(), command.email())
            .orElseThrow(() -> new CommonException(USER_NOT_FOUND));

        user.updateMyInfo(command);
    }
}

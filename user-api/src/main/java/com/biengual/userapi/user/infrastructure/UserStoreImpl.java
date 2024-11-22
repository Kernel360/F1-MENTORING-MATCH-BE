package com.biengual.userapi.user.infrastructure;

import static com.biengual.core.response.error.code.UserErrorCode.*;

import java.util.List;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.category.CategoryEntity;
import com.biengual.core.domain.entity.user.UserCategoryEntity;
import com.biengual.core.domain.entity.user.UserEntity;
import com.biengual.core.enums.PointReason;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.category.domain.CategoryRepository;
import com.biengual.userapi.user.domain.UserCategoryCustomRepository;
import com.biengual.userapi.user.domain.UserCategoryRepository;
import com.biengual.userapi.user.domain.UserCommand;
import com.biengual.userapi.user.domain.UserCustomRepository;
import com.biengual.userapi.user.domain.UserRepository;
import com.biengual.userapi.user.domain.UserStore;
import com.biengual.userapi.validator.UserValidator;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class UserStoreImpl implements UserStore {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final UserCategoryCustomRepository userCategoryCustomRepository;
    private final UserCustomRepository userCustomRepository;
    private final UserValidator userValidator;

    // 본인 정보 수정
    @Override
    public void updateMyInfo(UserCommand.UpdateMyInfo command) {
        UserEntity user = userRepository.findByIdAndEmail(command.userId(), command.email())
            .orElseThrow(() -> new CommonException(USER_NOT_FOUND));

        user.updateMyInfo(
            userValidator.verifyNicknamePattern(command.nickname()),
            command.phoneNumber(), command.birth(), command.gender()    // TODO : 이 부분 업데이트 기능 추가하면 verify 필요
        );
        userRepository.save(user);

        if (command.categoryIds() != null) {
            // user가 이미 관심 카테고리로 등록한 CategoryIds List
            List<Long> alreadyRegisteredMyCategoryIds =
                userCategoryCustomRepository.findAllMyRegisteredCategoryId(command.userId());

            saveAdditionalMyCategories(alreadyRegisteredMyCategoryIds, command);

            deleteRemovalMyCategoryIds(alreadyRegisteredMyCategoryIds, command);
        }
    }

    @Override
    public void updateLastLoginTime(Long userId) {
        userCustomRepository.updateLastLoginTime(userId);
    }

    @Override
    public void updatePoint(Long userId, PointReason pointReason) {
        userCustomRepository.updatePointByPointReason(userId, pointReason);
    }

    // user의 관심 Category 목록에 추가
    private void saveAdditionalMyCategories(
        List<Long> alreadyRegisteredMyCategoryIds, UserCommand.UpdateMyInfo command
    ) {
        // user의 관심 목록에서 추가될 CategoryIds List
        List<Long> additionalMyCategoryIds = command.categoryIds().stream()
            .filter(categoryId -> !alreadyRegisteredMyCategoryIds.contains(categoryId))
            .toList();

        // TODO: additionalMyCategoryIds에 들어 있는 모든 Id에 대해 CategoryEntity가 존재하는지 검증이 필요한지 생각해볼 것
        List<CategoryEntity> additionalCategories = categoryRepository.findAllById(additionalMyCategoryIds);

        List<UserCategoryEntity> additionalUserCategories = additionalCategories.stream()
            .map(command::toUserCategoryEntity)
            .toList();

        userCategoryRepository.saveAll(additionalUserCategories);
    }

    // user의 관심 Category 목록에 제거
    private void deleteRemovalMyCategoryIds(
        List<Long> alreadyRegisteredMyCategoryIds, UserCommand.UpdateMyInfo command
    ) {
        // user의 관심 목록에서 제거될 CategoryIds List
        List<Long> removalMyCategoryIds = alreadyRegisteredMyCategoryIds.stream()
            .filter(categoryId -> !command.categoryIds().contains(categoryId))
            .toList();

        // removalMyCategoryIds가 비어있어도 쿼리가 나가기 때문에 비어있지 않을 때 쿼리가 나가도록 검증
        if (!removalMyCategoryIds.isEmpty()) {
            userCategoryCustomRepository.deleteAllByUserIdAndCategoryIdIn(command.userId(), removalMyCategoryIds);
        }
    }
}

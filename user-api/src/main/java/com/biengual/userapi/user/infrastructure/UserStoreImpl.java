package com.biengual.userapi.user.infrastructure;

import com.biengual.userapi.core.annotation.DataProvider;
import com.biengual.userapi.core.domain.entity.category.CategoryEntity;
import com.biengual.userapi.category.domain.CategoryRepository;
import com.biengual.userapi.core.response.error.exception.CommonException;
import com.biengual.userapi.core.domain.entity.user.UserCategoryEntity;
import com.biengual.userapi.user.domain.UserCommand;
import com.biengual.userapi.core.domain.entity.user.UserEntity;
import com.biengual.userapi.user.domain.UserStore;
import com.biengual.userapi.user.domain.UserCategoryCustomRepository;
import com.biengual.userapi.user.domain.UserCategoryRepository;
import com.biengual.userapi.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.biengual.userapi.core.response.error.code.UserErrorCode.USER_NOT_FOUND;

@DataProvider
@RequiredArgsConstructor
public class UserStoreImpl implements UserStore {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final UserCategoryCustomRepository userCategoryCustomRepository;

    // 본인 정보 수정
    @Override
    public void updateMyInfo(UserCommand.UpdateMyInfo command) {

        UserEntity user = userRepository.findByIdAndEmail(command.userId(), command.email())
            .orElseThrow(() -> new CommonException(USER_NOT_FOUND));

        // UserEntity Dirty Checking
        user.updateMyInfo(command);

        // user가 이미 관심 카테고리로 등록한 CategoryIds List
        List<Long> alreadyRegisteredMyCategoryIds =
            userCategoryCustomRepository.findAllMyRegisteredCategoryId(command.userId());

        saveAdditionalMyCategories(alreadyRegisteredMyCategoryIds, command);

        deleteRemovalMyCategoryIds(alreadyRegisteredMyCategoryIds, command);
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

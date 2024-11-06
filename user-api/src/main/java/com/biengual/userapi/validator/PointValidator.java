package com.biengual.userapi.validator;

import static com.biengual.core.response.error.code.PointErrorCode.*;

import java.time.LocalDate;

import com.biengual.core.annotation.Validator;
import com.biengual.core.domain.entity.user.UserEntity;
import com.biengual.core.enums.PointReason;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentReader;

import lombok.RequiredArgsConstructor;

@Validator
@RequiredArgsConstructor
public class PointValidator {
    private final ContentReader contentReader;

    // 업데이트 전에 포인트가 음수가 되지 않는지 검증
    public void verifyUpdatePoint(Long currentPoint, PointReason reason) {
        if (reason.add(currentPoint) < 0) {
            throw new CommonException(POINT_NEVER_MINUS);
        }
    }

    // 하루 첫 로그인인지 검증
    public boolean verifyFirstDailyLogin(UserEntity user) {
        return user.getLastLoginTime().toLocalDate().isBefore(LocalDate.now());
    }

    // 컨텐츠가 유효한지와 포인트가 필요한지 검증
    public boolean verifyContentView(ContentCommand.GetDetail command) {
        contentReader.findContentIsActivated(command.contentId());

        return contentReader.checkAlreadyReadable(command);
    }

    // 미션 성공했는지 검증 - 미션은 F -> T로만 바뀜
    public boolean verifyMission(boolean missionCommand, boolean missionInfo) {
        return missionCommand || missionInfo;
    }
}
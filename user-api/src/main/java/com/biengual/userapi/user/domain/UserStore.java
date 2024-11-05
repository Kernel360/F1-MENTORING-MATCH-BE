package com.biengual.userapi.user.domain;

import com.biengual.core.enums.PointReason;

/**
 * User 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface UserStore {
    void updateMyInfo(UserCommand.UpdateMyInfo command);
    void updateLastLoginTime(Long userId);
    void updatePoint(Long userId, PointReason pointReason);
}

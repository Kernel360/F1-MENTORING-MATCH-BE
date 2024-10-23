package com.biengual.userapi.user.domain;

/**
 * User 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface UserStore {
    void updateMyInfo(UserCommand.UpdateMyInfo command);
}

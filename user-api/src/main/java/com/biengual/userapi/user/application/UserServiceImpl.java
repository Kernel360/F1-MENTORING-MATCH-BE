package com.biengual.userapi.user.application;

import static com.biengual.core.response.error.code.PointErrorCode.*;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.core.domain.entity.user.UserEntity;
import com.biengual.core.enums.PointReason;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.core.util.CookieUtil;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentReader;
import com.biengual.userapi.mission.domain.MissionReader;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import com.biengual.userapi.pointhistory.domain.PointHistoryStore;
import com.biengual.userapi.user.domain.RefreshTokenStore;
import com.biengual.userapi.user.domain.UserCommand;
import com.biengual.userapi.user.domain.UserInfo;
import com.biengual.userapi.user.domain.UserReader;
import com.biengual.userapi.user.domain.UserService;
import com.biengual.userapi.user.domain.UserStore;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final CookieUtil cookieUtil;
    private final UserReader userReader;
    private final UserStore userStore;
    private final RefreshTokenStore refreshTokenStore;
    private final MissionReader missionReader;
    private final PointHistoryStore pointHistoryStore;
    private final ContentReader contentReader;

    // 본인 정보 조회
    @Override
    @Transactional(readOnly = true)
    public UserInfo.MyInfo getMyInfo(Long userId) {
        return userReader.findMyInfo(userId);
    }

    // 본인 정보 수정
    @Override
    @Transactional
    public void updateMyInfo(UserCommand.UpdateMyInfo command) {
        userStore.updateMyInfo(command);
    }

    // 본인 회원 가입 날짜 조회
    @Override
    @Transactional(readOnly = true)
    public UserInfo.MySignUpTime getMySignUpTime(Long userId) {
        return userReader.findMySignUpTime(userId);
    }

    // OAuth 유저 회원가입, 로그인 처리 및 미션 생성
    @Override
    @Transactional
    public UserEntity getUserByOAuthUser(OAuth2UserPrincipal principal) {
        UserEntity user = userReader.findUser(principal);

        // 미션 생성되지 않은 경우 생성
        // TODO: 로그인마다 처리해야 하기 때문에 다른 방법 있는지 고민
        missionReader.findMission(user.getId());

        return user;
    }

    // 유저 로그아웃
    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Long userId) {
        cookieUtil.removeAccessTokenCookie(request, response);
        cookieUtil.removeRefreshTokenCookie(request, response);

        refreshTokenStore.delete(userId);
    }

    // 유저 로그인 상태 확인
    @Override
    public Boolean getLoginStatus(OAuth2UserPrincipal principal) {
        return principal != null;
    }

    @Override
    @Transactional
    public void updatePointByReason(ContentCommand.GetDetail command, PointReason reason) {
        // validate content
        contentReader.findContentIsActivated(command.contentId());

        if (contentReader.checkAlreadyReadable(command)) {
            return;
        }

        // 포인트 업데이트
        updatePoint(userReader.findUserPoint(command.userId()), command.userId(), reason);
    }

    @Override
    @Transactional
    public void updatePointByFirstDailyLogin(UserEntity user) {
        // 로그인 포인트 지급
        if (!user.getLastLoginTime().toLocalDate().isBefore(LocalDate.now())) {
            return;
        }
        // 포인트 업데이트
        updatePoint(user.getCurrentPoint(), user.getId(), PointReason.FIRST_DAILY_LOG_IN);

        // 유저 마지막 로그인 시간 업데이트
        userStore.updateLastLoginTime(user.getId());
    }

    // Internal Methods ===============================================================================================
    // 업데이트 전에 포인트가 음수가 되지 않는지 확인
    // TODO: 비동기
    private void updatePoint(Long currentPoint, Long userId, PointReason reason) {

        verifyUpdatePoint(currentPoint, reason);

        userStore.updatePoint(userId, reason);
        Long updatedPoint = userReader.findUserPoint(userId);

        // TODO: 클래스화
        // 포인트 기록 저장
        pointHistoryStore.recordPointHistory(userId, reason, updatedPoint);
    }

    private void verifyUpdatePoint(Long currentPoint, PointReason reason) {
        if (currentPoint + reason.getValue() < 0) { //TODO: .add()로 처리할 수 있음 노션 참고
            throw new CommonException(POINT_NEVER_MINUS);
        }
    }
}

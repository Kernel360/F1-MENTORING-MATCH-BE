package com.biengual.core.constant;

/**
 * Request Dto 검증 실패 메시지 관리 클래스
 *
 * @author 문찬욱
 */
public class BadRequestMessageConstant {
    public static final String MAX_CATEGORY_SELECTION_ERROR_MESSAGE = "카테고리 목록은 최대 5개 선택 가능";
    public static final String BLANK_CONTENT_KEYWORD_ERROR_MESSAGE = "컨테츠 검색 키워드 blank 허용 불가";
    public static final String NULL_MISSION_LIST_ERROR_MESSAGE = "미션 체크 목록 null 허용 불가";
    public static final String DATE_PATTERN_MISMATCH = "입력된 날짜가 잘못된 형식";
    public static final String NULL_CONTENT_ID_ERROR_MESSAGE = "컨텐츠 Id는 null 허용 불가";
    public static final String NULL_CONTENT_LEVEL_ERROR_MESSAGE = "컨텐츠 level은 null 허용 불가";
    public static final String NICKNAME_SIZE_LIMIT_ERROR_MESSAGE = "닉네임 길이는 4~12 사이의 값만 가능";
    public static final String NICKNAME_PATTERN_ERROR_MESSAGE = "닉네임에는 특수문자 혹은 두 번 이상의 연속 띄어쓰기 불가";
}

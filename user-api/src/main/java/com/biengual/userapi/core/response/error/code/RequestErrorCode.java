package com.biengual.userapi.core.response.error.code;

import com.biengual.userapi.core.response.status.RequestServiceStatus;
import com.biengual.userapi.core.response.status.ServiceStatus;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;

/**
 * Request 관련 실패 메시지를 서비스 응답 포맷으로 변환하기 위한 enum
 *
 * @author 문찬욱
 */
@RequiredArgsConstructor
public enum RequestErrorCode implements ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, RequestServiceStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;
    private final ServiceStatus serviceStatus;
    private String message;

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return serviceStatus.getServiceStatus();
    }

    @Override
    public String getMessage() {
        return message;
    }

    // RequestDto 검증 실패 메시지 set
    public void setMessage(MethodArgumentNotValidException e) {
        this.message = e.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining("\n"));
    }

    public void setMessage(ConstraintViolationException e) {
        this.message = e.getMessage();
    }

    // RequestBody Json 포맷 실패 메시지 set
    public void setMessage(HttpMessageNotReadableException e) {
        this.message = e.getMessage();
    }
}

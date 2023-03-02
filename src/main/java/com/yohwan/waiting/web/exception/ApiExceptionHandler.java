package com.yohwan.waiting.web.exception;

import com.yohwan.waiting.web.controller.common.dto.ApiResponseDto;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.yohwan.waiting.web.exception.custom.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponseDto globalException(Exception e){
        log.info("[globalException][message={}]", e.getMessage(), e);
        return new ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ClientAbortException.class)
    public void clientAbortException(ClientAbortException e){
        log.info("[clientAbortException][message={}][클라이언트 연결 끊김]", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JWTVerificationException.class)
    public ApiResponseDto verificationException(JWTVerificationException e){
        log.info("[verificationException][message={}]", e.getMessage());
        return new ApiResponseDto(HttpStatus.BAD_REQUEST, "토큰정보가 옳바르지않습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JWTDecodeException.class)
    public ApiResponseDto decodeException(JWTDecodeException e){
        log.info("[decodeException][message={}]", e.getMessage());
        return new ApiResponseDto(HttpStatus.BAD_REQUEST, "토큰정보가 옳바르지않습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthenticationException.class)
    public ApiResponseDto authenticationException(AuthenticationException e){
        log.info("[authenticationException][message={}]", e.getMessage());
        return new ApiResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponseDto bindException(BindException e){
        log.info("[bindException][message={}]", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder stringBuilder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append("\n");
        }
        return new ApiResponseDto(HttpStatus.BAD_REQUEST, stringBuilder.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponseDto methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("[methodArgumentNotValidException][message={}]", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder stringBuilder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append("\n");
        }
        return new ApiResponseDto(HttpStatus.BAD_REQUEST, stringBuilder.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponseDto methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info("[methodArgumentTypeMismatchException][message={}]", e.getMessage());
        return new ApiResponseDto(HttpStatus.BAD_REQUEST,"요청 형식이 옳바르지 않습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponseDto methodArgumentTypeMismatchException(HttpMessageNotReadableException e) {
        log.info("[methodArgumentTypeMismatchException][message={}]", e.getMessage());
        return new ApiResponseDto(HttpStatus.BAD_REQUEST, "요청 형식이 옳바르지 않습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MemberException.class)
    public ApiResponseDto memberException(MemberException e){
        log.info("[memberException][message={}]", e.getMessage());
        return new ApiResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(VisitorException.class)
    public ApiResponseDto visitorException(VisitorException e){
        log.info("[visitorException][message={}]", e.getMessage());
        return new ApiResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthException.class)
    public ApiResponseDto authException(AuthException e){
        log.info("[authException][message={}]", e.getMessage());
        return new ApiResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CommonException.class)
    public ApiResponseDto commonException(CommonException e){
        log.info("[commonException][message={}]", e.getMessage());
        return new ApiResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SystemSettingException.class)
    public ApiResponseDto systemSettingException(SystemSettingException e){
        log.info("[systemSettingException][message={}]", e.getMessage());
        return new ApiResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}

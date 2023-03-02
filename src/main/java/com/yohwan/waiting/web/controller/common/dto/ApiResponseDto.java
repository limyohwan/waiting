package com.yohwan.waiting.web.controller.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class ApiResponseDto<T> {

    private String result;
    private int statusCode;
    private String message;
    private T data;

    public ApiResponseDto(HttpStatus statusCode, T data) {
        this.result = getResultByStatusCode(Integer.toString(statusCode.value()));
        this.statusCode = statusCode.value();
        this.data =data;
    }

    public ApiResponseDto(HttpStatus statusCode, String message) {
        this.result = getResultByStatusCode(Integer.toString(statusCode.value()));
        this.statusCode = statusCode.value();
        this.message = message;
    }

    public ApiResponseDto(int statusCode, String message) {
        this.result = getResultByStatusCode(Integer.toString(statusCode));
        this.statusCode = statusCode;
        this.message = message;
    }

    private String getResultByStatusCode(String statusCode){
        if(statusCode.startsWith("2")){
            return "success";
        }else{
            return "fail";
        }
    }
}

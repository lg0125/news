package com.chris.news.common.exception;

import com.chris.news.model.common.constant.AppHttpCodeEnum;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final AppHttpCodeEnum appHttpCodeEnum;

    public CustomException(AppHttpCodeEnum appHttpCodeEnum){
        this.appHttpCodeEnum = appHttpCodeEnum;
    }

}

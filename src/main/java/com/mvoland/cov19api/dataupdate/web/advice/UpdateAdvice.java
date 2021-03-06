package com.mvoland.cov19api.dataupdate.web.advice;

import com.mvoland.cov19api.dataupdate.exception.CannotUpdateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class UpdateAdvice {

    @ResponseBody
    @ExceptionHandler(CannotUpdateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String regionNotFoundHandler(CannotUpdateException ex) {
        return ex.getMessage();
    }


}
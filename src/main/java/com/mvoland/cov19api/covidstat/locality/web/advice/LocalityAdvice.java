package com.mvoland.cov19api.covidstat.locality.web.advice;

import com.mvoland.cov19api.covidstat.locality.exception.DepartmentNotFoundException;
import com.mvoland.cov19api.covidstat.locality.exception.RegionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class LocalityAdvice {

    @ResponseBody
    @ExceptionHandler(RegionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String regionNotFoundHandler(RegionNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(DepartmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String departmentNotFoundHandler(DepartmentNotFoundException ex) {
        return ex.getMessage();
    }


}
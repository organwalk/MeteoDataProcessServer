package com.weather.exceptions;

import com.weather.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Set;

/**
 * 全局异常拦截
 * by organwalk 2023-04-02
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获 MethodArgumentNotValidException 异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        StringBuilder errorMsg = new StringBuilder();
        for (ObjectError error : allErrors) {
            errorMsg.append(error.getDefaultMessage()).append(";");
        }
        return Result.fail(errorMsg.toString());
    }
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result handConstraintViolationException(ConstraintViolationException e, HttpServletRequest request){
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder errorMsg = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            errorMsg.append(violation.getMessage()).append(";");
        }
        return Result.fail(errorMsg.toString());
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public Result handleInternalServerError(Exception e) {
        return Result.fail("内部服务错误，请稍后重试");
    }

    // 捕获400 Bad Request异常
    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public Result handleBadRequestException(Exception e, HttpServletRequest request) {
        return Result.fail("请求参数错误或类型不匹配");
    }

    // 捕获404 Not Found异常
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handleNotFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        return Result.fail("无法找到请求接口");
    }
}

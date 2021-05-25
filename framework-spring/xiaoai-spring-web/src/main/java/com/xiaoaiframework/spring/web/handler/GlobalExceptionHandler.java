package com.xiaoaiframework.spring.web.handler;

import com.xiaoaiframework.core.base.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {


    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultBean handle(MethodArgumentNotValidException exception) {

        StringBuilder builder = new StringBuilder();

        if(exception instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException exs = (MethodArgumentNotValidException) exception;
            List<ObjectError> errors = exs.getBindingResult().getAllErrors();

            for (ObjectError error : errors) {
                //打印验证不通过的信息
                String message = error.getDefaultMessage();
                logger.warn("参数校验失败: "+message);

                builder.append(message).append(",");
            }
            builder.deleteCharAt(builder.length()-1);
        }
        return ResultBean.fail().setMessage(builder.toString());
    }
}

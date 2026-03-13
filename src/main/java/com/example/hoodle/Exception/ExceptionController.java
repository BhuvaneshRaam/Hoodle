package com.example.hoodle.Exception;

import com.example.hoodle.Constants.ErrorCode;
import com.example.hoodle.Constants.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandler(CustomException ex){
        ErrorResponse errorResponse = new ErrorResponse(ErrorMessage.Internal_Server_Error,ex.getCode(), ex.getErrorMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

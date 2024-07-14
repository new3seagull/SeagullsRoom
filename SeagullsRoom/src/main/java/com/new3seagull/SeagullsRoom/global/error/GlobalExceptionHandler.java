package com.new3seagull.SeagullsRoom.global.error;

import com.new3seagull.SeagullsRoom.global.util.ApiUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(CustomException.class)
//    public String handleCustomException(CustomException ex) {
//
//        // 예외 유형에 따라 리다이렉션 처리
//        if (ex.getExceptionCode() == ExceptionCode.POST_NOT_FOUND) {
//            return "redirect:/posts?notExist";
//        } else if(ex.getExceptionCode() == ExceptionCode.POST_AUTHORITY_FORBIDDEN){
//            return "redirect:/posts?noPermission";
//        }
//        else {
//            return "redirect:/";
//        }
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknownServerError(Exception e) {
        String message = extractDesiredMessage(e.getMessage());

        ApiUtils.ApiFail apiResult = ApiUtils.fail(message, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String errorMessage = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
        return ResponseEntity.badRequest()
            .body(ApiUtils.fail(errorMessage, HttpStatus.BAD_REQUEST));
    }

    // 메시지에서 원하는 부분만 노출 되도록 처리
    private String extractDesiredMessage(String fullMessage) {
        if (fullMessage != null && fullMessage.contains("Required request body is missing")) {
            return "Required request body is missing.";
        }
        return fullMessage;
    }
}
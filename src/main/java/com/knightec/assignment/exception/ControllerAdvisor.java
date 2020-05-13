package com.knightec.assignment.exception;

import com.knightec.assignment.translation.TranslationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    private static Logger logger = LoggerFactory.getLogger(TranslationController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception ex,
                                         HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> errors = new HashMap<>();
        if (ex instanceof ConstraintViolationException) {
            errors.put("message",ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }else if( ex instanceof ResponseStatusException) {
            errors.put("message",((ResponseStatusException) ex).getReason());
            return ResponseEntity.status(((ResponseStatusException) ex).getStatus()).body(errors);
        }else if( ex instanceof MethodArgumentNotValidException) {
            errors.put("message",((MethodArgumentNotValidException) ex).getBindingResult().getFieldError().getDefaultMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        } else {
            logger.error("Internal error occurred",ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
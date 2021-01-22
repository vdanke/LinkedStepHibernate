package org.step.linked.step.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.step.linked.step.exceptions.ExceptionDescription;
import org.step.linked.step.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = {NumberFormatException.class})
    public ResponseEntity<ExceptionDescription> handleNumberFormatException(HttpServletRequest request, NumberFormatException e) {
        return convertExceptionToResponseEntity(e, HttpStatus.BAD_REQUEST, request.getRequestURI(), request.getMethod());
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<ExceptionDescription> handleNullPointerException(HttpServletRequest request, NullPointerException e) {
        return convertExceptionToResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(), request.getMethod());
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ExceptionDescription> handleNotFoundException(HttpServletRequest request, NotFoundException e) {
        return convertExceptionToResponseEntity(e, HttpStatus.NOT_FOUND, request.getRequestURI(), request.getMethod());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Map<Object, Object>> handleUnknownException(Exception e) {
        Map<Object, Object> errorMap = new HashMap<>();
        errorMap.put("exceptionType", e.getClass().getSimpleName());
        errorMap.put("localDateTime", LocalDateTime.now().toString());
        Stream.of(e.getStackTrace())
                .forEach(st -> errorMap.put(st.getMethodName(), st.getClassName()));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(errorMap);
    }

    private ResponseEntity<ExceptionDescription> convertExceptionToResponseEntity(
            Exception e, HttpStatus status, String path, String method) {
        return ResponseEntity
                .status(status)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(
                        new ExceptionDescription(
                                e.getClass().getSimpleName(),
                                e.getLocalizedMessage(),
                                LocalDateTime.now().toString(),
                                path,
                                method
                        )
                );
    }
}

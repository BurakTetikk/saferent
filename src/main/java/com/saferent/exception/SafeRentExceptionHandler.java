package com.saferent.exception;

import com.saferent.exception.message.ApiResponseError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class SafeRentExceptionHandler extends ResponseEntityExceptionHandler {


    Logger logger = LoggerFactory.getLogger(SafeRentExceptionHandler.class);



    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {

        ApiResponseError apiResponseError = new ApiResponseError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getDescription(false));

        return buildResponseEntity(apiResponseError);

    }

    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflictException(ConflictException exception, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.CONFLICT, exception.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.FORBIDDEN, exception.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException exception, WebRequest request) {
        ApiResponseError error =
                new ApiResponseError(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getDescription(false));

        return buildResponseEntity(error);
    }



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());


        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST, errors.get(0).toString(), request.getDescription(false));


        return buildResponseEntity(error);


    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false));


        return buildResponseEntity(error);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {


        ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false));


        return buildResponseEntity(error);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false));


        return buildResponseEntity(error);
    }



    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException exception, WebRequest request) {

        ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), request.getDescription(false));


        return buildResponseEntity(error);

    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGeneralException(Exception exception, WebRequest request) {

        ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), request.getDescription(false));


        return buildResponseEntity(error);

    }


    private ResponseEntity<Object> buildResponseEntity(ApiResponseError error) {
        logger.error(error.getMessage());
        return new ResponseEntity<>(error, error.getStatus());
    }






}

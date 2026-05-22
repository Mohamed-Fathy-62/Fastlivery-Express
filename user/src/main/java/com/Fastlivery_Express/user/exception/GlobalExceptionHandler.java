package com.Fastlivery_Express.user.exception;

import com.Fastlivery_Express.user.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "Request validation failed",
                request,
                validationErrors
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception,
                                                                            WebRequest webRequest) {
        return buildErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", exception, webRequest);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                                 WebRequest webRequest) {
        return buildErrorResponseEntity(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", exception, webRequest);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyExistsException(UserAlreadyExistsException exception,
                                                                                 WebRequest webRequest){
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, "USER_ALREADY_EXISTS", exception, webRequest);
    }

    @ExceptionHandler(UserEmailAlreadyUsed.class)
    public ResponseEntity<ErrorResponseDto> handleUserEmailAlreadyUsed(UserEmailAlreadyUsed exception,
                                                                       WebRequest webRequest){
        return buildErrorResponseEntity(HttpStatus.CONFLICT, "USER_EMAIL_ALREADY_USED", exception, webRequest);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException exception,
                                                                                 WebRequest webRequest){
        return buildErrorResponseEntity(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", exception, webRequest);
    }

    private ResponseEntity<ErrorResponseDto> buildErrorResponseEntity(HttpStatus status, String error,
                                                                      Exception exception, WebRequest webRequest) {
        return new ResponseEntity<>(buildErrorResponse(status, error, exception.getMessage(), webRequest, null), status);
    }

    private ErrorResponseDto buildErrorResponse(HttpStatus status, String error, String message,
                                                WebRequest webRequest, Map<String, String> validationErrors) {
        return new ErrorResponseDto(
                false,
                status.value(),
                error,
                message,
                extractPath(webRequest),
                validationErrors,
                LocalDateTime.now()
        );
    }

    private String extractPath(WebRequest webRequest) {
        return webRequest.getDescription(false).replace("uri=", "");
    }
}

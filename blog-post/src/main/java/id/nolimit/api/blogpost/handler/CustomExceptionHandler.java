package id.nolimit.api.blogpost.handler;

import id.nolimit.api.blogpost.model.BaseResponse;
import id.nolimit.api.blogpost.model.ValidationError;
import id.nolimit.api.blogpost.util.MessageStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<BaseResponse<?>> defaultErrorHandler(HttpServletRequest req, Exception e) {
        log.error(e);
        ValidationError validationError = ValidationError.builder()
                .fieldName(null)
                .value(null)
                .message(e.getLocalizedMessage())
                .build();
        List<ValidationError> errors = Collections.singletonList(validationError);
        BaseResponse<?> baseResponse = BaseResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(MessageStatus.FAILED)
                .errors(errors)
                .data(null)
                .build();
        return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<BaseResponse<?>> restException(HttpServletRequest req, ResponseStatusException e) {
        log.error(e);
        ValidationError validationError = ValidationError.builder()
                .fieldName(null)
                .value(null)
                .message(e.getReason())
                .build();
        List<ValidationError> errors = Collections.singletonList(validationError);
        BaseResponse<?> baseResponse = BaseResponse.builder()
                .code(e.getStatusCode().value())
                .message(MessageStatus.FAILED)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
        return new ResponseEntity<>(baseResponse, e.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?>> handleValidationErrors(HttpServletRequest req, MethodArgumentNotValidException ex) {
        log.error(ex);
        List<ValidationError> validationErrors = new ArrayList<>();
        for (FieldError fieldError : ex.getFieldErrors()) {
            ValidationError validationError = new ValidationError();
            validationError.setFieldName(fieldError.getField());
            validationError.setValue(fieldError.getRejectedValue());
            validationError.setMessage(fieldError.getDefaultMessage());
            validationErrors.add(validationError);
        }

        BaseResponse<?> baseResponse = BaseResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.name())
                .errors(validationErrors)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

}

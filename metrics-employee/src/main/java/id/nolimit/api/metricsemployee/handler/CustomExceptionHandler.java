package id.nolimit.api.metricsemployee.handler;

import id.nolimit.api.metricsemployee.constant.ConstantValue;
import id.nolimit.api.metricsemployee.model.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@Log4j2
public class CustomExceptionHandler {



    @ExceptionHandler(value = {Exception.class, IOException.class, RuntimeException.class})
    public ResponseEntity<BaseResponse<?>> defaultErrorHandler(HttpServletRequest req, Exception e) {
        log.error(e);
        BaseResponse<?> baseResponse = BaseResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ConstantValue.FAILED)
                .data(null)
                .build();
        return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

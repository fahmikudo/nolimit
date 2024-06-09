package id.nolimit.api.blogpost.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.nolimit.api.blogpost.model.BaseResponse;
import id.nolimit.api.blogpost.model.ValidationError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ForbiddenHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        ValidationError validationError = ValidationError.builder()
                .fieldName(null)
                .value(null)
                .message(accessDeniedException.getLocalizedMessage())
                .build();
        List<ValidationError> errors = Collections.singletonList(validationError);
        BaseResponse<?> baseResponse = BaseResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message(HttpStatus.FORBIDDEN.name())
                .errors(errors)
                .data(null)
                .build();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
    }
}

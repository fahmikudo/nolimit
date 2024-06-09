package id.nolimit.api.blogpost.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.nolimit.api.blogpost.model.BaseResponse;
import id.nolimit.api.blogpost.model.ValidationError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class UnauthorizedHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        ValidationError validationError = ValidationError.builder()
                .fieldName(null)
                .value(null)
                .message(authException.getLocalizedMessage())
                .build();
        List<ValidationError> errors = Collections.singletonList(validationError);
        BaseResponse<?> baseResponse = BaseResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(HttpStatus.UNAUTHORIZED.name())
                .errors(errors)
                .data(null)
                .build();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
    }
}

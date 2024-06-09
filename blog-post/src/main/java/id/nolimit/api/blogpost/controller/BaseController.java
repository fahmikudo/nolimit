package id.nolimit.api.blogpost.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.nolimit.api.blogpost.entity.User;
import id.nolimit.api.blogpost.exception.CustomRuntimeException;
import id.nolimit.api.blogpost.model.UserContext;
import id.nolimit.api.blogpost.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public abstract class BaseController {

    protected final AuthService authService;

    protected User getCurrentUser() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UserContext userContext = objectMapper.readValue(principal, UserContext.class);
            return authService.getUserActive(userContext.getEmail());
        } catch (JsonProcessingException e) {
            throw new CustomRuntimeException(e);
        }
    }

}

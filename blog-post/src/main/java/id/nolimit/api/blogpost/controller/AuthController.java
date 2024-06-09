package id.nolimit.api.blogpost.controller;

import id.nolimit.api.blogpost.model.BaseResponse;
import id.nolimit.api.blogpost.model.request.AuthLoginRequest;
import id.nolimit.api.blogpost.model.request.AuthRegistrationRequest;
import id.nolimit.api.blogpost.model.response.AuthLoginResponse;
import id.nolimit.api.blogpost.model.response.AuthRegistrationResponse;
import id.nolimit.api.blogpost.service.AuthService;
import id.nolimit.api.blogpost.util.MessageStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<AuthRegistrationResponse>> authUserRegistration(@RequestBody @Valid AuthRegistrationRequest request) {
        AuthRegistrationResponse registrationResponse = authService.userRegistration(request);
        BaseResponse<AuthRegistrationResponse> authRegistrationResponseBaseResponse = BaseResponse.<AuthRegistrationResponse>builder()
                .code(HttpStatus.OK.value())
                .message(MessageStatus.SUCCESS)
                .data(registrationResponse)
                .build();
        return new ResponseEntity<>(authRegistrationResponseBaseResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<AuthLoginResponse>> authLogin(@RequestBody @Valid AuthLoginRequest request) {
        AuthLoginResponse loginResponse = authService.authLogin(request);
        BaseResponse<AuthLoginResponse> authLoginResponseBaseResponse = BaseResponse.<AuthLoginResponse>builder()
                .code(HttpStatus.OK.value())
                .message(MessageStatus.SUCCESS)
                .data(loginResponse)
                .build();
        return new ResponseEntity<>(authLoginResponseBaseResponse, HttpStatus.OK);
    }

}

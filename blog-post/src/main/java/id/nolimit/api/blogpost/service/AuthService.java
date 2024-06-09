package id.nolimit.api.blogpost.service;

import id.nolimit.api.blogpost.entity.User;
import id.nolimit.api.blogpost.model.request.AuthLoginRequest;
import id.nolimit.api.blogpost.model.request.AuthRegistrationRequest;
import id.nolimit.api.blogpost.model.response.AuthLoginResponse;
import id.nolimit.api.blogpost.model.response.AuthRegistrationResponse;

public interface AuthService {

    AuthRegistrationResponse userRegistration(AuthRegistrationRequest request);

    AuthLoginResponse authLogin(AuthLoginRequest request);

    User getUserActive(String email);

}

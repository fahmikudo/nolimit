package id.nolimit.api.blogpost.service.impl;

import id.nolimit.api.blogpost.entity.LoginAbleUser;
import id.nolimit.api.blogpost.entity.User;
import id.nolimit.api.blogpost.model.request.AuthLoginRequest;
import id.nolimit.api.blogpost.model.request.AuthRegistrationRequest;
import id.nolimit.api.blogpost.model.response.AuthLoginResponse;
import id.nolimit.api.blogpost.model.response.AuthRegistrationResponse;
import id.nolimit.api.blogpost.repository.UserRepository;
import id.nolimit.api.blogpost.security.JWTUtil;
import id.nolimit.api.blogpost.service.AuthService;
import id.nolimit.api.blogpost.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = {Exception.class, ResponseStatusException.class})
    public AuthRegistrationResponse userRegistration(AuthRegistrationRequest request) {
        validatePassword(request);
        Optional<User> user = userRepository.findByEmail(request.email());
        if (user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use.");
        }
        User newUser = new User();
        newUser.setName(request.name());
        newUser.setEmail(request.email());

        String passwordEncoded = passwordEncoder.encode(request.password());
        newUser.setPassword(passwordEncoded);
        User savedUser = userRepository.save(newUser);

        return AuthRegistrationResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .build();
    }

    @Override
    public AuthLoginResponse authLogin(AuthLoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.email());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist.");
        }

        LoginAbleUser loginAbleUser = user.get();
        if (!passwordEncoder.matches(request.password(), loginAbleUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password.");
        }

        return AuthLoginResponse.builder()
                .token(jwtUtil.generateToken(loginAbleUser))
                .email(loginAbleUser.getEmail())
                .name(user.get().getName())
                .build();
    }

    private void validatePassword(AuthRegistrationRequest request) {
        if (StringUtil.isNullOrEmpty(request.password()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required.");
        if (!request.password().equals(request.passwordConfirmation()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password and password confirmation are not equal.");
    }
}

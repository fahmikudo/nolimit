package id.nolimit.api.blogpost.service;

import id.nolimit.api.blogpost.entity.User;
import id.nolimit.api.blogpost.model.request.AuthLoginRequest;
import id.nolimit.api.blogpost.model.request.AuthRegistrationRequest;
import id.nolimit.api.blogpost.model.response.AuthLoginResponse;
import id.nolimit.api.blogpost.model.response.AuthRegistrationResponse;
import id.nolimit.api.blogpost.repository.UserRepository;
import id.nolimit.api.blogpost.security.JWTUtil;
import id.nolimit.api.blogpost.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

class AuthServiceTest {

    private static final String ENCODED_PASSWORD = "$2a$10$1l4ONCkGwXdZvUoofaDu9exM8XQxQjeZyTm2dtiBN2P66csptD.He";

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTUtil jwtUtil;

    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthServiceImpl(userRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void givenNullPassword_whenExecuteUserRegistration_shouldGivenException() {
        AuthRegistrationRequest authRegistrationRequest = AuthRegistrationRequest.builder()
                .email("email@example.com")
                .name("Test Name")
                .password(null)
                .passwordConfirmation(null)
                .build();
        Exception e = Assertions.assertThrows(Exception.class, () -> authService.userRegistration(authRegistrationRequest));
        Assertions.assertTrue(e instanceof ResponseStatusException);
        Assertions.assertEquals("Password is required.", ((ResponseStatusException) e).getReason());
    }

    @Test
    void givenInvalidValuePassword_whenExecuteUserRegistration_shouldGivenException() {
        AuthRegistrationRequest authRegistrationRequest = AuthRegistrationRequest.builder()
                .email("email@example.com")
                .name("Test Name")
                .password("admin1234")
                .passwordConfirmation("nimda123")
                .build();
        Exception e = Assertions.assertThrows(Exception.class, () -> authService.userRegistration(authRegistrationRequest));
        Assertions.assertTrue(e instanceof ResponseStatusException);
        Assertions.assertEquals("Password and password confirmation are not equal.", ((ResponseStatusException) e).getReason());
    }

    @Test
    void givenEmailAlreadyExists_whenExecuteUserRegistration_shouldGivenException() {
        AuthRegistrationRequest authRegistrationRequest = AuthRegistrationRequest.builder()
                .email("email@example.com")
                .name("Test Name")
                .password("admin1234")
                .passwordConfirmation("admin1234")
                .build();
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(mockUser()));
        Exception e = Assertions.assertThrows(Exception.class, () -> authService.userRegistration(authRegistrationRequest));
        Assertions.assertTrue(e instanceof ResponseStatusException);
        Assertions.assertEquals("Email already in use.", ((ResponseStatusException) e).getReason());
    }

    @Test
    void givenValidRequest_whenExecuteUserRegistration_shouldGivenValue() {
        AuthRegistrationRequest authRegistrationRequest = AuthRegistrationRequest.builder()
                .email("email@example.com")
                .name("Test Name")
                .password("admin1234")
                .passwordConfirmation("admin1234")
                .build();
        Mockito.when(userRepository.save(any(User.class))).thenReturn(mockUser());
        AuthRegistrationResponse registrationResponse = authService.userRegistration(authRegistrationRequest);
        Assertions.assertEquals("email@example.com", registrationResponse.email());
        Assertions.assertEquals("Test Name", registrationResponse.name());
    }

    @Test
    void givenEmailNotFound_whenExecuteAuthLogin_shouldGivenException() {
        AuthLoginRequest authLoginRequest = AuthLoginRequest.builder()
                .email("email@example.com")
                .password("admin1234")
                .build();
        Mockito.when(userRepository.findByEmail(authLoginRequest.email())).thenReturn(Optional.empty());
        Exception e = Assertions.assertThrows(Exception.class, () -> authService.authLogin(authLoginRequest));
        Assertions.assertTrue(e instanceof ResponseStatusException);
        Assertions.assertEquals("User doesn't exist.", ((ResponseStatusException) e).getReason());
    }

    @Test
    void givenPasswordNotMatch_whenExecuteAuthLogin_shouldGivenException() {
        AuthLoginRequest authLoginRequest = AuthLoginRequest.builder()
                .email("email@example.com")
                .password("nimda1234")
                .build();
        Mockito.when(userRepository.findByEmail(authLoginRequest.email())).thenReturn(Optional.of(mockUser()));
        Exception e = Assertions.assertThrows(Exception.class, () -> authService.authLogin(authLoginRequest));
        Assertions.assertTrue(e instanceof ResponseStatusException);
        Assertions.assertEquals("Incorrect username or password.", ((ResponseStatusException) e).getReason());
    }

    @Test
    void givenValidRequest_whenExecuteAuthLogin_shouldGivenException() {
        AuthLoginRequest authLoginRequest = AuthLoginRequest.builder()
                .email("email@example.com")
                .password("admin1234")
                .build();
        User userWithEncodedPassword = mockUser();
        userWithEncodedPassword.setPassword(ENCODED_PASSWORD);
        Mockito.when(userRepository.findByEmail(authLoginRequest.email())).thenReturn(Optional.of(userWithEncodedPassword));
        Mockito.when(passwordEncoder.matches(authLoginRequest.password(), ENCODED_PASSWORD)).thenReturn(true);
        AuthLoginResponse authLoginResponse = authService.authLogin(authLoginRequest);
        Assertions.assertEquals("email@example.com", authLoginResponse.email());
        Assertions.assertEquals("Test Name", authLoginResponse.name());
    }

    private User mockUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@example.com");
        user.setName("Test Name");
        user.setPassword("admin1234");
        return user;
    }

}

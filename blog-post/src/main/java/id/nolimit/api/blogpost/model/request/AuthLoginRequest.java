package id.nolimit.api.blogpost.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AuthLoginRequest(
        @Email @NotNull String email,
        @NotNull String password
) {
}

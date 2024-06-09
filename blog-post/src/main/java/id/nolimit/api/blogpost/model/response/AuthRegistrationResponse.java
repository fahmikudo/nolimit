package id.nolimit.api.blogpost.model.response;

import lombok.Builder;

@Builder
public record AuthRegistrationResponse(
        Long id,
        String email,
        String name
) {
}

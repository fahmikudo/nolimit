package id.nolimit.api.blogpost.model.response;

import lombok.Builder;

@Builder
public record AuthLoginResponse(
        String token,
        String email,
        String name
) {

}

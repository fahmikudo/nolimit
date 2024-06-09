package id.nolimit.api.blogpost.model.response;

import lombok.Builder;

@Builder
public record PostResponse (
        Long id,
        String content,
        Long createdAt,
        Long updatedAt,
        Long authorId
) {
}

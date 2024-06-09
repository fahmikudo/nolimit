package id.nolimit.api.blogpost.model.request;

import lombok.Builder;

@Builder
public record PostRequest (
        String content
)  {
}

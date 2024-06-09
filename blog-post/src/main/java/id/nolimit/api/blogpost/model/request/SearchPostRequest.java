package id.nolimit.api.blogpost.model.request;

import id.nolimit.api.blogpost.model.PagingRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchPostRequest extends PagingRequest {

    @Builder(builderMethodName = "SearchPostRequestBuilder")
    public SearchPostRequest(@NotNull Integer page, @NotNull Integer size, String sort, String order) {
        super(page, size, sort, order);
    }
}

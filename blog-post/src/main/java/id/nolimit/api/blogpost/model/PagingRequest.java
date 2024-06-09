package id.nolimit.api.blogpost.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Builder
public class PagingRequest {

    @NotNull
    private Integer page;
    @NotNull
    private Integer size;
    private String sort;
    private String order;

    public PagingRequest(Integer page, Integer size, String sort, String order) {
        this.page = page;
        this.size = size;
        this.sort = sort;
        this.order = order;
    }
}

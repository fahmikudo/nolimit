package id.nolimit.api.blogpost.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ValidationError {

    private String fieldName;
    private Object value;
    private String message;

}

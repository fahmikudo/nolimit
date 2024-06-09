package id.nolimit.api.blogpost.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomRuntimeException extends RuntimeException {
    private final Throwable throwable;
    public CustomRuntimeException(Throwable throwable) {
        super(throwable);
        this.throwable = throwable;
    }
}

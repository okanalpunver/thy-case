package ThyCase.ThyCaseWS.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {
    private Instant timestamp = Instant.now();
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldError> fieldErrors;

    @Getter
    @Setter
    public static class FieldError {
        private String field;
        private String message;
    }
}

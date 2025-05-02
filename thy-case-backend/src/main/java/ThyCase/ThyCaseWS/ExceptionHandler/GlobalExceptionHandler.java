package ThyCase.ThyCaseWS.ExceptionHandler;

import ThyCase.ThyCaseWS.Dto.ErrorResponse;
import ThyCase.ThyCaseWS.Dto.ErrorResponse.FieldError;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.*;
import java.util.stream.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> onValidationError(MethodArgumentNotValidException ex,
                                                           WebRequest request) {
        BindingResult br = ex.getBindingResult();
        List<FieldError> fieldErrors = br.getFieldErrors().stream()
                .map(err -> {
                    FieldError fe = new FieldError();
                    fe.setField(err.getField());
                    fe.setMessage(err.getDefaultMessage());
                    return fe;
                })
                .collect(Collectors.toList());

        ErrorResponse body = new ErrorResponse();
        body.setStatus(HttpStatus.BAD_REQUEST.value());
        body.setError("Validation Failed");
        body.setMessage("One or more fields have errors");
        body.setFieldErrors(fieldErrors);
        body.setPath(request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> onNotFound(EntityNotFoundException ex,
                                                    WebRequest request) {
        ErrorResponse body = new ErrorResponse();
        body.setStatus(HttpStatus.NOT_FOUND.value());
        body.setError("Not Found");
        body.setMessage(ex.getMessage());
        body.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> onBadArg(IllegalArgumentException ex,
                                                  WebRequest request) {
        ErrorResponse body = new ErrorResponse();
        body.setStatus(HttpStatus.BAD_REQUEST.value());
        body.setError("Bad Request");
        body.setMessage(ex.getMessage());
        body.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> onGeneric(Exception ex,
                                                   WebRequest request) {
        ErrorResponse body = new ErrorResponse();
        body.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.setError("Internal Server Error");
        body.setMessage("An unexpected error occurred");
        body.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}

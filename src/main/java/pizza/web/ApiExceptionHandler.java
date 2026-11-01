package pizza.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pizza.customer.CustomerNotFoundException;
import pizza.product.ProductNotFoundException;

import java.util.Map;
import java.util.TreeMap;

// Übung a): fachliche Exceptions als ProblemDetail (RFC 9457) mit passendem Status statt HTTP 500.
// Übung b): ResponseEntityExceptionHandler liefert die Spring-MVC-Exceptions, etwa einen
// Validierungsfehler, ebenfalls als ProblemDetail; wir ergänzen dort die Feldfehler.
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ProductNotFoundException.class, CustomerNotFoundException.class})
    public ProblemDetail handleNotFound(RuntimeException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new TreeMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        ex.getBody().setProperty("errors", errors);
        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }
}

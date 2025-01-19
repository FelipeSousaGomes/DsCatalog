package br.devsuperior.dscatalog.controller.handlers;

import br.devsuperior.dscatalog.exceptions.DataBaseException;
import br.devsuperior.dscatalog.exceptions.NotFoundException;
import br.devsuperior.dscatalog.exceptions.StandardError;
import br.devsuperior.dscatalog.exceptions.ValidationError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

        @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardError> notFoundException(NotFoundException e, HttpServletRequest request) {
            StandardError err = new StandardError();
            err.setTimestamp(Instant.now());
            err.setStatus(HttpStatus.NOT_FOUND.value());
            err.setError("Resource not Found");
            err.setMessage(e.getMessage());
            err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardError> dataBaseException (DataBaseException e, HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError("Database exception");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation (MethodArgumentNotValidException e, HttpServletRequest request) {
       ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        err.setError("Validation exception");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
                    err.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

}
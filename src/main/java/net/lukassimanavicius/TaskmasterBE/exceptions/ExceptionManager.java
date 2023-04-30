package net.lukassimanavicius.TaskmasterBE.exceptions;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ExceptionManager {

    /**
     * Handles exceptions for bad IDs
     */
    @ExceptionHandler({EntityNotFoundException.class, MissingPathVariableException.class})
    private ResponseEntity handleBadID(Exception ex) {
        String message = "Bad id, please try again!";

        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity handleBadType(MethodArgumentTypeMismatchException exception) {
        String message = "Bad id, please try again!";

        Throwable cause = exception.getCause();
        if (cause instanceof ConversionFailedException) {
            cause = cause.getCause();
            if (cause instanceof IllegalArgumentException) {
                cause = cause.getCause();
                if (cause instanceof DateTimeParseException) {
                    DateTimeParseException dateException = (DateTimeParseException) cause;
                    message = "Invalid format: " + dateException.getParsedString() + ", please try again!";
                }
            }
        }
        return ResponseEntity.badRequest().body(message);
    }


    /**
     * Handles bad date and time format exceptions
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity handleDateParsing(DateTimeParseException exception) {
        String message = "Invalid format: " + exception.getParsedString() + ", please try again!";

        return ResponseEntity.badRequest().body(message);
    }

    /**
     * Handles bad time values exception
     */
    @ExceptionHandler(BadTimeException.class)
    public ResponseEntity handleBadTimeValues(BadTimeException exception) {
        String message = exception.getMessage();
        return ResponseEntity.badRequest().body(message);
    }

    /**
     * Handles exceptions for validation
     */
    @ExceptionHandler({ConstraintViolationException.class})
    private ResponseEntity handleBadValidation(ConstraintViolationException constraint) {
        String message = "";
        for (ConstraintViolation violation : constraint.getConstraintViolations()) {
            message = violation.getMessage();
        }

        return ResponseEntity.badRequest().body(message);
    }
}

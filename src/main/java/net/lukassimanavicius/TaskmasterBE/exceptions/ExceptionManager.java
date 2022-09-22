package net.lukassimanavicius.TaskmasterBE.exceptions;

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
    private ResponseEntity handleBadID() {
        return ResponseEntity.badRequest().body("Bad id, please try again!");
    }

    /**
     * Handles exceptions for bad types
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    private ResponseEntity handleTypeExceptions() {
        return ResponseEntity.badRequest().body("Wrong input, please try again!");
    }

    /**
     * Handles bad date and time format exceptions
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity handleDateParsing(DateTimeParseException exception) {
        String message = "Invalid format: " + exception.getParsedString() + " please try again!" ;

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

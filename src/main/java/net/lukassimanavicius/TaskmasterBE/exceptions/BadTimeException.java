package net.lukassimanavicius.TaskmasterBE.exceptions;

public class BadTimeException extends RuntimeException {

    public BadTimeException(String message) {
        super(message);
    }
}
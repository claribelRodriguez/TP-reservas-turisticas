package com.app.TPreservasturisticas.exception;

public class NoEncontradoException extends RuntimeException {
    public NoEncontradoException(String message) {
        super("404 " + message);
    }
}

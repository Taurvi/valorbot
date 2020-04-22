package com.github.taurvi.valor.exceptions;

public class ValorException extends RuntimeException {
    public ValorException(String message) {
        super(message);
    }

    public ValorException(Throwable error) {
        super(error);
    }
}

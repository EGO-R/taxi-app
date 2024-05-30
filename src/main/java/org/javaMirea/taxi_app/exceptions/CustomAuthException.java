package org.javaMirea.taxi_app.exceptions;

public class CustomAuthException extends RuntimeException{
    public CustomAuthException(String message) {
        super(message);
    }
}

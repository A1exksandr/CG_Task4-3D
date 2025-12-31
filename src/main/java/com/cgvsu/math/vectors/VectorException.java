package com.cgvsu.math.vectors;

public class VectorException extends Throwable {
    public VectorException(String message) {
        super(message);
    }
    public VectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
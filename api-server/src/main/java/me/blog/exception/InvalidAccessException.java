package me.blog.exception;

public class InvalidAccessException extends IllegalArgumentException {

    public InvalidAccessException(String s) {
        super(s);
    }
}

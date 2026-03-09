package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JsonSyntaxException_459_5Test {

    @Test
    @Timeout(8000)
    public void testConstructorWithMessage() {
        String message = "Syntax error";
        JsonSyntaxException exception = new JsonSyntaxException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithMessageAndCause() {
        String message = "Syntax error";
        Throwable cause = new RuntimeException("Root cause");
        JsonSyntaxException exception = new JsonSyntaxException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithCause() {
        Throwable cause = new RuntimeException("Root cause");
        JsonSyntaxException exception = new JsonSyntaxException(cause);
        assertEquals(cause.toString(), exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
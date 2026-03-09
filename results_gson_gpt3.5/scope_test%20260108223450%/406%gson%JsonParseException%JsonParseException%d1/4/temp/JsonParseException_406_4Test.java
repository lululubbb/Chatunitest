package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class JsonParseException_406_4Test {

    @Test
    @Timeout(8000)
    public void testConstructorWithMessage() {
        String message = "Test message";
        JsonParseException exception = new JsonParseException(message);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithMessageAndCause() {
        String message = "Test message";
        Throwable cause = new RuntimeException("Cause exception");
        JsonParseException exception = new JsonParseException(message, cause);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithCause() {
        Throwable cause = new RuntimeException("Cause exception");
        JsonParseException exception = new JsonParseException(cause);
        assertNotNull(exception);
        assertEquals(cause, exception.getCause());
    }

    @Test
    @Timeout(8000)
    public void testThrowingJsonParseException() {
        String message = "Throw test";
        Throwable cause = new RuntimeException("Cause");
        JsonParseException exception = assertThrows(JsonParseException.class, () -> {
            throw new JsonParseException(message, cause);
        });
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
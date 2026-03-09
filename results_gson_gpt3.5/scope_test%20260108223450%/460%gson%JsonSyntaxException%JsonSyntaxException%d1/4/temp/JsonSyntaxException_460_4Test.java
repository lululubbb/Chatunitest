package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JsonSyntaxException_460_4Test {

    @Test
    @Timeout(8000)
    public void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Cause exception");
        JsonSyntaxException ex = new JsonSyntaxException("Error message", cause);
        assertEquals("Error message", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithMessage() {
        JsonSyntaxException ex = new JsonSyntaxException("Only message");
        assertEquals("Only message", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithCause() {
        Throwable cause = new RuntimeException("Cause only");
        JsonSyntaxException ex = new JsonSyntaxException(cause);
        assertEquals(cause.toString(), ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
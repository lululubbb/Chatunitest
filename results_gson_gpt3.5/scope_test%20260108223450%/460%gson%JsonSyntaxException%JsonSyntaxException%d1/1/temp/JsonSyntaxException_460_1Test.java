package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JsonSyntaxException_460_1Test {

    @Test
    @Timeout(8000)
    public void testConstructor_MessageAndCause() {
        Throwable cause = new RuntimeException("root cause");
        JsonSyntaxException ex = new JsonSyntaxException("error message", cause);
        assertEquals("error message", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_MessageOnly() {
        JsonSyntaxException ex = new JsonSyntaxException("error message only");
        assertEquals("error message only", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_CauseOnly() {
        Throwable cause = new RuntimeException("root cause only");
        JsonSyntaxException ex = new JsonSyntaxException(cause);
        assertEquals(cause.toString(), ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
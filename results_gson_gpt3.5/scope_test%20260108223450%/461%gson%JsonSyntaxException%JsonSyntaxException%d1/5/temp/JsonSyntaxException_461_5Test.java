package com.google.gson;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonSyntaxException;

public class JsonSyntaxException_461_5Test {

    @Test
    @Timeout(8000)
    public void testConstructorWithThrowable() {
        Throwable cause = new RuntimeException("Root cause");
        JsonSyntaxException ex = new JsonSyntaxException(cause);
        assertNotNull(ex);
        assertEquals(cause, ex.getCause());
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithMessage() {
        String msg = "Syntax error";
        JsonSyntaxException ex = new JsonSyntaxException(msg);
        assertNotNull(ex);
        assertEquals(msg, ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithMessageAndThrowable() {
        String msg = "Syntax error";
        Throwable cause = new RuntimeException("Root cause");
        JsonSyntaxException ex = new JsonSyntaxException(msg, cause);
        assertNotNull(ex);
        assertEquals(msg, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
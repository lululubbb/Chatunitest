package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.StringReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_224_5Test {

    private JsonReader jsonReader;
    private Method syntaxErrorMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        jsonReader = new JsonReader(new StringReader(""));
        syntaxErrorMethod = JsonReader.class.getDeclaredMethod("syntaxError", String.class);
        syntaxErrorMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void syntaxError_shouldThrowMalformedJsonExceptionWithMessageAndLocation() throws Throwable {
        String message = "Test error message";

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            try {
                syntaxErrorMethod.invoke(jsonReader, message);
            } catch (InvocationTargetException e) {
                throw e;
            }
        });

        Throwable cause = thrown.getCause();
        // The method throws MalformedJsonException which extends IOException
        // Check that cause is instance of MalformedJsonException and message contains input message
        assertEquals(MalformedJsonException.class, cause.getClass());
        String causeMessage = cause.getMessage();
        // The message should start with the input message and include location string
        org.junit.jupiter.api.Assertions.assertTrue(causeMessage.startsWith(message));
        org.junit.jupiter.api.Assertions.assertTrue(causeMessage.length() > message.length());
    }
}
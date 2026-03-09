package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_224_3Test {

    private JsonReader jsonReader;
    private Method syntaxErrorMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        Reader reader = new StringReader("");
        jsonReader = new JsonReader(reader);
        syntaxErrorMethod = JsonReader.class.getDeclaredMethod("syntaxError", String.class);
        syntaxErrorMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testSyntaxErrorThrowsMalformedJsonExceptionWithMessageAndLocation() {
        String message = "Test error message";

        try {
            syntaxErrorMethod.invoke(jsonReader, message);
            fail("Expected IOException to be thrown");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof MalformedJsonException, "Expected MalformedJsonException");
            String expectedStart = message;
            String actualMessage = cause.getMessage();
            assertTrue(actualMessage.startsWith(expectedStart), 
                "Exception message should start with the input message");
            // The message should contain the location string appended
            String locationString = null;
            try {
                Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
                locationStringMethod.setAccessible(true);
                locationString = (String) locationStringMethod.invoke(jsonReader);
            } catch (Exception ex) {
                // ignore - locationString is private, but we can skip this check if inaccessible
            }
            if (locationString != null) {
                assertTrue(actualMessage.endsWith(locationString), 
                    "Exception message should end with location string");
            }
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException thrown: " + e.getMessage());
        }
    }
}
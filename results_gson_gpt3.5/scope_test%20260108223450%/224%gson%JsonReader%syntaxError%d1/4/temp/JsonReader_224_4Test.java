package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_224_4Test {

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
    public void testSyntaxErrorThrowsMalformedJsonExceptionWithLocationString() {
        // We invoke the private method syntaxError with a test message and expect a MalformedJsonException
        String testMessage = "Test error message: ";
        try {
            syntaxErrorMethod.invoke(jsonReader, testMessage);
            fail("Expected MalformedJsonException to be thrown");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof MalformedJsonException, "Exception should be MalformedJsonException");
            String message = cause.getMessage();
            // The message should start with the testMessage and include locationString()
            assertTrue(message.startsWith(testMessage), "Message should start with the test message");
            // locationString() is package-private, so we call it via reflection to verify the message contains it
            try {
                Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
                locationStringMethod.setAccessible(true);
                String location = (String) locationStringMethod.invoke(jsonReader);
                assertTrue(message.endsWith(location), "Message should end with location string");
            } catch (Exception ex) {
                fail("Failed to invoke locationString method via reflection");
            }
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }
}
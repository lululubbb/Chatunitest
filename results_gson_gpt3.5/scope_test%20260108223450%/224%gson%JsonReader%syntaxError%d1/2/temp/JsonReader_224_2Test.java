package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class JsonReader_224_2Test {

    @Test
    @Timeout(8000)
    void syntaxError_shouldThrowMalformedJsonExceptionWithMessageAndLocation() throws Exception {
        // Arrange
        JsonReader jsonReader = new JsonReader(new java.io.StringReader(""));
        String message = "Test error message";

        // Use reflection to access private method syntaxError
        Method syntaxErrorMethod = JsonReader.class.getDeclaredMethod("syntaxError", String.class);
        syntaxErrorMethod.setAccessible(true);

        // Also access locationString method to get the appended string
        Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
        locationStringMethod.setAccessible(true);
        String location = (String) locationStringMethod.invoke(jsonReader);

        // Act & Assert
        IOException thrown = assertThrows(IOException.class, () -> {
            try {
                syntaxErrorMethod.invoke(jsonReader, message);
            } catch (InvocationTargetException e) {
                // unwrap the cause
                throw e.getCause();
            }
        });
        assertTrue(thrown instanceof MalformedJsonException);
        assertEquals(message + location, thrown.getMessage());
    }
}
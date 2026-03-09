package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonElement_593_4Test {

    private JsonElement jsonElement;

    @BeforeEach
    void setUp() {
        jsonElement = new JsonElement() {
            @Override
            public JsonElement deepCopy() {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void toString_returnsJsonString_whenNoException() throws IOException {
        try (MockedStatic<Streams> streamsMockedStatic = mockStatic(Streams.class)) {
            // Arrange
            streamsMockedStatic.when(() -> Streams.write(any(JsonElement.class), any(JsonWriter.class)))
                    .thenAnswer(invocation -> {
                        JsonWriter writer = invocation.getArgument(1);
                        writer.value("mocked");
                        return null;
                    });

            // Act
            String result = jsonElement.toString();

            // Assert
            assertEquals("\"mocked\"", result);
            streamsMockedStatic.verify(() -> Streams.write(eq(jsonElement), any(JsonWriter.class)));
        }
    }

    @Test
    @Timeout(8000)
    void toString_throwsAssertionError_whenIOException() throws IOException {
        try (MockedStatic<Streams> streamsMockedStatic = mockStatic(Streams.class)) {
            // Arrange
            streamsMockedStatic.when(() -> Streams.write(any(JsonElement.class), any(JsonWriter.class)))
                    .thenThrow(new IOException("io exception"));

            // Act & Assert
            AssertionError error = assertThrows(AssertionError.class, () -> jsonElement.toString());
            assertTrue(error.getCause() instanceof IOException);
            assertEquals("io exception", error.getCause().getMessage());
            streamsMockedStatic.verify(() -> Streams.write(eq(jsonElement), any(JsonWriter.class)));
        }
    }
}
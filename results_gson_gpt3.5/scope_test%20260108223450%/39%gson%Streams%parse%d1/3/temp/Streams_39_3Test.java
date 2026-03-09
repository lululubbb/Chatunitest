package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.EOFException;
import java.io.IOException;

public class Streams_39_3Test {

    private JsonReader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    public void testParse_successfulRead() throws Exception {
        // Arrange
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);
        JsonElement expectedElement = mock(JsonElement.class);

        try (MockedStatic<TypeAdapters> typeAdaptersStatic = mockStatic(TypeAdapters.class)) {
            typeAdaptersStatic.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader)).thenReturn(expectedElement);

            // Act
            JsonElement actual = Streams.parse(mockReader);

            // Assert
            assertSame(expectedElement, actual);
            verify(mockReader, atLeastOnce()).peek();
            // Correct verification: verify the static method call by invoking it exactly once
            typeAdaptersStatic.verify(() -> TypeAdapters.JSON_ELEMENT.read(any(JsonReader.class)), times(1));
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_eofExceptionWhenEmpty_returnsJsonNull() throws Exception {
        // Arrange
        doThrow(new EOFException()).when(mockReader).peek();

        // Act
        JsonElement result = Streams.parse(mockReader);

        // Assert
        assertSame(JsonNull.INSTANCE, result);
        verify(mockReader, atLeastOnce()).peek();
    }

    @Test
    @Timeout(8000)
    public void testParse_eofExceptionWhenNotEmpty_throwsJsonSyntaxException() throws Exception {
        // Arrange
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);

        try (MockedStatic<TypeAdapters> typeAdaptersStatic = mockStatic(TypeAdapters.class)) {
            typeAdaptersStatic.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader))
                    .thenThrow(new EOFException());

            // Act & Assert
            JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
            assertTrue(ex.getCause() instanceof EOFException);
            verify(mockReader, atLeastOnce()).peek();
            // Correct verification: verify the static method call by invoking it exactly once
            typeAdaptersStatic.verify(() -> TypeAdapters.JSON_ELEMENT.read(any(JsonReader.class)), times(1));
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_malformedJsonException_throwsJsonSyntaxException() throws Exception {
        // Arrange
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);

        try (MockedStatic<TypeAdapters> typeAdaptersStatic = mockStatic(TypeAdapters.class)) {
            typeAdaptersStatic.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader))
                    .thenThrow(new MalformedJsonException("malformed"));

            // Act & Assert
            JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
            assertTrue(ex.getCause() instanceof MalformedJsonException);
            verify(mockReader, atLeastOnce()).peek();
            // Correct verification: verify the static method call by invoking it exactly once
            typeAdaptersStatic.verify(() -> TypeAdapters.JSON_ELEMENT.read(any(JsonReader.class)), times(1));
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_ioException_throwsJsonIOException() throws Exception {
        // Arrange
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);

        try (MockedStatic<TypeAdapters> typeAdaptersStatic = mockStatic(TypeAdapters.class)) {
            typeAdaptersStatic.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader))
                    .thenThrow(new IOException("io"));

            // Act & Assert
            JsonIOException ex = assertThrows(JsonIOException.class, () -> Streams.parse(mockReader));
            assertTrue(ex.getCause() instanceof IOException);
            verify(mockReader, atLeastOnce()).peek();
            // Correct verification: verify the static method call by invoking it exactly once
            typeAdaptersStatic.verify(() -> TypeAdapters.JSON_ELEMENT.read(any(JsonReader.class)), times(1));
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_numberFormatException_throwsJsonSyntaxException() throws Exception {
        // Arrange
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);

        try (MockedStatic<TypeAdapters> typeAdaptersStatic = mockStatic(TypeAdapters.class)) {
            typeAdaptersStatic.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader))
                    .thenAnswer(invocation -> {
                        // Simulate NumberFormatException during reading without calling real method
                        throw new NumberFormatException("number format");
                    });

            // Act & Assert
            JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
            assertTrue(ex.getCause() instanceof NumberFormatException);
            verify(mockReader, atLeastOnce()).peek();
            // Correct verification: verify the static method call by invoking it exactly once
            typeAdaptersStatic.verify(() -> TypeAdapters.JSON_ELEMENT.read(any(JsonReader.class)), times(1));
        }
    }
}
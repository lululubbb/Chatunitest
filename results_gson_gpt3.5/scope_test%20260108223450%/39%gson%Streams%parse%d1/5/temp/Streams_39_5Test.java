package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Method;

public class Streams_39_5Test {

    private JsonReader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    public void testParse_ReturnsJsonElement_WhenNoException() throws Exception {
        // Arrange
        // No need to mock peek() for non-void method, just leave it as default (does nothing)
        // JsonReader.peek() returns JsonToken, so we mock it to return a token (e.g. BEGIN_OBJECT)
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);

        JsonElement expectedElement = mock(JsonElement.class);

        try (MockedStatic<TypeAdapters> mockedTypeAdapters = Mockito.mockStatic(TypeAdapters.class)) {
            mockedTypeAdapters.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader)).thenReturn(expectedElement);

            // Act
            JsonElement result = Streams.parse(mockReader);

            // Assert
            assertSame(expectedElement, result);
            verify(mockReader).peek();
            mockedTypeAdapters.verify(() -> TypeAdapters.JSON_ELEMENT.read(mockReader));
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_ReturnsJsonNull_WhenEOFExceptionAndEmpty() throws Exception {
        // Arrange
        when(mockReader.peek()).thenThrow(new EOFException());

        // Act
        JsonElement result = Streams.parse(mockReader);

        // Assert
        assertSame(JsonNull.INSTANCE, result);
        verify(mockReader).peek();
    }

    @Test
    @Timeout(8000)
    public void testParse_ThrowsJsonSyntaxException_WhenEOFExceptionAndNotEmpty() throws Exception {
        // Arrange
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);

        try (MockedStatic<TypeAdapters> mockedTypeAdapters = Mockito.mockStatic(TypeAdapters.class)) {
            mockedTypeAdapters.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader)).thenThrow(new EOFException());

            // Act & Assert
            JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
            assertTrue(ex.getCause() instanceof EOFException);

            verify(mockReader).peek();
            mockedTypeAdapters.verify(() -> TypeAdapters.JSON_ELEMENT.read(mockReader));
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_ThrowsJsonSyntaxException_WhenMalformedJsonException() throws Exception {
        // Arrange
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);

        try (MockedStatic<TypeAdapters> mockedTypeAdapters = Mockito.mockStatic(TypeAdapters.class)) {
            mockedTypeAdapters.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader)).thenThrow(new MalformedJsonException("malformed"));

            // Act & Assert
            JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
            assertTrue(ex.getCause() instanceof MalformedJsonException);

            verify(mockReader).peek();
            mockedTypeAdapters.verify(() -> TypeAdapters.JSON_ELEMENT.read(mockReader));
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_ThrowsJsonIOException_WhenIOException() throws Exception {
        // Arrange
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);

        try (MockedStatic<TypeAdapters> mockedTypeAdapters = Mockito.mockStatic(TypeAdapters.class)) {
            mockedTypeAdapters.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader)).thenThrow(new IOException("io error"));

            // Act & Assert
            JsonIOException ex = assertThrows(JsonIOException.class, () -> Streams.parse(mockReader));
            assertTrue(ex.getCause() instanceof IOException);

            verify(mockReader).peek();
            mockedTypeAdapters.verify(() -> TypeAdapters.JSON_ELEMENT.read(mockReader));
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_ThrowsJsonSyntaxException_WhenNumberFormatException() throws Exception {
        // Arrange
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);

        try (MockedStatic<TypeAdapters> mockedTypeAdapters = Mockito.mockStatic(TypeAdapters.class)) {
            mockedTypeAdapters.when(() -> TypeAdapters.JSON_ELEMENT.read(mockReader)).thenThrow(new NumberFormatException("number format"));

            // Act & Assert
            JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
            assertTrue(ex.getCause() instanceof NumberFormatException);

            verify(mockReader).peek();
            mockedTypeAdapters.verify(() -> TypeAdapters.JSON_ELEMENT.read(mockReader));
        }
    }
}
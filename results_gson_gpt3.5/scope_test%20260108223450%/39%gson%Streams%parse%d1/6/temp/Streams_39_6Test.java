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
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Streams_39_6Test {

    private JsonReader mockReader;

    @BeforeEach
    public void setup() {
        mockReader = mock(JsonReader.class);
    }

    private void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    @Test
    @Timeout(8000)
    public void testParse_ValidJsonElementReturned() throws Exception {
        // Arrange
        JsonElement expectedElement = mock(JsonElement.class);

        @SuppressWarnings("unchecked")
        com.google.gson.TypeAdapter<JsonElement> mockAdapter = mock(com.google.gson.TypeAdapter.class);

        Field jsonElementField = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
        setFinalStatic(jsonElementField, mockAdapter);

        when(mockAdapter.read(mockReader)).thenReturn(expectedElement);
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);

        // Act
        JsonElement result = Streams.parse(mockReader);

        // Assert
        assertSame(expectedElement, result);
        verify(mockReader).peek();
        verify(mockAdapter).read(mockReader);
    }

    @Test
    @Timeout(8000)
    public void testParse_EmptyDocument_ReturnsJsonNull() throws Exception {
        // Arrange
        doThrow(new EOFException()).when(mockReader).peek();

        // Act
        JsonElement result = Streams.parse(mockReader);

        // Assert
        assertSame(JsonNull.INSTANCE, result);
        verify(mockReader).peek();
    }

    @Test
    @Timeout(8000)
    public void testParse_EOFExceptionThrownAfterPeek() throws Exception {
        // Arrange
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);

        @SuppressWarnings("unchecked")
        com.google.gson.TypeAdapter<JsonElement> mockAdapter = mock(com.google.gson.TypeAdapter.class);
        Field jsonElementField = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
        setFinalStatic(jsonElementField, mockAdapter);
        when(mockAdapter.read(mockReader)).thenThrow(new EOFException());

        // Act & Assert
        JsonParseException ex = assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
        assertTrue(ex.getCause() instanceof EOFException);
        verify(mockReader).peek();
        verify(mockAdapter).read(mockReader);
    }

    @Test
    @Timeout(8000)
    public void testParse_MalformedJsonExceptionThrown() throws Exception {
        // Arrange
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);

        @SuppressWarnings("unchecked")
        com.google.gson.TypeAdapter<JsonElement> mockAdapter = mock(com.google.gson.TypeAdapter.class);
        Field jsonElementField = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
        setFinalStatic(jsonElementField, mockAdapter);
        when(mockAdapter.read(mockReader)).thenThrow(new MalformedJsonException("malformed"));

        // Act & Assert
        JsonParseException ex = assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
        assertTrue(ex.getCause() instanceof MalformedJsonException);
        verify(mockReader).peek();
        verify(mockAdapter).read(mockReader);
    }

    @Test
    @Timeout(8000)
    public void testParse_IOExceptionThrown() throws Exception {
        // Arrange
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);

        @SuppressWarnings("unchecked")
        com.google.gson.TypeAdapter<JsonElement> mockAdapter = mock(com.google.gson.TypeAdapter.class);
        Field jsonElementField = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
        setFinalStatic(jsonElementField, mockAdapter);
        when(mockAdapter.read(mockReader)).thenThrow(new IOException("io"));

        // Act & Assert
        JsonIOException ex = assertThrows(JsonIOException.class, () -> Streams.parse(mockReader));
        assertTrue(ex.getCause() instanceof IOException);
        verify(mockReader).peek();
        verify(mockAdapter).read(mockReader);
    }

    @Test
    @Timeout(8000)
    public void testParse_NumberFormatExceptionThrown() throws Exception {
        // Arrange
        when(mockReader.peek()).thenReturn(com.google.gson.stream.JsonToken.BEGIN_OBJECT);

        @SuppressWarnings("unchecked")
        com.google.gson.TypeAdapter<JsonElement> mockAdapter = mock(com.google.gson.TypeAdapter.class);
        Field jsonElementField = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
        setFinalStatic(jsonElementField, mockAdapter);
        when(mockAdapter.read(mockReader)).thenThrow(new NumberFormatException("number"));

        // Act & Assert
        JsonParseException ex = assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
        assertTrue(ex.getCause() instanceof NumberFormatException);
        verify(mockReader).peek();
        verify(mockAdapter).read(mockReader);
    }
}
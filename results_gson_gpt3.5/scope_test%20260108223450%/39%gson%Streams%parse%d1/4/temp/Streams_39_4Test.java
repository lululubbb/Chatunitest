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
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Streams_39_4Test {

    JsonReader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(JsonReader.class);
    }

    private static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    @Test
    @Timeout(8000)
    public void testParse_ReturnsJsonElement_WhenReaderNotEmpty() throws Exception {
        // Arrange
        doReturn(JsonToken.BEGIN_OBJECT).when(mockReader).peek();
        JsonElement expectedElement = mock(JsonElement.class);

        Field jsonElementField = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");

        Object originalJsonElementAdapter = jsonElementField.get(null);

        try {
            Object adapterMock = Mockito.mock(originalJsonElementAdapter.getClass());
            setFinalStatic(jsonElementField, adapterMock);

            @SuppressWarnings("unchecked")
            com.google.gson.TypeAdapter<JsonElement> typedAdapterMock = (com.google.gson.TypeAdapter<JsonElement>) adapterMock;

            when(typedAdapterMock.read(mockReader)).thenReturn(expectedElement);

            // Act
            JsonElement result = Streams.parse(mockReader);

            // Assert
            assertSame(expectedElement, result);
        } finally {
            setFinalStatic(jsonElementField, originalJsonElementAdapter);
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_ReturnsJsonNull_WhenEmptyDocument() throws Exception {
        // Arrange
        doThrow(new EOFException()).when(mockReader).peek();

        // Act
        JsonElement result = Streams.parse(mockReader);

        // Assert
        assertSame(JsonNull.INSTANCE, result);
    }

    @Test
    @Timeout(8000)
    public void testParse_ThrowsJsonSyntaxException_OnPrematureEOF() throws Exception {
        // Arrange
        JsonReader spyReader = spy(mockReader);
        doReturn(JsonToken.BEGIN_OBJECT).doThrow(new EOFException()).when(spyReader).peek();

        Field jsonElementField = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
        Object originalJsonElementAdapter = jsonElementField.get(null);

        try {
            Object adapterMock = Mockito.mock(originalJsonElementAdapter.getClass());
            setFinalStatic(jsonElementField, adapterMock);

            @SuppressWarnings("unchecked")
            com.google.gson.TypeAdapter<JsonElement> typedAdapterMock = (com.google.gson.TypeAdapter<JsonElement>) adapterMock;

            when(typedAdapterMock.read(spyReader)).thenThrow(new EOFException());

            // Act & Assert
            assertThrows(JsonSyntaxException.class, () -> Streams.parse(spyReader));
        } finally {
            setFinalStatic(jsonElementField, originalJsonElementAdapter);
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_ThrowsJsonSyntaxException_OnMalformedJsonException() throws Exception {
        // Arrange
        doReturn(JsonToken.BEGIN_OBJECT).when(mockReader).peek();

        Field jsonElementField = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
        Object originalJsonElementAdapter = jsonElementField.get(null);

        try {
            Object adapterMock = Mockito.mock(originalJsonElementAdapter.getClass());
            setFinalStatic(jsonElementField, adapterMock);

            @SuppressWarnings("unchecked")
            com.google.gson.TypeAdapter<JsonElement> typedAdapterMock = (com.google.gson.TypeAdapter<JsonElement>) adapterMock;

            when(typedAdapterMock.read(mockReader)).thenThrow(new MalformedJsonException("malformed"));

            // Act & Assert
            assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
        } finally {
            setFinalStatic(jsonElementField, originalJsonElementAdapter);
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_ThrowsJsonIOException_OnIOException() throws Exception {
        // Arrange
        doReturn(JsonToken.BEGIN_OBJECT).when(mockReader).peek();

        Field jsonElementField = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
        Object originalJsonElementAdapter = jsonElementField.get(null);

        try {
            Object adapterMock = Mockito.mock(originalJsonElementAdapter.getClass());
            setFinalStatic(jsonElementField, adapterMock);

            @SuppressWarnings("unchecked")
            com.google.gson.TypeAdapter<JsonElement> typedAdapterMock = (com.google.gson.TypeAdapter<JsonElement>) adapterMock;

            when(typedAdapterMock.read(mockReader)).thenThrow(new IOException("io"));

            // Act & Assert
            assertThrows(JsonIOException.class, () -> Streams.parse(mockReader));
        } finally {
            setFinalStatic(jsonElementField, originalJsonElementAdapter);
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_ThrowsJsonSyntaxException_OnNumberFormatException() throws Exception {
        // Arrange
        doReturn(JsonToken.BEGIN_OBJECT).when(mockReader).peek();

        Field jsonElementField = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
        Object originalJsonElementAdapter = jsonElementField.get(null);

        try {
            Object adapterMock = Mockito.mock(originalJsonElementAdapter.getClass());
            setFinalStatic(jsonElementField, adapterMock);

            @SuppressWarnings("unchecked")
            com.google.gson.TypeAdapter<JsonElement> typedAdapterMock = (com.google.gson.TypeAdapter<JsonElement>) adapterMock;

            when(typedAdapterMock.read(mockReader)).thenThrow(new NumberFormatException("number format"));

            // Act & Assert
            assertThrows(JsonSyntaxException.class, () -> Streams.parse(mockReader));
        } finally {
            setFinalStatic(jsonElementField, originalJsonElementAdapter);
        }
    }
}
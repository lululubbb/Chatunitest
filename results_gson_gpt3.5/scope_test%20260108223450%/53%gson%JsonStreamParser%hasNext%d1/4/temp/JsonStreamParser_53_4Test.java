package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonStreamParser_53_4Test {

    private JsonReader mockJsonReader;
    private JsonStreamParser jsonStreamParser;

    @BeforeEach
    void setUp() throws Exception {
        mockJsonReader = mock(JsonReader.class);
        jsonStreamParser = new JsonStreamParser(new StringReader(""));

        // Use reflection to inject the mock JsonReader into the private final field 'parser'
        Field parserField = JsonStreamParser.class.getDeclaredField("parser");
        parserField.setAccessible(true);
        parserField.set(jsonStreamParser, mockJsonReader);

        // Use reflection to get the 'lock' field and set it to a new Object for synchronization
        Field lockField = JsonStreamParser.class.getDeclaredField("lock");
        lockField.setAccessible(true);
        lockField.set(jsonStreamParser, new Object());
    }

    @Test
    @Timeout(8000)
    void hasNext_returnsTrue_whenPeekIsNotEndDocument() throws IOException {
        when(mockJsonReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);

        boolean result = jsonStreamParser.hasNext();

        assertTrue(result);
        verify(mockJsonReader, times(1)).peek();
    }

    @Test
    @Timeout(8000)
    void hasNext_returnsFalse_whenPeekIsEndDocument() throws IOException {
        when(mockJsonReader.peek()).thenReturn(JsonToken.END_DOCUMENT);

        boolean result = jsonStreamParser.hasNext();

        assertFalse(result);
        verify(mockJsonReader, times(1)).peek();
    }

    @Test
    @Timeout(8000)
    void hasNext_throwsJsonSyntaxException_whenMalformedJsonExceptionThrown() throws IOException {
        when(mockJsonReader.peek()).thenThrow(new MalformedJsonException("Malformed"));

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> jsonStreamParser.hasNext());

        assertNotNull(thrown);
        assertTrue(thrown.getCause() instanceof MalformedJsonException);
        verify(mockJsonReader, times(1)).peek();
    }

    @Test
    @Timeout(8000)
    void hasNext_throwsJsonIOException_whenIOExceptionThrown() throws IOException {
        when(mockJsonReader.peek()).thenThrow(new IOException("IO error"));

        JsonIOException thrown = assertThrows(JsonIOException.class, () -> jsonStreamParser.hasNext());

        assertNotNull(thrown);
        assertTrue(thrown.getCause() instanceof IOException);
        verify(mockJsonReader, times(1)).peek();
    }
}
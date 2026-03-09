package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonStreamParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonStreamParser_53_3Test {

    private JsonStreamParser jsonStreamParser;
    private JsonReader mockJsonReader;
    private final Object lock = new Object();

    @BeforeEach
    public void setUp() throws Exception {
        mockJsonReader = mock(JsonReader.class);
        // Create JsonStreamParser instance using constructor with dummy String
        jsonStreamParser = new JsonStreamParser("{}");
        // Inject mockJsonReader into jsonStreamParser.parser field via reflection
        Field parserField = JsonStreamParser.class.getDeclaredField("parser");
        parserField.setAccessible(true);
        parserField.set(jsonStreamParser, mockJsonReader);
        // Inject lock field to the same lock object to control synchronization
        Field lockField = JsonStreamParser.class.getDeclaredField("lock");
        lockField.setAccessible(true);
        lockField.set(jsonStreamParser, lock);
    }

    @Test
    @Timeout(8000)
    public void testHasNextReturnsTrueWhenNotEndDocument() throws Exception {
        when(mockJsonReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);

        boolean result = jsonStreamParser.hasNext();

        assertTrue(result);
        verify(mockJsonReader, times(1)).peek();
    }

    @Test
    @Timeout(8000)
    public void testHasNextReturnsFalseWhenEndDocument() throws Exception {
        when(mockJsonReader.peek()).thenReturn(JsonToken.END_DOCUMENT);

        boolean result = jsonStreamParser.hasNext();

        assertFalse(result);
        verify(mockJsonReader, times(1)).peek();
    }

    @Test
    @Timeout(8000)
    public void testHasNextThrowsJsonSyntaxExceptionOnMalformedJsonException() throws Exception {
        MalformedJsonException malformedJsonException = new MalformedJsonException("Malformed JSON");
        when(mockJsonReader.peek()).thenThrow(malformedJsonException);

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
            jsonStreamParser.hasNext();
        });

        assertSame(malformedJsonException, thrown.getCause());
        verify(mockJsonReader, times(1)).peek();
    }

    @Test
    @Timeout(8000)
    public void testHasNextThrowsJsonIOExceptionOnIOException() throws Exception {
        IOException ioException = new IOException("IO error");
        when(mockJsonReader.peek()).thenThrow(ioException);

        JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
            jsonStreamParser.hasNext();
        });

        assertSame(ioException, thrown.getCause());
        verify(mockJsonReader, times(1)).peek();
    }
}
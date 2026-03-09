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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;

public class JsonStreamParser_53_5Test {

    private JsonStreamParser jsonStreamParser;
    private JsonReader mockJsonReader;
    private final Object lock = new Object();

    @BeforeEach
    public void setup() throws Exception {
        // Create a real JsonStreamParser with dummy input
        jsonStreamParser = new JsonStreamParser("[]");

        // Create mock JsonReader
        mockJsonReader = mock(JsonReader.class);

        // Use reflection to inject the mockJsonReader into jsonStreamParser
        Field parserField = JsonStreamParser.class.getDeclaredField("parser");
        parserField.setAccessible(true);
        parserField.set(jsonStreamParser, mockJsonReader);

        // Use reflection to inject the lock object from jsonStreamParser
        Field lockField = JsonStreamParser.class.getDeclaredField("lock");
        lockField.setAccessible(true);
        lockField.set(jsonStreamParser, lock);
    }

    @Test
    @Timeout(8000)
    public void testHasNextReturnsTrueWhenNotEndDocument() throws Exception {
        synchronized (lock) {
            when(mockJsonReader.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
            boolean result = jsonStreamParser.hasNext();
            assertTrue(result);
            verify(mockJsonReader, times(1)).peek();
        }
    }

    @Test
    @Timeout(8000)
    public void testHasNextReturnsFalseWhenEndDocument() throws Exception {
        synchronized (lock) {
            when(mockJsonReader.peek()).thenReturn(JsonToken.END_DOCUMENT);
            boolean result = jsonStreamParser.hasNext();
            assertFalse(result);
            verify(mockJsonReader, times(1)).peek();
        }
    }

    @Test
    @Timeout(8000)
    public void testHasNextThrowsJsonSyntaxExceptionOnMalformedJsonException() throws Exception {
        synchronized (lock) {
            when(mockJsonReader.peek()).thenThrow(new MalformedJsonException("malformed"));
            JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> jsonStreamParser.hasNext());
            assertTrue(thrown.getCause() instanceof MalformedJsonException);
            verify(mockJsonReader, times(1)).peek();
        }
    }

    @Test
    @Timeout(8000)
    public void testHasNextThrowsJsonIOExceptionOnIOException() throws Exception {
        synchronized (lock) {
            when(mockJsonReader.peek()).thenThrow(new IOException("io error"));
            JsonIOException thrown = assertThrows(JsonIOException.class, () -> jsonStreamParser.hasNext());
            assertTrue(thrown.getCause() instanceof IOException);
            verify(mockJsonReader, times(1)).peek();
        }
    }
}
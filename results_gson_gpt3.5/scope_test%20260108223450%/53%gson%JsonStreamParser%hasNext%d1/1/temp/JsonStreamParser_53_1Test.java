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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JsonStreamParser_53_1Test {

    private JsonStreamParser jsonStreamParser;
    private JsonReader mockJsonReader;
    private final Object lock = new Object();

    @BeforeEach
    public void setup() throws Exception {
        // Create a dummy JsonStreamParser instance using the String constructor
        jsonStreamParser = new JsonStreamParser("{}");

        // Create mock JsonReader
        mockJsonReader = mock(JsonReader.class);

        // Use reflection to set the private final parser field to the mock
        Field parserField = JsonStreamParser.class.getDeclaredField("parser");
        parserField.setAccessible(true);
        parserField.set(jsonStreamParser, mockJsonReader);

        // Use reflection to set the private final lock field to the lock object
        Field lockField = JsonStreamParser.class.getDeclaredField("lock");
        lockField.setAccessible(true);
        lockField.set(jsonStreamParser, lock);
    }

    @Test
    @Timeout(8000)
    public void testHasNextReturnsTrueWhenNotEndDocument() throws IOException {
        synchronized (lock) {
            when(mockJsonReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);

            boolean result = jsonStreamParser.hasNext();

            assertTrue(result);
            verify(mockJsonReader, times(1)).peek();
        }
    }

    @Test
    @Timeout(8000)
    public void testHasNextReturnsFalseWhenEndDocument() throws IOException {
        synchronized (lock) {
            when(mockJsonReader.peek()).thenReturn(JsonToken.END_DOCUMENT);

            boolean result = jsonStreamParser.hasNext();

            assertFalse(result);
            verify(mockJsonReader, times(1)).peek();
        }
    }

    @Test
    @Timeout(8000)
    public void testHasNextThrowsJsonSyntaxExceptionOnMalformedJsonException() throws IOException {
        synchronized (lock) {
            MalformedJsonException exception = new MalformedJsonException("Malformed JSON");
            when(mockJsonReader.peek()).thenThrow(exception);

            JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
                jsonStreamParser.hasNext();
            });

            assertSame(exception, thrown.getCause());
            verify(mockJsonReader, times(1)).peek();
        }
    }

    @Test
    @Timeout(8000)
    public void testHasNextThrowsJsonIOExceptionOnIOException() throws IOException {
        synchronized (lock) {
            IOException exception = new IOException("IO error");
            when(mockJsonReader.peek()).thenThrow(exception);

            JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
                jsonStreamParser.hasNext();
            });

            assertSame(exception, thrown.getCause());
            verify(mockJsonReader, times(1)).peek();
        }
    }
}
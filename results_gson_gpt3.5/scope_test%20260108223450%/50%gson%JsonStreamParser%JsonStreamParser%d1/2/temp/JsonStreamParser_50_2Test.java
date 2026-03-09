package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JsonStreamParser_50_2Test {

    private final String validJson = "{\"key\":\"value\"}";
    private final String invalidJson = "{key:\"value\""; // malformed JSON

    @Test
    @Timeout(8000)
    public void testConstructorWithString_shouldCreateParser() {
        JsonStreamParser parser = new JsonStreamParser(validJson);
        assertNotNull(parser);
        assertTrue(parser instanceof Iterator);
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithReader_shouldCreateParser() {
        Reader reader = new StringReader(validJson);
        JsonStreamParser parser = new JsonStreamParser(reader);
        assertNotNull(parser);
        assertTrue(parser instanceof Iterator);
    }

    @Test
    @Timeout(8000)
    public void testHasNextAndNext_withValidJson() {
        JsonStreamParser parser = new JsonStreamParser(validJson);
        assertTrue(parser.hasNext());
        JsonElement element = parser.next();
        assertNotNull(element);
        assertFalse(parser.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testNext_withoutHasNext_shouldThrowNoSuchElementException() throws Exception {
        JsonStreamParser parser = new JsonStreamParser(validJson);
        // consume the only element
        parser.next();
        // next call should throw
        assertThrows(NoSuchElementException.class, parser::next);
    }

    @Test
    @Timeout(8000)
    public void testRemove_shouldThrowUnsupportedOperationException() {
        JsonStreamParser parser = new JsonStreamParser(validJson);
        assertThrows(UnsupportedOperationException.class, parser::remove);
    }

    @Test
    @Timeout(8000)
    public void testHasNext_withMalformedJson_shouldThrowRuntimeException() throws Exception {
        Reader reader = new StringReader(invalidJson);
        JsonStreamParser parser = new JsonStreamParser(reader);

        // Reflection to get the private field 'parser' to mock JsonReader behavior
        Field parserField = JsonStreamParser.class.getDeclaredField("parser");
        parserField.setAccessible(true);
        JsonReader mockJsonReader = mock(JsonReader.class);
        parserField.set(parser, mockJsonReader);

        when(mockJsonReader.peek()).thenThrow(new MalformedJsonException("Malformed JSON"));

        assertThrows(RuntimeException.class, parser::hasNext);
    }

    @Test
    @Timeout(8000)
    public void testNext_withIOException_shouldThrowRuntimeException() throws Exception {
        JsonStreamParser parser = new JsonStreamParser(validJson);

        Field parserField = JsonStreamParser.class.getDeclaredField("parser");
        parserField.setAccessible(true);
        JsonReader mockJsonReader = mock(JsonReader.class);
        parserField.set(parser, mockJsonReader);

        when(mockJsonReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
        // Streams.parse is static, so we can't mock it easily here, but we can simulate IOException by throwing from mockJsonReader methods
        when(mockJsonReader.hasNext()).thenThrow(new IOException("IO error"));

        // next() will call Streams.parse(parser) internally which calls parser.peek() and parser.next* methods.
        // Because we cannot mock static Streams.parse, we expect a RuntimeException wrapping IOException.
        assertThrows(RuntimeException.class, parser::next);
    }

    @Test
    @Timeout(8000)
    public void testLockField_shouldBeNonNull() throws Exception {
        JsonStreamParser parser = new JsonStreamParser(validJson);
        Field lockField = JsonStreamParser.class.getDeclaredField("lock");
        lockField.setAccessible(true);
        Object lock = lockField.get(parser);
        assertNotNull(lock);
    }

}
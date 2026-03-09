package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class JsonTreeReader_233_3Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a minimal JsonElement mock, since JsonTreeReader constructor requires one
        // We can use null since constructor implementation is not provided and test focuses on hasNext()
        jsonTreeReader = new JsonTreeReader(null);
    }

    @Test
    @Timeout(8000)
    public void testHasNext_whenPeekReturnsBeginObject() throws Exception {
        setPeekReturn(JsonToken.BEGIN_OBJECT);
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_whenPeekReturnsBeginArray() throws Exception {
        setPeekReturn(JsonToken.BEGIN_ARRAY);
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_whenPeekReturnsName() throws Exception {
        setPeekReturn(JsonToken.NAME);
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_whenPeekReturnsString() throws Exception {
        setPeekReturn(JsonToken.STRING);
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_whenPeekReturnsNumber() throws Exception {
        setPeekReturn(JsonToken.NUMBER);
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_whenPeekReturnsBoolean() throws Exception {
        setPeekReturn(JsonToken.BOOLEAN);
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_whenPeekReturnsNull() throws Exception {
        setPeekReturn(JsonToken.NULL);
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_whenPeekReturnsEndObject() throws Exception {
        setPeekReturn(JsonToken.END_OBJECT);
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_whenPeekReturnsEndArray() throws Exception {
        setPeekReturn(JsonToken.END_ARRAY);
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_whenPeekReturnsEndDocument() throws Exception {
        setPeekReturn(JsonToken.END_DOCUMENT);
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_whenPeekThrowsIOException() throws Exception {
        Method peekMethod = JsonTreeReader.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);

        JsonTreeReader spyReader = spy(jsonTreeReader);
        doThrow(new IOException("peek IOException")).when(spyReader).peek();

        IOException thrown = assertThrows(IOException.class, spyReader::hasNext);
        assertEquals("peek IOException", thrown.getMessage());
    }

    private void setPeekReturn(JsonToken token) throws Exception {
        Method peekMethod = JsonTreeReader.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);

        JsonTreeReader spyReader = spy(jsonTreeReader);
        doReturn(token).when(spyReader).peek();

        // Replace jsonTreeReader with spy to use mocked peek()
        this.jsonTreeReader = spyReader;
    }
}
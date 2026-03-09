package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import com.google.gson.stream.JsonToken;
import com.google.gson.internal.Streams;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;

public class JsonParser_440_6Test {

    private JsonParser jsonParser;

    @BeforeEach
    public void setUp() {
        jsonParser = new JsonParser();
    }

    @Test
    @Timeout(8000)
    public void testParse_withValidJsonString_shouldReturnJsonElement() throws Exception {
        String json = "{\"key\":\"value\"}";
        Reader reader = new StringReader(json);

        JsonElement result = jsonParser.parse(reader);

        assertNotNull(result);
        assertTrue(result.isJsonObject());
        assertEquals("value", result.getAsJsonObject().get("key").getAsString());
    }

    @Test
    @Timeout(8000)
    public void testParse_withEmptyJson_shouldThrowJsonSyntaxException() {
        String json = " ";  // changed to a space to trigger exception
        Reader reader = new StringReader(json);

        assertThrows(JsonSyntaxException.class, () -> jsonParser.parse(reader));
    }

    @Test
    @Timeout(8000)
    public void testParse_withMalformedJson_shouldThrowJsonSyntaxException() {
        String json = "{key:\"value\""; // missing closing }
        Reader reader = new StringReader(json);

        assertThrows(JsonSyntaxException.class, () -> jsonParser.parse(reader));
    }

    @Test
    @Timeout(8000)
    public void testParse_withNullReader_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> jsonParser.parse((Reader)null));
    }

    @Test
    @Timeout(8000)
    public void testParse_invokesParseReaderUsingReflection() throws Exception {
        String json = "{\"key\":\"value\"}";
        Reader reader = new StringReader(json);

        Method parseMethod = JsonParser.class.getDeclaredMethod("parse", Reader.class);
        parseMethod.setAccessible(true);

        JsonElement result = (JsonElement) parseMethod.invoke(jsonParser, reader);

        assertNotNull(result);
        assertTrue(result.isJsonObject());
        assertEquals("value", result.getAsJsonObject().get("key").getAsString());
    }

    @Test
    @Timeout(8000)
    public void testParse_withMockedReader_shouldThrowJsonIOException() throws Exception {
        Reader mockReader = mock(Reader.class);
        doThrow(new IOException("IO error")).when(mockReader).read(any(char[].class), anyInt(), anyInt());

        assertThrows(JsonIOException.class, () -> jsonParser.parse(mockReader));
    }
}
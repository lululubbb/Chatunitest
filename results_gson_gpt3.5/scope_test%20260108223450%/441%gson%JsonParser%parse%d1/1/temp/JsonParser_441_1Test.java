package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonParser_441_1Test {

    private JsonParser parser;

    @BeforeEach
    public void setUp() {
        parser = new JsonParser();
    }

    @Test
    @Timeout(8000)
    public void testParse_withValidJsonReader_invokesParseReader() throws Exception {
        // Use a real JsonReader with valid JSON instead of a mock to avoid NPE in internal parsing
        String json = "{\"key\":\"value\"}";
        JsonReader jsonReader = new JsonReader(new StringReader(json));

        // Use reflection to get parseReader(JsonReader) static method
        Method parseReaderMethod = JsonParser.class.getDeclaredMethod("parseReader", JsonReader.class);
        parseReaderMethod.setAccessible(true);

        // Call parse(JsonReader) method
        JsonElement result = parser.parse(jsonReader);

        // The result should be equal to the result of parseReader(jsonReader)
        // We need a fresh JsonReader because parseReader consumes the reader
        JsonReader jsonReaderForExpected = new JsonReader(new StringReader(json));
        JsonElement expected = (JsonElement) parseReaderMethod.invoke(null, jsonReaderForExpected);

        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    public void testParse_withStringJson_validJson() throws Exception {
        String json = "{\"key\":\"value\"}";

        // Use reflection to get private parse(String) method
        Method parseStringMethod = JsonParser.class.getDeclaredMethod("parse", String.class);
        parseStringMethod.setAccessible(true);

        JsonElement element = (JsonElement) parseStringMethod.invoke(parser, json);

        assertNotNull(element);
    }

    @Test
    @Timeout(8000)
    public void testParse_withReader_validJson() throws Exception {
        Reader reader = new StringReader("{\"key\":\"value\"}");

        // Use reflection to get private parse(Reader) method
        Method parseReaderMethod = JsonParser.class.getDeclaredMethod("parse", Reader.class);
        parseReaderMethod.setAccessible(true);

        JsonElement element = (JsonElement) parseReaderMethod.invoke(parser, reader);

        assertNotNull(element);
    }

    @Test
    @Timeout(8000)
    public void testParse_withMalformedJson_throwsJsonSyntaxException() throws Exception {
        String malformedJson = "{key:\"value\""; // Missing closing }

        // Use reflection to get private parse(String) method
        Method parseStringMethod = JsonParser.class.getDeclaredMethod("parse", String.class);
        parseStringMethod.setAccessible(true);

        try {
            parseStringMethod.invoke(parser, malformedJson);
            fail("Expected JsonSyntaxException");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof JsonSyntaxException);
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_withNullJsonReader_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse((JsonReader) null));
    }

    @Test
    @Timeout(8000)
    public void testParse_withNullString_throwsNullPointerException() throws Exception {
        Method parseStringMethod = JsonParser.class.getDeclaredMethod("parse", String.class);
        parseStringMethod.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                () -> parseStringMethod.invoke(parser, new Object[]{null}));

        assertTrue(thrown.getCause() instanceof NullPointerException);
    }

    @Test
    @Timeout(8000)
    public void testParse_withNullReader_throwsNullPointerException() throws Exception {
        Method parseReaderMethod = JsonParser.class.getDeclaredMethod("parse", Reader.class);
        parseReaderMethod.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                () -> parseReaderMethod.invoke(parser, new Object[]{null}));

        assertTrue(thrown.getCause() instanceof NullPointerException);
    }
}
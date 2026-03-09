package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import com.google.gson.internal.Streams;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;

public class JsonParser_441_5Test {

    private JsonParser jsonParser;

    @BeforeEach
    public void setUp() {
        jsonParser = new JsonParser();
    }

    @Test
    @Timeout(8000)
    public void testParse_withValidJsonReader_shouldReturnJsonElement() throws Exception {
        // Prepare a JsonReader with simple JSON
        String json = "{\"key\":\"value\"}";
        JsonReader jsonReader = new JsonReader(new StringReader(json));

        // Invoke parse(JsonReader) method
        JsonElement result = jsonParser.parse(jsonReader);

        assertNotNull(result);
        // Since JsonElement is abstract, check that toString contains key and value
        assertTrue(result.toString().contains("key"));
        assertTrue(result.toString().contains("value"));
    }

    @Test
    @Timeout(8000)
    public void testParse_withMalformedJson_shouldThrowJsonSyntaxException() throws IOException {
        String malformedJson = "{key:\"value\""; // missing closing brace
        JsonReader jsonReader = new JsonReader(new StringReader(malformedJson));

        assertThrows(JsonSyntaxException.class, () -> {
            jsonParser.parse(jsonReader);
        });
    }

    @Test
    @Timeout(8000)
    public void testParse_withClosedJsonReader_shouldThrowIllegalStateException() throws IOException {
        String json = "{\"key\":\"value\"}";
        JsonReader jsonReader = spy(new JsonReader(new StringReader(json)));
        jsonReader.close();

        assertThrows(IllegalStateException.class, () -> {
            jsonParser.parse(jsonReader);
        });
    }

    @Test
    @Timeout(8000)
    public void testParseString_withValidJson_shouldReturnJsonElement() {
        String json = "{\"key\":\"value\"}";
        JsonElement element = JsonParser.parseString(json);

        assertNotNull(element);
        assertTrue(element.toString().contains("key"));
        assertTrue(element.toString().contains("value"));
    }

    @Test
    @Timeout(8000)
    public void testParseReader_withValidReader_shouldReturnJsonElement() {
        String json = "{\"key\":\"value\"}";
        Reader reader = new StringReader(json);

        JsonElement element = JsonParser.parseReader(reader);

        assertNotNull(element);
        assertTrue(element.toString().contains("key"));
        assertTrue(element.toString().contains("value"));
    }

    @Test
    @Timeout(8000)
    public void testParseReader_withValidJsonReader_shouldReturnJsonElement() {
        String json = "{\"key\":\"value\"}";
        JsonReader jsonReader = new JsonReader(new StringReader(json));

        JsonElement element = JsonParser.parseReader(jsonReader);

        assertNotNull(element);
        assertTrue(element.toString().contains("key"));
        assertTrue(element.toString().contains("value"));
    }

    @Test
    @Timeout(8000)
    public void testDeprecatedParseStringMethodViaReflection() throws Exception {
        Method parseStringMethod = JsonParser.class.getDeclaredMethod("parseString", String.class);
        parseStringMethod.setAccessible(true);

        String json = "{\"key\":\"value\"}";
        Object result = parseStringMethod.invoke(null, json);

        assertNotNull(result);
        assertTrue(result instanceof JsonElement);
        assertTrue(result.toString().contains("key"));
    }

    @Test
    @Timeout(8000)
    public void testDeprecatedParseReaderMethodViaReflection() throws Exception {
        Method parseReaderMethod = JsonParser.class.getDeclaredMethod("parseReader", Reader.class);
        parseReaderMethod.setAccessible(true);

        String json = "{\"key\":\"value\"}";
        Reader reader = new StringReader(json);
        Object result = parseReaderMethod.invoke(null, reader);

        assertNotNull(result);
        assertTrue(result instanceof JsonElement);
        assertTrue(result.toString().contains("key"));
    }

    @Test
    @Timeout(8000)
    public void testDeprecatedParseMethod_withString() throws Exception {
        // parse(String json) is deprecated, invoke via reflection
        Method parseMethod = JsonParser.class.getDeclaredMethod("parse", String.class);
        parseMethod.setAccessible(true);

        String json = "{\"key\":\"value\"}";
        Object result = parseMethod.invoke(jsonParser, json);

        assertNotNull(result);
        assertTrue(result instanceof JsonElement);
        assertTrue(result.toString().contains("key"));
    }

    @Test
    @Timeout(8000)
    public void testDeprecatedParseMethod_withReader() throws Exception {
        // parse(Reader json) is deprecated, invoke via reflection
        Method parseMethod = JsonParser.class.getDeclaredMethod("parse", Reader.class);
        parseMethod.setAccessible(true);

        String json = "{\"key\":\"value\"}";
        Reader reader = new StringReader(json);
        Object result = parseMethod.invoke(jsonParser, reader);

        assertNotNull(result);
        assertTrue(result instanceof JsonElement);
        assertTrue(result.toString().contains("key"));
    }
}
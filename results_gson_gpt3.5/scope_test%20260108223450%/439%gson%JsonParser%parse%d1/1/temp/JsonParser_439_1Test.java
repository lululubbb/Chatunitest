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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;

public class JsonParser_439_1Test {

    private JsonParser jsonParser;

    @BeforeEach
    public void setUp() {
        jsonParser = new JsonParser();
    }

    @Test
    @Timeout(8000)
    public void testParse_WithValidJsonString_ShouldReturnJsonElement() throws Exception {
        String json = "{\"key\":\"value\"}";

        try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
            JsonElement mockedElement = mock(JsonElement.class);
            mockedStatic.when(() -> JsonParser.parseString(json)).thenReturn(mockedElement);

            JsonElement result = jsonParser.parse(json);

            assertSame(mockedElement, result);
            mockedStatic.verify(() -> JsonParser.parseString(json));
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_WithNullString_ShouldThrowNullPointerException() throws Exception {
        String json = null;

        try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
            mockedStatic.when(() -> JsonParser.parseString(json)).thenThrow(NullPointerException.class);

            assertThrows(NullPointerException.class, () -> jsonParser.parse(json));
            mockedStatic.verify(() -> JsonParser.parseString(json));
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_ReflectionInvoke_ShouldReturnJsonElement() throws Exception {
        String json = "{\"key\":\"value\"}";

        try (MockedStatic<JsonParser> mockedStatic = Mockito.mockStatic(JsonParser.class)) {
            JsonElement mockedElement = mock(JsonElement.class);
            mockedStatic.when(() -> JsonParser.parseString(json)).thenReturn(mockedElement);

            Method parseMethod = JsonParser.class.getDeclaredMethod("parse", String.class);
            parseMethod.setAccessible(true);

            JsonElement result = (JsonElement) parseMethod.invoke(jsonParser, json);

            assertSame(mockedElement, result);
            mockedStatic.verify(() -> JsonParser.parseString(json));
        }
    }
}
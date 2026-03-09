package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReader_233_2Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a JsonElement for constructor, using JsonNull as simple element
        JsonElement element = JsonNull.INSTANCE;
        jsonTreeReader = new JsonTreeReader(element);
    }

    @Test
    @Timeout(8000)
    public void testHasNext_withEndObject() throws Exception {
        setStackWithToken(JsonToken.END_OBJECT);
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_withEndArray() throws Exception {
        setStackWithToken(JsonToken.END_ARRAY);
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_withEndDocument() throws Exception {
        setStackWithToken(JsonToken.END_DOCUMENT);
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_withOtherTokens() throws Exception {
        for (JsonToken token : JsonToken.values()) {
            if (token != JsonToken.END_OBJECT && token != JsonToken.END_ARRAY && token != JsonToken.END_DOCUMENT) {
                setStackWithToken(token);
                assertTrue(jsonTreeReader.hasNext(), "Failed for token: " + token);
            }
        }
    }

    private void setStackWithToken(JsonToken token) throws Exception {
        // Get stack field
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);

        // Get stackSize field
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        Object[] stack = (Object[]) stackField.get(jsonTreeReader);

        // Clear stack to avoid leftover data
        for (int i = 0; i < stack.length; i++) {
            stack[i] = null;
        }

        switch (token) {
            case BEGIN_ARRAY: {
                JsonArray array = new JsonArray();
                stack[0] = array;
                stackSizeField.setInt(jsonTreeReader, 1);
                break;
            }
            case END_ARRAY: {
                // For END_ARRAY, stack top is an Iterator with hasNext() == false, and below it a JsonArray
                JsonArray array = new JsonArray();
                Iterator<Object> iterator = new Iterator<Object>() {
                    @Override
                    public boolean hasNext() { return false; }
                    @Override
                    public Object next() { return null; }
                };
                stack[0] = array;
                stack[1] = iterator;
                stackSizeField.setInt(jsonTreeReader, 2);
                break;
            }
            case BEGIN_OBJECT: {
                JsonObject object = new JsonObject();
                stack[0] = object;
                stackSizeField.setInt(jsonTreeReader, 1);
                break;
            }
            case END_OBJECT: {
                // For END_OBJECT, stack top is an Iterator with hasNext() == false, and below it a JsonObject
                JsonObject object = new JsonObject();
                Iterator<Object> iterator = new Iterator<Object>() {
                    @Override
                    public boolean hasNext() { return false; }
                    @Override
                    public Object next() { return null; }
                };
                stack[0] = object;
                stack[1] = iterator;
                stackSizeField.setInt(jsonTreeReader, 2);
                break;
            }
            case NAME: {
                // peek() returns NAME if topObject is an Iterator with hasNext() true and next() returns Map.Entry
                JsonObject object = new JsonObject();
                object.add("key", JsonNull.INSTANCE);
                Iterator<Map.Entry<String, JsonElement>> iterator = object.entrySet().iterator();
                stack[0] = object;
                stack[1] = iterator;
                stackSizeField.setInt(jsonTreeReader, 2);
                break;
            }
            case STRING: {
                JsonPrimitive primitive = new JsonPrimitive("string");
                stack[0] = primitive;
                stackSizeField.setInt(jsonTreeReader, 1);
                break;
            }
            case NUMBER: {
                JsonPrimitive primitive = new JsonPrimitive(1);
                stack[0] = primitive;
                stackSizeField.setInt(jsonTreeReader, 1);
                break;
            }
            case BOOLEAN: {
                JsonPrimitive primitive = new JsonPrimitive(true);
                stack[0] = primitive;
                stackSizeField.setInt(jsonTreeReader, 1);
                break;
            }
            case NULL: {
                stack[0] = JsonNull.INSTANCE;
                stackSizeField.setInt(jsonTreeReader, 1);
                break;
            }
            case END_DOCUMENT: {
                // stackSize == 0 means end document
                stackSizeField.setInt(jsonTreeReader, 0);
                break;
            }
            default: {
                stack[0] = JsonNull.INSTANCE;
                stackSizeField.setInt(jsonTreeReader, 1);
                break;
            }
        }
    }
}
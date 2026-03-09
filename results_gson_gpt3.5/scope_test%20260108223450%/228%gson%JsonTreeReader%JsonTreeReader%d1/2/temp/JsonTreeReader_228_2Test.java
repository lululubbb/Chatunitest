package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_228_2Test {

    private JsonPrimitive jsonPrimitive;
    private JsonObject jsonObject;
    private JsonArray jsonArray;
    private JsonNull jsonNull;

    @BeforeEach
    public void setUp() {
        jsonPrimitive = new JsonPrimitive("test");
        jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(1));
        jsonArray.add(new JsonPrimitive(2));
        jsonNull = JsonNull.INSTANCE;
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithJsonPrimitive() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
        assertNotNull(reader);
        assertEquals(JsonToken.STRING, reader.peek());
        assertEquals("test", reader.nextString());
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithJsonObject() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        assertNotNull(reader);
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals("key", reader.nextName());
        assertEquals(JsonToken.STRING, reader.peek());
        assertEquals("value", reader.nextString());
        reader.endObject();
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithJsonArray() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(jsonArray);
        assertNotNull(reader);
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        reader.beginArray();
        assertTrue(reader.hasNext());
        assertEquals(1, reader.nextInt());
        assertTrue(reader.hasNext());
        assertEquals(2, reader.nextInt());
        assertFalse(reader.hasNext());
        reader.endArray();
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithJsonNull() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(jsonNull);
        assertNotNull(reader);
        assertEquals(JsonToken.NULL, reader.peek());
        reader.nextNull();
    }

    @Test
    @Timeout(8000)
    public void testPrivatePeekStack() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
        Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
        peekStackMethod.setAccessible(true);
        Object stackTop = peekStackMethod.invoke(reader);
        assertNotNull(stackTop);
    }

    @Test
    @Timeout(8000)
    public void testPrivatePopStack() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
        Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
        popStackMethod.setAccessible(true);
        Object popped = popStackMethod.invoke(reader);
        assertEquals(jsonPrimitive, popped);
    }

    @Test
    @Timeout(8000)
    public void testPrivatePush() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
        Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
        pushMethod.setAccessible(true);
        JsonPrimitive newPrimitive = new JsonPrimitive("new");
        pushMethod.invoke(reader, newPrimitive);

        Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
        peekStackMethod.setAccessible(true);
        Object stackTop = peekStackMethod.invoke(reader);
        assertEquals(newPrimitive, stackTop);
    }

    @Test
    @Timeout(8000)
    public void testPrivateExpect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
        Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
        expectMethod.setAccessible(true);

        // Should not throw when expected token matches
        expectMethod.invoke(reader, JsonToken.STRING);

        // Should throw when expected token does not match
        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            expectMethod.invoke(reader, JsonToken.NUMBER);
        });
        assertTrue(exception.getCause() instanceof IllegalStateException);
    }

    @Test
    @Timeout(8000)
    public void testPrivateNextNameBooleanSkip() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        JsonObject obj = new JsonObject();
        obj.addProperty("a", "b");
        JsonTreeReader reader = new JsonTreeReader(obj);
        reader.beginObject();

        Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
        nextNameMethod.setAccessible(true);

        String name = (String) nextNameMethod.invoke(reader, true);
        assertEquals("a", name);
    }

    @Test
    @Timeout(8000)
    public void testPromoteNameToValue() throws IOException {
        JsonObject obj = new JsonObject();
        obj.addProperty("a", "b");
        JsonTreeReader reader = new JsonTreeReader(obj);
        reader.beginObject();
        String name = reader.nextName();
        assertEquals("a", name);
        reader.promoteNameToValue();
        assertEquals(JsonToken.STRING, reader.peek());
        assertEquals("b", reader.nextString());
        reader.endObject();
    }

    @Test
    @Timeout(8000)
    public void testGetPathMethods() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        Method getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
        getPathMethod.setAccessible(true);
        String path = (String) getPathMethod.invoke(reader, false);
        assertNotNull(path);

        String previousPath = reader.getPreviousPath();
        assertNotNull(previousPath);

        String pathPublic = reader.getPath();
        assertNotNull(pathPublic);
    }

    @Test
    @Timeout(8000)
    public void testLocationString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
        Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
        locationStringMethod.setAccessible(true);
        String location = (String) locationStringMethod.invoke(reader);
        assertNotNull(location);
    }

    @Test
    @Timeout(8000)
    public void testClose() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
        reader.close();
        Exception exception = assertThrows(IllegalStateException.class, reader::peek);
        assertTrue(exception.getMessage().contains("JsonReader is closed"));
    }

    @Test
    @Timeout(8000)
    public void testSkipValue() throws IOException {
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("a"));
        array.add(new JsonPrimitive("b"));
        JsonTreeReader reader = new JsonTreeReader(array);
        reader.beginArray();
        reader.skipValue();
        assertEquals("b", reader.nextString());
        reader.endArray();
    }

    @Test
    @Timeout(8000)
    public void testNextJsonElement() throws IOException {
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
        JsonElement element = reader.nextJsonElement();
        assertEquals(jsonPrimitive, element);
    }
}
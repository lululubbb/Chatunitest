package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

class JsonTreeReader_231_2Test {

    private JsonTreeReader jsonTreeReader;
    private JsonObject jsonObject;

    @BeforeEach
    void setUp() throws Exception {
        jsonObject = new JsonObject();
        jsonObject.addProperty("key1", "value1");
        jsonObject.addProperty("key2", "value2");

        jsonTreeReader = new JsonTreeReader(jsonObject);

        // Use reflection to set stack and stackSize to simulate peekStack returning jsonObject
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = (Object[]) stackField.get(jsonTreeReader);
        stackArray[0] = jsonObject;
        stackField.set(jsonTreeReader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);
    }

    @Test
    @Timeout(8000)
    void beginObject_success() throws Exception {
        // Invoke beginObject via reflection
        Method beginObjectMethod = JsonTreeReader.class.getDeclaredMethod("beginObject");
        beginObjectMethod.setAccessible(true);
        beginObjectMethod.invoke(jsonTreeReader);

        // Verify stack has new top as iterator of jsonObject.entrySet()
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = (Object[]) stackField.get(jsonTreeReader);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(jsonTreeReader);

        assertEquals(2, stackSize);

        Object top = stackArray[stackSize - 1];
        assertTrue(top instanceof Iterator);

        @SuppressWarnings("unchecked")
        Iterator<Map.Entry<String, JsonElement>> iterator = (Iterator<Map.Entry<String, JsonElement>>) top;
        assertTrue(iterator.hasNext());
        Map.Entry<String, JsonElement> entry = iterator.next();
        assertEquals("key1", entry.getKey());
        assertEquals("value1", entry.getValue().getAsString());
    }

    @Test
    @Timeout(8000)
    void beginObject_expectThrowsIOException() throws Exception {
        // Setup a JsonTreeReader with stack top set to a String to cause expect to throw IOException
        JsonTreeReader proxyReader = new JsonTreeReader(jsonObject);

        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = (Object[]) stackField.get(proxyReader);
        stackArray[0] = "not a JsonObject"; // invalid stack top to cause IOException
        stackField.set(proxyReader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(proxyReader, 1);

        Method beginObjectMethod = JsonTreeReader.class.getDeclaredMethod("beginObject");
        beginObjectMethod.setAccessible(true);

        IOException thrown = assertThrows(IOException.class, () -> {
            try {
                beginObjectMethod.invoke(proxyReader);
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof IOException) {
                    throw (IOException) cause;
                }
                throw e;
            }
        });
        assertNotNull(thrown);
    }

    @Test
    @Timeout(8000)
    void beginObject_stackTopNotJsonObjectClassCastException() throws Exception {
        // Set stack top to an object not JsonObject to cause IOException wrapping ClassCastException
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stackArray = (Object[]) stackField.get(jsonTreeReader);
        stackArray[0] = "not a JsonObject";
        stackField.set(jsonTreeReader, stackArray);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 1);

        Method beginObjectMethod = JsonTreeReader.class.getDeclaredMethod("beginObject");
        beginObjectMethod.setAccessible(true);

        IOException thrown = assertThrows(IOException.class, () -> {
            try {
                beginObjectMethod.invoke(jsonTreeReader);
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof IOException) {
                    throw (IOException) cause;
                }
                throw e;
            }
        });
        // The cause of IOException is ClassCastException
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof ClassCastException);
    }
}
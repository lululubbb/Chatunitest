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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReader_239_1Test {

    private JsonTreeReader reader;

    @BeforeEach
    public void setUp() {
        // We create a JsonObject with some properties for testing
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name1", "value1");
        jsonObject.addProperty("name2", "value2");
        reader = new JsonTreeReader(jsonObject);
    }

    @Test
    @Timeout(8000)
    public void testNextName_normal() throws Throwable {
        // beginObject to position on the object
        reader.beginObject();

        // Call nextName() which calls nextName(false)
        String name1 = reader.nextName();
        assertEquals("name1", name1);

        // Need to consume the value after the name to move to next name
        reader.nextString();

        String name2 = reader.nextName();
        assertEquals("name2", name2);

        // Consume the second value as well
        reader.nextString();

        reader.endObject();
    }

    @Test
    @Timeout(8000)
    public void testNextName_skipNameTrue() throws Throwable {
        // Using reflection to invoke private nextName(boolean)
        Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
        nextNameMethod.setAccessible(true);

        reader.beginObject();

        // skipName = true, so nextName should skip the name and return it but not update pathNames
        String name = (String) nextNameMethod.invoke(reader, true);
        assertEquals("name1", name);

        // Consume the value after the skipped name to be able to endObject without error
        reader.nextString();

        // The pathNames array should not be updated at stackSize-1 index
        // We use reflection to check private fields
        String[] pathNames = (String[]) getPrivateField(reader, "pathNames");
        int stackSize = (int) getPrivateField(reader, "stackSize");
        // Fix: The pathNames slot is set to "<skipped>" when skipName is true
        assertEquals("<skipped>", pathNames[stackSize - 1]);

        // Consume the second name and value normally to complete the object
        String name2 = reader.nextName();
        assertEquals("name2", name2);
        reader.nextString();

        reader.endObject();
    }

    @Test
    @Timeout(8000)
    public void testNextName_noObjectOnStack_throws() throws Throwable {
        // Using reflection to invoke private nextName(boolean)
        Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
        nextNameMethod.setAccessible(true);

        // Pop the initial JsonObject off the stack so top is not JsonObject
        // We simulate by popping stack until empty and pushing a JsonArray
        Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
        popStackMethod.setAccessible(true);
        while ((int) getPrivateField(reader, "stackSize") > 0) {
            popStackMethod.invoke(reader);
        }

        // Push a JsonArray instead of JsonObject
        Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(reader, new JsonArray());
        setPrivateField(reader, "stackSize", 1);

        // nextName should throw IllegalStateException because top is not JsonObject
        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> nextNameMethod.invoke(reader, false));
        assertTrue(ex.getCause() instanceof IllegalStateException);
    }

    @Test
    @Timeout(8000)
    public void testNextName_emptyObject_throws() throws Throwable {
        JsonObject emptyObject = new JsonObject();
        JsonTreeReader emptyReader = new JsonTreeReader(emptyObject);
        emptyReader.beginObject();

        Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
        nextNameMethod.setAccessible(true);

        // nextName should throw IllegalStateException because object is empty (no next entry)
        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> nextNameMethod.invoke(emptyReader, false));
        assertTrue(ex.getCause() instanceof IllegalStateException);

        emptyReader.endObject();
    }

    // Helper to get private field value via reflection
    private Object getPrivateField(Object instance, String fieldName) throws Exception {
        var field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    // Helper to set private field value via reflection
    private void setPrivateField(Object instance, String fieldName, Object value) throws Exception {
        var field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }
}
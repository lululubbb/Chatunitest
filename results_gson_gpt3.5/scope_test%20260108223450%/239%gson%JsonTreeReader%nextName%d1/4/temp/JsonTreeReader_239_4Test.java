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

public class JsonTreeReader_239_4Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() {
        // We will use JsonObject as the root element for JsonTreeReader
        JsonObject root = new JsonObject();
        jsonTreeReader = new JsonTreeReader(root);
    }

    @Test
    @Timeout(8000)
    public void testNextName_returnsName_whenObjectHasSingleEntry() throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("key", "value");
        JsonTreeReader reader = new JsonTreeReader(obj);

        // beginObject to set reader state correctly
        reader.beginObject();

        // Call nextName() which internally calls nextName(false)
        String name = reader.nextName();
        assertEquals("key", name);

        // nextName should update pathNames for current stack position
        // We can verify that nextName returns the property name

        // consume the value to avoid IllegalStateException on endObject
        reader.nextString();

        // endObject to clean up
        reader.endObject();
    }

    @Test
    @Timeout(8000)
    public void testNextName_throwsException_whenNotObject() throws Exception {
        JsonArray array = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(array);

        reader.beginArray();

        // nextName() should throw IllegalStateException because current element is not object
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            reader.nextName();
        });

        reader.endArray();
    }

    @Test
    @Timeout(8000)
    public void testNextName_privateMethod_skipNameTrue_returnsName() throws Throwable {
        JsonObject obj = new JsonObject();
        obj.addProperty("alpha", "beta");
        JsonTreeReader reader = new JsonTreeReader(obj);

        // beginObject to set state
        reader.beginObject();

        // Use reflection to get private method nextName(boolean)
        Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
        nextNameMethod.setAccessible(true);

        // Invoke with skipName = true
        String name = (String) nextNameMethod.invoke(reader, true);
        assertEquals("alpha", name);

        // consume the value to avoid IllegalStateException on endObject
        reader.nextString();

        reader.endObject();
    }

    @Test
    @Timeout(8000)
    public void testNextName_privateMethod_skipNameFalse_returnsName() throws Throwable {
        JsonObject obj = new JsonObject();
        obj.addProperty("foo", "bar");
        JsonTreeReader reader = new JsonTreeReader(obj);

        reader.beginObject();

        Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
        nextNameMethod.setAccessible(true);

        String name = (String) nextNameMethod.invoke(reader, false);
        assertEquals("foo", name);

        // consume the value to avoid IllegalStateException on endObject
        reader.nextString();

        reader.endObject();
    }

    @Test
    @Timeout(8000)
    public void testNextName_multipleProperties_iteratesOverAllNames() throws IOException {
        JsonObject obj = new JsonObject();
        obj.addProperty("one", "1");
        obj.addProperty("two", "2");
        JsonTreeReader reader = new JsonTreeReader(obj);

        reader.beginObject();

        String firstName = reader.nextName();
        assertTrue(firstName.equals("one") || firstName.equals("two"));

        // consume the first value
        reader.nextString();

        String secondName = reader.nextName();
        assertTrue(secondName.equals("one") || secondName.equals("two"));
        assertNotEquals(firstName, secondName);

        // consume the second value
        reader.nextString();

        reader.endObject();
    }

    @Test
    @Timeout(8000)
    public void testNextName_afterEndObject_throwsException() throws IOException {
        JsonObject obj = new JsonObject();
        obj.addProperty("a", "b");
        JsonTreeReader reader = new JsonTreeReader(obj);

        reader.beginObject();
        String name = reader.nextName();
        assertEquals("a", name);
        reader.nextString();
        reader.endObject();

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            reader.nextName();
        });
        assertTrue(thrown.getMessage().toLowerCase().contains("expected a name but was"));
    }

    @Test
    @Timeout(8000)
    public void testNextName_onEmptyObject_throwsException() throws IOException {
        JsonObject obj = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(obj);

        reader.beginObject();

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            reader.nextName();
        });
        assertTrue(thrown.getMessage().toLowerCase().contains("expected a name but was"));
    }
}
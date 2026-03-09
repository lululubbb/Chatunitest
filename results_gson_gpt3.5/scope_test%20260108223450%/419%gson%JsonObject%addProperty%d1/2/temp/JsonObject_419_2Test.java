package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class JsonObject_419_2Test {

    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() {
        jsonObject = spy(new JsonObject());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withNonNullNumber() {
        Number value = 123;

        jsonObject.addProperty("age", value);

        verify(jsonObject).add(eq("age"), any(JsonPrimitive.class));
        JsonElement element = jsonObject.get("age");
        assertNotNull(element);
        assertTrue(element instanceof JsonPrimitive);
        assertEquals(value, ((JsonPrimitive) element).getAsNumber());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withNullNumber() {
        jsonObject.addProperty("age", (Number) null);

        verify(jsonObject).add(eq("age"), eq(JsonNull.INSTANCE));
        JsonElement element = jsonObject.get("age");
        assertNotNull(element);
        assertEquals(JsonNull.INSTANCE, element);
    }

    @Test
    @Timeout(8000)
    public void testAddMethodDirectly() {
        JsonPrimitive primitive = new JsonPrimitive(42);
        jsonObject.add("test", primitive);

        JsonElement element = jsonObject.get("test");
        assertSame(primitive, element);
    }

    @Test
    @Timeout(8000)
    public void testRemoveMethod() {
        jsonObject.addProperty("key", 10);
        JsonElement removed = jsonObject.remove("key");

        assertNotNull(removed);
        assertTrue(removed instanceof JsonPrimitive);
        assertEquals(10, ((JsonPrimitive) removed).getAsInt());
        assertFalse(jsonObject.has("key"));
    }

    @Test
    @Timeout(8000)
    public void testAddPropertyStringAndBooleanAndCharacter() {
        jsonObject.addProperty("string", "value");
        jsonObject.addProperty("bool", true);
        jsonObject.addProperty("char", 'c');

        assertEquals("value", jsonObject.getAsJsonPrimitive("string").getAsString());
        assertEquals(true, jsonObject.getAsJsonPrimitive("bool").getAsBoolean());
        assertEquals('c', jsonObject.getAsJsonPrimitive("char").getAsCharacter());
    }

    @Test
    @Timeout(8000)
    public void testEntrySetAndKeySetAndSizeAndIsEmpty() {
        assertTrue(jsonObject.isEmpty());
        assertEquals(0, jsonObject.size());

        jsonObject.addProperty("one", 1);
        jsonObject.addProperty("two", 2);

        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        Set<String> keySet = jsonObject.keySet();

        assertEquals(2, entrySet.size());
        assertEquals(2, keySet.size());
        assertTrue(jsonObject.has("one"));
        assertFalse(jsonObject.isEmpty());
        assertEquals(2, jsonObject.size());
    }

    @Test
    @Timeout(8000)
    public void testGetMethodsReturnCorrectTypes() {
        jsonObject.addProperty("prim", 5);
        jsonObject.add("array", spy(new com.google.gson.JsonArray()));
        jsonObject.add("obj", spy(new JsonObject()));

        assertNotNull(jsonObject.get("prim"));
        assertEquals(5, jsonObject.getAsJsonPrimitive("prim").getAsInt());
        assertNotNull(jsonObject.getAsJsonArray("array"));
        assertNotNull(jsonObject.getAsJsonObject("obj"));
    }

    @Test
    @Timeout(8000)
    public void testAsMapReturnsUnmodifiableMap() {
        jsonObject.addProperty("key", 5);
        Map<String, JsonElement> map = jsonObject.asMap();

        assertEquals(1, map.size());
        assertTrue(map.containsKey("key"));
        assertThrows(UnsupportedOperationException.class, () -> {
            // Use the iterator's remove method to trigger UnsupportedOperationException
            map.entrySet().iterator().remove();
        });
    }

    @Test
    @Timeout(8000)
    public void testEqualsAndHashCode() {
        JsonObject obj1 = new JsonObject();
        JsonObject obj2 = new JsonObject();

        obj1.addProperty("key", 1);
        obj2.addProperty("key", 1);

        assertEquals(obj1, obj2);
        assertEquals(obj1.hashCode(), obj2.hashCode());

        obj2.addProperty("key2", 2);
        assertNotEquals(obj1, obj2);
    }

    @Test
    @Timeout(8000)
    public void testDeepCopyCreatesDistinctObject() {
        jsonObject.addProperty("key", 10);
        JsonObject copy = jsonObject.deepCopy();

        assertNotSame(jsonObject, copy);
        assertEquals(jsonObject.toString(), copy.toString());

        copy.addProperty("key2", 20);
        assertFalse(jsonObject.has("key2"));
        assertTrue(copy.has("key2"));
    }

    @Test
    @Timeout(8000)
    public void testInvokePrivateAddMethodUsingReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
        addMethod.setAccessible(true);

        JsonPrimitive primitive = new JsonPrimitive(99);
        addMethod.invoke(jsonObject, "reflectKey", primitive);

        JsonElement element = jsonObject.get("reflectKey");
        assertNotNull(element);
        assertEquals(primitive, element);
    }
}
package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_418_4Test {

    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() {
        jsonObject = spy(new JsonObject());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withNonNullValue_callsAddWithJsonPrimitive() {
        String property = "key";
        String value = "value";

        jsonObject.addProperty(property, value);

        verify(jsonObject).add(eq(property), argThat(arg -> arg instanceof JsonPrimitive && ((JsonPrimitive)arg).getAsString().equals(value)));
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withNullValue_callsAddWithJsonNull() {
        String property = "key";
        String value = null;

        jsonObject.addProperty(property, value);

        verify(jsonObject).add(eq(property), eq(JsonNull.INSTANCE));
    }

    @Test
    @Timeout(8000)
    public void testAdd_privateMethod_invokedViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
        addMethod.setAccessible(true);

        JsonPrimitive primitive = new JsonPrimitive("test");
        addMethod.invoke(jsonObject, "prop", primitive);

        assertTrue(jsonObject.has("prop"));
        JsonElement element = jsonObject.get("prop");
        assertTrue(element instanceof JsonPrimitive);
        assertEquals("test", ((JsonPrimitive)element).getAsString());
    }
}
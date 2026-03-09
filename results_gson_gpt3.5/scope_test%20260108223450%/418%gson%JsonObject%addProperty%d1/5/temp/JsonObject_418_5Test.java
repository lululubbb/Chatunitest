package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class JsonObject_418_5Test {

    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() {
        jsonObject = spy(new JsonObject());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withNonNullValue() {
        String property = "key";
        String value = "value";

        jsonObject.addProperty(property, value);

        // Verify add called with correct parameters
        verify(jsonObject).add(eq(property), any(JsonPrimitive.class));
        JsonElement element = jsonObject.get(property);
        assertNotNull(element);
        assertTrue(element instanceof JsonPrimitive);
        assertEquals(value, ((JsonPrimitive) element).getAsString());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withNullValue() {
        String property = "key";
        String value = null;

        jsonObject.addProperty(property, value);

        verify(jsonObject).add(eq(property), eq(JsonNull.INSTANCE));
        JsonElement element = jsonObject.get(property);
        assertSame(JsonNull.INSTANCE, element);
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_invokesPrivateAddMethod() throws Exception {
        String property = "privateKey";
        String value = "privateValue";

        // Use reflection to invoke private add method directly
        Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
        addMethod.setAccessible(true);

        JsonPrimitive jsonPrimitive = new JsonPrimitive(value);
        addMethod.invoke(jsonObject, property, jsonPrimitive);

        JsonElement element = jsonObject.get(property);
        assertNotNull(element);
        assertTrue(element instanceof JsonPrimitive);
        assertEquals(value, ((JsonPrimitive) element).getAsString());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_overwriteExistingProperty() {
        String property = "key";
        String firstValue = "first";
        String secondValue = "second";

        jsonObject.addProperty(property, firstValue);
        assertEquals(firstValue, jsonObject.getAsJsonPrimitive(property).getAsString());

        jsonObject.addProperty(property, secondValue);
        assertEquals(secondValue, jsonObject.getAsJsonPrimitive(property).getAsString());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withEmptyString() {
        String property = "empty";
        String value = "";

        jsonObject.addProperty(property, value);

        JsonPrimitive primitive = jsonObject.getAsJsonPrimitive(property);
        assertNotNull(primitive);
        assertEquals("", primitive.getAsString());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withSpecialCharacters() {
        String property = "special";
        String value = "\n\t\u2603";

        jsonObject.addProperty(property, value);

        JsonPrimitive primitive = jsonObject.getAsJsonPrimitive(property);
        assertNotNull(primitive);
        assertEquals(value, primitive.getAsString());
    }
}
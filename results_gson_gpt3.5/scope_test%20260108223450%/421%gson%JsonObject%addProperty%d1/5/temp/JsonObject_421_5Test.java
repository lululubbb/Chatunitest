package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class JsonObject_421_5Test {

    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() {
        jsonObject = new JsonObject();
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withNonNullCharacter() throws Exception {
        String property = "charProp";
        Character value = 'a';

        // Use reflection to spy on add method
        JsonObject spyJsonObject = spy(jsonObject);

        // Call addProperty
        spyJsonObject.addProperty(property, value);

        // Verify add called with correct arguments
        verify(spyJsonObject).add(eq(property), any(JsonPrimitive.class));

        // Check that the member was added correctly
        JsonElement element = spyJsonObject.get(property);
        assertNotNull(element);
        assertTrue(element instanceof JsonPrimitive);
        JsonPrimitive primitive = (JsonPrimitive) element;
        assertEquals(value.charValue(), primitive.getAsCharacter());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withNullCharacter() throws Exception {
        String property = "nullCharProp";
        Character value = null;

        JsonObject spyJsonObject = spy(jsonObject);

        spyJsonObject.addProperty(property, value);

        verify(spyJsonObject).add(eq(property), eq(JsonNull.INSTANCE));

        JsonElement element = spyJsonObject.get(property);
        assertNotNull(element);
        assertSame(JsonNull.INSTANCE, element);
    }

    @Test
    @Timeout(8000)
    public void testAdd_invokedByAddProperty() throws Exception {
        // Use reflection to access private members map
        Field membersField = JsonObject.class.getDeclaredField("members");
        membersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

        assertTrue(members.isEmpty());

        String property = "test";
        Character value = 'z';

        jsonObject.addProperty(property, value);

        // After addProperty, members map should contain the property
        assertFalse(members.isEmpty());
        assertTrue(members.containsKey(property));
        JsonElement element = members.get(property);
        assertTrue(element instanceof JsonPrimitive);
        JsonPrimitive primitive = (JsonPrimitive) element;
        assertEquals(value.charValue(), primitive.getAsCharacter());
    }
}
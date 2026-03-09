package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class JsonObject_419_1Test {

    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() {
        jsonObject = new JsonObject();
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withNonNullNumber_addsJsonPrimitive() throws Exception {
        String property = "testNumber";
        Number value = 123;

        jsonObject.addProperty(property, value);

        // Use reflection to get private field 'members'
        Field membersField = JsonObject.class.getDeclaredField("members");
        membersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

        assertTrue(members.containsKey(property));
        JsonElement element = members.get(property);
        assertNotNull(element);
        assertTrue(element instanceof JsonPrimitive);
        JsonPrimitive primitive = (JsonPrimitive) element;
        assertEquals(value, primitive.getAsNumber());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withNullNumber_addsJsonNull() throws Exception {
        String property = "nullNumber";
        Number value = null;

        jsonObject.addProperty(property, value);

        // Use reflection to get private field 'members'
        Field membersField = JsonObject.class.getDeclaredField("members");
        membersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

        assertTrue(members.containsKey(property));
        JsonElement element = members.get(property);
        assertNotNull(element);
        assertSame(JsonNull.INSTANCE, element);
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_invokesAddMethod_correctly() throws Exception {
        JsonObject spyObject = spy(new JsonObject());

        String property = "spyNumber";
        Number value = 456;

        spyObject.addProperty(property, value);

        // Verify add() called with correct args
        ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);

        verify(spyObject).add(propertyCaptor.capture(), elementCaptor.capture());

        assertEquals(property, propertyCaptor.getValue());
        JsonElement element = elementCaptor.getValue();
        assertNotNull(element);
        assertTrue(element instanceof JsonPrimitive);
        JsonPrimitive primitive = (JsonPrimitive) element;
        assertEquals(value, primitive.getAsNumber());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_invokesAddMethod_withNullValue() throws Exception {
        JsonObject spyObject = spy(new JsonObject());

        String property = "spyNullNumber";
        Number value = null;

        spyObject.addProperty(property, value);

        // Verify add() called with JsonNull.INSTANCE
        ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);

        verify(spyObject).add(propertyCaptor.capture(), elementCaptor.capture());

        assertEquals(property, propertyCaptor.getValue());
        JsonElement element = elementCaptor.getValue();
        assertSame(JsonNull.INSTANCE, element);
    }
}
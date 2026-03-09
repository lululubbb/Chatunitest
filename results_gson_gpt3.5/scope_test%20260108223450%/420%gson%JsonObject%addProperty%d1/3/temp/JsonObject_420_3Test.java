package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.Map;

public class JsonObject_420_3Test {

    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() {
        jsonObject = spy(new JsonObject());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_WithBooleanTrue() {
        jsonObject.addProperty("keyTrue", Boolean.TRUE);

        ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<JsonElement> valueCaptor = ArgumentCaptor.forClass(JsonElement.class);
        verify(jsonObject).add(propertyCaptor.capture(), valueCaptor.capture());

        assertEquals("keyTrue", propertyCaptor.getValue());
        JsonElement element = valueCaptor.getValue();
        assertTrue(element instanceof JsonPrimitive);
        assertEquals(Boolean.TRUE, ((JsonPrimitive) element).getAsBoolean());

        // Verify internal members map contains the key
        Map<String, JsonElement> members = getMembersMap(jsonObject);
        assertTrue(members.containsKey("keyTrue"));
        assertEquals(element, members.get("keyTrue"));
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_WithBooleanFalse() {
        jsonObject.addProperty("keyFalse", Boolean.FALSE);

        ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<JsonElement> valueCaptor = ArgumentCaptor.forClass(JsonElement.class);
        verify(jsonObject).add(propertyCaptor.capture(), valueCaptor.capture());

        assertEquals("keyFalse", propertyCaptor.getValue());
        JsonElement element = valueCaptor.getValue();
        assertTrue(element instanceof JsonPrimitive);
        assertEquals(Boolean.FALSE, ((JsonPrimitive) element).getAsBoolean());

        Map<String, JsonElement> members = getMembersMap(jsonObject);
        assertTrue(members.containsKey("keyFalse"));
        assertEquals(element, members.get("keyFalse"));
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_WithBooleanNull() {
        // Cast null explicitly to Boolean to avoid ambiguity
        jsonObject.addProperty("keyNull", (Boolean) null);

        ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<JsonElement> valueCaptor = ArgumentCaptor.forClass(JsonElement.class);
        verify(jsonObject).add(propertyCaptor.capture(), valueCaptor.capture());

        assertEquals("keyNull", propertyCaptor.getValue());
        JsonElement element = valueCaptor.getValue();
        assertSame(JsonNull.INSTANCE, element);

        Map<String, JsonElement> members = getMembersMap(jsonObject);
        assertTrue(members.containsKey("keyNull"));
        assertSame(JsonNull.INSTANCE, members.get("keyNull"));
    }

    private Map<String, JsonElement> getMembersMap(JsonObject jsonObject) {
        try {
            Field membersField = JsonObject.class.getDeclaredField("members");
            membersField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);
            return members;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access private field 'members': " + e.getMessage());
            return null;
        }
    }
}
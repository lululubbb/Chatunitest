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
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class JsonObject_420_6Test {

    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() {
        jsonObject = spy(new JsonObject());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withBooleanTrue() {
        jsonObject.addProperty("keyTrue", true);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<JsonElement> valueCaptor = ArgumentCaptor.forClass(JsonElement.class);
        verify(jsonObject).add(keyCaptor.capture(), valueCaptor.capture());

        assertEquals("keyTrue", keyCaptor.getValue());
        JsonElement value = valueCaptor.getValue();
        assertTrue(value instanceof JsonPrimitive);
        assertEquals(true, ((JsonPrimitive) value).getAsBoolean());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withBooleanFalse() {
        jsonObject.addProperty("keyFalse", false);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<JsonElement> valueCaptor = ArgumentCaptor.forClass(JsonElement.class);
        verify(jsonObject).add(keyCaptor.capture(), valueCaptor.capture());

        assertEquals("keyFalse", keyCaptor.getValue());
        JsonElement value = valueCaptor.getValue();
        assertTrue(value instanceof JsonPrimitive);
        assertEquals(false, ((JsonPrimitive) value).getAsBoolean());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withBooleanNull() {
        jsonObject.addProperty("keyNull", (Boolean) null);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<JsonElement> valueCaptor = ArgumentCaptor.forClass(JsonElement.class);
        verify(jsonObject).add(keyCaptor.capture(), valueCaptor.capture());

        assertEquals("keyNull", keyCaptor.getValue());
        JsonElement value = valueCaptor.getValue();
        assertSame(JsonNull.INSTANCE, value);
    }

    @Test
    @Timeout(8000)
    public void testPrivateMembersFieldContainsAddedProperty() throws Exception {
        // Add a property to actually add to members map
        jsonObject.addProperty("privateKey", true);

        Field membersField = JsonObject.class.getDeclaredField("members");
        membersField.setAccessible(true);
        Object members = membersField.get(jsonObject);

        assertTrue(members instanceof Map);
        Map<?, ?> membersMap = (Map<?, ?>) members;

        assertTrue(membersMap.containsKey("privateKey"));
        JsonElement element = (JsonElement) membersMap.get("privateKey");
        assertTrue(element instanceof JsonPrimitive);
        assertEquals(true, ((JsonPrimitive) element).getAsBoolean());
    }

    @Test
    @Timeout(8000)
    public void testAddMethodIsCalledWithCorrectParameters() {
        jsonObject.addProperty("testKey", true);
        verify(jsonObject).add(eq("testKey"), any(JsonElement.class));
    }

}
package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.Map;

public class JsonObject_416_3Test {

    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() {
        jsonObject = new JsonObject();
    }

    @Test
    @Timeout(8000)
    public void testAdd_WithNonNullValue_AddsProperty() throws Exception {
        JsonPrimitive value = new JsonPrimitive("testValue");
        jsonObject.add("testProperty", value);

        // Access private field members via reflection
        Field membersField = JsonObject.class.getDeclaredField("members");
        membersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);

        assertTrue(members.containsKey("testProperty"));
        assertEquals(value, members.get("testProperty"));
    }

    @Test
    @Timeout(8000)
    public void testAdd_WithNullValue_AddsJsonNullInstance() throws Exception {
        jsonObject.add("testProperty", null);

        // Access private field members via reflection
        Field membersField = JsonObject.class.getDeclaredField("members");
        membersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);

        assertTrue(members.containsKey("testProperty"));
        assertSame(JsonNull.INSTANCE, members.get("testProperty"));
    }
}
package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;

import java.lang.reflect.Field;

public class JsonObject_428_2Test {

    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() {
        jsonObject = new JsonObject();
    }

    @Test
    @Timeout(8000)
    public void testGetAsJsonPrimitive_existingMember() throws Exception {
        // Using reflection to access private field 'members'
        Field membersField = JsonObject.class.getDeclaredField("members");
        membersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        com.google.gson.internal.LinkedTreeMap<String, JsonElement> members =
                (com.google.gson.internal.LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

        JsonPrimitive primitive = new JsonPrimitive("value");
        members.put("key", primitive);

        JsonPrimitive result = jsonObject.getAsJsonPrimitive("key");
        assertNotNull(result);
        assertEquals(primitive, result);
    }

    @Test
    @Timeout(8000)
    public void testGetAsJsonPrimitive_nonExistingMember() {
        JsonPrimitive result = jsonObject.getAsJsonPrimitive("missingKey");
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testGetAsJsonPrimitive_nullKey() {
        // Should return null if key is null and no entry exists
        JsonPrimitive result = jsonObject.getAsJsonPrimitive(null);
        assertNull(result);
    }
}
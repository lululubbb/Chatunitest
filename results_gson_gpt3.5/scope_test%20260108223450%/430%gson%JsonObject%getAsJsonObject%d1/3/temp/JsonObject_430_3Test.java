package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonObject_430_3Test {

    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() {
        jsonObject = new JsonObject();
    }

    @Test
    @Timeout(8000)
    public void testGetAsJsonObject_existingMember_returnsJsonObject() throws Exception {
        JsonObject expected = new JsonObject();

        // Using reflection to access private 'members' field and insert the expected JsonObject
        Field membersField = JsonObject.class.getDeclaredField("members");
        membersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
        members.put("testMember", expected);

        JsonObject actual = jsonObject.getAsJsonObject("testMember");
        assertSame(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testGetAsJsonObject_nonExistingMember_returnsNull() {
        JsonObject actual = jsonObject.getAsJsonObject("nonExisting");
        assertNull(actual);
    }

    @Test
    @Timeout(8000)
    public void testGetAsJsonObject_memberIsNotJsonObject_throwsClassCastException() throws Exception {
        // Add a JsonPrimitive to members with key "primitive"
        JsonPrimitive primitive = new JsonPrimitive("stringValue");

        Field membersField = JsonObject.class.getDeclaredField("members");
        membersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
        members.put("primitive", primitive);

        assertThrows(ClassCastException.class, () -> jsonObject.getAsJsonObject("primitive"));
    }
}
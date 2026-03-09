package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonObject_417_6Test {

    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() throws Exception {
        jsonObject = new JsonObject();

        // Use reflection to inject a mock LinkedTreeMap into the private final field 'members'
        Field membersField = JsonObject.class.getDeclaredField("members");
        membersField.setAccessible(true);

        @SuppressWarnings("unchecked")
        LinkedTreeMap<String, JsonElement> mockMembers = mock(LinkedTreeMap.class);

        membersField.set(jsonObject, mockMembers);
    }

    @Test
    @Timeout(8000)
    public void testRemove_existingProperty_returnsRemovedElement() {
        String property = "key";
        JsonElement removedElement = mock(JsonElement.class);

        // Retrieve the mocked members map
        LinkedTreeMap<String, JsonElement> members = getMembersMap(jsonObject);

        when(members.remove(property)).thenReturn(removedElement);

        JsonElement result = jsonObject.remove(property);

        assertSame(removedElement, result);
        verify(members).remove(property);
    }

    @Test
    @Timeout(8000)
    public void testRemove_nonExistingProperty_returnsNull() {
        String property = "nonExistingKey";

        LinkedTreeMap<String, JsonElement> members = getMembersMap(jsonObject);

        when(members.remove(property)).thenReturn(null);

        JsonElement result = jsonObject.remove(property);

        assertNull(result);
        verify(members).remove(property);
    }

    private LinkedTreeMap<String, JsonElement> getMembersMap(JsonObject jsonObject) {
        try {
            Field membersField = JsonObject.class.getDeclaredField("members");
            membersField.setAccessible(true);
            @SuppressWarnings("unchecked")
            LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
            return members;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
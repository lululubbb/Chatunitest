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

public class JsonObject_417_1Test {

    private JsonObject jsonObject;

    @BeforeEach
    void setUp() throws Exception {
        jsonObject = new JsonObject();
        // Inject a mocked LinkedTreeMap into the private final field 'members'
        LinkedTreeMap<String, JsonElement> mockedMembers = mock(LinkedTreeMap.class);
        Field membersField = JsonObject.class.getDeclaredField("members");
        membersField.setAccessible(true);
        membersField.set(jsonObject, mockedMembers);
    }

    @Test
    @Timeout(8000)
    void remove_existingProperty_returnsRemovedElement() {
        LinkedTreeMap<String, JsonElement> members = getMembersMap(jsonObject);
        JsonElement removedElement = mock(JsonElement.class);
        when(members.remove("key")).thenReturn(removedElement);

        JsonElement result = jsonObject.remove("key");

        assertSame(removedElement, result);
        verify(members).remove("key");
    }

    @Test
    @Timeout(8000)
    void remove_nonExistingProperty_returnsNull() {
        LinkedTreeMap<String, JsonElement> members = getMembersMap(jsonObject);
        when(members.remove("absent")).thenReturn(null);

        JsonElement result = jsonObject.remove("absent");

        assertNull(result);
        verify(members).remove("absent");
    }

    @SuppressWarnings("unchecked")
    private LinkedTreeMap<String, JsonElement> getMembersMap(JsonObject obj) {
        try {
            Field membersField = JsonObject.class.getDeclaredField("members");
            membersField.setAccessible(true);
            return (LinkedTreeMap<String, JsonElement>) membersField.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
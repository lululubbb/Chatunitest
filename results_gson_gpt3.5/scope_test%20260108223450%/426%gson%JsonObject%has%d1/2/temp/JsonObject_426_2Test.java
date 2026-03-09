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

public class JsonObject_426_2Test {

  private JsonObject jsonObject;

  @BeforeEach
  void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  void has_existingKey_returnsTrue() throws Exception {
    // Use reflection to access private 'members' field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    JsonElement mockElement = mock(JsonElement.class);
    members.put("key1", mockElement);

    assertTrue(jsonObject.has("key1"));
  }

  @Test
    @Timeout(8000)
  void has_nonExistingKey_returnsFalse() {
    assertFalse(jsonObject.has("nonExistingKey"));
  }

  @Test
    @Timeout(8000)
  void has_nullKey_returnsFalse() {
    // The members map likely does not allow null keys, but test anyway
    assertFalse(jsonObject.has(null));
  }
}
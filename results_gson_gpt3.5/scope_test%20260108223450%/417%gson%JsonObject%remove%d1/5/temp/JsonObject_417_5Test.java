package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_417_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  void remove_existingProperty_returnsRemovedElement() throws Exception {
    // Use reflection to access the private members field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    JsonElement element = mock(JsonElement.class);
    members.put("key1", element);

    JsonElement removed = jsonObject.remove("key1");

    assertSame(element, removed);
    assertFalse(members.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  void remove_nonExistingProperty_returnsNull() {
    JsonElement removed = jsonObject.remove("nonexistent");
    assertNull(removed);
  }

  @Test
    @Timeout(8000)
  void remove_nullProperty_returnsNull() {
    // According to LinkedTreeMap behavior, removing null key returns null
    JsonElement removed = jsonObject.remove(null);
    assertNull(removed);
  }
}
package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.LinkedHashSet;

public class JsonObject_423_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testKeySet_Empty() {
    Set<String> keys = jsonObject.keySet();
    assertNotNull(keys);
    assertTrue(keys.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testKeySet_WithElements() throws Exception {
    // Use reflection to access private field 'members'
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    @SuppressWarnings("unchecked")
    com.google.gson.internal.LinkedTreeMap<String, JsonElement> members =
        (com.google.gson.internal.LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    JsonPrimitive element1 = new JsonPrimitive("value1");
    JsonPrimitive element2 = new JsonPrimitive(123);

    members.put("key1", element1);
    members.put("key2", element2);

    Set<String> keys = jsonObject.keySet();

    assertNotNull(keys);
    assertEquals(2, keys.size());
    assertTrue(keys.contains("key1"));
    assertTrue(keys.contains("key2"));
  }
}
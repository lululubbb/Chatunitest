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

class JsonObject_431_2Test {

  private JsonObject jsonObject;

  @BeforeEach
  void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  void testAsMap_EmptyInitially() throws Exception {
    Map<String, JsonElement> map = jsonObject.asMap();
    assertNotNull(map);
    assertTrue(map.isEmpty());

    // Use reflection to verify that returned map is the actual members field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);
    assertSame(members, map);
  }

  @Test
    @Timeout(8000)
  void testAsMap_WithEntries() throws Exception {
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);

    jsonObject.add("key1", element1);
    jsonObject.add("key2", element2);

    Map<String, JsonElement> map = jsonObject.asMap();
    assertNotNull(map);
    assertEquals(2, map.size());
    assertSame(element1, map.get("key1"));
    assertSame(element2, map.get("key2"));

    // Modifying the map should reflect in the JsonObject members map
    JsonElement element3 = mock(JsonElement.class);
    map.put("key3", element3);
    assertSame(element3, jsonObject.get("key3"));
  }
}
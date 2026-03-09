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

public class JsonObject_431_4Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testAsMap_EmptyInitially() throws Exception {
    Map<String, JsonElement> map = jsonObject.asMap();
    assertNotNull(map);
    assertTrue(map.isEmpty());

    // Check that returned map is the actual members map via reflection
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);
    assertSame(members, map);
  }

  @Test
    @Timeout(8000)
  public void testAsMap_WithEntries() throws Exception {
    JsonPrimitive value1 = new JsonPrimitive("value1");
    JsonPrimitive value2 = new JsonPrimitive(123);

    jsonObject.add("key1", value1);
    jsonObject.add("key2", value2);

    Map<String, JsonElement> map = jsonObject.asMap();

    assertNotNull(map);
    assertEquals(2, map.size());
    assertTrue(map.containsKey("key1"));
    assertTrue(map.containsKey("key2"));
    assertSame(value1, map.get("key1"));
    assertSame(value2, map.get("key2"));

    // Check that modifying the map affects the JsonObject members map
    map.remove("key1");
    assertFalse(jsonObject.has("key1"));
  }
}
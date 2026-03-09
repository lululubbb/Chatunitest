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

public class JsonObject_431_3Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testAsMap_InitiallyEmpty() {
    Map<String, JsonElement> map = jsonObject.asMap();
    assertNotNull(map);
    assertTrue(map.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testAsMap_WithEntries() throws Exception {
    // Prepare a mock JsonElement
    JsonElement element = mock(JsonElement.class);

    // Use reflection to add entries directly to the private 'members' field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.put("key1", element);
    members.put("key2", element);

    Map<String, JsonElement> map = jsonObject.asMap();

    assertNotNull(map);
    assertEquals(2, map.size());
    assertSame(element, map.get("key1"));
    assertSame(element, map.get("key2"));

    // Verify that the returned map is the actual members map (same instance)
    assertSame(members, map);
  }

  @Test
    @Timeout(8000)
  public void testAsMap_ModifyReturnedMapReflectsInJsonObject() throws Exception {
    JsonElement element = mock(JsonElement.class);

    Map<String, JsonElement> map = jsonObject.asMap();
    map.put("newKey", element);

    // Access private members field to verify the change
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertEquals(1, members.size());
    assertSame(element, members.get("newKey"));
  }
}
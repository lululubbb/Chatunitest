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

public class JsonObject_431_1Test {

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

    // Verify that map is the same as the private field "members"
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    Object members = membersField.get(jsonObject);
    assertSame(members, map);
  }

  @Test
    @Timeout(8000)
  public void testAsMap_WithEntries() throws Exception {
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);

    // Use add method to add entries
    jsonObject.add("key1", element1);
    jsonObject.add("key2", element2);

    Map<String, JsonElement> map = jsonObject.asMap();
    assertNotNull(map);
    assertEquals(2, map.size());
    assertSame(element1, map.get("key1"));
    assertSame(element2, map.get("key2"));

    // Verify that map is the same as the private field "members"
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    Object members = membersField.get(jsonObject);
    assertSame(members, map);
  }
}
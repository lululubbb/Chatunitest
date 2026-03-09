package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_422_6Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testEntrySet_empty() {
    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
    assertNotNull(entries);
    assertTrue(entries.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testEntrySet_withEntries() throws Exception {
    // Use reflection to get the private 'members' field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    // Create a LinkedTreeMap and add entries
    LinkedTreeMap<String, JsonElement> members = new LinkedTreeMap<>(false);
    JsonPrimitive value1 = new JsonPrimitive("value1");
    JsonPrimitive value2 = new JsonPrimitive(123);
    members.put("key1", value1);
    members.put("key2", value2);

    // Set the private field 'members' to our map with entries
    membersField.set(jsonObject, members);

    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();

    assertNotNull(entries);
    assertEquals(2, entries.size());

    boolean foundKey1 = false;
    boolean foundKey2 = false;
    for (Map.Entry<String, JsonElement> entry : entries) {
      if ("key1".equals(entry.getKey())) {
        foundKey1 = true;
        assertEquals(value1, entry.getValue());
      } else if ("key2".equals(entry.getKey())) {
        foundKey2 = true;
        assertEquals(value2, entry.getValue());
      }
    }

    assertTrue(foundKey1);
    assertTrue(foundKey2);
  }
}
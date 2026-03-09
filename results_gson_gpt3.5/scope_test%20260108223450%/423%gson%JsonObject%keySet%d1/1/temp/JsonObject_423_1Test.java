package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

public class JsonObject_423_1Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void keySet_emptyObject_returnsEmptySet() {
    Set<String> keys = jsonObject.keySet();
    assertNotNull(keys);
    assertTrue(keys.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void keySet_afterAddingProperties_containsAddedKeys() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", 123);
    jsonObject.addProperty("key3", true);

    Set<String> keys = jsonObject.keySet();

    assertNotNull(keys);
    assertEquals(3, keys.size());
    assertTrue(keys.contains("key1"));
    assertTrue(keys.contains("key2"));
    assertTrue(keys.contains("key3"));
  }

  @Test
    @Timeout(8000)
  public void keySet_afterRemovingProperty_doesNotContainRemovedKey() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", "value2");

    JsonElement removed = jsonObject.remove("key1");
    assertNotNull(removed);

    Set<String> keys = jsonObject.keySet();

    assertNotNull(keys);
    assertEquals(1, keys.size());
    assertFalse(keys.contains("key1"));
    assertTrue(keys.contains("key2"));
  }

  @Test
    @Timeout(8000)
  public void keySet_reflectMembersModification_reflectsInKeySet() throws Exception {
    // Use reflection to access private field 'members'
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    // Put an entry directly in members map
    members.put("reflectedKey", JsonNull.INSTANCE);

    Set<String> keys = jsonObject.keySet();

    assertNotNull(keys);
    assertTrue(keys.contains("reflectedKey"));
  }
}
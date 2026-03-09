package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonObject_keySet_Test {

  private JsonObject jsonObject;

  @BeforeEach
  void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  void keySet_emptyObject_returnsEmptySet() {
    Set<String> keys = jsonObject.keySet();
    assertNotNull(keys);
    assertTrue(keys.isEmpty());
  }

  @Test
    @Timeout(8000)
  void keySet_afterAddingProperties_containsAllKeys() {
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
  void keySet_afterRemovingProperty_keyIsRemoved() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", 123);
    jsonObject.remove("key1");

    Set<String> keys = jsonObject.keySet();

    assertNotNull(keys);
    assertEquals(1, keys.size());
    assertFalse(keys.contains("key1"));
    assertTrue(keys.contains("key2"));
  }
}
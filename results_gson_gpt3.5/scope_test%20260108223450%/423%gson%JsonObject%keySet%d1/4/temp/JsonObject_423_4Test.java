package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

public class JsonObject_423_4Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testKeySet_empty() {
    Set<String> keys = jsonObject.keySet();
    assertNotNull(keys);
    assertTrue(keys.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testKeySet_withEntries() throws Exception {
    // Use reflection to access private 'members' field and add entries directly
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members =
        (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    JsonPrimitive v1 = new JsonPrimitive("value1");
    JsonPrimitive v2 = new JsonPrimitive(123);

    members.put("key1", v1);
    members.put("key2", v2);

    Set<String> keys = jsonObject.keySet();

    assertNotNull(keys);
    assertEquals(2, keys.size());
    assertTrue(keys.contains("key1"));
    assertTrue(keys.contains("key2"));
  }

  @Test
    @Timeout(8000)
  public void testKeySet_afterAddMethod() {
    jsonObject.addProperty("prop1", "stringValue");
    jsonObject.addProperty("prop2", 42);
    jsonObject.addProperty("prop3", true);

    Set<String> keys = jsonObject.keySet();

    assertNotNull(keys);
    assertEquals(3, keys.size());
    assertTrue(keys.contains("prop1"));
    assertTrue(keys.contains("prop2"));
    assertTrue(keys.contains("prop3"));
  }

  @Test
    @Timeout(8000)
  public void testKeySet_afterRemoveMethod() {
    jsonObject.addProperty("prop1", "val1");
    jsonObject.addProperty("prop2", "val2");
    jsonObject.addProperty("prop3", "val3");

    jsonObject.remove("prop2");

    Set<String> keys = jsonObject.keySet();

    assertNotNull(keys);
    assertEquals(2, keys.size());
    assertTrue(keys.contains("prop1"));
    assertFalse(keys.contains("prop2"));
    assertTrue(keys.contains("prop3"));
  }
}
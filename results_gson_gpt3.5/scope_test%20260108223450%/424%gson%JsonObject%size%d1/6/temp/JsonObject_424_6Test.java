package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_424_6Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testSize_empty() {
    assertEquals(0, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void testSize_nonEmpty() throws Exception {
    // Use reflection to access private field 'members'
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    com.google.gson.internal.LinkedTreeMap<String, JsonElement> members =
        (com.google.gson.internal.LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    // Initially empty
    assertEquals(0, jsonObject.size());

    // Add one member
    members.put("key1", new JsonPrimitive("value1"));
    assertEquals(1, jsonObject.size());

    // Add another member
    members.put("key2", new JsonPrimitive(123));
    assertEquals(2, jsonObject.size());

    // Remove a member
    members.remove("key1");
    assertEquals(1, jsonObject.size());

    // Clear all members
    members.clear();
    assertEquals(0, jsonObject.size());
  }
}
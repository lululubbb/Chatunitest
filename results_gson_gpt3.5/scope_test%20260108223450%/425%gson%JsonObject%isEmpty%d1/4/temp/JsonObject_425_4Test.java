package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonObject_425_4Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testIsEmpty_whenEmpty_shouldReturnTrue() {
    // Initially the JsonObject is empty
    assertTrue(jsonObject.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testIsEmpty_whenNotEmpty_shouldReturnFalse() throws Exception {
    // Use reflection to access private field "members"
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members =
        (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    // Add a dummy entry to members to simulate non-empty state
    members.put("key", new JsonPrimitive("value"));

    assertFalse(jsonObject.isEmpty());
  }
}
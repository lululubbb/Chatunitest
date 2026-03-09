package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonObject_425_2Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testIsEmpty_whenEmpty_shouldReturnTrue() throws Exception {
    // Ensure members map is empty via reflection
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    Object members = membersField.get(jsonObject);
    // Clear the map if necessary (should be empty by default)
    if (members instanceof java.util.Map) {
      ((java.util.Map<?, ?>) members).clear();
    }

    assertTrue(jsonObject.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testIsEmpty_whenNotEmpty_shouldReturnFalse() throws Exception {
    // Add a property to make members non-empty
    jsonObject.addProperty("key", "value");

    assertFalse(jsonObject.isEmpty());
  }
}
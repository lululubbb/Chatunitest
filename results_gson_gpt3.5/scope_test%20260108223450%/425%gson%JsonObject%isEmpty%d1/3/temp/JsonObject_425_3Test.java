package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JsonObject_425_3Test {

  @Test
    @Timeout(8000)
  public void testIsEmpty_whenEmpty() {
    JsonObject jsonObject = new JsonObject();
    assertTrue(jsonObject.isEmpty(), "New JsonObject should be empty");
  }

  @Test
    @Timeout(8000)
  public void testIsEmpty_whenNotEmpty() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key", "value");
    assertFalse(jsonObject.isEmpty(), "JsonObject with one property should not be empty");
  }

}
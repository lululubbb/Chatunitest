package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_425_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testIsEmpty_whenNewObject_thenTrue() {
    assertTrue(jsonObject.isEmpty(), "New JsonObject should be empty");
  }

  @Test
    @Timeout(8000)
  public void testIsEmpty_afterAddProperty_thenFalse() {
    jsonObject.addProperty("key", "value");
    assertFalse(jsonObject.isEmpty(), "JsonObject with one property should not be empty");
  }

  @Test
    @Timeout(8000)
  public void testIsEmpty_afterAddAndRemoveProperty_thenTrue() {
    jsonObject.addProperty("key", "value");
    jsonObject.remove("key");
    assertTrue(jsonObject.isEmpty(), "JsonObject should be empty after removing the only property");
  }
}
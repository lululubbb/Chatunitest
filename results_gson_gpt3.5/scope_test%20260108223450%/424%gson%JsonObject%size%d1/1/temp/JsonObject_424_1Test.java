package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_424_1Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testSize_EmptyObject() {
    assertEquals(0, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void testSize_AfterAddingOneProperty() {
    jsonObject.addProperty("key1", "value1");
    assertEquals(1, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void testSize_AfterAddingMultipleProperties() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", 123);
    jsonObject.addProperty("key3", true);
    assertEquals(3, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void testSize_AfterRemovingProperty() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", 123);
    jsonObject.remove("key1");
    assertEquals(1, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void testSize_AfterRemovingNonExistentProperty() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.remove("nonexistent");
    assertEquals(1, jsonObject.size());
  }
}
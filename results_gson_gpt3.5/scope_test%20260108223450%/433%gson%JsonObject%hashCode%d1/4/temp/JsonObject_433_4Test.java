package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_433_4Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testHashCode_emptyObject() {
    int expected = jsonObject.entrySet().hashCode();
    assertEquals(expected, jsonObject.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_withProperties() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", 123);
    jsonObject.addProperty("key3", true);

    int expected = jsonObject.entrySet().hashCode();
    assertEquals(expected, jsonObject.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_afterRemoveProperty() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", 123);
    jsonObject.remove("key1");

    int expected = jsonObject.entrySet().hashCode();
    assertEquals(expected, jsonObject.hashCode());
  }

}
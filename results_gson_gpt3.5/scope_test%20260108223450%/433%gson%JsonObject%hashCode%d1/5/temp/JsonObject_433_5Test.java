package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_433_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testHashCode_emptyObject() {
    // When JsonObject is empty, hashCode should be consistent with empty map's hashCode
    int expectedHashCode = jsonObject.entrySet().hashCode();
    assertEquals(expectedHashCode, jsonObject.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_withMembers() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", 123);
    jsonObject.addProperty("key3", true);

    int expectedHashCode = jsonObject.entrySet().hashCode();
    assertEquals(expectedHashCode, jsonObject.hashCode());
  }
}
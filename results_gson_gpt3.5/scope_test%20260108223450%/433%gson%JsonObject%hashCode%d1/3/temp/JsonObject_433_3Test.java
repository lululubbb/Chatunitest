package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_433_3Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testHashCode_emptyObject() {
    // When JsonObject is empty, its hashCode should be the hashCode of an empty LinkedTreeMap
    int expectedHashCode = jsonObject.entrySet().hashCode();
    assertEquals(expectedHashCode, jsonObject.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_withMembers() {
    // Add some members and check hashCode consistency
    JsonPrimitive value1 = new JsonPrimitive("value1");
    JsonPrimitive value2 = new JsonPrimitive(123);
    jsonObject.add("key1", value1);
    jsonObject.add("key2", value2);

    int expectedHashCode = jsonObject.entrySet().hashCode();
    assertEquals(expectedHashCode, jsonObject.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_consistency() {
    // hashCode should be consistent across multiple calls
    jsonObject.addProperty("a", "test");
    int hash1 = jsonObject.hashCode();
    int hash2 = jsonObject.hashCode();
    assertEquals(hash1, hash2);
  }
}
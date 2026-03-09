package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class JsonObject_433_1Test {

  @Test
    @Timeout(8000)
  public void testHashCode_emptyObject() {
    JsonObject jsonObject = new JsonObject();
    int expectedHashCode = jsonObject.entrySet().hashCode();
    assertEquals(expectedHashCode, jsonObject.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_nonEmptyObject() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", 123);
    int expectedHashCode = jsonObject.entrySet().hashCode();
    assertEquals(expectedHashCode, jsonObject.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_sameContent_sameHashCode() {
    JsonObject jsonObject1 = new JsonObject();
    jsonObject1.addProperty("key", "value");

    JsonObject jsonObject2 = new JsonObject();
    jsonObject2.addProperty("key", "value");

    assertEquals(jsonObject1.hashCode(), jsonObject2.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_differentContent_sameHashCode() {
    // It is possible for hashCodes to collide, so avoid asserting not equals.
    JsonObject jsonObject1 = new JsonObject();
    jsonObject1.addProperty("key1", "value1");

    JsonObject jsonObject2 = new JsonObject();
    jsonObject2.addProperty("key2", "value2");

    // Instead, assert they are not equal objects
    assertEquals(false, jsonObject1.equals(jsonObject2));
  }
}
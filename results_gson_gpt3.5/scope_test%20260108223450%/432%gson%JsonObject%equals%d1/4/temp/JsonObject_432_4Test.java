package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonObject_432_4Test {

  @Test
    @Timeout(8000)
  public void testEquals_sameInstance() {
    JsonObject jsonObject = new JsonObject();
    assertTrue(jsonObject.equals(jsonObject));
  }

  @Test
    @Timeout(8000)
  public void testEquals_nullObject() {
    JsonObject jsonObject = new JsonObject();
    assertFalse(jsonObject.equals(null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentClassObject() {
    JsonObject jsonObject = new JsonObject();
    Object other = new Object();
    assertFalse(jsonObject.equals(other));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentMembers() throws Exception {
    JsonObject jsonObject1 = new JsonObject();
    JsonObject jsonObject2 = new JsonObject();

    // Add a property to jsonObject1
    jsonObject1.addProperty("key1", "value1");

    // jsonObject2 remains empty, so members differ
    assertFalse(jsonObject1.equals(jsonObject2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_sameMembers() throws Exception {
    JsonObject jsonObject1 = new JsonObject();
    JsonObject jsonObject2 = new JsonObject();

    jsonObject1.addProperty("key1", "value1");
    jsonObject2.addProperty("key1", "value1");

    assertTrue(jsonObject1.equals(jsonObject2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentMembersWithMultipleProperties() {
    JsonObject jsonObject1 = new JsonObject();
    JsonObject jsonObject2 = new JsonObject();

    jsonObject1.addProperty("key1", "value1");
    jsonObject1.addProperty("key2", 123);

    jsonObject2.addProperty("key1", "value1");
    jsonObject2.addProperty("key2", 456);

    assertFalse(jsonObject1.equals(jsonObject2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_bothEmpty() {
    JsonObject jsonObject1 = new JsonObject();
    JsonObject jsonObject2 = new JsonObject();

    assertTrue(jsonObject1.equals(jsonObject2));
  }
}
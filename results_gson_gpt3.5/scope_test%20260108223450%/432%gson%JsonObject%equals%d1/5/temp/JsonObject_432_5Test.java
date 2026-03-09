package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JsonObjectEqualsTest {

  @Test
    @Timeout(8000)
  public void testEquals_sameReference() {
    JsonObject obj = new JsonObject();
    assertTrue(obj.equals(obj));
  }

  @Test
    @Timeout(8000)
  public void testEquals_null() {
    JsonObject obj = new JsonObject();
    assertFalse(obj.equals(null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentClass() {
    JsonObject obj = new JsonObject();
    String other = "not a JsonObject";
    assertFalse(obj.equals(other));
  }

  @Test
    @Timeout(8000)
  public void testEquals_emptyObjects() {
    JsonObject obj1 = new JsonObject();
    JsonObject obj2 = new JsonObject();
    assertTrue(obj1.equals(obj2));
    assertTrue(obj2.equals(obj1));
  }

  @Test
    @Timeout(8000)
  public void testEquals_sameMembers() {
    JsonObject obj1 = new JsonObject();
    JsonObject obj2 = new JsonObject();

    obj1.addProperty("key", "value");
    obj2.addProperty("key", "value");

    assertTrue(obj1.equals(obj2));
    assertTrue(obj2.equals(obj1));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentMembers() {
    JsonObject obj1 = new JsonObject();
    JsonObject obj2 = new JsonObject();

    obj1.addProperty("key1", "value1");
    obj2.addProperty("key2", "value2");

    assertFalse(obj1.equals(obj2));
    assertFalse(obj2.equals(obj1));
  }

  @Test
    @Timeout(8000)
  public void testEquals_membersWithDifferentValues() {
    JsonObject obj1 = new JsonObject();
    JsonObject obj2 = new JsonObject();

    obj1.addProperty("key", "value1");
    obj2.addProperty("key", "value2");

    assertFalse(obj1.equals(obj2));
    assertFalse(obj2.equals(obj1));
  }
}
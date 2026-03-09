package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.gson.internal.LinkedTreeMap;

public class JsonObject_432_2Test {

  @Test
    @Timeout(8000)
  public void testEquals_sameInstance() {
    JsonObject jsonObject = new JsonObject();
    assertTrue(jsonObject.equals(jsonObject));
  }

  @Test
    @Timeout(8000)
  public void testEquals_null() {
    JsonObject jsonObject = new JsonObject();
    assertFalse(jsonObject.equals(null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentClass() {
    JsonObject jsonObject = new JsonObject();
    Object other = new Object();
    assertFalse(jsonObject.equals(other));
  }

  @Test
    @Timeout(8000)
  public void testEquals_equalMembers() throws Exception {
    JsonObject jsonObject1 = new JsonObject();
    JsonObject jsonObject2 = new JsonObject();

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    // Use real LinkedTreeMap instances with identical content
    LinkedTreeMap<String, JsonElement> members1 = new LinkedTreeMap<>(false);
    LinkedTreeMap<String, JsonElement> members2 = new LinkedTreeMap<>(false);

    // Insert identical entries to ensure equals returns true
    JsonPrimitive element = new JsonPrimitive("value");
    members1.put("key", element);
    members2.put("key", element);

    membersField.set(jsonObject1, members1);
    membersField.set(jsonObject2, members2);

    assertTrue(jsonObject1.equals(jsonObject2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_unequalMembers() throws Exception {
    JsonObject jsonObject1 = new JsonObject();
    JsonObject jsonObject2 = new JsonObject();

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    LinkedTreeMap<String, JsonElement> members1 = new LinkedTreeMap<>(false);
    LinkedTreeMap<String, JsonElement> members2 = new LinkedTreeMap<>(false);

    // Insert different entries to ensure equals returns false
    members1.put("key1", new JsonPrimitive("value1"));
    members2.put("key2", new JsonPrimitive("value2"));

    membersField.set(jsonObject1, members1);
    membersField.set(jsonObject2, members2);

    assertFalse(jsonObject1.equals(jsonObject2));
  }
}
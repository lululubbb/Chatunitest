package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;
import com.google.gson.internal.LinkedTreeMap;

public class JsonObject_432_6Test {

  @Test
    @Timeout(8000)
  public void testEquals_SameReference() {
    JsonObject jsonObject = new JsonObject();
    assertTrue(jsonObject.equals(jsonObject));
  }

  @Test
    @Timeout(8000)
  public void testEquals_NullObject() {
    JsonObject jsonObject = new JsonObject();
    assertFalse(jsonObject.equals(null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_DifferentClass() {
    JsonObject jsonObject = new JsonObject();
    Object other = new Object();
    assertFalse(jsonObject.equals(other));
  }

  @Test
    @Timeout(8000)
  public void testEquals_DifferentMembers() throws Exception {
    JsonObject jsonObject1 = new JsonObject();
    JsonObject jsonObject2 = new JsonObject();

    // Use reflection to access private members field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    LinkedTreeMap<String, JsonElement> members1 = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject1);
    LinkedTreeMap<String, JsonElement> members2 = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject2);

    // Add different elements to members maps
    members1.put("key1", new JsonPrimitive("value1"));
    members2.put("key2", new JsonPrimitive("value2"));

    assertFalse(jsonObject1.equals(jsonObject2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_EqualMembers() throws Exception {
    JsonObject jsonObject1 = new JsonObject();
    JsonObject jsonObject2 = new JsonObject();

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    LinkedTreeMap<String, JsonElement> members1 = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject1);
    LinkedTreeMap<String, JsonElement> members2 = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject2);

    JsonPrimitive value = new JsonPrimitive("value");

    members1.put("key", value);
    members2.put("key", value);

    assertTrue(jsonObject1.equals(jsonObject2));
  }

}
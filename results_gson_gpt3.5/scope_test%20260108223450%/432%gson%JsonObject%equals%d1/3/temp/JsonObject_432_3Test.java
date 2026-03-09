package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;

public class JsonObject_432_3Test {

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

    // Use reflection to set the private final members field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    LinkedTreeMap<String, JsonElement> map1 = new LinkedTreeMap<>(false);
    LinkedTreeMap<String, JsonElement> map2 = new LinkedTreeMap<>(false);

    JsonPrimitive element = new JsonPrimitive("value");
    map1.put("key", element);
    map2.put("key", element);

    membersField.set(jsonObject1, map1);
    membersField.set(jsonObject2, map2);

    assertTrue(jsonObject1.equals(jsonObject2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_unequalMembers() throws Exception {
    JsonObject jsonObject1 = new JsonObject();
    JsonObject jsonObject2 = new JsonObject();

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    LinkedTreeMap<String, JsonElement> map1 = new LinkedTreeMap<>(false);
    LinkedTreeMap<String, JsonElement> map2 = new LinkedTreeMap<>(false);

    JsonPrimitive element1 = new JsonPrimitive("value1");
    JsonPrimitive element2 = new JsonPrimitive("value2");
    map1.put("key", element1);
    map2.put("key", element2);

    membersField.set(jsonObject1, map1);
    membersField.set(jsonObject2, map2);

    assertFalse(jsonObject1.equals(jsonObject2));
  }
}
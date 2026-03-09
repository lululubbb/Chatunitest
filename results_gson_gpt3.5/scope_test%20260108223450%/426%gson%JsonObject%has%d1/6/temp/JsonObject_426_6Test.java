package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_426_6Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testHas_memberExists() throws Exception {
    // Use reflection to access private field 'members'
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    JsonElement mockElement = mock(JsonElement.class);
    members.put("key1", mockElement);

    assertTrue(jsonObject.has("key1"));
  }

  @Test
    @Timeout(8000)
  public void testHas_memberDoesNotExist() {
    assertFalse(jsonObject.has("nonexistent"));
  }

  @Test
    @Timeout(8000)
  public void testHas_nullKey() throws Exception {
    // According to LinkedTreeMap implementation, null keys are not allowed.
    // So we test that jsonObject.has(null) returns false without inserting null key.
    assertFalse(jsonObject.has(null));
  }
}
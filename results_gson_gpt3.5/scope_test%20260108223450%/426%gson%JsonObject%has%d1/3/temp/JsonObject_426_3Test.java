package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonObject_426_3Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testHas_whenMemberExists_shouldReturnTrue() throws Exception {
    // Use reflection to access private 'members' field and add an entry
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    JsonPrimitive value = new JsonPrimitive("value");
    members.put("existingKey", value);

    assertTrue(jsonObject.has("existingKey"));
  }

  @Test
    @Timeout(8000)
  public void testHas_whenMemberDoesNotExist_shouldReturnFalse() {
    assertFalse(jsonObject.has("nonExistingKey"));
  }

  @Test
    @Timeout(8000)
  public void testHas_whenMemberNameIsNull_shouldReturnFalse() {
    // members.containsKey(null) should return false
    assertFalse(jsonObject.has(null));
  }
}
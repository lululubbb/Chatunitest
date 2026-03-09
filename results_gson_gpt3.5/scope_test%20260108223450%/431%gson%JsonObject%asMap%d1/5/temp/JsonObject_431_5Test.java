package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonObject_431_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  void asMap_shouldReturnUnderlyingMap() throws Exception {
    // Use reflection to access private field 'members'
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    // Initially, members map should be empty
    Map<String, JsonElement> mapFromMethod = jsonObject.asMap();
    assertSame(members, mapFromMethod);
    assertTrue(mapFromMethod.isEmpty());

    // Add an element to members directly
    JsonPrimitive element = new JsonPrimitive("value");
    members.put("key", element);

    // asMap should reflect the change
    Map<String, JsonElement> mapAfterAdd = jsonObject.asMap();
    assertSame(members, mapAfterAdd);
    assertEquals(1, mapAfterAdd.size());
    assertTrue(mapAfterAdd.containsKey("key"));
    assertEquals(element, mapAfterAdd.get("key"));
  }

}
package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.Map;
import com.google.gson.internal.LinkedTreeMap;

class JsonObject_416_6Test {

  private JsonObject jsonObject;

  @BeforeEach
  void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  void testAdd_withNonNullValue_addsEntry() throws Exception {
    JsonPrimitive value = new JsonPrimitive("testValue");
    jsonObject.add("key1", value);

    // Access private field 'members' via reflection to verify insertion
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);

    assertEquals(1, members.size());
    assertSame(value, members.get("key1"));
  }

  @Test
    @Timeout(8000)
  void testAdd_withNullValue_addsJsonNullInstance() throws Exception {
    jsonObject.add("keyNull", null);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);

    assertEquals(1, members.size());
    assertSame(JsonNull.INSTANCE, members.get("keyNull"));
  }

  @Test
    @Timeout(8000)
  void testAdd_overwritesExistingValue() throws Exception {
    JsonPrimitive firstValue = new JsonPrimitive("first");
    JsonPrimitive secondValue = new JsonPrimitive("second");

    jsonObject.add("keyOverwrite", firstValue);
    jsonObject.add("keyOverwrite", secondValue);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);

    assertEquals(1, members.size());
    assertSame(secondValue, members.get("keyOverwrite"));
  }
}
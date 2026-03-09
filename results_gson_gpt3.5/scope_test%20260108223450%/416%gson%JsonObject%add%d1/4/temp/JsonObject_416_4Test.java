package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.Map;
import com.google.gson.internal.LinkedTreeMap;

class JsonObject_416_4Test {

  private JsonObject jsonObject;

  @BeforeEach
  void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  void add_withNonNullValue_addsProperty() throws Exception {
    JsonPrimitive value = new JsonPrimitive("testValue");
    jsonObject.add("testProperty", value);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey("testProperty"));
    assertEquals(value, members.get("testProperty"));
  }

  @Test
    @Timeout(8000)
  void add_withNullValue_addsPropertyWithJsonNullInstance() throws Exception {
    jsonObject.add("nullProperty", null);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey("nullProperty"));
    assertEquals(JsonNull.INSTANCE, members.get("nullProperty"));
  }

  @Test
    @Timeout(8000)
  void add_overwritesExistingProperty() throws Exception {
    JsonPrimitive firstValue = new JsonPrimitive("first");
    JsonPrimitive secondValue = new JsonPrimitive("second");

    jsonObject.add("property", firstValue);
    jsonObject.add("property", secondValue);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertEquals(1, members.size());
    assertEquals(secondValue, members.get("property"));
  }
}
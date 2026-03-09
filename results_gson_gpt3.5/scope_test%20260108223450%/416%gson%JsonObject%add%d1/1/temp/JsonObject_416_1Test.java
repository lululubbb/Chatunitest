package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.Map;

public class JsonObject_416_1Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testAdd_withNonNullValue_addsProperty() throws Exception {
    JsonPrimitive value = new JsonPrimitive("testValue");
    jsonObject.add("key", value);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);

    assertEquals(1, members.size());
    assertTrue(members.containsKey("key"));
    assertSame(value, members.get("key"));
  }

  @Test
    @Timeout(8000)
  public void testAdd_withNullValue_addsJsonNullInstance() throws Exception {
    jsonObject.add("key", null);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);

    assertEquals(1, members.size());
    assertTrue(members.containsKey("key"));
    assertSame(JsonNull.INSTANCE, members.get("key"));
  }

  @Test
    @Timeout(8000)
  public void testAdd_overwritesExistingValue() throws Exception {
    JsonPrimitive firstValue = new JsonPrimitive("first");
    JsonPrimitive secondValue = new JsonPrimitive("second");

    jsonObject.add("key", firstValue);
    jsonObject.add("key", secondValue);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);

    assertEquals(1, members.size());
    assertSame(secondValue, members.get("key"));
  }
}
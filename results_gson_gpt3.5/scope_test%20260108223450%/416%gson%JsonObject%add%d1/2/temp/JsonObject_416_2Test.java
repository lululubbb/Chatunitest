package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class JsonObject_416_2Test {

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

    // Access private field members via reflection
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey("key"));
    assertSame(value, members.get("key"));
  }

  @Test
    @Timeout(8000)
  public void testAdd_withNullValue_addsJsonNullInstance() throws Exception {
    jsonObject.add("nullKey", null);

    // Access private field members via reflection
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey("nullKey"));
    assertSame(JsonNull.INSTANCE, members.get("nullKey"));
  }

  @Test
    @Timeout(8000)
  public void testAdd_overwritesExistingValue() throws Exception {
    JsonPrimitive initialValue = new JsonPrimitive("initial");
    JsonPrimitive newValue = new JsonPrimitive("new");

    jsonObject.add("key", initialValue);
    jsonObject.add("key", newValue);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);

    assertEquals(1, members.size());
    assertSame(newValue, members.get("key"));
  }

  @Test
    @Timeout(8000)
  public void testAdd_withEmptyStringKey() throws Exception {
    JsonPrimitive value = new JsonPrimitive("emptyKeyValue");
    jsonObject.add("", value);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, JsonElement> members = (Map<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey(""));
    assertSame(value, members.get(""));
  }

  @Test
    @Timeout(8000)
  public void testAdd_withNullKey_throwsNullPointerException() {
    JsonPrimitive value = new JsonPrimitive("value");
    assertThrows(NullPointerException.class, () -> jsonObject.add(null, value));
  }
}
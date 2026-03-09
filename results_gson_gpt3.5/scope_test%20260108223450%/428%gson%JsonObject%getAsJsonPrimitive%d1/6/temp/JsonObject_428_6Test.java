package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class JsonObject_428_6Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_existingPrimitive() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("value");
    // Use reflection to add to private members map
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    com.google.gson.internal.LinkedTreeMap<String, JsonElement> members =
        (com.google.gson.internal.LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.put("key", primitive);

    JsonPrimitive result = jsonObject.getAsJsonPrimitive("key");
    assertNotNull(result);
    assertSame(primitive, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_nonExistingKey() {
    JsonPrimitive result = jsonObject.getAsJsonPrimitive("missing");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_nullKey() {
    // Adding a null key with a JsonPrimitive value to test null key behavior
    assertThrows(NullPointerException.class, () -> {
      Field membersField = JsonObject.class.getDeclaredField("members");
      membersField.setAccessible(true);
      @SuppressWarnings("unchecked")
      com.google.gson.internal.LinkedTreeMap<String, JsonElement> members =
          (com.google.gson.internal.LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
      members.put(null, new JsonPrimitive("nullKey"));
    });
  }
}
package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import java.lang.reflect.Field;

public class JsonObject_428_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_existingMember() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("testValue");

    // Use reflection to access private 'members' field and add an entry
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    java.util.Map<String, JsonElement> members = (java.util.Map<String, JsonElement>) membersField.get(jsonObject);
    members.put("key1", primitive);

    JsonPrimitive result = jsonObject.getAsJsonPrimitive("key1");
    assertNotNull(result);
    assertEquals(primitive, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_nonExistingMember() {
    JsonPrimitive result = jsonObject.getAsJsonPrimitive("nonexistent");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_nullKey() {
    JsonPrimitive result = jsonObject.getAsJsonPrimitive(null);
    assertNull(result);
  }
}
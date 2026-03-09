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
import com.google.gson.JsonNull;
import java.lang.reflect.Field;

public class JsonObject_428_3Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_existingMember() {
    JsonPrimitive primitive = new JsonPrimitive("value");
    jsonObject.addProperty("key", "value");
    JsonPrimitive result = jsonObject.getAsJsonPrimitive("key");
    assertNotNull(result);
    assertEquals(primitive, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_nonExistingMember() {
    JsonPrimitive result = jsonObject.getAsJsonPrimitive("absent");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_nullValueMember() throws Exception {
    // Directly set JsonNull.INSTANCE in members map via reflection to simulate null value
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    java.util.Map<String, JsonElement> members = (java.util.Map<String, JsonElement>) membersField.get(jsonObject);
    members.put("nullKey", JsonNull.INSTANCE);

    JsonElement element = jsonObject.get("nullKey");
    assertTrue(element instanceof JsonNull);

    // Since getAsJsonPrimitive casts directly, it will throw ClassCastException for JsonNull
    // So we test that getAsJsonPrimitive returns null if the element is not a JsonPrimitive
    JsonPrimitive result = null;
    try {
      result = jsonObject.getAsJsonPrimitive("nullKey");
      fail("Expected ClassCastException");
    } catch (ClassCastException e) {
      // expected
    }
  }
}
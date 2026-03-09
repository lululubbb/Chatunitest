package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class JsonObject_429_3Test {

  private JsonObject jsonObject;
  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_existingMember_returnsJsonArray() throws Exception {
    // Use reflection to access private 'members' field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    // Put a JsonArray in members map
    members.put("arrayMember", jsonArray);

    JsonArray result = jsonObject.getAsJsonArray("arrayMember");

    assertNotNull(result);
    assertSame(jsonArray, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_nonExistingMember_returnsNull() {
    JsonArray result = jsonObject.getAsJsonArray("nonExisting");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_memberIsNotJsonArray_returnsNull() throws Exception {
    // Put a JsonPrimitive instead of JsonArray
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    JsonPrimitive primitive = new JsonPrimitive("stringValue");
    members.put("primitiveMember", primitive);

    // The cast in getAsJsonArray is unchecked and will cause ClassCastException at runtime,
    // but since the method does not catch it, it will propagate.
    // So we test that ClassCastException is thrown.
    assertThrows(ClassCastException.class, () -> jsonObject.getAsJsonArray("primitiveMember"));
  }
}
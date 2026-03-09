package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class JsonObject_429_1Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_existingMember_returnsJsonArray() throws Exception {
    JsonArray jsonArray = new JsonArray();

    // Use reflection to access private field 'members'
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

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
  public void testGetAsJsonArray_memberIsNotJsonArray_castClassCastException() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");

    // Use reflection to access private field 'members'
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    members.put("primitiveMember", jsonPrimitive);

    assertThrows(ClassCastException.class, () -> {
      jsonObject.getAsJsonArray("primitiveMember");
    });
  }
}
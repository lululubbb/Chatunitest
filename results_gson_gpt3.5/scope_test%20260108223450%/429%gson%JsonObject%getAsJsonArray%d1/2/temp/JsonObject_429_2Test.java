package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonObject_429_2Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_existingMember() throws Exception {
    JsonArray jsonArray = new JsonArray();

    // Use reflection to set the private 'members' field
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
  public void testGetAsJsonArray_nonExistingMember() {
    JsonArray result = jsonObject.getAsJsonArray("nonExisting");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_memberIsNotJsonArray() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("stringValue");

    // Use reflection to set the private 'members' field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    members.put("primitiveMember", jsonPrimitive);

    // This will throw ClassCastException because the member is not a JsonArray
    assertThrows(ClassCastException.class, () -> jsonObject.getAsJsonArray("primitiveMember"));
  }
}
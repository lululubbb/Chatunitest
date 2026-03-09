package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class JsonObject_429_4Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_existingMember_returnsJsonArray() throws Exception {
    JsonArray jsonArray = new JsonArray();

    // Inject the member into the private 'members' field using reflection
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    com.google.gson.internal.LinkedTreeMap<String, JsonElement> members =
        (com.google.gson.internal.LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
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
  public void testGetAsJsonArray_nullKey_returnsNull() {
    JsonArray result = jsonObject.getAsJsonArray(null);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_memberIsNotJsonArray_returnsNullCast() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("primitive");

    // Inject the member into the private 'members' field using reflection
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    com.google.gson.internal.LinkedTreeMap<String, JsonElement> members =
        (com.google.gson.internal.LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.put("primitiveMember", primitive);

    // The method performs a cast without type check, so a ClassCastException is expected
    assertThrows(ClassCastException.class, () -> jsonObject.getAsJsonArray("primitiveMember"));
  }
}
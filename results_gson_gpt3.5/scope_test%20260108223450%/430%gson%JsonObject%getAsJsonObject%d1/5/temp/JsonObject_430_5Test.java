package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_430_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonObject_existingMember_returnsCorrectJsonObject() throws Exception {
    JsonObject expected = new JsonObject();

    // Use reflection to access private 'members' field and put an entry
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    members.put("testMember", expected);

    JsonObject actual = jsonObject.getAsJsonObject("testMember");
    assertSame(expected, actual);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonObject_nonExistingMember_returnsNull() {
    JsonObject actual = jsonObject.getAsJsonObject("nonExisting");
    assertNull(actual);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonObject_memberIsNotJsonObject_classCastException() throws Exception {
    // Put a JsonPrimitive instead of JsonObject
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    JsonPrimitive primitive = new JsonPrimitive("stringValue");
    members.put("primitiveMember", primitive);

    assertThrows(ClassCastException.class, () -> {
      jsonObject.getAsJsonObject("primitiveMember");
    });
  }
}
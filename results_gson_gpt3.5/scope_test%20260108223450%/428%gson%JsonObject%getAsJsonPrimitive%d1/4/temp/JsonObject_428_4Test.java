package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonObject_428_4Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_existingMember_returnsJsonPrimitive() throws Exception {
    // Prepare a JsonPrimitive to add
    JsonPrimitive primitive = new JsonPrimitive("value");

    // Use reflection to access the private 'members' field and add the member
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.put("key", primitive);

    // Call the focal method
    JsonPrimitive result = jsonObject.getAsJsonPrimitive("key");

    // Verify the returned value is the same JsonPrimitive instance
    assertNotNull(result);
    assertSame(primitive, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_nonExistingMember_returnsNull() {
    // No member added for "missingKey"

    // Call the focal method
    JsonPrimitive result = jsonObject.getAsJsonPrimitive("missingKey");

    // Should return null if member not present
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_memberIsNotJsonPrimitive_throwsClassCastException() throws Exception {
    // Prepare a mock JsonElement that is not a JsonPrimitive
    JsonElement element = mock(JsonElement.class);

    // Use reflection to access the private 'members' field and add the member
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.put("key", element);

    // Expect ClassCastException when casting to JsonPrimitive
    assertThrows(ClassCastException.class, () -> jsonObject.getAsJsonPrimitive("key"));
  }
}
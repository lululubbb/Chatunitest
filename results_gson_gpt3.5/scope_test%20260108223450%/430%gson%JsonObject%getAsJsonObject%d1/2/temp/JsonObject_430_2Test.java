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

public class JsonObject_430_2Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonObject_existingMember_returnsJsonObject() throws Exception {
    // Prepare a JsonObject to be returned
    JsonObject memberJsonObject = new JsonObject();

    // Use reflection to set the private members field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    // Create a LinkedTreeMap and put the member
    LinkedTreeMap<String, JsonElement> membersMap = new LinkedTreeMap<>(false);
    membersMap.put("member", memberJsonObject);

    // Set the field
    membersField.set(jsonObject, membersMap);

    // Call the focal method
    JsonObject result = jsonObject.getAsJsonObject("member");

    // Verify the returned object is the one we put
    assertSame(memberJsonObject, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonObject_nonExistingMember_returnsNull() throws Exception {
    // Use reflection to set empty members map
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    LinkedTreeMap<String, JsonElement> membersMap = new LinkedTreeMap<>(false);
    membersField.set(jsonObject, membersMap);

    JsonObject result = jsonObject.getAsJsonObject("nonexistent");

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonObject_valueIsNotJsonObject_throwsClassCastException() throws Exception {
    // Prepare a JsonElement that is NOT a JsonObject
    JsonElement jsonElement = mock(JsonElement.class);

    // Use reflection to set the private members field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    LinkedTreeMap<String, JsonElement> membersMap = new LinkedTreeMap<>(false);
    membersMap.put("member", jsonElement);

    membersField.set(jsonObject, membersMap);

    // Expect ClassCastException because the element is not a JsonObject
    assertThrows(ClassCastException.class, () -> jsonObject.getAsJsonObject("member"));
  }
}
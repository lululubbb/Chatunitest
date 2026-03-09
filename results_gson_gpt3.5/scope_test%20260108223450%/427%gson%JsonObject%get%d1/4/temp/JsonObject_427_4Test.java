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

public class JsonObject_427_4Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGet_existingMember_returnsValue() throws Exception {
    // Prepare a JsonElement mock to be returned
    JsonElement element = mock(JsonElement.class);

    // Use reflection to access the private 'members' field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    // Put a key-value pair in the members map
    members.put("key1", element);

    // Call the focal method
    JsonElement result = jsonObject.get("key1");

    // Verify the returned element is the same as the one put in the map
    assertSame(element, result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nonExistingMember_returnsNull() {
    // The members map is empty, so get should return null for any key
    JsonElement result = jsonObject.get("nonExistingKey");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKey_returnsNull() {
    // The members map should return null if key is null
    JsonElement result = jsonObject.get(null);
    assertNull(result);
  }
}
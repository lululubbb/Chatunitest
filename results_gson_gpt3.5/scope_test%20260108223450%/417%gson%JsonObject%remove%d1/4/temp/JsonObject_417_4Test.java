package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_417_4Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testRemove_existingProperty_removesAndReturnsElement() throws Exception {
    // Prepare a JsonElement to add and then remove
    JsonElement element = mock(JsonElement.class);
    String key = "testKey";

    // Use reflection to access private 'members' field and insert directly
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.put(key, element);

    // Remove the element
    JsonElement removed = jsonObject.remove(key);

    // Verify the removed element is the same
    assertSame(element, removed);
    // Verify the element is no longer in the map
    assertFalse(members.containsKey(key));
  }

  @Test
    @Timeout(8000)
  public void testRemove_nonExistingProperty_returnsNull() {
    String key = "nonExistingKey";

    // Remove a key that does not exist
    JsonElement removed = jsonObject.remove(key);

    // Should return null
    assertNull(removed);
  }

  @Test
    @Timeout(8000)
  public void testRemove_nullProperty_returnsNull() {
    // Removing null property should not throw, returns null
    JsonElement removed = jsonObject.remove(null);
    assertNull(removed);
  }
}
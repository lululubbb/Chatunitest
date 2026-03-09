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

public class JsonObject_417_3Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testRemove_existingProperty_returnsJsonElement() throws Exception {
    // Arrange
    JsonElement element = mock(JsonElement.class);
    // Use reflection to access private 'members' field and put a value
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members =
        (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.put("key", element);

    // Act
    JsonElement removed = jsonObject.remove("key");

    // Assert
    assertSame(element, removed);
    assertFalse(members.containsKey("key"));
  }

  @Test
    @Timeout(8000)
  public void testRemove_nonExistingProperty_returnsNull() {
    // Act
    JsonElement removed = jsonObject.remove("nonExistingKey");

    // Assert
    assertNull(removed);
  }
}
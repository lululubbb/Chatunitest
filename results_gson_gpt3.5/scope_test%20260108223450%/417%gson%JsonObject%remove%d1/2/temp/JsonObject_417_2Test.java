package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class JsonObjectRemoveTest {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() throws Exception {
    jsonObject = new JsonObject();

    // Use reflection to set the private final field members to a spy LinkedTreeMap
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    LinkedTreeMap<String, JsonElement> membersSpy = spy(new LinkedTreeMap<>(false));
    membersField.set(jsonObject, membersSpy);
  }

  @Test
    @Timeout(8000)
  public void remove_existingProperty_removesAndReturnsElement() {
    // Arrange
    JsonElement element = mock(JsonElement.class);
    jsonObject.add("key1", element);

    // Act
    JsonElement removed = jsonObject.remove("key1");

    // Assert
    assertSame(element, removed);
    assertFalse(jsonObject.has("key1"));
  }

  @Test
    @Timeout(8000)
  public void remove_nonExistingProperty_returnsNull() {
    // Act
    JsonElement removed = jsonObject.remove("nonexistent");

    // Assert
    assertNull(removed);
  }

  @Test
    @Timeout(8000)
  public void remove_nullProperty_returnsNull() {
    // Act
    JsonElement removed = jsonObject.remove(null);

    // Assert
    assertNull(removed);
  }
}
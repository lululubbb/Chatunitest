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

public class JsonObject_427_1Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGet_existingMember_returnsValue() throws Exception {
    // Arrange
    JsonElement expectedElement = mock(JsonElement.class);
    // Use reflection to access private field 'members'
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.put("key", expectedElement);

    // Act
    JsonElement actual = jsonObject.get("key");

    // Assert
    assertSame(expectedElement, actual);
  }

  @Test
    @Timeout(8000)
  public void testGet_nonExistingMember_returnsNull() {
    // Act
    JsonElement actual = jsonObject.get("missingKey");

    // Assert
    assertNull(actual);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKey_returnsNull() {
    // Act
    JsonElement actual = jsonObject.get(null);

    // Assert
    assertNull(actual);
  }
}
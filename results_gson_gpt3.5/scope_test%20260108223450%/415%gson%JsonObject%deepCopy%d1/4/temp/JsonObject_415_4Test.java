package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Field;
import java.util.Map.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonObjectDeepCopyTest {

  private JsonObject original;
  private JsonElement mockElement1;
  private JsonElement mockElement2;

  @BeforeEach
  void setUp() {
    original = new JsonObject();

    // Mock JsonElement instances with deepCopy behavior
    mockElement1 = mock(JsonElement.class);
    mockElement2 = mock(JsonElement.class);

    // Mock deepCopy return values for the elements
    JsonElement mockCopy1 = mock(JsonElement.class);
    JsonElement mockCopy2 = mock(JsonElement.class);
    when(mockElement1.deepCopy()).thenReturn(mockCopy1);
    when(mockElement2.deepCopy()).thenReturn(mockCopy2);

    // Add mocked elements to the original JsonObject
    original.add("key1", mockElement1);
    original.add("key2", mockElement2);
  }

  @Test
    @Timeout(8000)
  void deepCopy_returnsDistinctCopyWithDeepCopiedMembers() throws Exception {
    // Act
    JsonObject copy = original.deepCopy();

    // Assert that copy is not the same instance
    assertNotSame(original, copy);

    // Assert size equality
    assertEquals(original.size(), copy.size());

    // Assert keys equality
    assertEquals(original.keySet(), copy.keySet());

    // Assert that each member in copy is the deepCopy of original member
    for (String key : original.keySet()) {
      JsonElement originalElement = original.get(key);
      JsonElement copiedElement = copy.get(key);

      // Verify that deepCopy() was called on the original element
      verify(originalElement).deepCopy();

      // They should not be the same instance
      assertNotSame(originalElement, copiedElement);
    }
  }

  @Test
    @Timeout(8000)
  void deepCopy_emptyJsonObject_returnsEmptyJsonObject() {
    JsonObject empty = new JsonObject();
    JsonObject copy = empty.deepCopy();

    assertNotSame(empty, copy);
    assertTrue(copy.isEmpty());
    assertEquals(0, copy.size());
  }

  @Test
    @Timeout(8000)
  void deepCopy_privateMembersFieldIsUsedAndCopiedCorrectly() throws Exception {
    // Use reflection to get the private field 'members' from original and copy
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    JsonObject copy = original.deepCopy();

    Object originalMembers = membersField.get(original);
    Object copyMembers = membersField.get(copy);

    // They should not be the same LinkedTreeMap instance
    assertNotSame(originalMembers, copyMembers);

    // The copy's members should contain deep copied elements
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> copyMap = (LinkedTreeMap<String, JsonElement>) copyMembers;

    for (Entry<String, JsonElement> entry : copyMap.entrySet()) {
      String key = entry.getKey();
      JsonElement copyValue = entry.getValue();
      JsonElement originalValue = original.get(key);

      // The copyValue should not be the same instance as originalValue
      assertNotSame(originalValue, copyValue);
    }
  }
}
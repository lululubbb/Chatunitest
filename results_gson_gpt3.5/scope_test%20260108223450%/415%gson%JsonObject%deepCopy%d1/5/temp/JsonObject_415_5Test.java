package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonObject_415_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  void deepCopy_emptyObject_returnsEmptyCopy() {
    JsonObject copy = jsonObject.deepCopy();
    assertNotNull(copy);
    assertNotSame(jsonObject, copy);
    assertEquals(0, copy.size());
    assertTrue(copy.isEmpty());
  }

  @Test
    @Timeout(8000)
  void deepCopy_withMembers_deepCopiesAllMembers() throws Exception {
    // Prepare mock JsonElement with deepCopy behavior
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element1Copy = mock(JsonElement.class);
    when(element1.deepCopy()).thenReturn(element1Copy);

    JsonElement element2 = mock(JsonElement.class);
    JsonElement element2Copy = mock(JsonElement.class);
    when(element2.deepCopy()).thenReturn(element2Copy);

    // Add mocked elements to jsonObject's private members map via reflection
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.put("key1", element1);
    members.put("key2", element2);

    // Call deepCopy
    JsonObject copy = jsonObject.deepCopy();

    // Verify copy is distinct
    assertNotSame(jsonObject, copy);

    // Verify members copied
    Field copyMembersField = JsonObject.class.getDeclaredField("members");
    copyMembersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> copyMembers = (LinkedTreeMap<String, JsonElement>) copyMembersField.get(copy);

    assertEquals(2, copyMembers.size());
    assertTrue(copyMembers.containsKey("key1"));
    assertTrue(copyMembers.containsKey("key2"));

    // Verify that deepCopy was called on each original element
    verify(element1).deepCopy();
    verify(element2).deepCopy();

    // Verify values in copy are the deepCopy results
    assertSame(element1Copy, copyMembers.get("key1"));
    assertSame(element2Copy, copyMembers.get("key2"));
  }

  @Test
    @Timeout(8000)
  void deepCopy_reflectiveInvocation_producesCorrectCopy() throws Exception {
    // Add a real JsonPrimitive member
    jsonObject.addProperty("name", "value");

    // Use reflection to get deepCopy method
    Method deepCopyMethod = JsonObject.class.getDeclaredMethod("deepCopy");
    deepCopyMethod.setAccessible(true);

    JsonObject copy = (JsonObject) deepCopyMethod.invoke(jsonObject);

    assertNotNull(copy);
    assertEquals(1, copy.size());
    assertTrue(copy.has("name"));
    assertEquals("value", copy.getAsJsonPrimitive("name").getAsString());
  }
}
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

public class JsonObject_415_1Test {

  private JsonObject jsonObject;

  @BeforeEach
  void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  void deepCopy_emptyMembers_returnsEmptyJsonObject() {
    // members is empty by default
    JsonObject copy = jsonObject.deepCopy();
    assertNotNull(copy);
    assertNotSame(jsonObject, copy);
    assertEquals(0, copy.size());
    assertTrue(copy.isEmpty());
  }

  @Test
    @Timeout(8000)
  void deepCopy_withMembers_copiesAllEntries() throws Exception {
    // Prepare a mock JsonElement with deepCopy stub
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element1Copy = mock(JsonElement.class);
    when(element1.deepCopy()).thenReturn(element1Copy);

    JsonElement element2 = mock(JsonElement.class);
    JsonElement element2Copy = mock(JsonElement.class);
    when(element2.deepCopy()).thenReturn(element2Copy);

    // Use reflection to access private members field and insert test entries
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> membersMap = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    membersMap.put("key1", element1);
    membersMap.put("key2", element2);

    // Call deepCopy
    JsonObject copy = jsonObject.deepCopy();

    // Validate copy is not the same object
    assertNotNull(copy);
    assertNotSame(jsonObject, copy);

    // Validate keys and entries in copy
    Set<Map.Entry<String, JsonElement>> copyEntries = copy.entrySet();
    assertEquals(2, copyEntries.size());

    // Validate keys and that deepCopy was called on each element
    verify(element1, times(1)).deepCopy();
    verify(element2, times(1)).deepCopy();

    // Validate copied values are the returned deepCopy mocks
    for (Map.Entry<String, JsonElement> entry : copyEntries) {
      if ("key1".equals(entry.getKey())) {
        assertSame(element1Copy, entry.getValue());
      } else if ("key2".equals(entry.getKey())) {
        assertSame(element2Copy, entry.getValue());
      } else {
        fail("Unexpected key in copied JsonObject: " + entry.getKey());
      }
    }
  }

  @Test
    @Timeout(8000)
  void deepCopy_reflectiveInvocation_consistentResult() throws Exception {
    // Add a property to original object
    jsonObject.addProperty("prop", "value");

    // Use reflection to invoke deepCopy method
    Method deepCopyMethod = JsonObject.class.getDeclaredMethod("deepCopy");
    deepCopyMethod.setAccessible(true);
    Object copyObj = deepCopyMethod.invoke(jsonObject);

    assertNotNull(copyObj);
    assertTrue(copyObj instanceof JsonObject);
    JsonObject copy = (JsonObject) copyObj;

    // Validate copied content
    assertEquals(1, copy.size());
    assertTrue(copy.has("prop"));
    assertEquals(jsonObject.get("prop"), copy.get("prop"));
    assertNotSame(jsonObject, copy);
  }
}
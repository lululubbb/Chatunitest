package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_422_3Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testEntrySet_Empty() throws Exception {
    // Access private field 'members' via reflection and clear it to ensure empty
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.clear();

    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
    assertNotNull(entries);
    assertTrue(entries.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testEntrySet_WithEntries() throws Exception {
    // Add entries using public add method
    JsonPrimitive value1 = new JsonPrimitive("value1");
    JsonPrimitive value2 = new JsonPrimitive(123);
    jsonObject.add("key1", value1);
    jsonObject.add("key2", value2);

    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
    assertNotNull(entries);
    assertEquals(2, entries.size());

    boolean foundKey1 = false;
    boolean foundKey2 = false;

    for (Map.Entry<String, JsonElement> entry : entries) {
      if ("key1".equals(entry.getKey())) {
        foundKey1 = true;
        assertEquals(value1, entry.getValue());
      } else if ("key2".equals(entry.getKey())) {
        foundKey2 = true;
        assertEquals(value2, entry.getValue());
      }
    }

    assertTrue(foundKey1);
    assertTrue(foundKey2);
  }

  @Test
    @Timeout(8000)
  public void testEntrySet_ReflectMembersWithMock() throws Exception {
    // Create a mock LinkedTreeMap and set it into the private field 'members'
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> mockMembers = mock(LinkedTreeMap.class);

    Map.Entry<String, JsonElement> mockEntry = mock(Map.Entry.class);
    when(mockEntry.getKey()).thenReturn("mockKey");
    JsonPrimitive mockValue = new JsonPrimitive("mockValue");
    when(mockEntry.getValue()).thenReturn(mockValue);

    Set<Map.Entry<String, JsonElement>> mockEntrySet = Set.of(mockEntry);
    when(mockMembers.entrySet()).thenReturn(mockEntrySet);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    membersField.set(jsonObject, mockMembers);

    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();

    assertNotNull(entries);
    assertEquals(1, entries.size());
    Map.Entry<String, JsonElement> entry = entries.iterator().next();
    assertEquals("mockKey", entry.getKey());
    assertEquals(mockValue, entry.getValue());

    verify(mockMembers).entrySet();
  }
}
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

public class JsonObject_422_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testEntrySet_empty() {
    Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
    assertNotNull(entrySet);
    assertTrue(entrySet.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testEntrySet_withElements() throws Exception {
    // Prepare a LinkedTreeMap mock with entries
    LinkedTreeMap<String, JsonElement> mockMembers = mock(LinkedTreeMap.class);
    Map.Entry<String, JsonElement> mockEntry = mock(Map.Entry.class);
    when(mockEntry.getKey()).thenReturn("key1");
    JsonElement mockValue = mock(JsonElement.class);
    when(mockEntry.getValue()).thenReturn(mockValue);

    Set<Map.Entry<String, JsonElement>> mockEntrySet = Set.of(mockEntry);
    when(mockMembers.entrySet()).thenReturn(mockEntrySet);

    // Inject mockMembers into jsonObject's private final field 'members' via reflection
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    membersField.set(jsonObject, mockMembers);

    Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();

    assertNotNull(entrySet);
    assertEquals(1, entrySet.size());
    Map.Entry<String, JsonElement> entry = entrySet.iterator().next();
    assertEquals("key1", entry.getKey());
    assertEquals(mockValue, entry.getValue());

    verify(mockMembers).entrySet();
  }
}
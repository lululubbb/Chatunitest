package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class JsonObject_422_1Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testEntrySet_Empty() {
    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
    assertNotNull(entries);
    assertTrue(entries.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testEntrySet_WithEntries() throws Exception {
    // Use reflection to access private members field to inject mocked LinkedTreeMap
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> mockedMembers = mock(LinkedTreeMap.class);

    Map.Entry<String, JsonElement> mockedEntry = mock(Map.Entry.class);
    when(mockedEntry.getKey()).thenReturn("key1");
    when(mockedEntry.getValue()).thenReturn(new JsonPrimitive("value1"));

    Set<Map.Entry<String, JsonElement>> mockedEntrySet = Set.of(mockedEntry);
    when(mockedMembers.entrySet()).thenReturn(mockedEntrySet);

    membersField.set(jsonObject, mockedMembers);

    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();

    assertNotNull(entries);
    assertEquals(1, entries.size());
    Map.Entry<String, JsonElement> entry = entries.iterator().next();
    assertEquals("key1", entry.getKey());
    assertEquals("value1", ((JsonPrimitive) entry.getValue()).getAsString());

    verify(mockedMembers).entrySet();
  }
}
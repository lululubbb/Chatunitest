package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class JsonObject_422_2Test {

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
    // Prepare LinkedTreeMap mock to inject into JsonObject
    LinkedTreeMap<String, JsonElement> membersMock = spy(new LinkedTreeMap<>(false));
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);

    membersMock.put("key1", element1);
    membersMock.put("key2", element2);

    // Inject the mocked members map into the private field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    membersField.set(jsonObject, membersMock);

    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();

    assertNotNull(entries);
    assertEquals(2, entries.size());

    boolean foundKey1 = false;
    boolean foundKey2 = false;
    for (Map.Entry<String, JsonElement> entry : entries) {
      if ("key1".equals(entry.getKey())) {
        foundKey1 = true;
        assertSame(element1, entry.getValue());
      }
      if ("key2".equals(entry.getKey())) {
        foundKey2 = true;
        assertSame(element2, entry.getValue());
      }
    }
    assertTrue(foundKey1);
    assertTrue(foundKey2);
  }
}
package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_415_2Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void deepCopy_emptyObject_returnsEmptyCopy() throws Exception {
    JsonObject copy = jsonObject.deepCopy();
    assertNotNull(copy);
    assertNotSame(jsonObject, copy);
    assertEquals(0, copy.size());
    assertTrue(copy.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void deepCopy_withEntries_returnsDeepCopiedObject() throws Exception {
    // Prepare mock JsonElement with deepCopy stub
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element1Copy = mock(JsonElement.class);
    when(element1.deepCopy()).thenReturn(element1Copy);

    JsonElement element2 = mock(JsonElement.class);
    JsonElement element2Copy = mock(JsonElement.class);
    when(element2.deepCopy()).thenReturn(element2Copy);

    jsonObject.add("key1", element1);
    jsonObject.add("key2", element2);

    JsonObject copy = jsonObject.deepCopy();

    assertNotNull(copy);
    assertNotSame(jsonObject, copy);
    assertEquals(jsonObject.size(), copy.size());
    assertTrue(copy.has("key1"));
    assertTrue(copy.has("key2"));
    assertSame(element1Copy, copy.get("key1"));
    assertSame(element2Copy, copy.get("key2"));

    // Verify original elements deepCopy called exactly once
    verify(element1, times(1)).deepCopy();
    verify(element2, times(1)).deepCopy();
  }

  @Test
    @Timeout(8000)
  public void deepCopy_reflectiveInvocation_returnsDeepCopy() throws Exception {
    // Use reflection to invoke private field and verify internal state
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    assertNotNull(members);
    assertTrue(members.isEmpty());

    JsonElement element = mock(JsonElement.class);
    JsonElement elementCopy = mock(JsonElement.class);
    when(element.deepCopy()).thenReturn(elementCopy);

    jsonObject.add("reflectiveKey", element);

    Method deepCopyMethod = JsonObject.class.getDeclaredMethod("deepCopy");
    deepCopyMethod.setAccessible(true);

    JsonObject copy = (JsonObject) deepCopyMethod.invoke(jsonObject);

    assertNotNull(copy);
    assertNotSame(jsonObject, copy);
    assertEquals(1, copy.size());
    assertSame(elementCopy, copy.get("reflectiveKey"));

    verify(element, times(1)).deepCopy();
  }
}
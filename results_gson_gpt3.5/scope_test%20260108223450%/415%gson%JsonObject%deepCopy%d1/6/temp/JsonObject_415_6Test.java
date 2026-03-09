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

public class JsonObject_415_6Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void deepCopy_emptyObject_returnsEmptyCopy() {
    JsonObject copy = jsonObject.deepCopy();
    assertNotNull(copy);
    assertNotSame(jsonObject, copy);
    assertEquals(0, copy.size());
    assertTrue(copy.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void deepCopy_withMembers_copiesAllEntries() throws Exception {
    // Prepare mock JsonElement with deepCopy behavior
    JsonElement mockElement1 = mock(JsonElement.class);
    JsonElement mockElement2 = mock(JsonElement.class);
    JsonElement deepCopy1 = mock(JsonElement.class);
    JsonElement deepCopy2 = mock(JsonElement.class);

    when(mockElement1.deepCopy()).thenReturn(deepCopy1);
    when(mockElement2.deepCopy()).thenReturn(deepCopy2);

    // Use reflection to access private members field and add entries directly
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    members.put("key1", mockElement1);
    members.put("key2", mockElement2);

    JsonObject copy = jsonObject.deepCopy();

    // Verify that deepCopy was called on each member
    verify(mockElement1).deepCopy();
    verify(mockElement2).deepCopy();

    // The copy should not be the same instance
    assertNotSame(jsonObject, copy);
    assertEquals(2, copy.size());
    assertTrue(copy.has("key1"));
    assertTrue(copy.has("key2"));

    // The values in the copy should be the deep copies returned by mocks
    assertSame(deepCopy1, copy.get("key1"));
    assertSame(deepCopy2, copy.get("key2"));
  }

  @Test
    @Timeout(8000)
  public void deepCopy_reflectionInvocation_matchesDirectInvocation() throws Exception {
    // Add one property normally
    jsonObject.addProperty("prop", "value");

    // Obtain deepCopy method by reflection
    Method deepCopyMethod = JsonObject.class.getDeclaredMethod("deepCopy");
    deepCopyMethod.setAccessible(true);

    JsonObject copy = (JsonObject) deepCopyMethod.invoke(jsonObject);

    assertNotNull(copy);
    assertNotSame(jsonObject, copy);
    assertEquals(1, copy.size());
    assertTrue(copy.has("prop"));
    assertEquals(jsonObject.get("prop"), copy.get("prop"));
  }
}
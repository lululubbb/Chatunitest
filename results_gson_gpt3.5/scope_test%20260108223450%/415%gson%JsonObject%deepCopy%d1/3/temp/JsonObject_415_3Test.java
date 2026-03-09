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

public class JsonObject_415_3Test {

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
  public void deepCopy_withMembers_returnsDeepCopiedObject() throws Exception {
    // Prepare mock JsonElement with deepCopy behavior
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element1Copy = mock(JsonElement.class);
    when(element1.deepCopy()).thenReturn(element1Copy);

    JsonElement element2 = mock(JsonElement.class);
    JsonElement element2Copy = mock(JsonElement.class);
    when(element2.deepCopy()).thenReturn(element2Copy);

    // Use reflection to access private members field and put entries
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.put("key1", element1);
    members.put("key2", element2);

    JsonObject copy = jsonObject.deepCopy();

    // Verify deepCopy called on each element
    verify(element1).deepCopy();
    verify(element2).deepCopy();

    // The copy should have same keys and deep copied values
    assertEquals(2, copy.size());
    assertTrue(copy.has("key1"));
    assertTrue(copy.has("key2"));
    assertSame(element1Copy, copy.get("key1"));
    assertSame(element2Copy, copy.get("key2"));

    // The original and copy members map are different instances
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> copyMembers = (LinkedTreeMap<String, JsonElement>) membersField.get(copy);
    assertNotSame(members, copyMembers);
  }

  @Test
    @Timeout(8000)
  public void deepCopy_nestedJsonObject_deepCopiesRecursively() {
    JsonObject nested = new JsonObject();
    JsonPrimitive primitive = new JsonPrimitive("value");
    nested.add("nestedKey", primitive);

    jsonObject.add("outerKey", nested);

    JsonObject copy = jsonObject.deepCopy();

    assertNotSame(jsonObject, copy);
    assertTrue(copy.has("outerKey"));

    JsonElement copiedNested = copy.get("outerKey");
    assertNotSame(nested, copiedNested);
    assertTrue(copiedNested.isJsonObject());

    JsonObject copiedNestedObj = copiedNested.getAsJsonObject();
    assertTrue(copiedNestedObj.has("nestedKey"));
    JsonPrimitive copiedPrimitive = copiedNestedObj.getAsJsonPrimitive("nestedKey");
    assertEquals(primitive.getAsString(), copiedPrimitive.getAsString());
  }
}
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
import org.mockito.ArgumentCaptor;

public class JsonObject_420_1Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = spy(new JsonObject());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNonNullBoolean_true() throws Exception {
    jsonObject.addProperty("key", Boolean.TRUE);

    ArgumentCaptor<String> propCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<JsonElement> elemCaptor = ArgumentCaptor.forClass(JsonElement.class);
    verify(jsonObject).add(propCaptor.capture(), elemCaptor.capture());

    assertEquals("key", propCaptor.getValue());
    JsonElement value = elemCaptor.getValue();
    assertTrue(value instanceof JsonPrimitive);
    assertEquals(Boolean.TRUE, ((JsonPrimitive) value).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNonNullBoolean_false() throws Exception {
    jsonObject.addProperty("key", Boolean.FALSE);

    ArgumentCaptor<String> propCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<JsonElement> elemCaptor = ArgumentCaptor.forClass(JsonElement.class);
    verify(jsonObject).add(propCaptor.capture(), elemCaptor.capture());

    assertEquals("key", propCaptor.getValue());
    JsonElement value = elemCaptor.getValue();
    assertTrue(value instanceof JsonPrimitive);
    assertEquals(Boolean.FALSE, ((JsonPrimitive) value).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNullBoolean() throws Exception {
    jsonObject.addProperty("key", (Boolean) null);

    ArgumentCaptor<String> propCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<JsonElement> elemCaptor = ArgumentCaptor.forClass(JsonElement.class);
    verify(jsonObject).add(propCaptor.capture(), elemCaptor.capture());

    assertEquals("key", propCaptor.getValue());
    JsonElement value = elemCaptor.getValue();
    assertSame(JsonNull.INSTANCE, value);
  }

  @Test
    @Timeout(8000)
  public void testAdd_privateMethodAddIsCalled() throws Exception {
    // Use reflection to get private field members and verify it changes after addProperty call
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    LinkedTreeMap<String, JsonElement> membersBefore = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    int sizeBefore = membersBefore.size();

    jsonObject.addProperty("key", Boolean.TRUE);

    LinkedTreeMap<String, JsonElement> membersAfter = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    assertEquals(sizeBefore + 1, membersAfter.size());
    assertTrue(membersAfter.containsKey("key"));
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_invokesAddUsingReflection() throws Exception {
    Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
    addMethod.setAccessible(true);

    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    addMethod.invoke(jsonObject, "refKey", jsonPrimitive);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey("refKey"));
    assertEquals(jsonPrimitive, members.get("refKey"));
  }
}
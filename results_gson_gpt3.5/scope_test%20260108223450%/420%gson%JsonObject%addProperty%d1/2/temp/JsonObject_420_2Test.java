package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class JsonObject_420_2Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = spy(new JsonObject());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withBooleanTrue() throws Exception {
    jsonObject.addProperty("keyTrue", true);

    ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);
    verify(jsonObject).add(propertyCaptor.capture(), elementCaptor.capture());

    assertEquals("keyTrue", propertyCaptor.getValue());
    JsonElement element = elementCaptor.getValue();
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(true, ((JsonPrimitive) element).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withBooleanFalse() throws Exception {
    jsonObject.addProperty("keyFalse", false);

    ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);
    verify(jsonObject).add(propertyCaptor.capture(), elementCaptor.capture());

    assertEquals("keyFalse", propertyCaptor.getValue());
    JsonElement element = elementCaptor.getValue();
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(false, ((JsonPrimitive) element).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withBooleanNull() throws Exception {
    jsonObject.addProperty("keyNull", (Boolean) null);

    ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);
    verify(jsonObject).add(propertyCaptor.capture(), elementCaptor.capture());

    assertEquals("keyNull", propertyCaptor.getValue());
    JsonElement element = elementCaptor.getValue();
    assertSame(JsonNull.INSTANCE, element);
  }

  @Test
    @Timeout(8000)
  public void testAddMethodAddsToMembersMap() throws Exception {
    // Use reflection to get the private members field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    JsonPrimitive primitive = new JsonPrimitive(true);
    // Call private add method via reflection
    Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
    addMethod.setAccessible(true);
    addMethod.invoke(jsonObject, "reflectedKey", primitive);

    assertTrue(members.containsKey("reflectedKey"));
    assertEquals(primitive, members.get("reflectedKey"));
  }
}
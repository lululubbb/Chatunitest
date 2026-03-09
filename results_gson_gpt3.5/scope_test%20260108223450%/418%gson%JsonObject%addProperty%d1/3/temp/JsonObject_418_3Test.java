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

public class JsonObject_418_3Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = spy(new JsonObject());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNonNullValue() throws Exception {
    String property = "key";
    String value = "value";

    jsonObject.addProperty(property, value);

    ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);
    verify(jsonObject).add(propertyCaptor.capture(), elementCaptor.capture());

    assertEquals(property, propertyCaptor.getValue());
    JsonElement element = elementCaptor.getValue();
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(value, ((JsonPrimitive) element).getAsString());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNullValue() throws Exception {
    String property = "key";

    jsonObject.addProperty(property, (String) null);

    ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);
    verify(jsonObject).add(propertyCaptor.capture(), elementCaptor.capture());

    assertEquals(property, propertyCaptor.getValue());
    JsonElement element = elementCaptor.getValue();
    assertSame(JsonNull.INSTANCE, element);
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_addsToMembersMap() throws Exception {
    String property = "test";
    String value = "val";

    jsonObject.addProperty(property, value);

    // Use reflection to access private members field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey(property));
    JsonElement element = members.get(property);
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(value, ((JsonPrimitive) element).getAsString());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_callsPrivateAddMethod() throws Exception {
    String property = "prop";
    String value = "val";

    jsonObject.addProperty(property, value);

    // Use reflection to get add method
    Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
    addMethod.setAccessible(true);

    // Since add is public per signature, but we verify it was called via spy
    verify(jsonObject).add(eq(property), any(JsonElement.class));
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withEmptyString() throws Exception {
    String property = "empty";
    String value = "";

    jsonObject.addProperty(property, value);

    ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);
    verify(jsonObject).add(eq(property), elementCaptor.capture());

    JsonElement element = elementCaptor.getValue();
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(value, ((JsonPrimitive) element).getAsString());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withSpecialCharacters() throws Exception {
    String property = "special";
    String value = "!@#$%^&*()_+";

    jsonObject.addProperty(property, value);

    ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);
    verify(jsonObject).add(eq(property), elementCaptor.capture());

    JsonElement element = elementCaptor.getValue();
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(value, ((JsonPrimitive) element).getAsString());
  }
}
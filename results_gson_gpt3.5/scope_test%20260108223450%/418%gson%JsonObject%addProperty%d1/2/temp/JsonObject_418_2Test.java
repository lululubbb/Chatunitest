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

public class JsonObject_418_2Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_WithNonNullValue_AddsJsonPrimitive() throws Exception {
    String property = "key";
    String value = "value";

    // Spy on jsonObject to verify add call
    JsonObject spyJsonObject = spy(jsonObject);

    // Call focal method
    spyJsonObject.addProperty(property, value);

    // Verify add called with JsonPrimitive wrapping the value
    verify(spyJsonObject).add(eq(property), argThat(argument -> {
      if (!(argument instanceof JsonPrimitive)) return false;
      JsonPrimitive primitive = (JsonPrimitive) argument;
      // Use reflection to get the primitive value (since JsonPrimitive is final and no getter shown)
      try {
        Field valueField = JsonPrimitive.class.getDeclaredField("value");
        valueField.setAccessible(true);
        Object val = valueField.get(primitive);
        return value.equals(val);
      } catch (Exception e) {
        return false;
      }
    }));
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_WithNullValue_AddsJsonNullInstance() throws Exception {
    String property = "nullKey";
    String value = null;

    JsonObject spyJsonObject = spy(jsonObject);

    spyJsonObject.addProperty(property, value);

    verify(spyJsonObject).add(eq(property), eq(JsonNull.INSTANCE));
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_ActuallyAddsToMembers() throws Exception {
    String property = "testKey";
    String value = "testValue";

    jsonObject.addProperty(property, value);

    // Use reflection to access private members field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey(property));
    JsonElement element = members.get(property);
    assertTrue(element instanceof JsonPrimitive);

    // Check that the JsonPrimitive holds the correct value
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object primitiveValue = valueField.get(element);
    assertEquals(value, primitiveValue);
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_WithEmptyString_AddsJsonPrimitive() throws Exception {
    String property = "empty";
    String value = "";

    jsonObject.addProperty(property, value);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey(property));
    JsonElement element = members.get(property);
    assertTrue(element instanceof JsonPrimitive);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object primitiveValue = valueField.get(element);
    assertEquals(value, primitiveValue);
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_WithSpecialCharacters_AddsJsonPrimitive() throws Exception {
    String property = "special";
    String value = "äöüß@€";

    jsonObject.addProperty(property, value);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey(property));
    JsonElement element = members.get(property);
    assertTrue(element instanceof JsonPrimitive);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object primitiveValue = valueField.get(element);
    assertEquals(value, primitiveValue);
  }
}
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

public class JsonObject_419_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNonNullNumber_addsJsonPrimitive() throws Exception {
    // Arrange
    String property = "age";
    Number value = 42;

    // Spy on jsonObject to verify add call
    JsonObject spyJsonObject = spy(jsonObject);

    // Act
    spyJsonObject.addProperty(property, value);

    // Assert
    // Verify add was called with property and JsonPrimitive wrapping the number
    verify(spyJsonObject).add(eq(property), argThat(jsonElement -> {
      if (!(jsonElement instanceof JsonPrimitive)) return false;
      JsonPrimitive primitive = (JsonPrimitive) jsonElement;
      try {
        Method getAsNumber = JsonPrimitive.class.getDeclaredMethod("getAsNumber");
        getAsNumber.setAccessible(true);
        Number n = (Number) getAsNumber.invoke(primitive);
        return value.equals(n);
      } catch (Exception e) {
        return false;
      }
    }));

    // Also verify internal members map contains the property with correct JsonPrimitive
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(spyJsonObject);
    assertTrue(members.containsKey(property));
    JsonElement element = members.get(property);
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    Method getAsNumber = JsonPrimitive.class.getDeclaredMethod("getAsNumber");
    getAsNumber.setAccessible(true);
    Number storedNumber = (Number) getAsNumber.invoke(primitive);
    assertEquals(value, storedNumber);
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNullNumber_addsJsonNull() throws Exception {
    // Arrange
    String property = "height";
    Number value = null;

    // Spy on jsonObject to verify add call
    JsonObject spyJsonObject = spy(jsonObject);

    // Act
    spyJsonObject.addProperty(property, value);

    // Assert
    // Verify add was called with property and JsonNull.INSTANCE
    verify(spyJsonObject).add(eq(property), eq(JsonNull.INSTANCE));

    // Also verify internal members map contains the property with JsonNull.INSTANCE
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(spyJsonObject);
    assertTrue(members.containsKey(property));
    JsonElement element = members.get(property);
    assertSame(JsonNull.INSTANCE, element);
  }
}
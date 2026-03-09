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

public class JsonObject_421_1Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNonNullCharacter_addsJsonPrimitive() throws Exception {
    char value = 'a';
    String property = "charProp";

    // Call the focal method
    jsonObject.addProperty(property, value);

    // Use reflection to access private field 'members'
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey(property));
    JsonElement element = members.get(property);
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(new JsonPrimitive(value), element);
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNullCharacter_addsJsonNull() throws Exception {
    Character value = null;
    String property = "nullCharProp";

    jsonObject.addProperty(property, value);

    // Use reflection to access private field 'members'
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey(property));
    JsonElement element = members.get(property);
    assertSame(JsonNull.INSTANCE, element);
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_invokesAddMethodProperly() throws Exception {
    JsonObject spyJsonObject = spy(new JsonObject());
    String property = "prop";
    Character value = 'z';

    // We want to verify add() is called with expected parameters
    doNothing().when(spyJsonObject).add(anyString(), any(JsonElement.class));

    spyJsonObject.addProperty(property, value);

    ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);

    verify(spyJsonObject).add(propertyCaptor.capture(), elementCaptor.capture());

    assertEquals(property, propertyCaptor.getValue());
    JsonElement capturedElement = elementCaptor.getValue();
    assertTrue(capturedElement instanceof JsonPrimitive);
    assertEquals(new JsonPrimitive(value), capturedElement);
  }
}
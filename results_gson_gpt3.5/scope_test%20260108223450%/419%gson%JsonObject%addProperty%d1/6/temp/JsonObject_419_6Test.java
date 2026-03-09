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

public class JsonObject_419_6Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNonNullNumber_addsJsonPrimitive() throws Exception {
    String property = "age";
    Number value = 42;

    jsonObject.addProperty(property, value);

    // Use reflection to get the private members field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey(property));
    JsonElement element = members.get(property);
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(value, ((JsonPrimitive) element).getAsNumber());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNullNumber_addsJsonNull() throws Exception {
    String property = "score";
    Number value = null;

    jsonObject.addProperty(property, value);

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
  public void testAddProperty_invokesAddMethod() throws Exception {
    // Spy on JsonObject to verify add method call
    JsonObject spyJsonObject = spy(new JsonObject());

    String property = "count";
    Number value = 7;

    spyJsonObject.addProperty(property, value);

    // Verify add called with correct arguments
    verify(spyJsonObject).add(eq(property), argThat(arg -> arg instanceof JsonPrimitive && ((JsonPrimitive) arg).getAsNumber().equals(value)));
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withZeroNumber() throws Exception {
    String property = "zero";
    Number value = 0;

    jsonObject.addProperty(property, value);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey(property));
    JsonElement element = members.get(property);
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(value, ((JsonPrimitive) element).getAsNumber());
  }
}
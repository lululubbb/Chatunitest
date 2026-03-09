package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class JsonObject_419_3Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = spy(new JsonObject());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNonNullNumber_addsJsonPrimitive() {
    Number number = 123;

    jsonObject.addProperty("key", number);

    ArgumentCaptor<JsonElement> captor = ArgumentCaptor.forClass(JsonElement.class);
    verify(jsonObject).add(eq("key"), captor.capture());

    JsonElement addedElement = captor.getValue();
    assertNotNull(addedElement);
    assertTrue(addedElement instanceof JsonPrimitive);
    assertEquals(number, ((JsonPrimitive) addedElement).getAsNumber());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNullNumber_addsJsonNullInstance() {
    jsonObject.addProperty("key", (Number) null);

    ArgumentCaptor<JsonElement> captor = ArgumentCaptor.forClass(JsonElement.class);
    verify(jsonObject).add(eq("key"), captor.capture());

    JsonElement addedElement = captor.getValue();
    assertNotNull(addedElement);
    assertSame(JsonNull.INSTANCE, addedElement);
  }

  @Test
    @Timeout(8000)
  public void testAdd_invokedByAddProperty_addsToMembersMap() throws Exception {
    // Use real JsonObject instance instead of spy to test add method effect
    JsonObject realJsonObject = new JsonObject();

    // Use reflection to access private members field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(realJsonObject);

    // Invoke addProperty to add a property
    realJsonObject.addProperty("key", 42);

    // Confirm that members map contains the key and correct JsonElement
    assertTrue(members.containsKey("key"));
    JsonElement element = members.get("key");
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(42, ((JsonPrimitive) element).getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testPrivateAddMethod_invocationUsingReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
    addMethod.setAccessible(true);

    JsonPrimitive primitive = new JsonPrimitive(99);
    addMethod.invoke(jsonObject, "reflectKey", primitive);

    // Verify that addProperty returns expected value by checking members map via reflection
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey("reflectKey"));
    assertEquals(primitive, members.get("reflectKey"));
  }
}
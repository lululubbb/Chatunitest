package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_419_4Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = spy(new JsonObject());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNonNullNumber_addsCorrectJsonPrimitive() {
    Number number = 123;
    doNothing().when(jsonObject).add(eq("age"), any(JsonElement.class));

    jsonObject.addProperty("age", number);

    verify(jsonObject).add(eq("age"), argThat(element -> {
      if (!(element instanceof JsonPrimitive)) return false;
      JsonPrimitive primitive = (JsonPrimitive) element;
      return primitive.getAsNumber().equals(number);
    }));
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNullNumber_addsJsonNullInstance() throws Exception {
    // Use reflection to call addProperty(String, Number) to avoid ambiguity
    Method addPropertyNumberMethod = JsonObject.class.getDeclaredMethod("addProperty", String.class, Number.class);
    addPropertyNumberMethod.setAccessible(true);

    doNothing().when(jsonObject).add(eq("key"), any(JsonElement.class));
    addPropertyNumberMethod.invoke(jsonObject, "key", (Number) null);

    verify(jsonObject).add(eq("key"), eq(JsonNull.INSTANCE));
  }

  @Test
    @Timeout(8000)
  public void testAddMethod_addsElementToMembersMap() throws Exception {
    // Use reflection to invoke private add method
    Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
    addMethod.setAccessible(true);

    JsonPrimitive primitive = new JsonPrimitive(42);
    addMethod.invoke(jsonObject, "answer", primitive);

    JsonElement retrieved = jsonObject.get("answer");
    assertEquals(primitive, retrieved);
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_invokesAddMethodCorrectly() throws Exception {
    // Use reflection to invoke private add method to verify integration indirectly
    jsonObject.addProperty("pi", 3.14);

    JsonElement element = jsonObject.get("pi");
    assertNotNull(element);
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(3.14, ((JsonPrimitive) element).getAsNumber().doubleValue());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withDifferentNumberTypes() {
    jsonObject.addProperty("int", 10);
    jsonObject.addProperty("long", 10L);
    jsonObject.addProperty("float", 1.5f);
    jsonObject.addProperty("double", 2.5d);
    jsonObject.addProperty("byte", (byte) 1);
    jsonObject.addProperty("short", (short) 2);

    assertEquals(10, jsonObject.getAsJsonPrimitive("int").getAsInt());
    assertEquals(10L, jsonObject.getAsJsonPrimitive("long").getAsLong());
    assertEquals(1.5f, jsonObject.getAsJsonPrimitive("float").getAsFloat());
    assertEquals(2.5d, jsonObject.getAsJsonPrimitive("double").getAsDouble());
    assertEquals(1, jsonObject.getAsJsonPrimitive("byte").getAsByte());
    assertEquals(2, jsonObject.getAsJsonPrimitive("short").getAsShort());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNullPropertyName() {
    assertThrows(NullPointerException.class, () -> jsonObject.addProperty(null, 1));
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNullPropertyNameAndNullValue() {
    assertThrows(InvocationTargetException.class, () -> {
      Method addPropertyNumberMethod = JsonObject.class.getDeclaredMethod("addProperty", String.class, Number.class);
      addPropertyNumberMethod.setAccessible(true);
      addPropertyNumberMethod.invoke(jsonObject, (String) null, (Number) null);
    }, "Expected InvocationTargetException due to null key");

    // Now verify that the cause is NullPointerException
    try {
      Method addPropertyNumberMethod = JsonObject.class.getDeclaredMethod("addProperty", String.class, Number.class);
      addPropertyNumberMethod.setAccessible(true);
      addPropertyNumberMethod.invoke(jsonObject, (String) null, (Number) null);
      fail("Expected InvocationTargetException not thrown");
    } catch (InvocationTargetException e) {
      assertTrue(e.getCause() instanceof NullPointerException, "Cause should be NullPointerException");
    } catch (Throwable t) {
      fail("Unexpected exception type: " + t);
    }
  }
}
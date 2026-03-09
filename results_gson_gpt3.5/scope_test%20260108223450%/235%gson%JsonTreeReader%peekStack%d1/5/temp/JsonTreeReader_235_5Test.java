package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeReader_235_5Test {

  private JsonTreeReader jsonTreeReader;
  private Method peekStackMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, IllegalAccessException, NoSuchFieldException {
    // Create a JsonArray element to construct JsonTreeReader (just to satisfy constructor)
    JsonArray jsonArray = new JsonArray();
    jsonTreeReader = new JsonTreeReader(jsonArray);

    // Access private peekStack method
    peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);

    // Ensure stackSize is 0 and stack is empty before each test
    setFieldValue(jsonTreeReader, "stackSize", 0);
    Object[] stackArray = (Object[]) getFieldValue(jsonTreeReader, "stack");
    for (int i = 0; i < stackArray.length; i++) {
      stackArray[i] = null;
    }
  }

  @Test
    @Timeout(8000)
  public void testPeekStack_withEmptyStack_shouldThrowArrayIndexOutOfBoundsException() {
    // stackSize is 0, so peekStack should throw ArrayIndexOutOfBoundsException
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
      try {
        peekStackMethod.invoke(jsonTreeReader);
      } catch (InvocationTargetException e) {
        // Rethrow the underlying exception to be caught by assertThrows
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  public void testPeekStack_withOneElement_shouldReturnThatElement() throws IllegalAccessException, InvocationTargetException, NoSuchFieldException {
    // Use reflection to set stackSize and stack manually
    Object[] stackArray = (Object[]) getFieldValue(jsonTreeReader, "stack");
    stackArray[0] = "testElement";
    setFieldValue(jsonTreeReader, "stackSize", 1);

    Object result = peekStackMethod.invoke(jsonTreeReader);
    assertEquals("testElement", result);
  }

  @Test
    @Timeout(8000)
  public void testPeekStack_withMultipleElements_shouldReturnTopElement() throws IllegalAccessException, InvocationTargetException, NoSuchFieldException {
    Object[] stackArray = (Object[]) getFieldValue(jsonTreeReader, "stack");
    stackArray[0] = "bottom";
    stackArray[1] = 123;
    stackArray[2] = new JsonPrimitive("top");
    setFieldValue(jsonTreeReader, "stackSize", 3);

    Object result = peekStackMethod.invoke(jsonTreeReader);
    assertTrue(result instanceof JsonPrimitive);
    assertEquals("top", ((JsonPrimitive) result).getAsString());
  }

  // Helper methods to access private fields

  private Object getFieldValue(Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
    java.lang.reflect.Field field = JsonTreeReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }

  private void setFieldValue(Object instance, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
    java.lang.reflect.Field field = JsonTreeReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }
}
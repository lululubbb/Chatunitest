package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JsonTreeReader_235_4Test {

  private JsonTreeReader jsonTreeReader;
  private Method peekStackMethod;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonElement mock to pass to constructor (could be null since not used here)
    jsonTreeReader = new JsonTreeReader(null);

    // Access private method peekStack via reflection
    peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testPeekStack_emptyStack_shouldThrowArrayIndexOutOfBoundsException() throws Exception {
    // Ensure stackSize is 0 and stack is empty
    setStackSize(0);

    assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
      try {
        peekStackMethod.invoke(jsonTreeReader);
      } catch (java.lang.reflect.InvocationTargetException e) {
        // Rethrow the underlying exception to be caught by assertThrows
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  public void testPeekStack_singleElement_shouldReturnElement() throws Exception {
    // Push an element on the stack using reflection to set private fields
    Object element = new Object();
    setStackElement(0, element);
    setStackSize(1);

    Object result = peekStackMethod.invoke(jsonTreeReader);
    assertSame(element, result);
  }

  @Test
    @Timeout(8000)
  public void testPeekStack_multipleElements_shouldReturnTopElement() throws Exception {
    Object first = new Object();
    Object second = new Object();
    Object third = new Object();

    setStackElement(0, first);
    setStackElement(1, second);
    setStackElement(2, third);
    setStackSize(3);

    Object result = peekStackMethod.invoke(jsonTreeReader);
    assertSame(third, result);
  }

  private void setStackElement(int index, Object value) throws Exception {
    var stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[index] = value;
  }

  private void setStackSize(int size) throws Exception {
    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, size);
  }
}
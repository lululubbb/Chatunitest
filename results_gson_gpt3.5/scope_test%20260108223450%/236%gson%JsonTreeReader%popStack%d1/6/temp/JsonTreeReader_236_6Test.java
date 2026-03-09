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

public class JsonTreeReader_236_6Test {

  private JsonTreeReader jsonTreeReader;
  private Method popStackMethod;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a dummy JsonElement to construct JsonTreeReader
    // Since JsonTreeReader constructor requires a JsonElement, we can use JsonNull.INSTANCE
    jsonTreeReader = new JsonTreeReader(com.google.gson.JsonNull.INSTANCE);

    // Access private method popStack via reflection
    popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void popStack_emptyStackSizeZero_shouldThrowArrayIndexOutOfBounds() throws Exception {
    // stackSize is initially 0, calling popStack should cause ArrayIndexOutOfBoundsException
    // because it does --stackSize (becomes -1) and accesses stack[-1]
    // We need to ensure stackSize is 0 and stack array is empty or default
    setField(jsonTreeReader, "stackSize", 0);
    setField(jsonTreeReader, "stack", new Object[32]);

    Exception exception = assertThrows(Exception.class, () -> {
      try {
        popStackMethod.invoke(jsonTreeReader);
      } catch (java.lang.reflect.InvocationTargetException e) {
        // unwrap the cause to check if it's ArrayIndexOutOfBoundsException
        throw e.getCause();
      }
    });
    assertTrue(exception instanceof ArrayIndexOutOfBoundsException);
  }

  @Test
    @Timeout(8000)
  public void popStack_stackSizeOne_shouldReturnTopAndNullifyStackSlot() throws Exception {
    // Prepare stack with one element and stackSize = 1
    Object element = new Object();
    setField(jsonTreeReader, "stackSize", 1);
    Object[] stack = new Object[32];
    stack[0] = element;
    setField(jsonTreeReader, "stack", stack);

    // Invoke popStack
    Object result = popStackMethod.invoke(jsonTreeReader);

    // Assert returned object is the element
    assertSame(element, result);

    // Assert stackSize is decreased to 0
    int stackSize = (int) getField(jsonTreeReader, "stackSize");
    assertEquals(0, stackSize);

    // Assert stack[0] is null
    Object[] updatedStack = (Object[]) getField(jsonTreeReader, "stack");
    assertNull(updatedStack[0]);
  }

  @Test
    @Timeout(8000)
  public void popStack_stackSizeMultiple_shouldReturnTopAndNullifyStackSlot() throws Exception {
    // Prepare stack with multiple elements and stackSize = 3
    Object element0 = new Object();
    Object element1 = new Object();
    Object element2 = new Object();
    setField(jsonTreeReader, "stackSize", 3);
    Object[] stack = new Object[32];
    stack[0] = element0;
    stack[1] = element1;
    stack[2] = element2;
    setField(jsonTreeReader, "stack", stack);

    // Invoke popStack
    Object result = popStackMethod.invoke(jsonTreeReader);

    // Assert returned object is the last element
    assertSame(element2, result);

    // Assert stackSize is decreased to 2
    int stackSize = (int) getField(jsonTreeReader, "stackSize");
    assertEquals(2, stackSize);

    // Assert stack[2] is null
    Object[] updatedStack = (Object[]) getField(jsonTreeReader, "stack");
    assertNull(updatedStack[2]);

    // Assert other elements remain unchanged
    assertSame(element0, updatedStack[0]);
    assertSame(element1, updatedStack[1]);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    java.lang.reflect.Field field = JsonTreeReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private Object getField(Object target, String fieldName) throws Exception {
    java.lang.reflect.Field field = JsonTreeReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }
}
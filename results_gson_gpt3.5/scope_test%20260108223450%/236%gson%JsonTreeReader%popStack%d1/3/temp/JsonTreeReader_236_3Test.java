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

public class JsonTreeReader_236_3Test {

  private JsonTreeReader jsonTreeReader;
  private Method popStackMethod;

  @BeforeEach
  public void setUp() throws Exception {
    // Create an instance with a dummy JsonElement (mock or null is fine since we won't use it)
    jsonTreeReader = new JsonTreeReader(null);

    // Access the private popStack method via reflection
    popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testPopStack_whenStackIsEmpty_thenThrowsArrayIndexOutOfBoundsException() throws Exception {
    // ensure stackSize is 0 and stack is empty
    setStackSize(0);
    setStack(new Object[32]);

    // When invoking a method reflectively, exceptions are wrapped in InvocationTargetException,
    // so unwrap it to check the cause.
    Throwable thrown = assertThrows(Throwable.class, () -> popStackMethod.invoke(jsonTreeReader));
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof ArrayIndexOutOfBoundsException, "Expected ArrayIndexOutOfBoundsException but got " + cause);
  }

  @Test
    @Timeout(8000)
  public void testPopStack_whenStackHasOneElement_thenReturnsElementAndClearsSlot() throws Exception {
    // Use reflection to set stackSize and stack array
    setStackSize(1);
    Object[] stack = new Object[32];
    Object element = new Object();
    stack[0] = element;
    setStack(stack);

    Object result = popStackMethod.invoke(jsonTreeReader);

    assertSame(element, result);
    assertEquals(0, getStackSize());
    Object[] stackAfter = getStack();
    assertNull(stackAfter[0]);
  }

  @Test
    @Timeout(8000)
  public void testPopStack_whenStackHasMultipleElements_thenReturnsTopElementAndAdjustsStack() throws Exception {
    setStackSize(3);
    Object[] stack = new Object[32];
    Object element0 = new Object();
    Object element1 = new Object();
    Object element2 = new Object();
    stack[0] = element0;
    stack[1] = element1;
    stack[2] = element2;
    setStack(stack);

    Object result = popStackMethod.invoke(jsonTreeReader);

    assertSame(element2, result);
    assertEquals(2, getStackSize());
    Object[] stackAfter = getStack();
    assertNull(stackAfter[2]);
    assertSame(element0, stackAfter[0]);
    assertSame(element1, stackAfter[1]);
  }

  // Helper methods to access private fields via reflection

  private void setStackSize(int size) throws Exception {
    var field = JsonTreeReader.class.getDeclaredField("stackSize");
    field.setAccessible(true);
    field.setInt(jsonTreeReader, size);
  }

  private int getStackSize() throws Exception {
    var field = JsonTreeReader.class.getDeclaredField("stackSize");
    field.setAccessible(true);
    return field.getInt(jsonTreeReader);
  }

  private void setStack(Object[] stack) throws Exception {
    var field = JsonTreeReader.class.getDeclaredField("stack");
    field.setAccessible(true);
    field.set(jsonTreeReader, stack);
  }

  private Object[] getStack() throws Exception {
    var field = JsonTreeReader.class.getDeclaredField("stack");
    field.setAccessible(true);
    return (Object[]) field.get(jsonTreeReader);
  }
}
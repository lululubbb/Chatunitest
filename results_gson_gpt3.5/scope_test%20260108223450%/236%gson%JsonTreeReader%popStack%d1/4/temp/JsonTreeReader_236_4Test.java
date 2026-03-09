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

public class JsonTreeReader_236_4Test {

  private JsonTreeReader jsonTreeReader;
  private Method popStackMethod;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonElement mock to pass to the constructor since it requires one
    // We can use a JsonNull instance as a simple JsonElement
    jsonTreeReader = new JsonTreeReader(com.google.gson.JsonNull.INSTANCE);

    // Access private popStack method via reflection
    popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testPopStack_whenStackHasOneElement() throws Exception {
    // Use reflection to push an element onto the stack by directly manipulating fields
    // stackSize = 1; stack[0] = "testElement"
    setStackField(new Object[]{"testElement"});
    setStackSizeField(1);

    Object popped = popStackMethod.invoke(jsonTreeReader);

    assertEquals("testElement", popped);

    // stackSize should be decremented to 0
    int stackSize = getStackSizeField();
    assertEquals(0, stackSize);

    // stack[0] should be null after pop
    Object[] stack = getStackField();
    assertNull(stack[0]);
  }

  @Test
    @Timeout(8000)
  public void testPopStack_whenStackHasMultipleElements() throws Exception {
    // Prepare stack with multiple elements: {"a", "b", "c"}, stackSize = 3
    setStackField(new Object[]{"a", "b", "c"});
    setStackSizeField(3);

    Object popped1 = popStackMethod.invoke(jsonTreeReader);
    assertEquals("c", popped1);
    assertEquals(2, getStackSizeField());
    assertNull(getStackField()[2]);

    Object popped2 = popStackMethod.invoke(jsonTreeReader);
    assertEquals("b", popped2);
    assertEquals(1, getStackSizeField());
    assertNull(getStackField()[1]);

    Object popped3 = popStackMethod.invoke(jsonTreeReader);
    assertEquals("a", popped3);
    assertEquals(0, getStackSizeField());
    assertNull(getStackField()[0]);
  }

  // Helper methods to access private fields via reflection

  private void setStackField(Object[] stackContent) throws Exception {
    var stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    // Create a new array of length 32 and copy stackContent in front
    Object[] newStack = new Object[32];
    System.arraycopy(stackContent, 0, newStack, 0, stackContent.length);
    stackField.set(jsonTreeReader, newStack);
  }

  private Object[] getStackField() throws Exception {
    var stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (Object[]) stackField.get(jsonTreeReader);
  }

  private void setStackSizeField(int size) throws Exception {
    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, size);
  }

  private int getStackSizeField() throws Exception {
    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    return stackSizeField.getInt(jsonTreeReader);
  }
}
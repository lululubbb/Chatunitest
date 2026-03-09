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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeReader_236_1Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a minimal JsonElement mock or real instance to call constructor
    // Since JsonElement is abstract, use JsonNull.INSTANCE as simplest concrete subclass
    jsonTreeReader = new JsonTreeReader(com.google.gson.JsonNull.INSTANCE);
  }

  @Test
    @Timeout(8000)
  public void testPopStack_withOneElement() throws Exception {
    // Use reflection to get private fields and methods
    var stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    // Prepare stack with one element
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = "testElement";
    stackSizeField.setInt(jsonTreeReader, 1);

    // Access popStack method
    Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);

    // Invoke popStack
    Object result = popStackMethod.invoke(jsonTreeReader);

    // Assertions
    assertEquals("testElement", result);
    // stackSize should be 0 now
    assertEquals(0, stackSizeField.getInt(jsonTreeReader));
    // stack[0] should be null after pop
    assertNull(stack[0]);
  }

  @Test
    @Timeout(8000)
  public void testPopStack_withMultipleElements() throws Exception {
    var stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = "first";
    stack[1] = "second";
    stack[2] = "third";
    stackSizeField.setInt(jsonTreeReader, 3);

    Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);

    // Pop last element "third"
    Object result1 = popStackMethod.invoke(jsonTreeReader);
    assertEquals("third", result1);
    assertEquals(2, stackSizeField.getInt(jsonTreeReader));
    assertNull(stack[2]);

    // Pop second element "second"
    Object result2 = popStackMethod.invoke(jsonTreeReader);
    assertEquals("second", result2);
    assertEquals(1, stackSizeField.getInt(jsonTreeReader));
    assertNull(stack[1]);

    // Pop first element "first"
    Object result3 = popStackMethod.invoke(jsonTreeReader);
    assertEquals("first", result3);
    assertEquals(0, stackSizeField.getInt(jsonTreeReader));
    assertNull(stack[0]);
  }

  @Test
    @Timeout(8000)
  public void testPopStack_emptyStack() throws Exception {
    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 0);

    Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);

    // When stackSize is 0, decrementing will cause negative index access
    // This will throw ArrayIndexOutOfBoundsException
    assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
      popStackMethod.invoke(jsonTreeReader);
    });
  }
}
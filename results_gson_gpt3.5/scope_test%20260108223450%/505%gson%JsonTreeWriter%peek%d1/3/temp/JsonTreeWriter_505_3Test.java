package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_505_3Test {

  @Test
    @Timeout(8000)
  public void peek_whenStackEmpty_throwsIndexOutOfBoundsException() throws Exception {
    JsonTreeWriter writer = new JsonTreeWriter();

    // stack is initially empty, so peek should throw IndexOutOfBoundsException
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    Exception exception = assertThrows(InvocationTargetException.class, () -> {
      peekMethod.invoke(writer);
    });

    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IndexOutOfBoundsException);
  }

  @Test
    @Timeout(8000)
  public void peek_whenStackHasOneElement_returnsThatElement() throws Exception {
    JsonTreeWriter writer = new JsonTreeWriter();

    // Use reflection to get the stack field and add an element
    var stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    JsonPrimitive element = new JsonPrimitive("element");
    stack.add(element);

    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    Object result = peekMethod.invoke(writer);
    assertNotNull(result);
    assertTrue(result instanceof JsonPrimitive);
    assertEquals(element, result);
  }

  @Test
    @Timeout(8000)
  public void peek_whenStackHasMultipleElements_returnsLastElement() throws Exception {
    JsonTreeWriter writer = new JsonTreeWriter();

    var stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    JsonPrimitive first = new JsonPrimitive("first");
    JsonObject second = new JsonObject();
    JsonArray third = new JsonArray();

    stack.add(first);
    stack.add(second);
    stack.add(third);

    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    Object result = peekMethod.invoke(writer);
    assertNotNull(result);
    assertTrue(result instanceof JsonArray);
    assertEquals(third, result);
  }
}
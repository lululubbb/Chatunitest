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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class JsonTreeReader_235_6Test {

  private JsonTreeReader jsonTreeReader;
  private Field stackField;
  private Field stackSizeField;
  private Method peekStackMethod;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonElement mock to pass to constructor (can be null because we won't use it)
    jsonTreeReader = new JsonTreeReader(null);

    // Access private fields
    stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    // Access private method peekStack
    peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void peekStack_whenStackIsEmpty_shouldThrowArrayIndexOutOfBoundsException() throws Exception {
    // stackSize = 0 means stack[stackSize - 1] = stack[-1], should throw
    stackSizeField.setInt(jsonTreeReader, 0);
    stackField.set(jsonTreeReader, new Object[32]);

    ArrayIndexOutOfBoundsException thrown = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
      try {
        peekStackMethod.invoke(jsonTreeReader);
      } catch (InvocationTargetException e) {
        // unwrap the cause and rethrow if it's ArrayIndexOutOfBoundsException, else rethrow original
        Throwable cause = e.getCause();
        if (cause instanceof ArrayIndexOutOfBoundsException) {
          throw (ArrayIndexOutOfBoundsException) cause;
        } else {
          throw e;
        }
      }
    });
    // optionally assert message or index if needed
  }

  @Test
    @Timeout(8000)
  public void peekStack_whenStackHasOneElement_shouldReturnThatElement() throws Exception {
    Object[] stack = new Object[32];
    Object element = new Object();
    stack[0] = element;

    stackField.set(jsonTreeReader, stack);
    stackSizeField.setInt(jsonTreeReader, 1);

    Object result = peekStackMethod.invoke(jsonTreeReader);
    assertSame(element, result);
  }

  @Test
    @Timeout(8000)
  public void peekStack_whenStackHasMultipleElements_shouldReturnTopElement() throws Exception {
    Object[] stack = new Object[32];
    Object bottom = new Object();
    Object middle = new Object();
    Object top = new Object();
    stack[0] = bottom;
    stack[1] = middle;
    stack[2] = top;

    stackField.set(jsonTreeReader, stack);
    stackSizeField.setInt(jsonTreeReader, 3);

    Object result = peekStackMethod.invoke(jsonTreeReader);
    assertSame(top, result);
  }

  @Test
    @Timeout(8000)
  public void peekStack_whenStackSizeIsMax_shouldReturnTopElement() throws Exception {
    Object[] stack = new Object[32];
    Object top = new Object();
    stack[31] = top;

    stackField.set(jsonTreeReader, stack);
    stackSizeField.setInt(jsonTreeReader, 32);

    Object result = peekStackMethod.invoke(jsonTreeReader);
    assertSame(top, result);
  }
}
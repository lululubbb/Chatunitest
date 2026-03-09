package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeWriter_521_2Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testFlush_shouldNotThrowOrChangeState() throws IOException {
    // flush() is overridden to do nothing; it should not throw or alter state
    JsonElement beforeFlush = jsonTreeWriter.get();
    jsonTreeWriter.flush();
    JsonElement afterFlush = jsonTreeWriter.get();
    assertEquals(beforeFlush, afterFlush);
  }

  @Test
    @Timeout(8000)
  public void testFlush_whenStackModified_shouldRemainUnchanged() throws IOException {
    // Use reflection to invoke private put(JsonElement) to add element to stack
    JsonElement element = JsonNull.INSTANCE;
    try {
      Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
      putMethod.setAccessible(true);
      try {
        putMethod.invoke(jsonTreeWriter, element);
      } catch (InvocationTargetException e) {
        fail("Invocation failed: " + e.getCause());
      }
    } catch (NoSuchMethodException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }

    JsonElement beforeFlush = jsonTreeWriter.get();
    jsonTreeWriter.flush();
    JsonElement afterFlush = jsonTreeWriter.get();
    assertEquals(beforeFlush, afterFlush);
  }

  @Test
    @Timeout(8000)
  public void testGet_andPeek_reflectionAccess() {
    try {
      Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
      peekMethod.setAccessible(true);

      // Initially stack is empty, peek should throw IndexOutOfBoundsException wrapped in InvocationTargetException
      InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
        peekMethod.invoke(jsonTreeWriter);
      });
      assertTrue(thrown.getCause() instanceof IndexOutOfBoundsException);

      // Add element to stack and test peek returns it
      Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
      putMethod.setAccessible(true);
      try {
        putMethod.invoke(jsonTreeWriter, JsonNull.INSTANCE);
      } catch (InvocationTargetException e) {
        fail("Invocation failed: " + e.getCause());
      }

      Object peeked = null;
      try {
        peeked = peekMethod.invoke(jsonTreeWriter);
      } catch (InvocationTargetException e) {
        fail("Invocation failed: " + e.getCause());
      }
      assertNotNull(peeked);
      assertTrue(peeked instanceof JsonElement);

    } catch (NoSuchMethodException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}
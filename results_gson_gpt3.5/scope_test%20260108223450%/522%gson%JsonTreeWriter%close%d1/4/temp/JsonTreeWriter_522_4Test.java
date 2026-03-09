package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonTreeWriter_522_4Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void close_whenStackIsEmpty_addsSentinelClosed() throws Exception {
    // Initially stack is empty
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<?> stack = (List<?>) stackField.get(jsonTreeWriter);
    assertTrue(stack.isEmpty());

    jsonTreeWriter.close();

    // After close, stack contains SENTINEL_CLOSED
    Object sentinelClosed = getStaticField(JsonTreeWriter.class, "SENTINEL_CLOSED");
    assertEquals(1, stack.size());
    assertEquals(sentinelClosed, stack.get(0));
  }

  @Test
    @Timeout(8000)
  void close_whenStackIsNotEmpty_throwsIOException() throws Exception {
    // Add an element to stack to simulate incomplete document
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<Object> stack = (List<Object>) stackField.get(jsonTreeWriter);
    stack.add(new JsonPrimitive("not empty"));

    IOException thrown = assertThrows(IOException.class, () -> jsonTreeWriter.close());
    assertEquals("Incomplete document", thrown.getMessage());

    // Stack remains unchanged (still contains the added element)
    assertEquals(1, stack.size());
    assertEquals(new JsonPrimitive("not empty"), stack.get(0));
  }

  // Helper method to get static private field value via reflection
  private static Object getStaticField(Class<?> clazz, String fieldName) throws Exception {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(null);
  }
}
package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_521_1Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void flush_shouldNotThrowAndKeepStateIntact() throws IOException, Exception {
    // Arrange
    // Use reflection to access private field 'stack' and add an element to test state remains intact
    List<JsonElement> stack = (List<JsonElement>) getField(jsonTreeWriter, "stack");
    stack.add(JsonNull.INSTANCE);
    setField(jsonTreeWriter, "product", JsonNull.INSTANCE);
    setField(jsonTreeWriter, "pendingName", "testName");

    // Act & Assert
    // flush() should not throw IOException or any exception
    assertDoesNotThrow(() -> jsonTreeWriter.flush());

    // Validate that the internal state is unchanged after flush
    List<JsonElement> stackAfter = (List<JsonElement>) getField(jsonTreeWriter, "stack");
    String pendingNameAfter = (String) getField(jsonTreeWriter, "pendingName");
    JsonElement productAfter = (JsonElement) getField(jsonTreeWriter, "product");

    assertEquals(1, stackAfter.size());
    assertEquals(JsonNull.INSTANCE, stackAfter.get(0));
    assertEquals("testName", pendingNameAfter);
    assertEquals(JsonNull.INSTANCE, productAfter);
  }

  @Test
    @Timeout(8000)
  public void flush_onNewInstance_shouldKeepProductJsonNull() throws IOException {
    // Act & Assert
    assertDoesNotThrow(() -> jsonTreeWriter.flush());
    JsonElement product = jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, product);
  }

  @Test
    @Timeout(8000)
  public void flush_reflectiveInvocation_shouldNotThrow() throws Exception {
    Method flushMethod = JsonTreeWriter.class.getDeclaredMethod("flush");
    flushMethod.setAccessible(true);
    assertDoesNotThrow(() -> flushMethod.invoke(jsonTreeWriter));
  }

  private Object getField(Object target, String fieldName) {
    try {
      var field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setField(Object target, String fieldName, Object value) {
    try {
      var field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
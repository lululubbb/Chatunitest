package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

class JsonTreeWriter_521_3Test {

  private JsonTreeWriter writer;

  @BeforeEach
  void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void flush_shouldDoNothingAndNotThrow() {
    assertDoesNotThrow(() -> writer.flush());
  }

  @Test
    @Timeout(8000)
  void flush_reflectionInvokePrivateUnwritableWriterFlush_throwsAssertionError() throws Exception {
    // Access private static field UNWRITABLE_WRITER
    var field = JsonTreeWriter.class.getDeclaredField("UNWRITABLE_WRITER");
    field.setAccessible(true);
    Writer unwritableWriter = (Writer) field.get(null);

    // flush method of the anonymous Writer class should throw AssertionError
    assertThrows(AssertionError.class, unwritableWriter::flush);
  }

  @Test
    @Timeout(8000)
  void get_shouldReturnJsonNullInitially() throws Exception {
    Method getMethod = JsonTreeWriter.class.getDeclaredMethod("get");
    getMethod.setAccessible(true);
    JsonElement result = (JsonElement) getMethod.invoke(writer);
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
    @Timeout(8000)
  void peek_shouldReturnTopOfStackOrThrow() throws Exception {
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    // Initially stack empty, peek should throw IndexOutOfBoundsException wrapped in InvocationTargetException
    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> peekMethod.invoke(writer));
    assertTrue(exception.getCause() instanceof IndexOutOfBoundsException);

    // Add element to stack via reflection
    var stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    stack.add(JsonNull.INSTANCE);

    JsonElement top = (JsonElement) peekMethod.invoke(writer);
    assertEquals(JsonNull.INSTANCE, top);
  }

  @Test
    @Timeout(8000)
  void put_shouldAddElementToStack() throws Exception {
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    var stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    JsonElement element = JsonNull.INSTANCE;

    // Clear the stack before invoking put to ensure it accepts the element
    stack.clear();

    // Add a dummy element to stack so put() can add properly (put expects stack not empty)
    stack.add(JsonNull.INSTANCE);

    putMethod.invoke(writer, element);

    assertEquals(2, stack.size());
    assertEquals(element, stack.get(stack.size() - 1));
  }
}
package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonTreeWriterFlushTest {
  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void flush_whenCalled_doesNotThrow() throws IOException {
    // flush method is empty, should not throw
    assertDoesNotThrow(() -> jsonTreeWriter.flush());
  }

  @Test
    @Timeout(8000)
  void flush_afterClose_doesNotThrow() throws IOException, Exception {
    // Use reflection to set product field to SENTINEL_CLOSED to simulate closed writer
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Field sentinelClosedField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelClosedField.setAccessible(true);
    productField.set(jsonTreeWriter, sentinelClosedField.get(null));

    // flush should still not throw
    assertDoesNotThrow(() -> jsonTreeWriter.flush());
  }

  @Test
    @Timeout(8000)
  void flush_withStackPopulated_doesNotThrow() throws IOException, Exception {
    // Use reflection to add a JsonElement to the stack
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.add(JsonNull.INSTANCE);

    // flush should still not throw
    assertDoesNotThrow(() -> jsonTreeWriter.flush());
  }

  @Test
    @Timeout(8000)
  void flush_withPendingNameSet_doesNotThrow() throws IOException, Exception {
    // Use reflection to set pendingName
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, "testName");

    // flush should still not throw
    assertDoesNotThrow(() -> jsonTreeWriter.flush());
  }

  @Test
    @Timeout(8000)
  void testPrivatePeekMethod_returnsCorrectElement() throws Exception {
    // Use reflection to add JsonElement to stack
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.add(JsonNull.INSTANCE);

    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    Object result = peekMethod.invoke(jsonTreeWriter);
    assertSame(JsonNull.INSTANCE, result);
  }

  @Test
    @Timeout(8000)
  void testPrivatePutMethod_addsElementToStack() throws Exception {
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    int initialSize = stack.size();
    putMethod.invoke(jsonTreeWriter, JsonNull.INSTANCE);
    assertEquals(initialSize + 1, stack.size());
    assertSame(JsonNull.INSTANCE, stack.get(stack.size() - 1));
  }
}
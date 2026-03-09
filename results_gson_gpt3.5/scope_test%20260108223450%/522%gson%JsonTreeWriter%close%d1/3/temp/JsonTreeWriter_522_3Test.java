package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class JsonTreeWriter_522_3Test {
  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void close_whenStackIsEmpty_addsSentinelClosed() throws Exception {
    // Ensure stack is empty initially
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<?> stack = (List<?>) stackField.get(writer);
    stack.clear();

    // Call close()
    writer.close();

    // Verify stack contains only SENTINEL_CLOSED
    assertEquals(1, stack.size());
    Object sentinelClosed = stack.get(0);

    Field sentinelField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelField.setAccessible(true);
    Object expectedSentinel = sentinelField.get(null);

    assertSame(expectedSentinel, sentinelClosed);
  }

  @Test
    @Timeout(8000)
  public void close_whenStackIsNotEmpty_throwsIOException() throws Exception {
    // Add dummy element to stack to make it non-empty
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<Object> stack = (List<Object>) stackField.get(writer);
    stack.add(mock(JsonElement.class));

    IOException exception = assertThrows(IOException.class, () -> writer.close());
    assertEquals("Incomplete document", exception.getMessage());
  }
}
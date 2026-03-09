package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class JsonTreeWriter_522_1Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void close_whenStackIsEmpty_addsSentinelClosed() throws Exception {
    // Ensure stack is empty
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<?> stack = (List<?>) stackField.get(jsonTreeWriter);
    stack.clear();
    assertTrue(stack.isEmpty());

    jsonTreeWriter.close();

    // Verify SENTINEL_CLOSED added to stack
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
    // Add dummy element to stack so it's not empty
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<Object> stack = (List<Object>) stackField.get(jsonTreeWriter);
    stack.clear();
    stack.add(new JsonPrimitive("dummy"));

    IOException thrown = assertThrows(IOException.class, () -> jsonTreeWriter.close());
    assertEquals("Incomplete document", thrown.getMessage());

    // Stack should remain unchanged
    assertEquals(1, stack.size());
    assertEquals("dummy", ((JsonPrimitive) stack.get(0)).getAsString());
  }
}
package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonTreeReader_247_6Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a minimal JsonElement mock for constructor (not used in close)
    JsonElement element = mock(JsonElement.class);
    jsonTreeReader = new JsonTreeReader(element);

    // Set stack and stackSize to some initial state different than closed sentinel
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] initialStack = new Object[32];
    initialStack[0] = new Object();
    stackField.set(jsonTreeReader, initialStack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);
  }

  @Test
    @Timeout(8000)
  public void close_setsStackToSentinelClosed_andStackSizeToOne() throws Exception {
    // Invoke close()
    jsonTreeReader.close();

    // Access private field stack
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);

    // Access private field stackSize
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonTreeReader);

    // Access private static field SENTINEL_CLOSED
    Field sentinelClosedField = JsonTreeReader.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelClosedField.setAccessible(true);
    Object sentinelClosed = sentinelClosedField.get(null);

    // Assert stack length is 1 and contains SENTINEL_CLOSED
    assertNotNull(stack);
    assertEquals(1, stack.length);
    assertSame(sentinelClosed, stack[0]);

    // Assert stackSize is 1
    assertEquals(1, stackSize);
  }
}
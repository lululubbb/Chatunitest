package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonTreeReader_247_5Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a minimal JsonElement mock because constructor requires it
    JsonElement element = mock(JsonElement.class);
    jsonTreeReader = new JsonTreeReader(element);
  }

  @Test
    @Timeout(8000)
  public void testClose_setsStackToSentinelClosedAndStackSizeToOne() throws Exception {
    // Use reflection to set stack and stackSize to non-default values first
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    // Set stack to a new Object array with some dummy content and stackSize > 1
    Object[] dummyStack = new Object[32];
    dummyStack[0] = new Object();
    dummyStack[1] = new Object();
    stackField.set(jsonTreeReader, dummyStack);
    stackSizeField.setInt(jsonTreeReader, 2);

    // Call close
    jsonTreeReader.close();

    // Get updated stack and stackSize
    Object[] updatedStack = (Object[]) stackField.get(jsonTreeReader);
    int updatedStackSize = stackSizeField.getInt(jsonTreeReader);

    // Access SENTINEL_CLOSED via reflection
    Field sentinelClosedField = JsonTreeReader.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelClosedField.setAccessible(true);
    Object sentinelClosed = sentinelClosedField.get(null);

    // Assertions
    assertNotNull(updatedStack);
    assertEquals(1, updatedStackSize);
    assertEquals(sentinelClosed, updatedStack[0]);
  }
}
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

public class JsonTreeReader_247_3Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a dummy JsonElement for constructor
    JsonElement element = mock(JsonElement.class);
    jsonTreeReader = new JsonTreeReader(element);
  }

  @Test
    @Timeout(8000)
  public void testClose_setsStackToSentinelClosedAndStackSizeOne() throws Exception {
    // Use reflection to set stack and stackSize to some non-default values
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] initialStack = new Object[32];
    initialStack[0] = new Object();
    stackField.set(jsonTreeReader, initialStack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 10);

    // Call close()
    jsonTreeReader.close();

    // Verify stack is replaced with array containing SENTINEL_CLOSED
    Object[] stackAfterClose = (Object[]) stackField.get(jsonTreeReader);
    assertNotNull(stackAfterClose);
    assertEquals(1, stackAfterClose.length);
    Field sentinelClosedField = JsonTreeReader.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelClosedField.setAccessible(true);
    Object sentinelClosed = sentinelClosedField.get(null);
    assertSame(sentinelClosed, stackAfterClose[0]);

    // Verify stackSize is set to 1
    int stackSizeAfterClose = stackSizeField.getInt(jsonTreeReader);
    assertEquals(1, stackSizeAfterClose);
  }
}
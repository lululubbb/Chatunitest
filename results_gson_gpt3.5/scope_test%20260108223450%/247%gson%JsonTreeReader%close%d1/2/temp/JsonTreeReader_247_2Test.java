package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.io.IOException;

public class JsonTreeReader_247_2Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() {
    JsonElement element = new JsonNull();
    jsonTreeReader = new JsonTreeReader(element);
  }

  @Test
    @Timeout(8000)
  public void testClose_setsStackToSentinelClosedAndStackSizeOne() throws Exception {
    // Arrange: set stack and stackSize to some initial state
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Object[] initialStack = new Object[32];
    initialStack[0] = new JsonNull();
    stackField.set(jsonTreeReader, initialStack);
    stackSizeField.setInt(jsonTreeReader, 1);

    // Act
    jsonTreeReader.close();

    // Assert
    Object[] stackAfterClose = (Object[]) stackField.get(jsonTreeReader);
    int stackSizeAfterClose = stackSizeField.getInt(jsonTreeReader);

    // Verify stackSize is 1
    assertEquals(1, stackSizeAfterClose);

    // Verify stack[0] is SENTINEL_CLOSED
    Field sentinelClosedField = JsonTreeReader.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelClosedField.setAccessible(true);
    Object sentinelClosed = sentinelClosedField.get(null);

    assertSame(sentinelClosed, stackAfterClose[0]);
  }
}
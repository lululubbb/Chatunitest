package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.google.gson.JsonNull;
import com.google.gson.internal.bind.JsonTreeReader;

public class JsonTreeReader_247_1Test {
  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Using reflection to create a JsonTreeReader instance with a JsonNull element (as JsonElement is abstract)
    Class<?> jsonNullClass = Class.forName("com.google.gson.JsonNull");
    Object jsonNullInstance = jsonNullClass.getDeclaredField("INSTANCE").get(null);
    jsonTreeReader = new JsonTreeReader((com.google.gson.JsonElement) jsonNullInstance);
  }

  @Test
    @Timeout(8000)
  public void testClose_resetsStackAndStackSize() throws Exception {
    // Prepare: modify private fields stack and stackSize
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Object[] initialStack = new Object[] { "test", "value" };
    stackField.set(jsonTreeReader, initialStack);
    stackSizeField.setInt(jsonTreeReader, initialStack.length);

    // Call close()
    jsonTreeReader.close();

    // Verify stack is reset to array containing SENTINEL_CLOSED
    Object[] stackAfterClose = (Object[]) stackField.get(jsonTreeReader);
    assertNotNull(stackAfterClose);
    assertEquals(1, stackAfterClose.length);

    // Access SENTINEL_CLOSED to check equality
    Field sentinelClosedField = JsonTreeReader.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelClosedField.setAccessible(true);
    Object sentinelClosed = sentinelClosedField.get(null);

    assertSame(sentinelClosed, stackAfterClose[0]);

    // Verify stackSize is reset to 1
    int stackSizeAfterClose = stackSizeField.getInt(jsonTreeReader);
    assertEquals(1, stackSizeAfterClose);
  }
}
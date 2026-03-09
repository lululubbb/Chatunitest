package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class JsonTreeWriter_512_4Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testValue_withNonNullString_returnsThisAndAddsJsonPrimitive() throws Exception {
    String testValue = "test";

    // Prepare the writer by beginning an array so stack is not empty
    jsonTreeWriter.beginArray();

    JsonWriter result = jsonTreeWriter.value(testValue);

    assertSame(jsonTreeWriter, result);

    // Use reflection to access private method peek() to verify the top element of stack
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    JsonElement topElement = (JsonElement) peekMethod.invoke(jsonTreeWriter);

    // The top element should be the JsonArray (because we began an array)
    // The JsonPrimitive should be inside this array, so we need to get the element inside the array
    // topElement is the JsonArray, so check its first element
    assertTrue(topElement.isJsonArray());
    assertEquals(1, topElement.getAsJsonArray().size());
    JsonElement firstElement = topElement.getAsJsonArray().get(0);

    // The first element inside the array should be the JsonPrimitive with the testValue
    assertTrue(firstElement instanceof JsonPrimitive);
    assertEquals(testValue, ((JsonPrimitive) firstElement).getAsString());
  }

  @Test
    @Timeout(8000)
  public void testValue_withNullString_callsNullValueAndReturns() throws IOException {
    // Spy on JsonTreeWriter to verify nullValue() is called
    JsonTreeWriter spyWriter = spy(jsonTreeWriter);

    // Use reflection to call value(String) with null to avoid ambiguity
    try {
      Method valueStringMethod = JsonTreeWriter.class.getMethod("value", String.class);
      JsonWriter result = (JsonWriter) valueStringMethod.invoke(spyWriter, new Object[] {null});
      verify(spyWriter).nullValue();
      assertSame(result, spyWriter.nullValue());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  public void testValue_withNullString_nullValueAddsJsonNull() throws Exception {
    // Use reflection to call value(String) with null to avoid ambiguity
    Method valueStringMethod = JsonTreeWriter.class.getMethod("value", String.class);
    valueStringMethod.invoke(jsonTreeWriter, new Object[] {null});

    // Use reflection to get the product element, which should be JsonNull.INSTANCE after nullValue()
    Method getMethod = JsonTreeWriter.class.getDeclaredMethod("get");
    getMethod.setAccessible(true);
    JsonElement product = (JsonElement) getMethod.invoke(jsonTreeWriter);

    assertEquals(JsonNull.INSTANCE, product);
  }
}
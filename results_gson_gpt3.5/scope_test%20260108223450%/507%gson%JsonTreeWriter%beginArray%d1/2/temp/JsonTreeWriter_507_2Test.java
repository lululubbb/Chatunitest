package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class JsonTreeWriter_507_2Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testBeginArray_shouldReturnThisAndAddNewJsonArrayToStack() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Call beginArray
    JsonWriter returned = jsonTreeWriter.beginArray();

    // Returned object should be the same instance
    assertSame(jsonTreeWriter, returned);

    // Use reflection to access private field 'stack'
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    // The stack should not be empty
    assertFalse(stack.isEmpty());

    // The last element in stack should be a JsonArray
    JsonElement lastElement = stack.get(stack.size() - 1);
    assertTrue(lastElement instanceof JsonArray);

    // Use reflection to access private method peek to verify top of stack
    // peek() is private, so we use reflection to invoke it
    JsonElement peeked = invokePrivatePeek(jsonTreeWriter);
    assertSame(lastElement, peeked);
  }

  private JsonElement invokePrivatePeek(JsonTreeWriter writer) {
    try {
      var method = JsonTreeWriter.class.getDeclaredMethod("peek");
      method.setAccessible(true);
      return (JsonElement) method.invoke(writer);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class JsonTreeWriterPeekTest {

  private JsonTreeWriter jsonTreeWriter;
  private Method peekMethod;
  private Field stackField;

  @BeforeEach
  public void setUp() throws Exception {
    jsonTreeWriter = new JsonTreeWriter();

    // Access private peek() method
    peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    // Access private stack field
    stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testPeekReturnsLastElementInStack() throws Exception {
    // Prepare stack with multiple JsonElements
    List<JsonElement> stack = new ArrayList<>();
    JsonPrimitive first = new JsonPrimitive("first");
    JsonObject second = new JsonObject();
    JsonArray third = new JsonArray();
    stack.add(first);
    stack.add(second);
    stack.add(third);

    // Inject stack into JsonTreeWriter instance
    stackField.set(jsonTreeWriter, stack);

    // Invoke peek()
    JsonElement result = (JsonElement) peekMethod.invoke(jsonTreeWriter);

    // Verify peek returns the last element
    assertSame(third, result);
  }

  @Test
    @Timeout(8000)
  public void testPeekSingleElementInStack() throws Exception {
    List<JsonElement> stack = new ArrayList<>();
    JsonNull element = JsonNull.INSTANCE;
    stack.add(element);

    stackField.set(jsonTreeWriter, stack);

    JsonElement result = (JsonElement) peekMethod.invoke(jsonTreeWriter);

    assertSame(element, result);
  }

  @Test
    @Timeout(8000)
  public void testPeekThrowsIndexOutOfBoundsWhenStackEmpty() throws Exception {
    List<JsonElement> stack = new ArrayList<>();
    stackField.set(jsonTreeWriter, stack);

    IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class, () -> {
      try {
        peekMethod.invoke(jsonTreeWriter);
      } catch (Exception e) {
        // unwrap the cause thrown by reflection invocation
        Throwable cause = e.getCause();
        if (cause instanceof IndexOutOfBoundsException) {
          throw (IndexOutOfBoundsException) cause;
        } else {
          throw e;
        }
      }
    });

    assertNotNull(exception);
  }
}
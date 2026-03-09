package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

class JsonTreeWriter_514_4Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void nullValue_shouldReturnThisAndPutJsonNullInstance() throws Exception {
    // Arrange
    // Use reflection to get the private 'stack' field
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    // Initially, the stack is empty, so call beginArray() to push a container
    jsonTreeWriter.beginArray();

    int initialStackSize = stack.size();

    // Act
    JsonWriter returned = jsonTreeWriter.nullValue();

    // Assert
    assertSame(jsonTreeWriter, returned, "nullValue() should return 'this'");

    // The stack size should increase by 1 and top element should be JsonNull.INSTANCE
    assertEquals(initialStackSize + 1, stack.size(), "Stack size should increase by 1 after nullValue()");

    JsonElement top = stack.get(stack.size() - 1);
    assertSame(JsonNull.INSTANCE, top, "Top element of stack should be JsonNull.INSTANCE");
  }

  @Test
    @Timeout(8000)
  void nullValue_shouldCallPutWithJsonNullInstance() throws Exception {
    // Spy on JsonTreeWriter to verify private method call via reflection
    JsonTreeWriter spyWriter = spy(new JsonTreeWriter());

    // Use reflection to get the private 'stack' field before call
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);

    // Push a container so nullValue() can add to stack
    spyWriter.beginArray();

    // Call nullValue()
    spyWriter.nullValue();

    // Verify that nullValue() was called (sanity check)
    verify(spyWriter).nullValue();

    // We cannot verify private method calls directly with Mockito,
    // but we can check stack contents as an indirect verification
    List<JsonElement> stack = (List<JsonElement>) stackField.get(spyWriter);
    assertFalse(stack.isEmpty(), "Stack should not be empty after nullValue()");
    assertSame(JsonNull.INSTANCE, stack.get(stack.size() - 1), "put should be called with JsonNull.INSTANCE");
  }
}
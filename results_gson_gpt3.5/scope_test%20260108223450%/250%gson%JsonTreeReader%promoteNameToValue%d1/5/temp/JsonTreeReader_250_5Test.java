package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReader_250_5Test {

  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonTreeReader with a dummy JsonElement (null is acceptable for this test)
    reader = new JsonTreeReader(null);
  }

  @Test
    @Timeout(8000)
  public void testPromoteNameToValue() throws Exception {
    // Prepare mocks for Iterator and Map.Entry
    @SuppressWarnings("unchecked")
    Iterator<Map.Entry<String, String>> mockIterator = mock(Iterator.class);
    @SuppressWarnings("unchecked")
    Map.Entry<String, String> mockEntry = mock(Map.Entry.class);

    when(mockIterator.next()).thenReturn(mockEntry);
    when(mockEntry.getKey()).thenReturn("keyName");
    when(mockEntry.getValue()).thenReturn("valueElement");

    // Use reflection to set the stack with the mock iterator at top
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stack[0] = mockIterator;
    stackField.set(reader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    // Set stackSize to 1, indicating one element on stack
    stackSizeField.setInt(reader, 1);

    // Use reflection to invoke promoteNameToValue on reader
    Method promoteNameToValueMethod = JsonTreeReader.class.getDeclaredMethod("promoteNameToValue");
    promoteNameToValueMethod.setAccessible(true);
    promoteNameToValueMethod.invoke(reader);

    // Verify that the iterator's next() was called once
    verify(mockIterator).next();

    // Verify stack after push operations
    Object[] updatedStack = (Object[]) stackField.get(reader);
    int updatedStackSize = (int) stackSizeField.get(reader);

    // After promoteNameToValue, stackSize should be 3 (original 1 + 2 pushes)
    assertEquals(3, updatedStackSize);

    // Top of stack (index 2) should be JsonPrimitive of key
    assertTrue(updatedStack[2] instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) updatedStack[2];
    assertEquals("keyName", primitive.getAsString());

    // Next below top (index 1) should be value pushed (the value from entry)
    assertEquals("valueElement", updatedStack[1]);

    // Bottom (index 0) should be the original iterator
    assertSame(mockIterator, updatedStack[0]);
  }
}
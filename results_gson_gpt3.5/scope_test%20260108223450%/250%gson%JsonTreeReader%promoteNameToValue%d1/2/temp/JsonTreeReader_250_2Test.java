package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReader_250_2Test {

  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a dummy JsonElement for constructor (using JsonNull)
    reader = new JsonTreeReader(JsonNull.INSTANCE);
  }

  @Test
    @Timeout(8000)
  public void testPromoteNameToValue_success() throws Exception {
    // Arrange
    // Mock an Iterator<Map.Entry<?, ?>> with one entry
    @SuppressWarnings("unchecked")
    Iterator<Map.Entry<Object, Object>> mockIterator = mock(Iterator.class);
    @SuppressWarnings("unchecked")
    Map.Entry<Object, Object> mockEntry = mock(Map.Entry.class);

    when(mockEntry.getKey()).thenReturn("mockKey");
    when(mockEntry.getValue()).thenReturn(new JsonPrimitive("mockValue"));
    when(mockIterator.next()).thenReturn(mockEntry);
    when(mockIterator.hasNext()).thenReturn(true);

    // Use reflection to set the private stack field with the mockIterator at top and stackSize = 1
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[0] = mockIterator;

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    // Also set the private pathIndices and pathNames arrays to valid initial values to avoid peek() errors
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    pathIndices[0] = 0;

    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(reader);
    pathNames[0] = null;

    // Act
    Method promoteNameToValue = JsonTreeReader.class.getDeclaredMethod("promoteNameToValue");
    promoteNameToValue.setAccessible(true);
    promoteNameToValue.invoke(reader);

    // Assert
    int stackSizeAfter = stackSizeField.getInt(reader);
    assertEquals(3, stackSizeAfter);

    Object[] stackAfter = (Object[]) stackField.get(reader);

    Object top = stackAfter[stackSizeAfter - 1];
    assertTrue(top instanceof JsonPrimitive);
    assertEquals("mockKey", ((JsonPrimitive) top).getAsString());

    Object second = stackAfter[stackSizeAfter - 2];
    assertTrue(second instanceof JsonPrimitive);
    assertEquals("mockValue", ((JsonPrimitive) second).getAsString());

    assertSame(mockIterator, stackAfter[0]);

    verify(mockIterator).next();
  }

  @Test
    @Timeout(8000)
  public void testPromoteNameToValue_expectThrows() throws Exception {
    // Since expect is private and final class cannot be subclassed,
    // we simulate failure by setting stack to an invalid state that causes expect to throw.

    // Arrange
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    // Put something invalid at top to cause expect(JsonToken.NAME) to fail
    stack[0] = "invalid"; // Not an Iterator

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    Method promoteNameToValue = JsonTreeReader.class.getDeclaredMethod("promoteNameToValue");
    promoteNameToValue.setAccessible(true);

    // Act & Assert
    Throwable thrown = assertThrows(Throwable.class, () -> promoteNameToValue.invoke(reader));
    // The IOException will be wrapped in InvocationTargetException, so unwrap:
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IOException);
  }
}
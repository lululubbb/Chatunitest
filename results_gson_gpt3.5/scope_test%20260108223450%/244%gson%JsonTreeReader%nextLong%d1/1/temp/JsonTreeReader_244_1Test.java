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
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonTreeReader_244_1Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonPrimitive with a long value
    JsonPrimitive primitive = new JsonPrimitive(1234567890123L);

    // Use the public constructor JsonTreeReader(JsonElement)
    jsonTreeReader = new JsonTreeReader(primitive);

    // Inject the primitive into the stack manually to control peekStack() and stackSize
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = primitive;
    stackField.set(jsonTreeReader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    // Inject pathIndices array with initial value 0
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 0;
    pathIndicesField.set(jsonTreeReader, pathIndices);

    // Spy the JsonTreeReader to mock peek() method
    jsonTreeReader = spy(jsonTreeReader);
  }

  @Test
    @Timeout(8000)
  public void nextLong_withNumberToken_returnsLongAndUpdatesPathIndices() throws Exception {
    // Arrange
    doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

    // Act
    long result = jsonTreeReader.nextLong();

    // Assert
    assertEquals(1234567890123L, result);

    // Verify stackSize decreased by one after popStack (stackSize was 1, now 0)
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(0, stackSize);

    // Since stackSize is now 0, pathIndices should not increment
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    assertEquals(0, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextLong_withStringToken_returnsLongAndUpdatesPathIndices() throws Exception {
    // Arrange
    // Replace stack with JsonPrimitive holding numeric string
    JsonPrimitive primitiveString = new JsonPrimitive("9876543210");
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = primitiveString;
    stackField.set(jsonTreeReader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    doReturn(JsonToken.STRING).when(jsonTreeReader).peek();

    // Act
    long result = jsonTreeReader.nextLong();

    // Assert
    assertEquals(9876543210L, result);

    // stackSize after popStack() should be 0
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(0, stackSize);

    // pathIndices not incremented because stackSize is 0
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    assertEquals(0, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextLong_withNumberToken_andStackSizeGreaterThanZero_incrementsPathIndices() throws Exception {
    // Arrange
    doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

    // Increase stackSize to 2 and set pathIndices[1] = 5
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 2);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 0; // ensure index 0 is initialized
    pathIndices[1] = 5;
    pathIndicesField.set(jsonTreeReader, pathIndices);

    // Also push a dummy JsonPrimitive on stack at index 0 and 1 so popStack works correctly
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    // Set index 1 to the primitive with 1234567890123L to ensure nextLong reads correct value
    stack[0] = new JsonPrimitive(42L);
    stack[1] = new JsonPrimitive(1234567890123L);
    stackField.set(jsonTreeReader, stack);

    // Act
    long result = jsonTreeReader.nextLong();

    // Assert
    assertEquals(1234567890123L, result);

    // stackSize decreased by 1: from 2 to 1
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(1, stackSize);

    // pathIndices[0] incremented by 1, was 0
    pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextLong_withInvalidToken_throwsIllegalStateException() throws Exception {
    // Arrange
    doReturn(JsonToken.BEGIN_ARRAY).when(jsonTreeReader).peek();

    // Act & Assert
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      jsonTreeReader.nextLong();
    });
    assertTrue(exception.getMessage().contains("Expected"));
    assertTrue(exception.getMessage().contains("but was"));
  }
}
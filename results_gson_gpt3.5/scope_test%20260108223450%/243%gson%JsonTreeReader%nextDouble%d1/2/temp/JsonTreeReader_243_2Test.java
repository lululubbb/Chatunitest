package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;

class JsonTreeReader_243_2Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a minimal JsonPrimitive to use in stack
    JsonPrimitive primitive = new JsonPrimitive(123.456);

    // Use reflection to call the constructor JsonTreeReader(JsonElement)
    // We can mock or create a dummy JsonElement for constructor argument.
    // Since JsonElement is abstract, we can mock it.
    com.google.gson.JsonElement mockElement = mock(com.google.gson.JsonElement.class);
    jsonTreeReader = new JsonTreeReader(mockElement);

    // Set stack with one JsonPrimitive element
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = primitive;
    stackField.set(jsonTreeReader, stack);

    // Set stackSize = 1
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    // Set pathIndices array to zeroes
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 0;
    pathIndicesField.set(jsonTreeReader, pathIndices);

    // Spy on jsonTreeReader to mock peek() method
    jsonTreeReader = spy(jsonTreeReader);
  }

  @Test
    @Timeout(8000)
  void nextDouble_returnsDouble_whenTokenIsNumber() throws Exception {
    // Arrange
    doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

    // Act
    double result = jsonTreeReader.nextDouble();

    // Assert
    assertEquals(123.456, result, 0.000001);

    // Validate stackSize decreased
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(0, stackSize);

    // Validate pathIndices incremented
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    // The increment occurs at stackSize - 1 after popStack, which is -1,
    // so no increment should happen because stackSize is 0 now.
    // Therefore, the correct assertion is:
    assertEquals(0, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_returnsDouble_whenTokenIsString() throws Exception {
    // Arrange
    doReturn(JsonToken.STRING).when(jsonTreeReader).peek();

    // Replace stack top with JsonPrimitive string representing a double
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = new JsonPrimitive("789.012");
    stackField.set(jsonTreeReader, stack);

    // Act
    double result = jsonTreeReader.nextDouble();

    // Assert
    assertEquals(789.012, result, 0.000001);

    // Validate stackSize decreased
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(0, stackSize);

    // Validate pathIndices incremented
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    // Same as above: no increment because stackSize after popStack is 0
    assertEquals(0, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_throwsIllegalStateException_whenTokenIsNotNumberOrString() throws Exception {
    // Arrange
    doReturn(JsonToken.BOOLEAN).when(jsonTreeReader).peek();

    // Act & Assert
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> jsonTreeReader.nextDouble());
    assertTrue(exception.getMessage().contains("Expected NUMBER but was BOOLEAN"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_throwsMalformedJsonException_whenNotLenientAndResultIsNaN() throws Exception {
    // Arrange
    doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

    // Replace stack top with JsonPrimitive NaN
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = new JsonPrimitive(Double.NaN);
    stackField.set(jsonTreeReader, stack);

    // Spy to override isLenient to false
    JsonTreeReader spyReader = spy(jsonTreeReader);
    doReturn(false).when(spyReader).isLenient();
    doReturn(JsonToken.NUMBER).when(spyReader).peek();

    // Act & Assert
    MalformedJsonException exception = assertThrows(MalformedJsonException.class, spyReader::nextDouble);
    assertTrue(exception.getMessage().contains("NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_throwsMalformedJsonException_whenNotLenientAndResultIsInfinite() throws Exception {
    // Arrange
    doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

    // Replace stack top with JsonPrimitive POSITIVE_INFINITY
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = new JsonPrimitive(Double.POSITIVE_INFINITY);
    stackField.set(jsonTreeReader, stack);

    // Spy to override isLenient to false
    JsonTreeReader spyReader = spy(jsonTreeReader);
    doReturn(false).when(spyReader).isLenient();
    doReturn(JsonToken.NUMBER).when(spyReader).peek();

    // Act & Assert
    MalformedJsonException exception = assertThrows(MalformedJsonException.class, spyReader::nextDouble);
    assertTrue(exception.getMessage().contains("NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_incrementsPathIndicesOnlyWhenStackSizeGreaterThanZero() throws Exception {
    // Arrange
    doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);

    // Case stackSize = 1
    stackSizeField.setInt(jsonTreeReader, 1);
    pathIndices[0] = 0;
    pathIndicesField.set(jsonTreeReader, pathIndices);
    stack[0] = new JsonPrimitive(42.0);
    stackField.set(jsonTreeReader, stack);

    double result1 = jsonTreeReader.nextDouble();
    // Refresh pathIndices after method call
    pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    assertEquals(1, pathIndices[0]);

    // Reset stack with new JsonPrimitive and stackSize=0 for next test
    stack[0] = new JsonPrimitive(42.0);
    stackField.set(jsonTreeReader, stack);

    // Case stackSize = 0 (after pop)
    stackSizeField.setInt(jsonTreeReader, 0);
    pathIndices[0] = 5;
    pathIndicesField.set(jsonTreeReader, pathIndices);

    // nextDouble will pop stack, but since stackSize is 0 before calling,
    // pathIndices should not increment
    double result2 = jsonTreeReader.nextDouble();

    // Refresh pathIndices after method call
    pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    // pathIndices should not increment because stackSize was 0 after pop
    assertEquals(5, pathIndices[0]);
  }
}
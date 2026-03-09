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
import java.lang.reflect.Method;

public class JsonTreeReader_243_1Test {

  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonPrimitive with a double value 123.456
    JsonPrimitive primitive = new JsonPrimitive(123.456);
    // Create JsonTreeReader instance with a dummy JsonElement via constructor
    reader = new JsonTreeReader(primitive);

    // Use reflection to set private fields stack, stackSize, pathIndices
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stack[0] = primitive;
    stackField.set(reader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndicesField.set(reader, pathIndices);
  }

  private JsonToken invokePeek(JsonTreeReader target) throws Exception {
    Method peekMethod = JsonTreeReader.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    return (JsonToken) peekMethod.invoke(target);
  }

  private Object invokePeekStack(JsonTreeReader target) throws Exception {
    Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);
    return peekStackMethod.invoke(target);
  }

  private Object invokePopStack(JsonTreeReader target) throws Exception {
    Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);
    return popStackMethod.invoke(target);
  }

  private void setStackSize(int size) throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, size);
  }

  private int getStackSize() throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    return stackSizeField.getInt(reader);
  }

  private void setPathIndicesAt(int index, int value) throws Exception {
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    pathIndices[index] = value;
  }

  private int getPathIndicesAt(int index) throws Exception {
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    return pathIndices[index];
  }

  /**
   * Helper method to call nextDouble() on a spy, but intercept calls to private peek(), peekStack(), popStack()
   * by reflection and stubbing.
   */
  private double callNextDoubleWithMocks(JsonTreeReader spyReader, JsonToken peekReturn, Object peekStackReturn,
                                         boolean mockPopStackDecrementStackSize) throws Exception {
    // We cannot mock private methods directly with Mockito, so we mock peek(), peekStack(), popStack() via reflection
    // by creating a subclass that overrides nextDouble and calls private methods via reflection.

    // Instead, we use doAnswer to intercept calls to peek(), peekStack(), popStack() via reflection

    // Create a spy that delegates peek(), peekStack(), popStack() calls to our mocked returns

    // Since nextDouble calls private methods, we call nextDouble via reflection on spyReader

    // But nextDouble is public, so we can call it directly

    // To control peek(), peekStack(), popStack(), we override them via spy with doAnswer on the spy's class

    // However, since these are private, Mockito cannot intercept them directly.

    // So instead, we create a subclass of JsonTreeReader with overridden methods to expose the private methods as protected,
    // then spy on that subclass.

    // But since we can't change source, we will invoke nextDouble() on spyReader, but before that,
    // override peek(), peekStack(), popStack() by reflection to return our desired values.

    // We can do this by using a dynamic proxy or by creating a subclass at runtime, but that is complicated.

    // Alternatively, we can temporarily replace the private methods with accessible versions using reflection.

    // Since this is complicated, we will implement the following approach:

    // 1. For peek(), since it's public, we can do: doReturn(peekReturn).when(spyReader).peek();
    doReturn(peekReturn).when(spyReader).peek();

    // 2. For peekStack() and popStack(), which are private, we cannot mock directly.
    // Instead, we override the private fields 'stack' and 'stackSize' to simulate peekStack/popStack behavior.

    // So, for peekStack(), we set stack[stackSize - 1] to peekStackReturn.

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(spyReader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(spyReader);

    if (stackSize == 0) {
      stackSize = 1;
      stackSizeField.setInt(spyReader, 1);
    }

    stack[stackSize - 1] = peekStackReturn;

    // For popStack(), if mockPopStackDecrementStackSize is true, we simulate popStack by decrementing stackSize by 1

    if (mockPopStackDecrementStackSize) {
      // We replace popStack() method by reflection to simulate decrementing stackSize
      // But since private methods cannot be mocked by Mockito, and we cannot override them,
      // we will invoke nextDouble() via reflection and after that manually decrement stackSize

      // Alternatively, we can spy on popStack() via doAnswer on the spyReader's class using reflection:

      // But Mockito cannot mock private methods.

      // So we do the following: we spy the popStack() call by replacing it with a public method via reflection.

      // Instead, we can create a proxy or just manually decrement stackSize in doAnswer on spyReader.popStack()

      // Since popStack is private, we cannot mock it directly.

      // So, we do the following: we create a spyReader that overrides nextDouble() to call our own implementation, but that's complicated.

      // So, simplest way: we call nextDouble() on spyReader, then manually decrement stackSize and simulate popStack.

      // But nextDouble calls popStack internally, so stackSize won't be decremented automatically.

      // So, we will create a subclass that overrides popStack() as protected and spy on it.

      // But since we cannot change source, we will implement a helper method to call nextDouble and after that decrement stackSize.

      // So we do:

      double result = spyReader.nextDouble();

      // Manually decrement stackSize to simulate popStack effect
      int currentStackSize = getStackSize();
      setStackSize(currentStackSize - 1);

      return result;
    } else {
      // For normal case, just call nextDouble(), popStack() will be called but stackSize won't change

      return spyReader.nextDouble();
    }
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_NumberToken_ReturnsValueAndUpdatesState() throws Exception {
    // Arrange
    JsonTreeReader spyReader = spy(reader);

    JsonPrimitive primitive = new JsonPrimitive(123.456);

    // Set stackSize and pathIndices
    setStackSize(1);
    setPathIndicesAt(0, 0);

    // Set stack top to primitive
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(spyReader);
    stack[0] = primitive;

    // Act
    double result = callNextDoubleWithMocks(spyReader, JsonToken.NUMBER, primitive, false);

    // Assert
    assertEquals(123.456, result, 0.000001);

    // popStack is private; we cannot verify it was called directly.
    // Instead, verify stackSize remains the same (because popStack does not decrement stackSize in this test)
    int stackSize = getStackSize();
    assertEquals(1, stackSize);

    // pathIndices[0] should be incremented by 1
    int updatedIndex = getPathIndicesAt(0);
    assertEquals(1, updatedIndex);
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_StringToken_ReturnsValue() throws Exception {
    JsonTreeReader spyReader = spy(reader);

    JsonPrimitive primitive = new JsonPrimitive("789.01");

    setStackSize(1);
    setPathIndicesAt(0, 5);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(spyReader);
    stack[0] = primitive;

    double result = callNextDoubleWithMocks(spyReader, JsonToken.STRING, primitive, false);

    assertEquals(789.01, result, 0.000001);

    int stackSize = getStackSize();
    assertEquals(1, stackSize);

    int updatedIndex = getPathIndicesAt(0);
    assertEquals(6, updatedIndex);
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_InvalidToken_ThrowsIllegalStateException() throws Exception {
    JsonTreeReader spyReader = spy(reader);

    setStackSize(1);
    setPathIndicesAt(0, 0);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(spyReader);
    stack[0] = new JsonPrimitive(1);

    doReturn(JsonToken.BEGIN_ARRAY).when(spyReader).peek();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::nextDouble);
    assertTrue(thrown.getMessage().contains("Expected NUMBER but was BEGIN_ARRAY"));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_NaN_ThrowsMalformedJsonException() throws Exception {
    JsonTreeReader spyReader = spy(reader);

    doReturn(JsonToken.NUMBER).when(spyReader).peek();

    JsonPrimitive primitive = mock(JsonPrimitive.class);
    when(primitive.getAsDouble()).thenReturn(Double.NaN);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(spyReader);
    stack[0] = primitive;

    setStackSize(1);
    setPathIndicesAt(0, 0);

    MalformedJsonException thrown = assertThrows(MalformedJsonException.class, spyReader::nextDouble);
    assertTrue(thrown.getMessage().contains("JSON forbids NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_Infinite_ThrowsMalformedJsonException() throws Exception {
    JsonTreeReader spyReader = spy(reader);

    doReturn(JsonToken.NUMBER).when(spyReader).peek();

    JsonPrimitive primitive = mock(JsonPrimitive.class);
    when(primitive.getAsDouble()).thenReturn(Double.POSITIVE_INFINITY);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(spyReader);
    stack[0] = primitive;

    setStackSize(1);
    setPathIndicesAt(0, 0);

    MalformedJsonException thrown = assertThrows(MalformedJsonException.class, spyReader::nextDouble);
    assertTrue(thrown.getMessage().contains("JSON forbids NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_StackSizeZero_DoesNotIncrementPathIndices() throws Exception {
    JsonTreeReader spyReader = spy(reader);

    doReturn(JsonToken.NUMBER).when(spyReader).peek();

    JsonPrimitive primitive = new JsonPrimitive(42.0);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(spyReader);
    stack[0] = primitive;

    setStackSize(1);
    setPathIndicesAt(0, 3);

    // We simulate popStack decrementing stackSize to zero by calling nextDouble and then decrementing stackSize manually
    double result = callNextDoubleWithMocks(spyReader, JsonToken.NUMBER, primitive, true);

    assertEquals(42.0, result, 0.000001);

    int pathIndex = getPathIndicesAt(0);
    // pathIndices should not increment because stackSize after popStack is 0
    assertEquals(3, pathIndex);

    int stackSize = getStackSize();
    assertEquals(0, stackSize);
  }
}
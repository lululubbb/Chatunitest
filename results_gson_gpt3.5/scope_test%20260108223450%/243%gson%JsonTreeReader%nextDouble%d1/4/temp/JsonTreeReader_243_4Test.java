package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class JsonTreeReader_243_4Test {

  private JsonTreeReader reader;

  private Object callPeekStack(JsonTreeReader reader) throws Exception {
    Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);
    return peekStackMethod.invoke(reader);
  }

  private void setStack(JsonTreeReader reader, Object topElement, int stackSizeValue, int pathIndexValue) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stack[0] = topElement;
    stackField.set(reader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, stackSizeValue);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndices[0] = pathIndexValue;
    pathIndicesField.set(reader, pathIndices);
  }

  private int getStackSize(JsonTreeReader reader) throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    return stackSizeField.getInt(reader);
  }

  private int getPathIndex(JsonTreeReader reader) throws Exception {
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    return pathIndices[0];
  }

  private void setLenient(JsonTreeReader reader, boolean lenient) throws Exception {
    Field lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(reader, lenient);
  }

  private void popStack(JsonTreeReader reader) throws Exception {
    Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);
    popStackMethod.invoke(reader);
  }

  private void incrementPathIndex(JsonTreeReader reader) throws Exception {
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    pathIndices[0]++;
  }

  @BeforeEach
  void setUp() throws Exception {
    // Create a minimal JsonPrimitive with a double value to pass to the stack
    JsonPrimitive primitive = new JsonPrimitive(123.456);

    // Construct JsonTreeReader with a dummy JsonElement (using null for simplicity)
    reader = new JsonTreeReader(null);

    // Setup stack, stackSize, pathIndices properly
    setStack(reader, primitive, 1, 0);

    // Spy on the reader to mock peek()
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.NUMBER).when(spyReader).peek();

    // Replace reader with spyReader for tests
    reader = spyReader;

    // Set lenient to false by default
    setLenient(reader, false);
  }

  @Test
    @Timeout(8000)
  void nextDouble_validNumber_returnsDoubleAndUpdatesState() throws Exception {
    // We cannot mock private peekStack(), so we rely on the stack set in setUp()

    double result = reader.nextDouble();
    assertEquals(123.456, result);

    // nextDouble calls popStack() which decreases stackSize by 1, so stackSize should be 0
    int stackSizeAfter = getStackSize(reader);
    assertEquals(0, stackSizeAfter);

    // nextDouble increments pathIndices[stackSize] after popStack(), so pathIndices[0] should be 1
    int pathIndexAfter = getPathIndex(reader);
    assertEquals(1, pathIndexAfter);
  }

  @Test
    @Timeout(8000)
  void nextDouble_invalidToken_throwsIllegalStateException() throws IOException {
    doReturn(JsonToken.BEGIN_ARRAY).when(reader).peek();
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> reader.nextDouble());
    assertTrue(exception.getMessage().contains("Expected NUMBER but was BEGIN_ARRAY"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_nanValue_notLenient_throwsMalformedJsonException() throws Exception {
    // Setup a JsonPrimitive returning NaN
    JsonPrimitive nanPrimitive = mock(JsonPrimitive.class);
    when(nanPrimitive.getAsDouble()).thenReturn(Double.NaN);

    // Setup stack and stackSize to contain nanPrimitive
    setStack(reader, nanPrimitive, 1, 0);

    doReturn(JsonToken.NUMBER).when(reader).peek();

    // Set lenient to false by reflection on superclass JsonReader
    setLenient(reader, false);

    MalformedJsonException ex = assertThrows(MalformedJsonException.class, () -> reader.nextDouble());
    assertTrue(ex.getMessage().contains("NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_infiniteValue_notLenient_throwsMalformedJsonException() throws Exception {
    JsonPrimitive infPrimitive = mock(JsonPrimitive.class);
    when(infPrimitive.getAsDouble()).thenReturn(Double.POSITIVE_INFINITY);

    // Setup stack and stackSize to contain infPrimitive
    setStack(reader, infPrimitive, 1, 0);

    doReturn(JsonToken.NUMBER).when(reader).peek();

    // Set lenient to false by reflection on superclass JsonReader
    setLenient(reader, false);

    MalformedJsonException ex = assertThrows(MalformedJsonException.class, () -> reader.nextDouble());
    assertTrue(ex.getMessage().contains("NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_lenientAllowsNaNAndInfinity_returnsValue() throws Exception {
    JsonPrimitive nanPrimitive = mock(JsonPrimitive.class);
    when(nanPrimitive.getAsDouble()).thenReturn(Double.NaN);

    // Setup stack and stackSize to contain nanPrimitive
    setStack(reader, nanPrimitive, 1, 0);

    doReturn(JsonToken.NUMBER).when(reader).peek();

    // Set lenient to true by reflection on superclass JsonReader
    setLenient(reader, true);

    double result = reader.nextDouble();
    assertTrue(Double.isNaN(result));

    int stackSizeAfter = getStackSize(reader);
    assertEquals(0, stackSizeAfter);
    int pathIndexAfter = getPathIndex(reader);
    assertEquals(1, pathIndexAfter);
  }
}
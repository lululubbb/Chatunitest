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

class JsonTreeReader_243_6Test {

  private JsonTreeReader reader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a minimal JsonPrimitive for testing
    JsonPrimitive primitive = new JsonPrimitive(123.456);

    // Instantiate JsonTreeReader with a dummy JsonElement (null here, we will override stack)
    reader = new JsonTreeReader(null);

    // Use reflection to set private fields

    // Set stack with one JsonPrimitive object
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = primitive;
    stackField.set(reader, stackArray);

    // Set stackSize to 1
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    // Set pathIndices array
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndicesArray = new int[32];
    pathIndicesField.set(reader, pathIndicesArray);

    // Spy on reader to mock peek() method
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.NUMBER).when(spyReader).peek();
    reader = spyReader;

    // Mock isLenient() to false by default
    doReturn(false).when(reader).isLenient();
  }

  @Test
    @Timeout(8000)
  void nextDouble_validNumber_returnsValueAndUpdatesState() throws Exception {
    double result = reader.nextDouble();
    assertEquals(123.456, result, 0.000001);

    // Verify stackSize decreased by 1 (from 1 to 0)
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(reader);
    assertEquals(0, stackSize);

    // Verify pathIndices[stackSize - 1] incremented only if stackSize > 0
    // Since stackSize is 0, no increment expected
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    // pathIndices[0] should be 0
    assertEquals(0, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_validStringNumber_returnsValue() throws Exception {
    // Setup stack with JsonPrimitive string representing a number
    JsonPrimitive primitive = new JsonPrimitive("789.01");
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = primitive;
    stackField.set(reader, stackArray);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    doReturn(JsonToken.STRING).when(reader).peek();

    double result = reader.nextDouble();
    assertEquals(789.01, result, 0.000001);

    int stackSize = stackSizeField.getInt(reader);
    assertEquals(0, stackSize);
  }

  @Test
    @Timeout(8000)
  void nextDouble_invalidToken_throwsIllegalStateException() throws IOException {
    doReturn(JsonToken.BOOLEAN).when(reader).peek();

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> reader.nextDouble());
    assertTrue(ex.getMessage().contains("Expected NUMBER but was BOOLEAN"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_nanOrInfiniteAndNotLenient_throwsMalformedJsonException() throws Exception {
    // Setup stack with JsonPrimitive containing NaN
    JsonPrimitive primitive = mock(JsonPrimitive.class);
    when(primitive.getAsDouble()).thenReturn(Double.NaN);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = primitive;
    stackField.set(reader, stackArray);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    doReturn(JsonToken.NUMBER).when(reader).peek();

    // isLenient returns false (default)

    MalformedJsonException ex = assertThrows(MalformedJsonException.class, () -> reader.nextDouble());
    assertTrue(ex.getMessage().contains("JSON forbids NaN and infinities"));

    // Now test with infinite
    when(primitive.getAsDouble()).thenReturn(Double.POSITIVE_INFINITY);
    stackSizeField.setInt(reader, 1);

    MalformedJsonException ex2 = assertThrows(MalformedJsonException.class, () -> reader.nextDouble());
    assertTrue(ex2.getMessage().contains("JSON forbids NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_nanOrInfiniteButLenient_allowsValue() throws Exception {
    // Setup stack with JsonPrimitive containing NaN
    JsonPrimitive primitive = mock(JsonPrimitive.class);
    when(primitive.getAsDouble()).thenReturn(Double.NaN);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = primitive;
    stackField.set(reader, stackArray);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    doReturn(JsonToken.NUMBER).when(reader).peek();

    // isLenient returns true
    doReturn(true).when(reader).isLenient();

    double result = reader.nextDouble();
    assertTrue(Double.isNaN(result));

    int stackSize = stackSizeField.getInt(reader);
    assertEquals(0, stackSize);
  }

  @Test
    @Timeout(8000)
  void nextDouble_decrementsStackSizeAndIncrementsPathIndicesWhenStackSizeGreaterThanOne() throws Exception {
    // Setup stackSize = 2, pathIndices[1] = 5
    JsonPrimitive primitive = new JsonPrimitive(42.0);
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = primitive;
    stackArray[1] = primitive;
    stackField.set(reader, stackArray);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 2);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndices[1] = 5;
    pathIndicesField.set(reader, pathIndices);

    doReturn(JsonToken.NUMBER).when(reader).peek();

    double result = reader.nextDouble();
    assertEquals(42.0, result);

    int stackSize = stackSizeField.getInt(reader);
    assertEquals(1, stackSize);

    int updatedIndex = ((int[]) pathIndicesField.get(reader))[0];
    // Since stackSize after pop is 1, pathIndices[0]++ should happen
    assertEquals(1, updatedIndex);
  }
}
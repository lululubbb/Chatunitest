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

class JsonTreeReader_243_3Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a dummy JsonPrimitive to push onto the stack
    JsonPrimitive primitive = mock(JsonPrimitive.class);
    when(primitive.getAsDouble()).thenReturn(123.456);

    // Create instance with dummy JsonElement (null since constructor details not provided)
    jsonTreeReader = new JsonTreeReader(null);

    // Use reflection to set stackSize to 1 and stack[0] to primitive
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = (Object[]) stackField.get(jsonTreeReader);
    stackArray[0] = primitive;
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    // Set lenient flag to false by reflection (inherited from JsonReader)
    Field lenientField = JsonTreeReader.class.getSuperclass().getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonTreeReader, false);

    // Set pathIndices array and stackSize for index increment test
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 0;

    // Ensure pathIndices and stackSize are consistent
    stackSizeField.setInt(jsonTreeReader, 1);

    // Spy on jsonTreeReader to mock peek() method
    JsonTreeReader spyReader = spy(jsonTreeReader);
    try {
      doReturn(JsonToken.NUMBER).when(spyReader).peek();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    jsonTreeReader = spyReader;
  }

  @Test
    @Timeout(8000)
  void nextDouble_returnsDoubleWhenTokenNumberAndValidValue() throws Exception {
    double result = jsonTreeReader.nextDouble();
    assertEquals(123.456, result);

    // Check that stackSize is decremented to 0
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(0, stackSize);

    // Check that pathIndices[stackSize - 1] incremented only if stackSize > 0 (here 0, so no increment)
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    assertEquals(0, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_throwsIllegalStateExceptionForWrongToken() throws Exception {
    doReturn(JsonToken.BEGIN_ARRAY).when(jsonTreeReader).peek();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonTreeReader.nextDouble());
    assertTrue(thrown.getMessage().contains("Expected NUMBER but was BEGIN_ARRAY"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_throwsMalformedJsonExceptionForNaNWhenNotLenient() throws Exception {
    JsonPrimitive primitive = mock(JsonPrimitive.class);
    when(primitive.getAsDouble()).thenReturn(Double.NaN);

    // Set stack with NaN primitive and stackSize=1
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = (Object[]) stackField.get(jsonTreeReader);
    stackArray[0] = primitive;

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

    // lenient = false by default from setUp

    MalformedJsonException thrown = assertThrows(MalformedJsonException.class, () -> jsonTreeReader.nextDouble());
    assertTrue(thrown.getMessage().contains("JSON forbids NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_allowsNaNWhenLenient() throws Exception {
    JsonPrimitive primitive = mock(JsonPrimitive.class);
    when(primitive.getAsDouble()).thenReturn(Double.NaN);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = (Object[]) stackField.get(jsonTreeReader);
    stackArray[0] = primitive;

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

    // Set lenient = true
    Field lenientField = JsonTreeReader.class.getSuperclass().getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonTreeReader, true);

    double result = jsonTreeReader.nextDouble();
    assertTrue(Double.isNaN(result));

    // stackSize decremented to 0
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(0, stackSize);
  }

  @Test
    @Timeout(8000)
  void nextDouble_incrementsPathIndicesWhenStackSizeGreaterThanZero() throws Exception {
    JsonPrimitive primitive = mock(JsonPrimitive.class);
    when(primitive.getAsDouble()).thenReturn(42.0);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = (Object[]) stackField.get(jsonTreeReader);
    // Fill stack[0] and stack[1] to avoid NullPointerException
    stackArray[0] = mock(JsonPrimitive.class); // dummy for index 0
    stackArray[1] = primitive;

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 2); // Set stackSize > 1

    doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 0;
    pathIndices[1] = 5;

    double result = jsonTreeReader.nextDouble();
    assertEquals(42.0, result);

    // stackSize decremented to 1
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(1, stackSize);

    // pathIndices[stackSize - 1] incremented (pathIndices[0])
    assertEquals(1, pathIndices[0]);

    // pathIndices[1] remains unchanged
    assertEquals(5, pathIndices[1]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_acceptsStringTokenIfConvertible() throws Exception {
    JsonPrimitive primitive = mock(JsonPrimitive.class);
    when(primitive.getAsDouble()).thenReturn(3.14);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = (Object[]) stackField.get(jsonTreeReader);
    stackArray[0] = primitive;

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    doReturn(JsonToken.STRING).when(jsonTreeReader).peek();

    double result = jsonTreeReader.nextDouble();
    assertEquals(3.14, result);

    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(0, stackSize);
  }
}
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

public class JsonTreeReader_243_5Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonPrimitive with a double value 42.0
    JsonPrimitive primitive = new JsonPrimitive(42.0);

    // Instantiate JsonTreeReader with a dummy JsonElement (null here, as constructor is not shown)
    jsonTreeReader = new JsonTreeReader(null);

    // Set stack to contain the JsonPrimitive
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stack[0] = primitive;
    stackField.set(jsonTreeReader, stack);

    // Set stackSize to 1
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    // Set pathIndices array with one element 0
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndicesField.set(jsonTreeReader, pathIndices);

    // Mock isLenient() to return false by spying the instance
    jsonTreeReader = spy(jsonTreeReader);
    doReturn(false).when(jsonTreeReader).isLenient();
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_NumberToken_ReturnsValueAndUpdatesPathIndices() throws Exception {
    // Mock peek() to return JsonToken.NUMBER
    doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

    // Call nextDouble and verify returned value is 42.0
    double result = jsonTreeReader.nextDouble();
    assertEquals(42.0, result);

    // Verify stackSize is decreased to 0
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(0, stackSize);

    // Verify pathIndices[stackSize - 1] incremented if stackSize > 0 (here stackSize=0, so no increment)
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    // Since stackSize is 0, no increment expected, pathIndices[0] remains 0
    assertEquals(0, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_StringToken_ReturnsValue() throws Exception {
    // Replace stack element with a JsonPrimitive holding a double as string "3.14"
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = new JsonPrimitive("3.14");
    stackField.set(jsonTreeReader, stack);

    // Set stackSize back to 1
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    // Mock peek() to return JsonToken.STRING
    doReturn(JsonToken.STRING).when(jsonTreeReader).peek();

    // Call nextDouble and verify returned value is 3.14
    double result = jsonTreeReader.nextDouble();
    assertEquals(3.14, result);

    // Verify stackSize is decreased to 0
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(0, stackSize);
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_IllegalStateExceptionOnUnexpectedToken() throws Exception {
    // Mock peek() to return JsonToken.BEGIN_OBJECT (not NUMBER or STRING)
    doReturn(JsonToken.BEGIN_OBJECT).when(jsonTreeReader).peek();

    // Use reflection to get locationString() private method
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String locationString = (String) locationStringMethod.invoke(jsonTreeReader);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonTreeReader.nextDouble();
    });
    assertTrue(thrown.getMessage().contains("Expected " + JsonToken.NUMBER + " but was " + JsonToken.BEGIN_OBJECT));
    assertTrue(thrown.getMessage().endsWith(locationString));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_MalformedJsonExceptionOnNaN() throws Exception {
    // Replace stack element with JsonPrimitive holding NaN
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = new JsonPrimitive(Double.NaN);
    stackField.set(jsonTreeReader, stack);

    // Set stackSize to 1
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    // Mock peek() to return JsonToken.NUMBER
    doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

    // Mock isLenient() to return false to trigger exception
    doReturn(false).when(jsonTreeReader).isLenient();

    MalformedJsonException thrown = assertThrows(MalformedJsonException.class, () -> {
      jsonTreeReader.nextDouble();
    });
    assertTrue(thrown.getMessage().contains("JSON forbids NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_AllowsNaNWhenLenient() throws Exception {
    // Replace stack element with JsonPrimitive holding NaN
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = new JsonPrimitive(Double.NaN);
    stackField.set(jsonTreeReader, stack);

    // Set stackSize to 1
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    // Mock peek() to return JsonToken.NUMBER
    doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

    // Mock isLenient() to return true to allow NaN
    doReturn(true).when(jsonTreeReader).isLenient();

    double result = jsonTreeReader.nextDouble();
    assertTrue(Double.isNaN(result));

    // Verify stackSize decreased to 0
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(0, stackSize);
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_PathIndicesIncrementWhenStackSizeGreaterThanZero() throws Exception {
    // Setup stackSize to 2 and pathIndices[1] = 5
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 2);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndices[1] = 5;
    pathIndicesField.set(jsonTreeReader, pathIndices);

    // Setup stack with two JsonPrimitives, top one with 10.0
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stack[0] = new JsonPrimitive(1.0);
    stack[1] = new JsonPrimitive(10.0);
    stackField.set(jsonTreeReader, stack);

    // Mock peek() to return JsonToken.NUMBER
    doReturn(JsonToken.NUMBER).when(jsonTreeReader).peek();

    // Call nextDouble, should pop top and increment pathIndices[1]
    double result = jsonTreeReader.nextDouble();
    assertEquals(10.0, result);

    // Verify stackSize now 1
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(1, stackSize);

    // Verify pathIndices[1] incremented from 5 to 6
    int[] updatedPathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    assertEquals(6, updatedPathIndices[1]);
  }
}
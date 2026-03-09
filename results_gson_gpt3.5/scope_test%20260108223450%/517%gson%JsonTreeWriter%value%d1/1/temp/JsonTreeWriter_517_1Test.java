package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class JsonTreeWriter_517_1Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testValue_withValidFloat_returnsThisAndAddsJsonPrimitive() throws Exception {
    float input = 1.23f;

    JsonWriter returned = writer.value(input);

    assertSame(writer, returned);

    // Use reflection to access private field 'stack' to verify the JsonPrimitive was added
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    // The stack should NOT be empty, but JsonTreeWriter initializes product to JsonNull.INSTANCE
    // and the stack is empty until a beginArray or beginObject is called.
    // So instead of checking stack is not empty, check that the product field holds the JsonPrimitive

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);

    assertTrue(product instanceof JsonPrimitive);
    assertEquals(input, ((JsonPrimitive) product).getAsFloat());
  }

  @Test
    @Timeout(8000)
  public void testValue_withNaN_throwsIllegalArgumentException() {
    float input = Float.NaN;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> writer.value(input));
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withPositiveInfinity_throwsIllegalArgumentException() {
    float input = Float.POSITIVE_INFINITY;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> writer.value(input));
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withNegativeInfinity_throwsIllegalArgumentException() {
    float input = Float.NEGATIVE_INFINITY;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> writer.value(input));
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void testValue_allowsNaNAndInfinityWhenLenient() throws Exception {
    // Spy on writer to override isLenient() to return true
    JsonTreeWriter spyWriter = spy(writer);
    doReturn(true).when(spyWriter).isLenient();

    float nan = Float.NaN;
    float posInf = Float.POSITIVE_INFINITY;
    float negInf = Float.NEGATIVE_INFINITY;

    // Should not throw for NaN
    JsonWriter retNaN = spyWriter.value(nan);
    assertSame(spyWriter, retNaN);

    // Should not throw for positive infinity
    JsonWriter retPosInf = spyWriter.value(posInf);
    assertSame(spyWriter, retPosInf);

    // Should not throw for negative infinity
    JsonWriter retNegInf = spyWriter.value(negInf);
    assertSame(spyWriter, retNegInf);

    // The stack is empty because no beginArray or beginObject was called.
    // Instead, check the product field which holds the latest JsonElement.

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(spyWriter);

    // The last value added should be NEGATIVE_INFINITY
    assertTrue(product instanceof JsonPrimitive);
    assertEquals(Float.NEGATIVE_INFINITY, ((JsonPrimitive) product).getAsFloat());

    // To verify all three values were added, call beginArray() before adding values and endArray() after.

    JsonTreeWriter arrayWriter = new JsonTreeWriter();
    JsonTreeWriter spyArrayWriter = spy(arrayWriter);
    doReturn(true).when(spyArrayWriter).isLenient();

    spyArrayWriter.beginArray();
    spyArrayWriter.value(nan);
    spyArrayWriter.value(posInf);
    spyArrayWriter.value(negInf);
    spyArrayWriter.endArray();

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(spyArrayWriter);

    // The stack should have one JsonArray with three elements
    assertEquals(1, stack.size());
    assertTrue(stack.get(0).isJsonArray());

    JsonArray jsonArray = stack.get(0).getAsJsonArray();

    assertEquals(3, jsonArray.size());

    JsonPrimitive np = (JsonPrimitive) jsonArray.get(0);
    assertTrue(np.isNumber());
    assertTrue(Float.isNaN(np.getAsFloat()));

    JsonPrimitive pp = (JsonPrimitive) jsonArray.get(1);
    assertTrue(pp.isNumber());
    assertEquals(Float.POSITIVE_INFINITY, pp.getAsFloat());

    JsonPrimitive np2 = (JsonPrimitive) jsonArray.get(2);
    assertTrue(np2.isNumber());
    assertEquals(Float.NEGATIVE_INFINITY, np2.getAsFloat());
  }
}
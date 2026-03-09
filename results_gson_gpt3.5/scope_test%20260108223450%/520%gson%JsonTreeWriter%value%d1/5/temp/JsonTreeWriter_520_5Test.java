package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;

class JsonTreeWriter_520_5Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void testValue_Number_Null_CallsNullValue() throws Exception {
    JsonTreeWriter spyWriter = spy(jsonTreeWriter);

    // When value is null, value(Number) should call nullValue()
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.value((Number) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  void testValue_Number_NonLenient_ThrowsForNaN() {
    JsonTreeWriter writer = jsonTreeWriter; // use original instance

    // Use reflection to set lenient field or override method isLenient via spy
    JsonTreeWriter spyWriter = spy(writer);
    doReturn(false).when(spyWriter).isLenient();

    Number nanValue = Double.NaN;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> spyWriter.value(nanValue));
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  void testValue_Number_NonLenient_ThrowsForInfinity() {
    JsonTreeWriter writer = jsonTreeWriter; // use original instance

    JsonTreeWriter spyWriter = spy(writer);
    doReturn(false).when(spyWriter).isLenient();

    Number infValue = Double.POSITIVE_INFINITY;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> spyWriter.value(infValue));
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  void testValue_Number_Lenient_AllowsNaNAndInfinity() throws IOException, Exception {
    JsonTreeWriter writer = jsonTreeWriter;

    JsonTreeWriter spyWriter = spy(writer);
    doReturn(true).when(spyWriter).isLenient();

    Number nanValue = Double.NaN;
    Number infValue = Double.POSITIVE_INFINITY;
    Number normalValue = 42;

    // Use reflection to spy on private 'put' method call count by spying on spyWriter and using spyWriter.value()

    // Instead of verify(spyWriter).put(...) which is private, verify calls indirectly by checking product field changes

    // For NaN
    spyWriter.value(nanValue);
    assertProductIsJsonPrimitiveWithValue(spyWriter, nanValue);

    // For Infinity
    spyWriter.value(infValue);
    assertProductIsJsonPrimitiveWithValue(spyWriter, infValue);

    // For normal number
    spyWriter.value(normalValue);
    assertProductIsJsonPrimitiveWithValue(spyWriter, normalValue);
  }

  @Test
    @Timeout(8000)
  void testValue_Number_NormalValue_PutsJsonPrimitive() throws Exception {
    Number number = 123;

    JsonTreeWriter writer = jsonTreeWriter;

    JsonTreeWriter spyWriter = spy(writer);
    doReturn(true).when(spyWriter).isLenient();

    spyWriter.value(number);

    // Access private field 'product' to verify it is JsonPrimitive with correct value
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object product = productField.get(spyWriter);
    assertTrue(product instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) product;
    assertEquals(number, primitive.getAsNumber());
  }

  private void assertProductIsJsonPrimitiveWithValue(JsonTreeWriter writer, Number expected) throws Exception {
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object product = productField.get(writer);
    assertTrue(product instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) product;
    if (expected == null) {
      assertEquals(null, primitive.getAsNumber());
    } else {
      assertEquals(expected.doubleValue(), primitive.getAsNumber().doubleValue(), 0.000001);
    }
  }
}
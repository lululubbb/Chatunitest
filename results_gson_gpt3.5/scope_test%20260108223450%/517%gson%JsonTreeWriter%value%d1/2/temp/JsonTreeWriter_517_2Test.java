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
import java.lang.reflect.Method;

public class JsonTreeWriter_517_2Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testValue_withValidFloat_returnsThisAndAddsJsonPrimitive() throws Exception {
    // Arrange
    float testValue = 1.23f;

    // Act
    JsonWriter returned = writer.value(testValue);

    // Assert
    assertSame(writer, returned);

    // Use reflection to access private field 'product' to verify correct value
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object product = productField.get(writer);
    assertTrue(product instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) product;
    assertEquals(testValue, primitive.getAsFloat());
  }

  @Test
    @Timeout(8000)
  public void testValue_withNaN_throwsIllegalArgumentException() {
    float nanValue = Float.NaN;

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> writer.value(nanValue));
    assertEquals("JSON forbids NaN and infinities: " + nanValue, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testValue_withPositiveInfinity_throwsIllegalArgumentException() {
    float infValue = Float.POSITIVE_INFINITY;

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> writer.value(infValue));
    assertEquals("JSON forbids NaN and infinities: " + infValue, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testValue_withNegativeInfinity_throwsIllegalArgumentException() {
    float negInfValue = Float.NEGATIVE_INFINITY;

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> writer.value(negInfValue));
    assertEquals("JSON forbids NaN and infinities: " + negInfValue, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testValue_whenLenientAllowsNaNAndInfinity() throws Exception {
    // Use reflection to set lenient to true by invoking the protected method setLenient(true)
    Method setLenientMethod = JsonWriter.class.getDeclaredMethod("setLenient", boolean.class);
    setLenientMethod.setAccessible(true);
    setLenientMethod.invoke(writer, true);

    // NaN should be allowed now
    JsonWriter returnedNaN = writer.value(Float.NaN);
    assertSame(writer, returnedNaN);
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object productNaN = productField.get(writer);
    assertTrue(productNaN instanceof JsonPrimitive);
    JsonPrimitive primitiveNaN = (JsonPrimitive) productNaN;
    assertEquals(Float.NaN, primitiveNaN.getAsFloat());

    // Infinity should be allowed now
    JsonWriter returnedInf = writer.value(Float.POSITIVE_INFINITY);
    assertSame(writer, returnedInf);
    Object productInf = productField.get(writer);
    assertTrue(productInf instanceof JsonPrimitive);
    JsonPrimitive primitiveInf = (JsonPrimitive) productInf;
    assertEquals(Float.POSITIVE_INFINITY, primitiveInf.getAsFloat());
  }
}
package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_518_6Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testValue_withValidDouble_returnsThisAndAddsJsonPrimitive() throws Exception {
    double validValue = 123.456;

    JsonWriter returned = writer.value(validValue);
    assertSame(writer, returned);

    // Use reflection to access private field 'product'
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object product = productField.get(writer);

    assertTrue(product instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) product;
    assertEquals(validValue, primitive.getAsDouble(), 0.0);
  }

  @Test
    @Timeout(8000)
  public void testValue_withNaN_throwsIllegalArgumentException() {
    double nanValue = Double.NaN;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(nanValue);
    });
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withPositiveInfinity_throwsIllegalArgumentException() {
    double infValue = Double.POSITIVE_INFINITY;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(infValue);
    });
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withNegativeInfinity_throwsIllegalArgumentException() {
    double negInfValue = Double.NEGATIVE_INFINITY;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(negInfValue);
    });
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void testValue_allowsNaNIfLenient() throws Exception {
    // Use reflection to set lenient to true
    Method setLenientMethod = JsonWriter.class.getDeclaredMethod("setLenient", boolean.class);
    setLenientMethod.setAccessible(true);
    setLenientMethod.invoke(writer, true);

    double nanValue = Double.NaN;

    JsonWriter returned = writer.value(nanValue);
    assertSame(writer, returned);

    // Use reflection to access private field 'product'
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object product = productField.get(writer);

    assertTrue(product instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) product;
    assertTrue(Double.isNaN(primitive.getAsDouble()));
  }
}
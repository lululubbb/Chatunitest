package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeWriter_517_6Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testValue_withValidFloat() throws Exception {
    float testValue = 1.5f;

    JsonWriter returned = writer.value(testValue);
    assertSame(writer, returned);

    // Use reflection to access private field 'product'
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
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(nanValue);
    });
    assertTrue(thrown.getMessage().contains("JSON forbids NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withPositiveInfinity_throwsIllegalArgumentException() {
    float infValue = Float.POSITIVE_INFINITY;
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(infValue);
    });
    assertTrue(thrown.getMessage().contains("JSON forbids NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withNegativeInfinity_throwsIllegalArgumentException() {
    float negInfValue = Float.NEGATIVE_INFINITY;
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(negInfValue);
    });
    assertTrue(thrown.getMessage().contains("JSON forbids NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  public void testValue_allowsNaN_ifLenient() throws Exception {
    // Use reflection to set lenient to true
    Method setLenient = JsonWriter.class.getDeclaredMethod("setLenient", boolean.class);
    setLenient.setAccessible(true);
    setLenient.invoke(writer, true);

    float nanValue = Float.NaN;

    JsonWriter returned = writer.value(nanValue);
    assertSame(writer, returned);

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object product = productField.get(writer);
    assertTrue(product instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) product;
    assertTrue(Float.isNaN(primitive.getAsFloat()));
  }
}
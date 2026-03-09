package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_518_4Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testValue_withValidDouble_returnsThis() throws IOException {
    JsonWriter returned = jsonTreeWriter.value(123.456);
    assertSame(jsonTreeWriter, returned);
  }

  @Test
    @Timeout(8000)
  public void testValue_withNaN_throwsIllegalArgumentException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(Double.NaN);
    });
    assertTrue(exception.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withPositiveInfinity_throwsIllegalArgumentException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(Double.POSITIVE_INFINITY);
    });
    assertTrue(exception.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withNegativeInfinity_throwsIllegalArgumentException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(Double.NEGATIVE_INFINITY);
    });
    assertTrue(exception.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void testValue_allowsNaNWhenLenient() throws Exception {
    // Use reflection to set lenient to true
    Method setLenientMethod = JsonWriter.class.getDeclaredMethod("setLenient", boolean.class);
    setLenientMethod.setAccessible(true);
    setLenientMethod.invoke(jsonTreeWriter, true);

    // Should not throw with NaN now
    JsonWriter returned = jsonTreeWriter.value(Double.NaN);
    assertSame(jsonTreeWriter, returned);
  }

  @Test
    @Timeout(8000)
  public void testPutCalledWithCorrectJsonPrimitive() throws Exception {
    double testValue = 42.0;
    jsonTreeWriter.value(testValue);

    // Use reflection to access the private 'product' field
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);

    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);

    assertNotNull(product);
    assertTrue(product instanceof JsonPrimitive);
    assertEquals(testValue, ((JsonPrimitive) product).getAsDouble());
  }
}
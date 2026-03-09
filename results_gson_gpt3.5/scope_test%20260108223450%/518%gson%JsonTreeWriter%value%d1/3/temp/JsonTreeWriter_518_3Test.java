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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonTreeWriter_518_3Test {

  private JsonTreeWriter writer;

  @BeforeEach
  void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void value_double_NormalValue_PutsJsonPrimitiveAndReturnsThis() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    double val = 123.456;

    // Use reflection to spy on private put method
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", com.google.gson.JsonElement.class);
    putMethod.setAccessible(true);

    // Because put is private and void, we can't verify directly; instead, we invoke value and then get the product field to check
    JsonWriter returned = writer.value(val);
    assertSame(writer, returned);

    // Use reflection to get product field value
    Method getMethod = JsonTreeWriter.class.getDeclaredMethod("get");
    getMethod.setAccessible(true);
    Object product = getMethod.invoke(writer);
    assertNotNull(product);
    assertTrue(product instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) product;
    assertEquals(val, primitive.getAsDouble(), 0d);
  }

  @Test
    @Timeout(8000)
  void value_double_NaN_ThrowsIllegalArgumentException() {
    double val = Double.NaN;

    // By default isLenient() returns false, so should throw
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> writer.value(val));
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  void value_double_Infinite_ThrowsIllegalArgumentException() {
    double val = Double.POSITIVE_INFINITY;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> writer.value(val));
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  void value_double_LenientAllowsNaNAndInfinity() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Use reflection to set lenient to true
    Method isLenientMethod = JsonWriter.class.getDeclaredMethod("isLenient");
    isLenientMethod.setAccessible(true);

    // We cannot set isLenient directly, so use reflection to override it by subclassing or use spy
    JsonTreeWriter spyWriter = spy(writer);
    when(spyWriter.isLenient()).thenReturn(true);

    double nan = Double.NaN;
    double inf = Double.NEGATIVE_INFINITY;

    JsonWriter retNan = spyWriter.value(nan);
    assertSame(spyWriter, retNan);

    JsonWriter retInf = spyWriter.value(inf);
    assertSame(spyWriter, retInf);
  }
}
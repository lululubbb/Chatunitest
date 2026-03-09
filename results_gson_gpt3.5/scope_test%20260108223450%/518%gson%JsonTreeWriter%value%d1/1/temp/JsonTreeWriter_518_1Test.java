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

import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeWriter_518_1Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void value_validDouble_returnsThis() throws IOException {
    JsonWriter result = writer.value(123.456);
    assertSame(writer, result);

    // Use reflection to access private 'get' method to verify internal state
    try {
      Method getMethod = JsonTreeWriter.class.getDeclaredMethod("get");
      getMethod.setAccessible(true);
      Object getResult = getMethod.invoke(writer);
      assertTrue(getResult instanceof JsonPrimitive);
      JsonPrimitive primitive = (JsonPrimitive) getResult;
      assertEquals(123.456, primitive.getAsDouble(), 0.000001);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void value_NaN_throwsIllegalArgumentException() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(Double.NaN);
    });
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  public void value_InfinitePositive_throwsIllegalArgumentException() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(Double.POSITIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void value_InfiniteNegative_throwsIllegalArgumentException() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(Double.NEGATIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void value_LenientAllowsNaNAndInfinite() throws Exception {
    // Use reflection to set lenient to true on the superclass JsonWriter
    Method setLenientMethod = JsonWriter.class.getDeclaredMethod("setLenient", boolean.class);
    setLenientMethod.setAccessible(true);
    setLenientMethod.invoke(writer, true);

    JsonWriter resultNaN = writer.value(Double.NaN);
    assertSame(writer, resultNaN);
    JsonWriter resultPosInf = writer.value(Double.POSITIVE_INFINITY);
    assertSame(writer, resultPosInf);
    JsonWriter resultNegInf = writer.value(Double.NEGATIVE_INFINITY);
    assertSame(writer, resultNegInf);
  }
}
package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.lang.reflect.Field;

public class JsonPrimitive_524_4Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withInteger() throws Exception {
    Integer input = 42;
    JsonPrimitive primitive = new JsonPrimitive(input);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(primitive);

    assertEquals(input, value);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withDouble() throws Exception {
    Double input = 3.14;
    JsonPrimitive primitive = new JsonPrimitive(input);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(primitive);

    assertEquals(input, value);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withLong() throws Exception {
    Long input = 123456789L;
    JsonPrimitive primitive = new JsonPrimitive(input);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(primitive);

    assertEquals(input, value);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_nullThrowsException() {
    assertThrows(NullPointerException.class, () -> new JsonPrimitive((Number) null));
  }
}
package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.math.BigInteger;

public class JsonPrimitive_536_6Test {

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_withBigIntegerValue() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("0"); // dummy init
    // Use reflection to set private final field 'value' to a BigInteger
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    BigInteger expected = new BigInteger("12345678901234567890");
    valueField.set(primitive, expected);

    BigInteger actual = primitive.getAsBigInteger();
    assertSame(expected, actual);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_withStringValue() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("0"); // dummy init
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    String strValue = "98765432109876543210";
    valueField.set(primitive, strValue);

    BigInteger actual = primitive.getAsBigInteger();
    BigInteger expected = new BigInteger(strValue);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_withLazilyParsedNumberValue() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("0"); // dummy init
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    LazilyParsedNumber lpn = new LazilyParsedNumber("11223344556677889900");
    valueField.set(primitive, lpn);

    BigInteger actual = primitive.getAsBigInteger();
    BigInteger expected = new BigInteger(lpn.toString());
    assertEquals(expected, actual);
  }
}
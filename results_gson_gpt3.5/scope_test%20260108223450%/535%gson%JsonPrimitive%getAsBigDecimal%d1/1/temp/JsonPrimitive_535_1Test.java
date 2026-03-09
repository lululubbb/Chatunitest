package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_535_1Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no-op
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_whenValueIsBigDecimal() throws Exception {
    BigDecimal bigDecimal = new BigDecimal("123.456");
    jsonPrimitive = new JsonPrimitive(bigDecimal);
    BigDecimal result = jsonPrimitive.getAsBigDecimal();
    assertSame(bigDecimal, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_whenValueIsStringRepresentingNumber() throws Exception {
    String numericString = "789.0123";
    jsonPrimitive = new JsonPrimitive(numericString);
    BigDecimal result = jsonPrimitive.getAsBigDecimal();
    assertEquals(new BigDecimal(numericString), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_whenValueIsNumberOtherThanBigDecimal() throws Exception {
    Number number = 42;
    jsonPrimitive = new JsonPrimitive(number);
    BigDecimal result = jsonPrimitive.getAsBigDecimal();
    assertEquals(new BigDecimal(jsonPrimitive.getAsString()), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_reflectionWithPrivateValueBigDecimal() throws Exception {
    jsonPrimitive = new JsonPrimitive("dummy");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    BigDecimal bigDecimal = new BigDecimal("456.789");
    valueField.set(jsonPrimitive, bigDecimal);
    BigDecimal result = jsonPrimitive.getAsBigDecimal();
    assertSame(bigDecimal, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_reflectionWithPrivateValueString() throws Exception {
    jsonPrimitive = new JsonPrimitive("dummy");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    String numberString = "321.654";
    valueField.set(jsonPrimitive, numberString);
    BigDecimal result = jsonPrimitive.getAsBigDecimal();
    assertEquals(new BigDecimal(numberString), result);
  }
}
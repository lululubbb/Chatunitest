package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class JsonPrimitive_535_3Test {

  @Test
    @Timeout(8000)
  void getAsBigDecimal_whenValueIsBigDecimal_returnsValueDirectly() {
    BigDecimal bd = new BigDecimal("123.45");
    JsonPrimitive jsonPrimitive = new JsonPrimitive(bd);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertSame(bd, result);
  }

  @Test
    @Timeout(8000)
  void getAsBigDecimal_whenValueIsString_returnsBigDecimalFromString() {
    String numberString = "6789.01";
    JsonPrimitive jsonPrimitive = new JsonPrimitive(numberString);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(new BigDecimal(numberString), result);
  }

  @Test
    @Timeout(8000)
  void getAsBigDecimal_whenValueIsLazilyParsedNumber_returnsBigDecimalFromString() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("9876.54");
    JsonPrimitive jsonPrimitive = new JsonPrimitive(lpn);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(new BigDecimal("9876.54"), result);
  }
}
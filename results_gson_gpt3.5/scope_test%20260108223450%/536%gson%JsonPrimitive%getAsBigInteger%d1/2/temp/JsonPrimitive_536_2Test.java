package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_536_2Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no default constructor, will instantiate in tests via reflection or constructors
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_withBigIntegerValue() throws Exception {
    BigInteger bigInt = new BigInteger("12345678901234567890");
    jsonPrimitive = new JsonPrimitive(bigInt);

    BigInteger result = jsonPrimitive.getAsBigInteger();

    assertSame(bigInt, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_withStringValue() throws Exception {
    String numberStr = "98765432109876543210";
    jsonPrimitive = new JsonPrimitive(numberStr);

    BigInteger result = jsonPrimitive.getAsBigInteger();

    assertEquals(new BigInteger(numberStr), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_withNumberValue() throws Exception {
    Number number = 42;
    jsonPrimitive = new JsonPrimitive(number);

    BigInteger result = jsonPrimitive.getAsBigInteger();

    assertEquals(new BigInteger(jsonPrimitive.getAsString()), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_withLazilyParsedNumberValue() throws Exception {
    // LazilyParsedNumber is internal, create instance via constructor
    Class<?> lazilyParsedNumberClass = Class.forName("com.google.gson.internal.LazilyParsedNumber");
    Object lazilyParsedNumber = lazilyParsedNumberClass.getConstructor(String.class).newInstance("1234567890");

    jsonPrimitive = new JsonPrimitive((Number) lazilyParsedNumber);

    BigInteger result = jsonPrimitive.getAsBigInteger();

    assertEquals(new BigInteger(jsonPrimitive.getAsString()), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_withInvalidString_throwsNumberFormatException() throws Exception {
    String invalidNumber = "notANumber";
    jsonPrimitive = new JsonPrimitive(invalidNumber);

    assertThrows(NumberFormatException.class, () -> {
      jsonPrimitive.getAsBigInteger();
    });
  }

  @Test
    @Timeout(8000)
  public void testPrivateFieldValue_setAndGet() throws Exception {
    jsonPrimitive = new JsonPrimitive("1");

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);

    assertEquals("1", value);
  }

  @Test
    @Timeout(8000)
  public void testPrivateIsIntegralMethod() throws Exception {
    jsonPrimitive = new JsonPrimitive(123);

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    boolean result = (boolean) isIntegralMethod.invoke(null, jsonPrimitive);

    assertTrue(result);
  }
}
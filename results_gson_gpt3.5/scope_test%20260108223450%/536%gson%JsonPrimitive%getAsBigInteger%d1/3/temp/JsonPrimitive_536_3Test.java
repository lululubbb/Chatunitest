package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonPrimitive_536_3Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  void setUp() throws Exception {
    // Instead of Proxy (which requires interface), directly instantiate JsonPrimitive without constructor
    jsonPrimitive = instantiateJsonPrimitive();
  }

  private JsonPrimitive instantiateJsonPrimitive() throws Exception {
    // Use Unsafe to instantiate without calling constructor
    java.lang.reflect.Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
    unsafeField.setAccessible(true);
    sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
    return (JsonPrimitive) unsafe.allocateInstance(JsonPrimitive.class);
  }

  private void setValue(Object val) throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, val);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_whenValueIsBigInteger() throws Exception {
    BigInteger bigInt = new BigInteger("12345678901234567890");
    setValue(bigInt);

    BigInteger result = jsonPrimitive.getAsBigInteger();

    assertSame(bigInt, result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_whenValueIsStringRepresentingBigInteger() throws Exception {
    String bigIntStr = "98765432109876543210";
    setValue(bigIntStr);

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(bigIntStr).when(spyPrimitive).getAsString();

    BigInteger result = spyPrimitive.getAsBigInteger();

    assertEquals(new BigInteger(bigIntStr), result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_whenValueIsLazilyParsedNumber() throws Exception {
    // LazilyParsedNumber is from com.google.gson.internal, simulate with string value
    String numberStr = "123456789012345678901234567890";
    Object lazilyParsedNumber = new com.google.gson.internal.LazilyParsedNumber(numberStr);
    setValue(lazilyParsedNumber);

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(numberStr).when(spyPrimitive).getAsString();

    BigInteger result = spyPrimitive.getAsBigInteger();

    assertEquals(new BigInteger(numberStr), result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_whenValueIsOtherNumber() throws Exception {
    // Use Integer to simulate a Number that is not BigInteger
    Integer intValue = 42;
    setValue(intValue);

    String intStr = "42";
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(intStr).when(spyPrimitive).getAsString();

    BigInteger result = spyPrimitive.getAsBigInteger();

    assertEquals(BigInteger.valueOf(42), result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_whenValueIsNull_shouldThrowException() throws Exception {
    setValue(null);

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    // Remove stub on getAsString() to allow NullPointerException from getAsString()
    // because getAsString() uses value internally and will throw NPE if value is null

    assertThrows(NullPointerException.class, spyPrimitive::getAsBigInteger);
  }

}
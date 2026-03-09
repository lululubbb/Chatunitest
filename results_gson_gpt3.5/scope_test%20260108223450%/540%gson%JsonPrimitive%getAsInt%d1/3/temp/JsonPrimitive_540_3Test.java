package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_540_3Test {

  JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setup() {
    // no-op, will instantiate in each test
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_whenNumber() {
    Number number = 42;
    jsonPrimitive = new JsonPrimitive(number);
    assertTrue(jsonPrimitive.isNumber());
    assertEquals(42, jsonPrimitive.getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_whenNumber_LazilyParsedNumber() {
    Number number = new com.google.gson.internal.LazilyParsedNumber("123");
    jsonPrimitive = new JsonPrimitive(number);
    assertTrue(jsonPrimitive.isNumber());
    assertEquals(123, jsonPrimitive.getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_whenString() {
    jsonPrimitive = new JsonPrimitive("456");
    assertFalse(jsonPrimitive.isNumber());
    assertEquals(456, jsonPrimitive.getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_whenStringNegative() {
    jsonPrimitive = new JsonPrimitive("-789");
    assertFalse(jsonPrimitive.isNumber());
    assertEquals(-789, jsonPrimitive.getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_whenStringInvalidNumber_throwsNumberFormatException() {
    jsonPrimitive = new JsonPrimitive("notANumber");
    assertFalse(jsonPrimitive.isNumber());
    assertThrows(NumberFormatException.class, () -> jsonPrimitive.getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_reflectiveInvoke() throws Exception {
    jsonPrimitive = new JsonPrimitive("101");
    Method method = JsonPrimitive.class.getDeclaredMethod("getAsInt");
    method.setAccessible(true);
    Object result = method.invoke(jsonPrimitive);
    assertEquals(101, result);
  }

}
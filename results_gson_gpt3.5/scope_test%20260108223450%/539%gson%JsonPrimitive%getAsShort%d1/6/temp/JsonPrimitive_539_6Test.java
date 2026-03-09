package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_539_6Test {

  private JsonPrimitive numberPrimitive;
  private JsonPrimitive stringPrimitive;

  @BeforeEach
  void setUp() {
    numberPrimitive = new JsonPrimitive(Short.valueOf((short)123));
    stringPrimitive = new JsonPrimitive("456");
  }

  @Test
    @Timeout(8000)
  void testGetAsShort_withNumber() {
    short result = numberPrimitive.getAsShort();
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  void testGetAsShort_withString() {
    short result = stringPrimitive.getAsShort();
    assertEquals(456, result);
  }

  @Test
    @Timeout(8000)
  void testGetAsShort_withString_invalidNumber_throwsNumberFormatException() throws Exception {
    JsonPrimitive invalidStringPrimitive = new JsonPrimitive("notANumber");
    Method getAsShort = JsonPrimitive.class.getDeclaredMethod("getAsShort");
    getAsShort.setAccessible(true);
    NumberFormatException thrown = assertThrows(NumberFormatException.class, () -> {
      try {
        getAsShort.invoke(invalidStringPrimitive);
      } catch (InvocationTargetException e) {
        // unwrap the actual cause
        throw e.getCause();
      }
    });
    // optionally assert message or other properties of the exception
  }
}
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

public class JsonPrimitive_539_1Test {

  private JsonPrimitive numberPrimitive;
  private JsonPrimitive stringPrimitive;

  @BeforeEach
  public void setUp() throws Exception {
    numberPrimitive = new JsonPrimitive(Short.valueOf((short)123));
    stringPrimitive = new JsonPrimitive("456");
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenNumber_returnsShortValue() {
    short result = numberPrimitive.getAsShort();
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenString_returnsParsedShort() {
    short result = stringPrimitive.getAsShort();
    assertEquals(456, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenStringInvalid_throwsNumberFormatException() throws Exception {
    JsonPrimitive invalidStringPrimitive = new JsonPrimitive("notANumber");
    Method getAsShortMethod = JsonPrimitive.class.getDeclaredMethod("getAsShort");
    getAsShortMethod.setAccessible(true);

    assertThrows(NumberFormatException.class, () -> {
      try {
        getAsShortMethod.invoke(invalidStringPrimitive);
      } catch (InvocationTargetException e) {
        // unwrap the underlying exception thrown by the invoked method
        throw e.getCause();
      }
    });
  }
}
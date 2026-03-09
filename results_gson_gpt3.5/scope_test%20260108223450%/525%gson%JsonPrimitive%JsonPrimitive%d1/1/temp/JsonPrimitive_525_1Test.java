package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_525_1Test {

  private Constructor<JsonPrimitive> stringConstructor;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    stringConstructor = JsonPrimitive.class.getDeclaredConstructor(String.class);
    stringConstructor.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withNonNullString_shouldSetValue() throws Exception {
    String testString = "testValue";
    JsonPrimitive jsonPrimitive = stringConstructor.newInstance(testString);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);

    assertEquals(testString, value);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withNullString_shouldThrowNullPointerException() {
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      stringConstructor.newInstance((String) null);
    });
    assertTrue(thrown.getCause() instanceof NullPointerException);
  }
}
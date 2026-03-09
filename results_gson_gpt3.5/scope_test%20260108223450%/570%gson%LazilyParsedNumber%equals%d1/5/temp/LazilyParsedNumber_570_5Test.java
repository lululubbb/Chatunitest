package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class LazilyParsedNumber_570_5Test {

  @Test
    @Timeout(8000)
  void testEquals_sameInstance() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    assertTrue(number.equals(number));
  }

  @Test
    @Timeout(8000)
  void testEquals_null() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    assertFalse(number.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentClass() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    assertFalse(number.equals("123"));
  }

  @Test
    @Timeout(8000)
  void testEquals_equalValueObjects() {
    LazilyParsedNumber number1 = new LazilyParsedNumber("123");
    LazilyParsedNumber number2 = new LazilyParsedNumber("123");
    assertTrue(number1.equals(number2));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentValueObjects() {
    LazilyParsedNumber number1 = new LazilyParsedNumber("123");
    LazilyParsedNumber number2 = new LazilyParsedNumber("456");
    assertFalse(number1.equals(number2));
  }

  @Test
    @Timeout(8000)
  void testEquals_valueFieldSameReference() throws Exception {
    LazilyParsedNumber number1 = Mockito.spy(new LazilyParsedNumber("123"));
    LazilyParsedNumber number2 = Mockito.spy(new LazilyParsedNumber("123"));

    // Use reflection to set the private final 'value' field to same String instance
    Field field = LazilyParsedNumber.class.getDeclaredField("value");
    field.setAccessible(true);
    String val = "sameValue";

    // Remove final modifier via reflection (Java 12+ may require additional handling)
    try {
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
    } catch (NoSuchFieldException ignored) {
      // Java 12+ does not have 'modifiers' field, ignore
    }

    field.set(number1, val);
    field.set(number2, val);

    // Instead of using spy, create real instances to avoid Mockito proxy interfering with equals
    LazilyParsedNumber realNumber1 = new LazilyParsedNumber(val);
    LazilyParsedNumber realNumber2 = new LazilyParsedNumber(val);

    // Set the private final 'value' field to the same String instance on these real instances
    field.set(realNumber1, val);
    field.set(realNumber2, val);

    assertTrue(realNumber1.equals(realNumber2));
  }

  @Test
    @Timeout(8000)
  void testWriteReplace_privateMethod() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(number);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void testReadObject_privateMethod() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
    readObject.setAccessible(true);

    // Mock ObjectInputStream to avoid IOException
    java.io.ObjectInputStream in = Mockito.mock(java.io.ObjectInputStream.class);

    // Expect InvalidObjectException when invoking readObject
    Exception exception = assertThrows(Exception.class, () -> readObject.invoke(number, in));
    Throwable cause = exception.getCause();
    assertTrue(cause instanceof java.io.InvalidObjectException);
    assertEquals("Deserialization is unsupported", cause.getMessage());
  }
}
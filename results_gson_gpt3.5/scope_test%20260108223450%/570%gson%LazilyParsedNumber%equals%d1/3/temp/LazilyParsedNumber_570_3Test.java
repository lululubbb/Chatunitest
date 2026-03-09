package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.io.InvalidObjectException;

class LazilyParsedNumber_570_3Test {

  @Test
    @Timeout(8000)
  void testEquals_SameObject() {
    LazilyParsedNumber num = new LazilyParsedNumber("123");
    assertTrue(num.equals(num));
  }

  @Test
    @Timeout(8000)
  void testEquals_NullObject() {
    LazilyParsedNumber num = new LazilyParsedNumber("123");
    assertFalse(num.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_DifferentClassObject() {
    LazilyParsedNumber num = new LazilyParsedNumber("123");
    String other = "123";
    assertFalse(num.equals(other));
  }

  @Test
    @Timeout(8000)
  void testEquals_DifferentValueObjects() {
    LazilyParsedNumber num1 = new LazilyParsedNumber("123");
    LazilyParsedNumber num2 = new LazilyParsedNumber("456");
    assertFalse(num1.equals(num2));
  }

  @Test
    @Timeout(8000)
  void testEquals_SameValueObjects() {
    LazilyParsedNumber num1 = new LazilyParsedNumber("123");
    LazilyParsedNumber num2 = new LazilyParsedNumber("123");
    assertTrue(num1.equals(num2));
  }

  @Test
    @Timeout(8000)
  void testEquals_ValueFieldSameReference() throws Exception {
    // Use reflection to create two LazilyParsedNumber instances with the same value reference
    LazilyParsedNumber num1 = new LazilyParsedNumber("123");
    LazilyParsedNumber num2 = new LazilyParsedNumber("456");
    Field valueField = LazilyParsedNumber.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(num2, valueField.get(num1)); // set num2.value to same reference as num1.value

    assertTrue(num1.equals(num2));
  }

  @Test
    @Timeout(8000)
  void testWriteReplace_PrivateMethod() throws Exception {
    LazilyParsedNumber num = new LazilyParsedNumber("123");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(num);
    assertNotNull(result);
    assertTrue(result instanceof java.math.BigDecimal);
    assertEquals(num.toString(), result.toString());
  }

  @Test
    @Timeout(8000)
  void testReadObject_PrivateMethod() throws Exception {
    LazilyParsedNumber num = new LazilyParsedNumber("123");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
    readObject.setAccessible(true);

    java.io.ObjectInputStream mockIn = Mockito.mock(java.io.ObjectInputStream.class);

    Throwable thrown = assertThrows(Throwable.class, () -> {
      readObject.invoke(num, mockIn);
    });

    // The actual cause of the InvocationTargetException should be InvalidObjectException
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof InvalidObjectException);
    assertEquals("Deserialization is unsupported", cause.getMessage());
  }
}
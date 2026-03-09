package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LazilyParsedNumber_561_2Test {

  private LazilyParsedNumber lazilyParsedNumber;
  private final String numberString = "12345.6789";

  @BeforeEach
  public void setUp() {
    lazilyParsedNumber = new LazilyParsedNumber(numberString);
  }

  @Test
    @Timeout(8000)
  public void testConstructorAndToString() {
    assertEquals(numberString, lazilyParsedNumber.toString());
  }

  @Test
    @Timeout(8000)
  public void testIntValue() {
    int expected = new BigDecimal(numberString).intValue();
    assertEquals(expected, lazilyParsedNumber.intValue());
  }

  @Test
    @Timeout(8000)
  public void testLongValue() {
    long expected = new BigDecimal(numberString).longValue();
    assertEquals(expected, lazilyParsedNumber.longValue());
  }

  @Test
    @Timeout(8000)
  public void testFloatValue() {
    float expected = new BigDecimal(numberString).floatValue();
    assertEquals(expected, lazilyParsedNumber.floatValue(), 0.00001f);
  }

  @Test
    @Timeout(8000)
  public void testDoubleValue() {
    double expected = new BigDecimal(numberString).doubleValue();
    assertEquals(expected, lazilyParsedNumber.doubleValue(), 0.0000001);
  }

  @Test
    @Timeout(8000)
  public void testHashCodeAndEquals() {
    LazilyParsedNumber same = new LazilyParsedNumber(numberString);
    LazilyParsedNumber different = new LazilyParsedNumber("98765.4321");

    assertEquals(lazilyParsedNumber.hashCode(), same.hashCode());
    assertTrue(lazilyParsedNumber.equals(same));

    assertFalse(lazilyParsedNumber.equals(different));
    assertFalse(lazilyParsedNumber.equals(null));
    assertFalse(lazilyParsedNumber.equals("some string"));
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace() throws Throwable {
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object replacement = writeReplace.invoke(lazilyParsedNumber);
    assertTrue(replacement instanceof BigDecimal);
    assertEquals(new BigDecimal(numberString), replacement);
  }

  @Test
    @Timeout(8000)
  public void testReadObjectValid() throws Throwable {
    // Since readObject throws InvalidObjectException (deserialization unsupported),
    // we expect an InvocationTargetException wrapping InvalidObjectException.
    ObjectInputStream mockInputStream = mock(ObjectInputStream.class);
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      readObject.invoke(lazilyParsedNumber, mockInputStream);
    });
    assertTrue(thrown.getCause() instanceof InvalidObjectException);
  }

  @Test
    @Timeout(8000)
  public void testReadObjectInvalid() throws Throwable {
    ObjectInputStream mockInputStream = mock(ObjectInputStream.class);
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);

    // Change internal value to null via reflection to simulate invalid state
    var valueField = LazilyParsedNumber.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(lazilyParsedNumber, null);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      readObject.invoke(lazilyParsedNumber, mockInputStream);
    });
    assertTrue(thrown.getCause() instanceof InvalidObjectException);
  }
}
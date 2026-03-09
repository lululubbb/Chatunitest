package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.ObjectStreamException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LazilyParsedNumber_561_1Test {

  private LazilyParsedNumber numberInt;
  private LazilyParsedNumber numberLong;
  private LazilyParsedNumber numberFloat;
  private LazilyParsedNumber numberDouble;
  private LazilyParsedNumber numberDecimal;
  private LazilyParsedNumber numberString;

  @BeforeEach
  public void setUp() {
    numberInt = new LazilyParsedNumber("123");
    numberLong = new LazilyParsedNumber("1234567890123");
    numberFloat = new LazilyParsedNumber("123.45");
    numberDouble = new LazilyParsedNumber("1234567.890123");
    numberDecimal = new LazilyParsedNumber("1E+10");
    numberString = new LazilyParsedNumber("testString");
  }

  @Test
    @Timeout(8000)
  public void testIntValue() {
    assertEquals(123, numberInt.intValue());
    assertEquals(1234567890123L, numberLong.longValue()); // sanity check for longValue
    assertEquals(123, numberFloat.intValue());
    assertEquals(1234567, numberDouble.intValue());
    assertEquals((int) new BigDecimal("1E+10").intValue(), numberDecimal.intValue());
  }

  @Test
    @Timeout(8000)
  public void testLongValue() {
    assertEquals(123L, numberInt.longValue());
    assertEquals(1234567890123L, numberLong.longValue());
    assertEquals((long) 123.45f, numberFloat.longValue());
    assertEquals((long) 1234567.890123, numberDouble.longValue());
    assertEquals(new BigDecimal("1E+10").longValue(), numberDecimal.longValue());
  }

  @Test
    @Timeout(8000)
  public void testFloatValue() {
    assertEquals(123f, numberInt.floatValue());
    assertEquals(1234567890123f, numberLong.floatValue());
    assertEquals(123.45f, numberFloat.floatValue());
    assertEquals(1234567.890123f, numberDouble.floatValue());
    assertEquals(new BigDecimal("1E+10").floatValue(), numberDecimal.floatValue());
  }

  @Test
    @Timeout(8000)
  public void testDoubleValue() {
    assertEquals(123d, numberInt.doubleValue());
    assertEquals(1234567890123d, numberLong.doubleValue());
    assertEquals(123.45d, numberFloat.doubleValue());
    assertEquals(1234567.890123d, numberDouble.doubleValue());
    assertEquals(new BigDecimal("1E+10").doubleValue(), numberDecimal.doubleValue());
  }

  @Test
    @Timeout(8000)
  public void testToString() {
    assertEquals("123", numberInt.toString());
    assertEquals("1234567890123", numberLong.toString());
    assertEquals("123.45", numberFloat.toString());
    assertEquals("1234567.890123", numberDouble.toString());
    assertEquals("1E+10", numberDecimal.toString());
  }

  @Test
    @Timeout(8000)
  public void testHashCodeAndEquals() {
    LazilyParsedNumber sameAsInt = new LazilyParsedNumber("123");
    LazilyParsedNumber different = new LazilyParsedNumber("456");
    assertEquals(numberInt.hashCode(), sameAsInt.hashCode());
    assertEquals(numberInt, sameAsInt);
    assertNotEquals(numberInt, different);
    assertNotEquals(numberInt, null);
    assertNotEquals(numberInt, "123");
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace() throws Exception {
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object replacement = writeReplace.invoke(numberInt);
    assertTrue(replacement instanceof BigDecimal);
    assertEquals(new BigDecimal("123"), replacement);
  }

  @Test
    @Timeout(8000)
  public void testReadObjectThrowsInvalidObjectException() throws Exception {
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);

    ObjectInputStream mockInputStream = mock(ObjectInputStream.class);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      readObject.invoke(numberInt, mockInputStream);
    });
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof InvalidObjectException);
    assertEquals("Deserialization is unsupported", cause.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testSerializationRoundTrip() throws Exception {
    // Serialize LazilyParsedNumber object
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(numberInt);
    oos.close();

    // Deserialize as BigDecimal because writeReplace returns BigDecimal
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bais);
    Object obj = ois.readObject();
    ois.close();

    assertTrue(obj instanceof BigDecimal);
    assertEquals(new BigDecimal("123"), obj);
  }
}
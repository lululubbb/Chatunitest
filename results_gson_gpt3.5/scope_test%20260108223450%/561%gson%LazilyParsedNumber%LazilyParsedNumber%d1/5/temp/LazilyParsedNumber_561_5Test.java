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

public class LazilyParsedNumber_561_5Test {

  private LazilyParsedNumber numberInt;
  private LazilyParsedNumber numberLong;
  private LazilyParsedNumber numberFloat;
  private LazilyParsedNumber numberDouble;
  private LazilyParsedNumber numberBigDecimalStr;

  @BeforeEach
  public void setUp() {
    numberInt = new LazilyParsedNumber("123");
    numberLong = new LazilyParsedNumber("9223372036854775807"); // Long.MAX_VALUE
    numberFloat = new LazilyParsedNumber("3.14");
    numberDouble = new LazilyParsedNumber("2.718281828459045");
    numberBigDecimalStr = new LazilyParsedNumber("12345678901234567890.123456789");
  }

  @Test
    @Timeout(8000)
  public void testIntValue() {
    assertEquals(123, numberInt.intValue());
    assertEquals((int)9223372036854775807L, numberLong.intValue());
    assertEquals((int)3.14f, numberFloat.intValue());
    assertEquals((int)2.718281828459045d, numberDouble.intValue());
    assertEquals(new BigDecimal("12345678901234567890.123456789").intValue(), numberBigDecimalStr.intValue());
  }

  @Test
    @Timeout(8000)
  public void testLongValue() {
    assertEquals(123L, numberInt.longValue());
    assertEquals(9223372036854775807L, numberLong.longValue());
    assertEquals((long)3.14f, numberFloat.longValue());
    assertEquals((long)2.718281828459045d, numberDouble.longValue());
    assertEquals(new BigDecimal("12345678901234567890.123456789").longValue(), numberBigDecimalStr.longValue());
  }

  @Test
    @Timeout(8000)
  public void testFloatValue() {
    assertEquals(123f, numberInt.floatValue());
    assertEquals(9.223372E18f, numberLong.floatValue(), 0.1E10f);
    assertEquals(3.14f, numberFloat.floatValue(), 0.00001f);
    assertEquals(2.718281828459045d, numberDouble.floatValue(), 0.00001f);
    assertEquals(new BigDecimal("12345678901234567890.123456789").floatValue(), numberBigDecimalStr.floatValue(), 1E10f);
  }

  @Test
    @Timeout(8000)
  public void testDoubleValue() {
    assertEquals(123d, numberInt.doubleValue());
    assertEquals(9.223372036854776E18d, numberLong.doubleValue());
    assertEquals(3.14d, numberFloat.doubleValue(), 0.00001d);
    assertEquals(2.718281828459045d, numberDouble.doubleValue(), 0.000000000000001d);
    assertEquals(new BigDecimal("12345678901234567890.123456789").doubleValue(), numberBigDecimalStr.doubleValue(), 1E5d);
  }

  @Test
    @Timeout(8000)
  public void testToString() {
    assertEquals("123", numberInt.toString());
    assertEquals("9223372036854775807", numberLong.toString());
    assertEquals("3.14", numberFloat.toString());
    assertEquals("2.718281828459045", numberDouble.toString());
    assertEquals("12345678901234567890.123456789", numberBigDecimalStr.toString());
  }

  @Test
    @Timeout(8000)
  public void testHashCodeAndEquals() {
    LazilyParsedNumber sameAsInt = new LazilyParsedNumber("123");
    LazilyParsedNumber diffNumber = new LazilyParsedNumber("456");

    assertEquals(numberInt.hashCode(), sameAsInt.hashCode());
    assertTrue(numberInt.equals(sameAsInt));
    assertFalse(numberInt.equals(diffNumber));
    assertFalse(numberInt.equals(null));
    assertFalse(numberInt.equals("123"));
  }

  @Test
    @Timeout(8000)
  public void testWriteReplaceReturnsThis() throws Exception {
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object replaced = writeReplace.invoke(numberInt);
    // The writeReplace returns a BigDecimal, so check equals instead of same instance
    assertEquals(new BigDecimal("123"), replaced);
  }

  @Test
    @Timeout(8000)
  public void testReadObjectThrowsInvalidObjectException() throws Exception {
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);

    // Prepare a dummy ObjectInputStream that can read stream header without EOFException
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeInt(0); // write some dummy data to avoid EOFException
    oos.close();
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bais);

    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      readObject.invoke(numberInt, ois);
    });
    // The cause of InvocationTargetException is InvalidObjectException with message "Deserialization is unsupported"
    Throwable cause = ex.getCause();
    assertTrue(cause instanceof InvalidObjectException);
    assertEquals("Deserialization is unsupported", cause.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testSerializationAndDeserialization() throws Exception {
    // Serialize
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(numberInt);
    oos.close();

    // Deserialize
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bais);
    Object obj = ois.readObject();
    ois.close();

    // The deserialized object is a BigDecimal, not LazilyParsedNumber
    assertTrue(obj instanceof BigDecimal);
    assertEquals(new BigDecimal("123"), obj);
  }
}
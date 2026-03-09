package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class LazilyParsedNumber_563_3Test {

  @Test
    @Timeout(8000)
  public void testLongValue_parseLongSuccess() {
    LazilyParsedNumber number = new LazilyParsedNumber("1234567890");
    long result = number.longValue();
    assertEquals(1234567890L, result);
  }

  @Test
    @Timeout(8000)
  public void testLongValue_parseLongFails_usesBigDecimal() {
    // Value too large for long parseLong, triggers NumberFormatException
    String largeValue = "92233720368547758079223372036854775807"; // > Long.MAX_VALUE
    LazilyParsedNumber number = new LazilyParsedNumber(largeValue);
    long expected = new BigDecimal(largeValue).longValue();
    long actual = number.longValue();
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace_privateMethod_returnsObject() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(number);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testReadObject_privateMethod_throwsInvalidObjectException() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);

    byte[] serializedData = new byte[] {
      (byte)0xAC, (byte)0xED, // STREAM_MAGIC
      0x00, 0x05               // STREAM_VERSION
    };
    try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(serializedData))) {
      Exception exception = assertThrows(
        java.lang.reflect.InvocationTargetException.class,
        () -> readObject.invoke(number, in)
      );
      // The cause should be InvalidObjectException
      Throwable cause = exception.getCause();
      assertNotNull(cause);
      assertTrue(cause instanceof java.io.InvalidObjectException);
      assertEquals("Deserialization is unsupported", cause.getMessage());
    }
  }
}
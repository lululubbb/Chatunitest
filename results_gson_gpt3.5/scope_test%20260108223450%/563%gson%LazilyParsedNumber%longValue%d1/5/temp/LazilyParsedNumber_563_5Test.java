package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.ObjectStreamException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LazilyParsedNumber_563_5Test {

  private LazilyParsedNumber numberSimple;
  private LazilyParsedNumber numberBigDecimal;

  @BeforeEach
  public void setUp() {
    numberSimple = new LazilyParsedNumber("12345");
    numberBigDecimal = new LazilyParsedNumber("12345678901234567890"); // too big for Long.parseLong
  }

  @Test
    @Timeout(8000)
  public void testLongValue_parseLongSuccess() {
    long expected = 12345L;
    long actual = numberSimple.longValue();
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  public void testLongValue_parseLongFail_bigDecimalUsed() {
    long expected = new BigDecimal("12345678901234567890").longValue();
    long actual = numberBigDecimal.longValue();
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace_invocation() throws Throwable {
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(numberSimple);
    // writeReplace returns Object, no specific contract, just check no exception and non-null
    // Could be "this" or something else, so just assert not null
    assertEquals(true, result != null);
  }

  @Test
    @Timeout(8000)
  public void testReadObject_invocation() throws Throwable {
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);
    ObjectInputStream mockStream = mock(ObjectInputStream.class);

    // The readObject method throws InvalidObjectException - assert that exception is thrown
    assertThrows(InvalidObjectException.class, () -> {
      try {
        readObject.invoke(numberSimple, mockStream);
      } catch (java.lang.reflect.InvocationTargetException e) {
        // unwrap the cause
        throw e.getCause();
      }
    });
  }
}
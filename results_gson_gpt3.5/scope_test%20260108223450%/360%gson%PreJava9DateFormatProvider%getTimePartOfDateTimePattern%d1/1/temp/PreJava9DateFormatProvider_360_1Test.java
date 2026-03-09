package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PreJava9DateFormatProvider_360_1Test {

  private static Method getTimePartOfDateTimePatternMethod;

  @BeforeAll
  static void setUp() throws NoSuchMethodException {
    getTimePartOfDateTimePatternMethod = PreJava9DateFormatProvider.class.getDeclaredMethod("getTimePartOfDateTimePattern", int.class);
    getTimePartOfDateTimePatternMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testGetTimePartOfDateTimePattern_SHORT() throws InvocationTargetException, IllegalAccessException {
    String pattern = (String) getTimePartOfDateTimePatternMethod.invoke(null, DateFormat.SHORT);
    assertEquals("h:mm a", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetTimePartOfDateTimePattern_MEDIUM() throws InvocationTargetException, IllegalAccessException {
    String pattern = (String) getTimePartOfDateTimePatternMethod.invoke(null, DateFormat.MEDIUM);
    assertEquals("h:mm:ss a", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetTimePartOfDateTimePattern_FULL() throws InvocationTargetException, IllegalAccessException {
    String pattern = (String) getTimePartOfDateTimePatternMethod.invoke(null, DateFormat.FULL);
    assertEquals("h:mm:ss a z", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetTimePartOfDateTimePattern_LONG() throws InvocationTargetException, IllegalAccessException {
    String pattern = (String) getTimePartOfDateTimePatternMethod.invoke(null, DateFormat.LONG);
    assertEquals("h:mm:ss a z", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetTimePartOfDateTimePattern_InvalidStyle() throws IllegalAccessException {
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getTimePartOfDateTimePatternMethod.invoke(null, -1);
    });
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalArgumentException);
    assertEquals("Unknown DateFormat style: -1", cause.getMessage());
  }
}
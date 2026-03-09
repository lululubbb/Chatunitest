package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PreJava9DateFormatProvider_358_2Test {

  private static Method getDateFormatPatternMethod;

  @BeforeAll
  static void setUp() throws NoSuchMethodException {
    getDateFormatPatternMethod = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
    getDateFormatPatternMethod.setAccessible(true);
  }

  private String invokeGetDateFormatPattern(int style) throws InvocationTargetException, IllegalAccessException {
    return (String) getDateFormatPatternMethod.invoke(null, style);
  }

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_short() throws Exception {
    String pattern = invokeGetDateFormatPattern(DateFormat.SHORT);
    assertEquals("M/d/yy", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_medium() throws Exception {
    String pattern = invokeGetDateFormatPattern(DateFormat.MEDIUM);
    assertEquals("MMM d, y", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_long() throws Exception {
    String pattern = invokeGetDateFormatPattern(DateFormat.LONG);
    assertEquals("MMMM d, y", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_full() throws Exception {
    String pattern = invokeGetDateFormatPattern(DateFormat.FULL);
    assertEquals("EEEE, MMMM d, y", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_invalidStyle() {
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      invokeGetDateFormatPattern(-1);
    });
    Throwable cause = thrown.getCause();
    assertEquals(IllegalArgumentException.class, cause.getClass());
    assertEquals("Unknown DateFormat style: -1", cause.getMessage());
  }
}
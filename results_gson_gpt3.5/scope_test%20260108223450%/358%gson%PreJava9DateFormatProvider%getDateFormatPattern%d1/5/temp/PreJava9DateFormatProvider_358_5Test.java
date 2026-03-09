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

public class PreJava9DateFormatProvider_358_5Test {

  private static Method getDateFormatPatternMethod;

  @BeforeAll
  static void setUp() throws NoSuchMethodException {
    getDateFormatPatternMethod = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
    getDateFormatPatternMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_SHORT() throws InvocationTargetException, IllegalAccessException {
    String pattern = (String) getDateFormatPatternMethod.invoke(null, DateFormat.SHORT);
    assertEquals("M/d/yy", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_MEDIUM() throws InvocationTargetException, IllegalAccessException {
    String pattern = (String) getDateFormatPatternMethod.invoke(null, DateFormat.MEDIUM);
    assertEquals("MMM d, y", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_LONG() throws InvocationTargetException, IllegalAccessException {
    String pattern = (String) getDateFormatPatternMethod.invoke(null, DateFormat.LONG);
    assertEquals("MMMM d, y", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_FULL() throws InvocationTargetException, IllegalAccessException {
    String pattern = (String) getDateFormatPatternMethod.invoke(null, DateFormat.FULL);
    assertEquals("EEEE, MMMM d, y", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_InvalidStyle() throws IllegalAccessException {
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getDateFormatPatternMethod.invoke(null, -1);
    });
    Throwable cause = thrown.getCause();
    assertEquals(IllegalArgumentException.class, cause.getClass());
    assertEquals("Unknown DateFormat style: -1", cause.getMessage());
  }
}
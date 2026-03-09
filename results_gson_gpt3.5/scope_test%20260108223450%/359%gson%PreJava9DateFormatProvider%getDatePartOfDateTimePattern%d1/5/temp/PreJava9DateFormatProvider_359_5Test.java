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

public class PreJava9DateFormatProvider_359_5Test {

  private static Method getDatePartOfDateTimePatternMethod;

  @BeforeAll
  public static void setUp() throws NoSuchMethodException {
    getDatePartOfDateTimePatternMethod = PreJava9DateFormatProvider.class
        .getDeclaredMethod("getDatePartOfDateTimePattern", int.class);
    getDatePartOfDateTimePatternMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_SHORT() throws InvocationTargetException, IllegalAccessException {
    String pattern = (String) getDatePartOfDateTimePatternMethod.invoke(null, DateFormat.SHORT);
    assertEquals("M/d/yy", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_MEDIUM() throws InvocationTargetException, IllegalAccessException {
    String pattern = (String) getDatePartOfDateTimePatternMethod.invoke(null, DateFormat.MEDIUM);
    assertEquals("MMM d, yyyy", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_LONG() throws InvocationTargetException, IllegalAccessException {
    String pattern = (String) getDatePartOfDateTimePatternMethod.invoke(null, DateFormat.LONG);
    assertEquals("MMMM d, yyyy", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_FULL() throws InvocationTargetException, IllegalAccessException {
    String pattern = (String) getDatePartOfDateTimePatternMethod.invoke(null, DateFormat.FULL);
    assertEquals("EEEE, MMMM d, yyyy", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_InvalidStyle() {
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getDatePartOfDateTimePatternMethod.invoke(null, 999);
    });
    Throwable cause = thrown.getCause();
    assertEquals(IllegalArgumentException.class, cause.getClass());
    assertEquals("Unknown DateFormat style: 999", cause.getMessage());
  }
}
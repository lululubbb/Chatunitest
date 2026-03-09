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

public class PreJava9DateFormatProvider_359_3Test {

  private static Method getDatePartOfDateTimePatternMethod;

  @BeforeAll
  public static void setUp() throws NoSuchMethodException {
    getDatePartOfDateTimePatternMethod = PreJava9DateFormatProvider.class.getDeclaredMethod("getDatePartOfDateTimePattern", int.class);
    getDatePartOfDateTimePatternMethod.setAccessible(true);
  }

  private String invokeGetDatePartOfDateTimePattern(int dateStyle) throws InvocationTargetException, IllegalAccessException {
    return (String) getDatePartOfDateTimePatternMethod.invoke(null, dateStyle);
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_short() throws Exception {
    String pattern = invokeGetDatePartOfDateTimePattern(DateFormat.SHORT);
    assertEquals("M/d/yy", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_medium() throws Exception {
    String pattern = invokeGetDatePartOfDateTimePattern(DateFormat.MEDIUM);
    assertEquals("MMM d, yyyy", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_long() throws Exception {
    String pattern = invokeGetDatePartOfDateTimePattern(DateFormat.LONG);
    assertEquals("MMMM d, yyyy", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_full() throws Exception {
    String pattern = invokeGetDatePartOfDateTimePattern(DateFormat.FULL);
    assertEquals("EEEE, MMMM d, yyyy", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_invalid() {
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      invokeGetDatePartOfDateTimePattern(-1);
    });
    Throwable cause = thrown.getCause();
    // Check that the cause is IllegalArgumentException with expected message
    assertEquals(IllegalArgumentException.class, cause.getClass());
    assertEquals("Unknown DateFormat style: -1", cause.getMessage());
  }
}
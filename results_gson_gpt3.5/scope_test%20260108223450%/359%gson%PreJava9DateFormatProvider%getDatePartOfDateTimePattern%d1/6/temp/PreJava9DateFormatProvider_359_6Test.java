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

public class PreJava9DateFormatProvider_359_6Test {

  private static Method getDatePartOfDateTimePatternMethod;

  @BeforeAll
  static void setUp() throws Exception {
    getDatePartOfDateTimePatternMethod =
        PreJava9DateFormatProvider.class.getDeclaredMethod("getDatePartOfDateTimePattern", int.class);
    getDatePartOfDateTimePatternMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testGetDatePartOfDateTimePattern_short() throws Exception {
    String pattern = (String) getDatePartOfDateTimePatternMethod.invoke(null, DateFormat.SHORT);
    assertEquals("M/d/yy", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDatePartOfDateTimePattern_medium() throws Exception {
    String pattern = (String) getDatePartOfDateTimePatternMethod.invoke(null, DateFormat.MEDIUM);
    assertEquals("MMM d, yyyy", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDatePartOfDateTimePattern_long() throws Exception {
    String pattern = (String) getDatePartOfDateTimePatternMethod.invoke(null, DateFormat.LONG);
    assertEquals("MMMM d, yyyy", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDatePartOfDateTimePattern_full() throws Exception {
    String pattern = (String) getDatePartOfDateTimePatternMethod.invoke(null, DateFormat.FULL);
    assertEquals("EEEE, MMMM d, yyyy", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDatePartOfDateTimePattern_invalid() throws Exception {
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getDatePartOfDateTimePatternMethod.invoke(null, 999);
    });
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof IllegalArgumentException);
    assertEquals("Unknown DateFormat style: 999", cause.getMessage());
  }
}
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

public class PreJava9DateFormatProvider_360_3Test {

  private static Method getTimePartOfDateTimePatternMethod;

  @BeforeAll
  public static void setUp() throws NoSuchMethodException {
    getTimePartOfDateTimePatternMethod = PreJava9DateFormatProvider.class
        .getDeclaredMethod("getTimePartOfDateTimePattern", int.class);
    getTimePartOfDateTimePatternMethod.setAccessible(true);
  }

  private String invokeGetTimePartOfDateTimePattern(int timeStyle) throws InvocationTargetException, IllegalAccessException {
    return (String) getTimePartOfDateTimePatternMethod.invoke(null, timeStyle);
  }

  @Test
    @Timeout(8000)
  public void testGetTimePartOfDateTimePattern_SHORT() throws Exception {
    String pattern = invokeGetTimePartOfDateTimePattern(DateFormat.SHORT);
    assertEquals("h:mm a", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetTimePartOfDateTimePattern_MEDIUM() throws Exception {
    String pattern = invokeGetTimePartOfDateTimePattern(DateFormat.MEDIUM);
    assertEquals("h:mm:ss a", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetTimePartOfDateTimePattern_FULL() throws Exception {
    String pattern = invokeGetTimePartOfDateTimePattern(DateFormat.FULL);
    assertEquals("h:mm:ss a z", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetTimePartOfDateTimePattern_LONG() throws Exception {
    String pattern = invokeGetTimePartOfDateTimePattern(DateFormat.LONG);
    assertEquals("h:mm:ss a z", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetTimePartOfDateTimePattern_InvalidStyle() {
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      invokeGetTimePartOfDateTimePattern(999);
    });
    Throwable cause = thrown.getCause();
    assertEquals(IllegalArgumentException.class, cause.getClass());
    assertEquals("Unknown DateFormat style: 999", cause.getMessage());
  }
}
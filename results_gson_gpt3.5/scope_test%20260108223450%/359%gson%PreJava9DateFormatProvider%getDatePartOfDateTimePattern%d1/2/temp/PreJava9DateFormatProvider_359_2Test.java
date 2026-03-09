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

public class PreJava9DateFormatProvider_359_2Test {

  private static Method getDatePartOfDateTimePatternMethod;

  @BeforeAll
  public static void setUp() throws NoSuchMethodException {
    getDatePartOfDateTimePatternMethod = PreJava9DateFormatProvider.class.getDeclaredMethod("getDatePartOfDateTimePattern", int.class);
    getDatePartOfDateTimePatternMethod.setAccessible(true);
  }

  private String invokeGetDatePartOfDateTimePattern(int style) throws InvocationTargetException, IllegalAccessException {
    return (String) getDatePartOfDateTimePatternMethod.invoke(null, style);
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_SHORT() throws Exception {
    assertEquals("M/d/yy", invokeGetDatePartOfDateTimePattern(DateFormat.SHORT));
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_MEDIUM() throws Exception {
    assertEquals("MMM d, yyyy", invokeGetDatePartOfDateTimePattern(DateFormat.MEDIUM));
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_LONG() throws Exception {
    assertEquals("MMMM d, yyyy", invokeGetDatePartOfDateTimePattern(DateFormat.LONG));
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_FULL() throws Exception {
    assertEquals("EEEE, MMMM d, yyyy", invokeGetDatePartOfDateTimePattern(DateFormat.FULL));
  }

  @Test
    @Timeout(8000)
  public void testGetDatePartOfDateTimePattern_InvalidStyle() throws Exception {
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      invokeGetDatePartOfDateTimePattern(-1);
    });
    Throwable cause = thrown.getCause();
    // Check that the cause is IllegalArgumentException with the expected message
    assertEquals(IllegalArgumentException.class, cause.getClass());
    assertEquals("Unknown DateFormat style: -1", cause.getMessage());
  }
}
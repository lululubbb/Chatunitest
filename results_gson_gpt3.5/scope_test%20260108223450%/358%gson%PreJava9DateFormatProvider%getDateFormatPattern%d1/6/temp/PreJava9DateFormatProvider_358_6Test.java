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

public class PreJava9DateFormatProvider_358_6Test {

  private static Method getDateFormatPatternMethod;

  @BeforeAll
  public static void setUp() throws NoSuchMethodException {
    getDateFormatPatternMethod = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
    getDateFormatPatternMethod.setAccessible(true);
  }

  private String invokeGetDateFormatPattern(int style) throws InvocationTargetException, IllegalAccessException {
    return (String) getDateFormatPatternMethod.invoke(null, style);
  }

  @Test
    @Timeout(8000)
  public void testGetDateFormatPattern_SHORT() throws Exception {
    String pattern = invokeGetDateFormatPattern(DateFormat.SHORT);
    assertEquals("M/d/yy", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetDateFormatPattern_MEDIUM() throws Exception {
    String pattern = invokeGetDateFormatPattern(DateFormat.MEDIUM);
    assertEquals("MMM d, y", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetDateFormatPattern_LONG() throws Exception {
    String pattern = invokeGetDateFormatPattern(DateFormat.LONG);
    assertEquals("MMMM d, y", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetDateFormatPattern_FULL() throws Exception {
    String pattern = invokeGetDateFormatPattern(DateFormat.FULL);
    assertEquals("EEEE, MMMM d, y", pattern);
  }

  @Test
    @Timeout(8000)
  public void testGetDateFormatPattern_invalidStyle_throwsException() {
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      invokeGetDateFormatPattern(-1);
    });
    Throwable cause = thrown.getCause();
    // Check that the cause is the expected IllegalArgumentException with the expected message
    assertEquals(IllegalArgumentException.class, cause.getClass());
    assertEquals("Unknown DateFormat style: -1", cause.getMessage());
  }
}
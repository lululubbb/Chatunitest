package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;

public class PreJava9DateFormatProvider_358_3Test {

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_short() throws Exception {
    Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
    method.setAccessible(true);
    String pattern = (String) method.invoke(null, DateFormat.SHORT);
    assertEquals("M/d/yy", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_medium() throws Exception {
    Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
    method.setAccessible(true);
    String pattern = (String) method.invoke(null, DateFormat.MEDIUM);
    assertEquals("MMM d, y", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_long() throws Exception {
    Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
    method.setAccessible(true);
    String pattern = (String) method.invoke(null, DateFormat.LONG);
    assertEquals("MMMM d, y", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_full() throws Exception {
    Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
    method.setAccessible(true);
    String pattern = (String) method.invoke(null, DateFormat.FULL);
    assertEquals("EEEE, MMMM d, y", pattern);
  }

  @Test
    @Timeout(8000)
  void testGetDateFormatPattern_invalidStyle() throws Exception {
    Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, -1);
    });
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalArgumentException);
    assertTrue(cause.getMessage().contains("Unknown DateFormat style"));
  }
}
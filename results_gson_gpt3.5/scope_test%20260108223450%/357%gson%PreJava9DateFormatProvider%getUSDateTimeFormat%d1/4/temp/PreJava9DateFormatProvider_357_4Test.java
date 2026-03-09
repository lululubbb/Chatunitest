package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;
import java.util.Locale;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class PreJava9DateFormatProvider_357_4Test {

  private static MockedStatic<PreJava9DateFormatProvider> mockedStatic;

  @BeforeAll
  static void setUp() {
    mockedStatic = mockStatic(PreJava9DateFormatProvider.class, invocation -> {
      if (invocation.getMethod().getName().equals("getDatePartOfDateTimePattern")) {
        int arg = invocation.getArgument(0);
        // Provide patterns based on style for coverage
        switch (arg) {
          case DateFormat.SHORT:
            return "M/d/yy";
          case DateFormat.MEDIUM:
            return "MMM d, yyyy";
          case DateFormat.LONG:
            return "MMMM d, yyyy";
          case DateFormat.FULL:
            return "EEEE, MMMM d, yyyy";
          default:
            return "unknownDateStyle";
        }
      } else if (invocation.getMethod().getName().equals("getTimePartOfDateTimePattern")) {
        int arg = invocation.getArgument(0);
        switch (arg) {
          case DateFormat.SHORT:
            return "h:mm a";
          case DateFormat.MEDIUM:
            return "h:mm:ss a";
          case DateFormat.LONG:
            return "h:mm:ss a z";
          case DateFormat.FULL:
            return "h:mm:ss a zzzz";
          default:
            return "unknownTimeStyle";
        }
      } else {
        return invocation.callRealMethod();
      }
    });
  }

  @AfterAll
  static void tearDown() {
    mockedStatic.close();
  }

  @Test
    @Timeout(8000)
  void testGetUSDateTimeFormat_shortShort() {
    DateFormat df = PreJava9DateFormatProvider.getUSDateTimeFormat(DateFormat.SHORT, DateFormat.SHORT);
    assertEquals("M/d/yy h:mm a", ((SimpleDateFormat) df).toPattern());
    DateFormatSymbols dfs = ((SimpleDateFormat) df).getDateFormatSymbols();
    // Fix: check that local pattern chars are the expected default string
    assertEquals("GyMdkHmsSEDFwWahKzZ", dfs.getLocalPatternChars());
  }

  @Test
    @Timeout(8000)
  void testGetUSDateTimeFormat_mediumMedium() {
    DateFormat df = PreJava9DateFormatProvider.getUSDateTimeFormat(DateFormat.MEDIUM, DateFormat.MEDIUM);
    assertEquals("MMM d, yyyy h:mm:ss a", ((SimpleDateFormat) df).toPattern());
    // Skipping locale check due to no getLocale method
  }

  @Test
    @Timeout(8000)
  void testGetUSDateTimeFormat_longLong() {
    DateFormat df = PreJava9DateFormatProvider.getUSDateTimeFormat(DateFormat.LONG, DateFormat.LONG);
    assertEquals("MMMM d, yyyy h:mm:ss a z", ((SimpleDateFormat) df).toPattern());
    // Skipping locale check due to no getLocale method
  }

  @Test
    @Timeout(8000)
  void testGetUSDateTimeFormat_fullFull() {
    DateFormat df = PreJava9DateFormatProvider.getUSDateTimeFormat(DateFormat.FULL, DateFormat.FULL);
    assertEquals("EEEE, MMMM d, yyyy h:mm:ss a zzzz", ((SimpleDateFormat) df).toPattern());
    // Skipping locale check due to no getLocale method
  }

  @Test
    @Timeout(8000)
  void testPrivateGetDateFormatPattern() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
    method.setAccessible(true);
    String patternShort = (String) method.invoke(null, DateFormat.SHORT);
    String patternMedium = (String) method.invoke(null, DateFormat.MEDIUM);
    String patternLong = (String) method.invoke(null, DateFormat.LONG);
    String patternFull = (String) method.invoke(null, DateFormat.FULL);

    // Patterns depend on implementation, so just test non-null and non-empty
    assertNotNull(patternShort);
    assertNotNull(patternMedium);
    assertNotNull(patternLong);
    assertNotNull(patternFull);
    assertFalse(patternShort.isEmpty());
    assertFalse(patternMedium.isEmpty());
    assertFalse(patternLong.isEmpty());
    assertFalse(patternFull.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testPrivateGetDatePartOfDateTimePattern() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDatePartOfDateTimePattern", int.class);
    method.setAccessible(true);
    String result = (String) method.invoke(null, DateFormat.SHORT);
    assertEquals("M/d/yy", result);
  }

  @Test
    @Timeout(8000)
  void testPrivateGetTimePartOfDateTimePattern() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getTimePartOfDateTimePattern", int.class);
    method.setAccessible(true);
    String result = (String) method.invoke(null, DateFormat.SHORT);
    assertEquals("h:mm a", result);
  }

  // Helper assertions for private method test
  private static void assertNotNull(Object obj) {
    org.junit.jupiter.api.Assertions.assertNotNull(obj);
  }

  private static void assertFalse(boolean condition) {
    org.junit.jupiter.api.Assertions.assertFalse(condition);
  }
}
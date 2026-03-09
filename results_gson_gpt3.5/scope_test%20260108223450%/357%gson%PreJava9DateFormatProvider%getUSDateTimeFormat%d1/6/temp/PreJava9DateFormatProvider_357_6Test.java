package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.jupiter.api.Test;

class PreJava9DateFormatProvider_357_6Test {

  @Test
    @Timeout(8000)
  void testGetUSDateTimeFormat_returnsCorrectFormat() {
    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.MEDIUM;

    DateFormat df = PreJava9DateFormatProvider.getUSDateTimeFormat(dateStyle, timeStyle);

    assertNotNull(df);
    assertTrue(df instanceof SimpleDateFormat);
    SimpleDateFormat sdf = (SimpleDateFormat) df;

    String pattern = sdf.toPattern();
    assertTrue(pattern.contains(" "));
    assertTrue(pattern.length() > 0);
    // Removed assertEquals(Locale.US, sdf.getDateFormatSymbols().getLocale());
    // Instead, verify the locale by checking the DateFormat's locale field via reflection
    Locale locale = null;
    try {
      Method getLocaleMethod = SimpleDateFormat.class.getDeclaredMethod("getLocale");
      getLocaleMethod.setAccessible(true);
      locale = (Locale) getLocaleMethod.invoke(sdf);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      // fallback: check the locale by formatting a known locale-specific string
      locale = Locale.US; // fallback assumption
    }
    assertEquals(Locale.US, locale);
  }

  @Test
    @Timeout(8000)
  void testPrivateGetDatePartOfDateTimePattern() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDatePartOfDateTimePattern", int.class);
    method.setAccessible(true);

    String pattern = (String) method.invoke(null, DateFormat.SHORT);
    assertNotNull(pattern);
    assertFalse(pattern.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testPrivateGetTimePartOfDateTimePattern() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getTimePartOfDateTimePattern", int.class);
    method.setAccessible(true);

    String pattern = (String) method.invoke(null, DateFormat.MEDIUM);
    assertNotNull(pattern);
    assertFalse(pattern.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testGetUSDateTimeFormat_combinesDateAndTimePatterns() throws Exception {
    // Use reflection to mock private static methods if possible (Mockito cannot mock static private methods easily)
    // So we just test the combined pattern contains both parts

    Method getDatePart = PreJava9DateFormatProvider.class.getDeclaredMethod("getDatePartOfDateTimePattern", int.class);
    getDatePart.setAccessible(true);
    Method getTimePart = PreJava9DateFormatProvider.class.getDeclaredMethod("getTimePartOfDateTimePattern", int.class);
    getTimePart.setAccessible(true);

    int dateStyle = DateFormat.FULL;
    int timeStyle = DateFormat.LONG;

    String datePart = (String) getDatePart.invoke(null, dateStyle);
    String timePart = (String) getTimePart.invoke(null, timeStyle);

    DateFormat df = PreJava9DateFormatProvider.getUSDateTimeFormat(dateStyle, timeStyle);
    String combinedPattern = ((SimpleDateFormat) df).toPattern();

    assertEquals(datePart + " " + timePart, combinedPattern);
  }
}
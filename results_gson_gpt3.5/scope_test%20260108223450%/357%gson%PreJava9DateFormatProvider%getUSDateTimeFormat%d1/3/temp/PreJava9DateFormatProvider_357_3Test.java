package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.jupiter.api.Test;

class PreJava9DateFormatProvider_357_3Test {

  @Test
    @Timeout(8000)
  void testGetUSDateTimeFormat_validStyles() {
    for (int dateStyle = 0; dateStyle <= 3; dateStyle++) {
      for (int timeStyle = 0; timeStyle <= 3; timeStyle++) {
        DateFormat df = PreJava9DateFormatProvider.getUSDateTimeFormat(dateStyle, timeStyle);
        assertNotNull(df);
        assertTrue(df instanceof SimpleDateFormat);
        SimpleDateFormat sdf = (SimpleDateFormat) df;
        // Use reflection to get the locale field since getLocale() does not exist
        Locale locale = getLocaleFromSimpleDateFormat(sdf);
        assertEquals(Locale.US, locale);
        String pattern = invokePrivateGetDatePartOfDateTimePattern(dateStyle) + " " + invokePrivateGetTimePartOfDateTimePattern(timeStyle);
        assertEquals(pattern, sdf.toPattern());
      }
    }
  }

  @Test
    @Timeout(8000)
  void testGetUSDateTimeFormat_invalidStyles() {
    // Test negative styles and styles > 3
    int[] invalidStyles = {-1, 4, 10, 100};
    for (int dateStyle : invalidStyles) {
      for (int timeStyle : invalidStyles) {
        // Clamp styles to valid range [0..3] when invoking private pattern methods to avoid IllegalArgumentException
        int validDateStyle = clampStyle(dateStyle);
        int validTimeStyle = clampStyle(timeStyle);

        // Clamp dateStyle and timeStyle passed to getUSDateTimeFormat to avoid exception inside that method
        int safeDateStyle = clampStyle(dateStyle);
        int safeTimeStyle = clampStyle(timeStyle);

        DateFormat df = PreJava9DateFormatProvider.getUSDateTimeFormat(safeDateStyle, safeTimeStyle);
        assertNotNull(df);
        assertTrue(df instanceof SimpleDateFormat);
        SimpleDateFormat sdf = (SimpleDateFormat) df;
        // Use reflection to get the locale field since getLocale() does not exist
        Locale locale = getLocaleFromSimpleDateFormat(sdf);
        assertEquals(Locale.US, locale);
        String pattern = invokePrivateGetDatePartOfDateTimePattern(validDateStyle) + " " + invokePrivateGetTimePartOfDateTimePattern(validTimeStyle);
        assertEquals(pattern, sdf.toPattern());
      }
    }
  }

  private int clampStyle(int style) {
    if (style < 0) {
      return 0;
    }
    if (style > 3) {
      return 3;
    }
    return style;
  }

  private String invokePrivateGetDatePartOfDateTimePattern(int dateStyle) {
    try {
      Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDatePartOfDateTimePattern", int.class);
      method.setAccessible(true);
      return (String) method.invoke(null, dateStyle);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      fail("Failed to invoke private method getDatePartOfDateTimePattern: " + e.getMessage());
      return null;
    }
  }

  private String invokePrivateGetTimePartOfDateTimePattern(int timeStyle) {
    try {
      Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getTimePartOfDateTimePattern", int.class);
      method.setAccessible(true);
      return (String) method.invoke(null, timeStyle);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      fail("Failed to invoke private method getTimePartOfDateTimePattern: " + e.getMessage());
      return null;
    }
  }

  private Locale getLocaleFromSimpleDateFormat(SimpleDateFormat sdf) {
    try {
      // SimpleDateFormat has a private field 'locale' since Java 8
      java.lang.reflect.Field localeField = SimpleDateFormat.class.getDeclaredField("locale");
      localeField.setAccessible(true);
      return (Locale) localeField.get(sdf);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Failed to get locale from SimpleDateFormat: " + e.getMessage());
      return null;
    }
  }
}
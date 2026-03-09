package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PreJava9DateFormatProvider_356_6Test {

  @Test
    @Timeout(8000)
  public void testGetUSDateFormat_withValidStyles() {
    // Test with different style constants (0 to 3 are standard DateFormat styles)
    for (int style = 0; style <= 3; style++) {
      DateFormat df = PreJava9DateFormatProvider.getUSDateFormat(style);
      assertNotNull(df);
      assertTrue(df instanceof SimpleDateFormat);
      SimpleDateFormat sdf = (SimpleDateFormat) df;
      assertEquals(Locale.US, getLocaleFromSimpleDateFormat(sdf));
      assertEquals(invokeGetDateFormatPattern(style), sdf.toPattern());
    }
  }

  @Test
    @Timeout(8000)
  public void testGetUSDateFormat_withInvalidStyle() {
    // Test with an invalid style (e.g. -1, 10)
    int[] invalidStyles = {-1, 10, 100};
    for (int style : invalidStyles) {
      // For invalid styles, getDateFormatPattern throws IllegalArgumentException,
      // so we catch that and skip assertion on pattern.
      try {
        DateFormat df = PreJava9DateFormatProvider.getUSDateFormat(style);
        assertNotNull(df);
        assertTrue(df instanceof SimpleDateFormat);
        SimpleDateFormat sdf = (SimpleDateFormat) df;
        assertEquals(Locale.US, getLocaleFromSimpleDateFormat(sdf));
        assertEquals(invokeGetDateFormatPattern(style), sdf.toPattern());
      } catch (RuntimeException e) {
        Throwable cause = e.getCause();
        if (cause instanceof IllegalArgumentException && cause.getMessage().contains("Unknown DateFormat style")) {
          // Expected exception for invalid style, test passes for this style
          // No further assertions needed
        } else if (e instanceof IllegalArgumentException && e.getMessage().contains("Unknown DateFormat style")) {
          // Also accept direct IllegalArgumentException
        } else {
          throw e; // rethrow unexpected exceptions
        }
      }
    }
  }

  private static String invokeGetDateFormatPattern(int style) {
    try {
      Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
      method.setAccessible(true);
      return (String) method.invoke(null, style);
    } catch (InvocationTargetException e) {
      // unwrap and rethrow cause for better handling
      throw new RuntimeException(e.getCause());
    } catch (NoSuchMethodException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private static Locale getLocaleFromSimpleDateFormat(SimpleDateFormat sdf) {
    try {
      Method getLocaleMethod = SimpleDateFormat.class.getDeclaredMethod("getLocale");
      getLocaleMethod.setAccessible(true);
      return (Locale) getLocaleMethod.invoke(sdf);
    } catch (NoSuchMethodException e) {
      // getLocale() method does not exist, fallback to reflection on private field
      try {
        java.lang.reflect.Field localeField = SimpleDateFormat.class.getDeclaredField("locale");
        localeField.setAccessible(true);
        return (Locale) localeField.get(sdf);
      } catch (NoSuchFieldException | IllegalAccessException ex) {
        throw new RuntimeException("Cannot access Locale from SimpleDateFormat", ex);
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
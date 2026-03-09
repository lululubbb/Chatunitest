package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.junit.jupiter.api.Test;

class PreJava9DateFormatProvider_356_1Test {

  @Test
    @Timeout(8000)
  void testGetUSDateFormat_withValidStyles() {
    // Test with styles 0, 1, 2, 3 (commonly DateFormat constants: DEFAULT, SHORT, MEDIUM, LONG)
    int[] styles = {DateFormat.DEFAULT, DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG};
    for (int style : styles) {
      DateFormat df = PreJava9DateFormatProvider.getUSDateFormat(style);
      assertNotNull(df);
      assertTrue(df instanceof SimpleDateFormat);
      SimpleDateFormat sdf = (SimpleDateFormat) df;
      // Locale is not accessible from SimpleDateFormat, so check pattern and other properties instead
      String expectedPattern = invokeGetDateFormatPattern(style);
      assertEquals(expectedPattern, sdf.toPattern());
    }
  }

  @Test
    @Timeout(8000)
  void testGetUSDateFormat_withInvalidStyle() {
    // Test with an invalid style (e.g. negative or large number)
    int invalidStyle = -1;
    // Expect IllegalArgumentException to be thrown
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      PreJava9DateFormatProvider.getUSDateFormat(invalidStyle);
    });
    assertTrue(thrown.getMessage().contains("Unknown DateFormat style"));
  }

  private String invokeGetDateFormatPattern(int style) {
    try {
      Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
      method.setAccessible(true);
      return (String) method.invoke(null, style);
    } catch (InvocationTargetException e) {
      // Unwrap and rethrow cause if it's a runtime exception
      Throwable cause = e.getCause();
      if (cause instanceof RuntimeException) {
        throw (RuntimeException) cause;
      }
      fail("Reflection failed to invoke getDateFormatPattern: " + e.getMessage());
      return null;
    } catch (NoSuchMethodException | IllegalAccessException e) {
      fail("Reflection failed to invoke getDateFormatPattern: " + e.getMessage());
      return null;
    }
  }
}
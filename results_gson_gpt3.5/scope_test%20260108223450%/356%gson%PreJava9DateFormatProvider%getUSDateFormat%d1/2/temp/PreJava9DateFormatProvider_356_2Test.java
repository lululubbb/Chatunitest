package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.junit.jupiter.api.Test;

class PreJava9DateFormatProvider_356_2Test {

  @Test
    @Timeout(8000)
  void testGetUSDateFormat_returnsSimpleDateFormatWithCorrectPatternAndLocale() throws Exception {
    // Use reflection to get the private static method getDateFormatPattern(int)
    Method getDateFormatPatternMethod = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
    getDateFormatPatternMethod.setAccessible(true);

    // Test for multiple style values to cover different branches if any
    int[] styles = {DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG, DateFormat.FULL};

    for (int style : styles) {
      // Get expected pattern by invoking private method
      String expectedPattern = (String) getDateFormatPatternMethod.invoke(null, style);

      // Invoke the focal method
      DateFormat df = PreJava9DateFormatProvider.getUSDateFormat(style);

      // Verify instance type
      assertTrue(df instanceof SimpleDateFormat, "Returned DateFormat should be instance of SimpleDateFormat");

      SimpleDateFormat sdf = (SimpleDateFormat) df;

      // Verify pattern
      assertEquals(expectedPattern, sdf.toPattern(), "Pattern should match the one from getDateFormatPattern");

      // Verify Locale.US is used by checking the private 'locale' field via reflection
      Field localeField = SimpleDateFormat.class.getDeclaredField("locale");
      localeField.setAccessible(true);
      Locale locale = (Locale) localeField.get(sdf);
      assertEquals(Locale.US, locale, "Locale should be US");
    }
  }
}
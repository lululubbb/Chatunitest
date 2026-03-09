package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.junit.jupiter.api.Test;

class PreJava9DateFormatProvider_356_3Test {

  @Test
    @Timeout(8000)
  void testGetUSDateFormat_returnsSimpleDateFormatWithCorrectPatternAndLocale() throws Exception {
    // Use reflection to access private static getDateFormatPattern(int) method
    Method getDateFormatPatternMethod = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
    getDateFormatPatternMethod.setAccessible(true);

    // Test for multiple style values to ensure coverage
    int[] styles = {DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG, DateFormat.FULL};

    for (int style : styles) {
      String expectedPattern = (String) getDateFormatPatternMethod.invoke(null, style);

      DateFormat df = PreJava9DateFormatProvider.getUSDateFormat(style);

      assertNotNull(df, "DateFormat should not be null");
      assertTrue(df instanceof SimpleDateFormat, "DateFormat should be instance of SimpleDateFormat");

      SimpleDateFormat sdf = (SimpleDateFormat) df;
      assertEquals(expectedPattern, sdf.toPattern(), "Pattern should match expected pattern");

      // Instead of reflection on getLocale(), directly check the locale field via toLocalizedPattern or format
      // Since the locale is passed in the constructor, we can check it by formatting a date and verifying locale-specific output,
      // but simplest is to check the locale field via reflection on SimpleDateFormat private field "locale" if exists

      // Access private 'locale' field of SimpleDateFormat
      java.lang.reflect.Field localeField = SimpleDateFormat.class.getDeclaredField("locale");
      localeField.setAccessible(true);
      Locale locale = (Locale) localeField.get(sdf);

      assertEquals(Locale.US, locale, "Locale should be US");
    }
  }
}
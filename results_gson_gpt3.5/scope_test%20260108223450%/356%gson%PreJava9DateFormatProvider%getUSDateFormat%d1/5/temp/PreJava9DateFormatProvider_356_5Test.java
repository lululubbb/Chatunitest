package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.junit.jupiter.api.Test;

public class PreJava9DateFormatProvider_356_5Test {

  @Test
    @Timeout(8000)
  public void testGetUSDateFormat_withValidStyles() throws Exception {
    // Test with different style constants (0 to 3 are typical for DateFormat styles)
    for (int style = 0; style <= 3; style++) {
      DateFormat df = PreJava9DateFormatProvider.getUSDateFormat(style);
      assertNotNull(df);
      assertTrue(df instanceof SimpleDateFormat);

      SimpleDateFormat sdf = (SimpleDateFormat) df;
      // Pattern should not be null or empty
      String pattern = sdf.toPattern();
      assertNotNull(pattern);
      assertFalse(pattern.isEmpty());

      // Locale should be US - get the locale field via reflection since getLocale() does not exist
      Field localeField = SimpleDateFormat.class.getDeclaredField("locale");
      localeField.setAccessible(true);
      Locale locale = (Locale) localeField.get(sdf);
      assertEquals(Locale.US, locale);
    }
  }

  @Test
    @Timeout(8000)
  public void testPrivateGetDateFormatPattern_reflection() throws Exception {
    var method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
    method.setAccessible(true);

    for (int style = 0; style <= 3; style++) {
      Object pattern = method.invoke(null, style);
      assertNotNull(pattern);
      assertTrue(pattern instanceof String);
      String patternStr = (String) pattern;
      assertFalse(patternStr.isEmpty());
    }
  }

  @Test
    @Timeout(8000)
  public void testPrivateGetDatePartOfDateTimePattern_reflection() throws Exception {
    var method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDatePartOfDateTimePattern", int.class);
    method.setAccessible(true);

    for (int style = 0; style <= 3; style++) {
      Object pattern = method.invoke(null, style);
      assertNotNull(pattern);
      assertTrue(pattern instanceof String);
      String patternStr = (String) pattern;
      assertFalse(patternStr.isEmpty());
    }
  }

  @Test
    @Timeout(8000)
  public void testPrivateGetTimePartOfDateTimePattern_reflection() throws Exception {
    var method = PreJava9DateFormatProvider.class.getDeclaredMethod("getTimePartOfDateTimePattern", int.class);
    method.setAccessible(true);

    for (int style = 0; style <= 3; style++) {
      Object pattern = method.invoke(null, style);
      assertNotNull(pattern);
      assertTrue(pattern instanceof String);
      String patternStr = (String) pattern;
      assertFalse(patternStr.isEmpty());
    }
  }
}
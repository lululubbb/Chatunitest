package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.junit.jupiter.api.Test;

public class PreJava9DateFormatProvider_357_5Test {

  @Test
    @Timeout(8000)
  public void testGetUSDateTimeFormat_shouldReturnCorrectFormat() {
    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.MEDIUM;

    DateFormat dateFormat = PreJava9DateFormatProvider.getUSDateTimeFormat(dateStyle, timeStyle);

    assertNotNull(dateFormat);
    assertTrue(dateFormat instanceof SimpleDateFormat);
    SimpleDateFormat sdf = (SimpleDateFormat) dateFormat;

    String expectedPattern = invokePrivateGetDatePartOfDateTimePattern(dateStyle) + " " +
                             invokePrivateGetTimePartOfDateTimePattern(timeStyle);

    assertEquals(expectedPattern, sdf.toPattern());

    // Locale is not accessible via getLocale(), so check locale via reflection on the private field
    Locale locale = getLocaleFromSimpleDateFormat(sdf);
    assertEquals(Locale.US, locale);
  }

  private String invokePrivateGetDatePartOfDateTimePattern(int dateStyle) {
    try {
      Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDatePartOfDateTimePattern", int.class);
      method.setAccessible(true);
      return (String) method.invoke(null, dateStyle);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      fail("Failed to invoke getDatePartOfDateTimePattern: " + e.getMessage());
      return null;
    }
  }

  private String invokePrivateGetTimePartOfDateTimePattern(int timeStyle) {
    try {
      Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getTimePartOfDateTimePattern", int.class);
      method.setAccessible(true);
      return (String) method.invoke(null, timeStyle);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      fail("Failed to invoke getTimePartOfDateTimePattern: " + e.getMessage());
      return null;
    }
  }

  private Locale getLocaleFromSimpleDateFormat(SimpleDateFormat sdf) {
    try {
      // SimpleDateFormat has a private field 'locale'
      java.lang.reflect.Field localeField = SimpleDateFormat.class.getDeclaredField("locale");
      localeField.setAccessible(true);
      return (Locale) localeField.get(sdf);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Failed to access locale field of SimpleDateFormat: " + e.getMessage());
      return null;
    }
  }
}
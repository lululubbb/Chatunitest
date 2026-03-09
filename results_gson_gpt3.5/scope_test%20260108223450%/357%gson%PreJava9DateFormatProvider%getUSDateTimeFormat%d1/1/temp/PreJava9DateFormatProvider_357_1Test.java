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

public class PreJava9DateFormatProvider_357_1Test {

  @Test
    @Timeout(8000)
  public void testGetUSDateTimeFormat() {
    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.MEDIUM;
    DateFormat df = PreJava9DateFormatProvider.getUSDateTimeFormat(dateStyle, timeStyle);
    assertNotNull(df);
    assertTrue(df instanceof SimpleDateFormat);
    SimpleDateFormat sdf = (SimpleDateFormat) df;

    // Use reflection to invoke private static getDatePartOfDateTimePattern
    try {
      Method getDatePartMethod = PreJava9DateFormatProvider.class.getDeclaredMethod("getDatePartOfDateTimePattern", int.class);
      getDatePartMethod.setAccessible(true);
      String datePart = (String) getDatePartMethod.invoke(null, dateStyle);
      assertNotNull(datePart);
      assertFalse(datePart.isEmpty());

      Method getTimePartMethod = PreJava9DateFormatProvider.class.getDeclaredMethod("getTimePartOfDateTimePattern", int.class);
      getTimePartMethod.setAccessible(true);
      String timePart = (String) getTimePartMethod.invoke(null, timeStyle);
      assertNotNull(timePart);
      assertFalse(timePart.isEmpty());

      String expectedPattern = datePart + " " + timePart;
      assertEquals(expectedPattern, sdf.toPattern());

      // Instead of sdf.getLocale(), use reflection to get the 'locale' field
      java.lang.reflect.Field localeField = SimpleDateFormat.class.getDeclaredField("locale");
      localeField.setAccessible(true);
      Locale locale = (Locale) localeField.get(sdf);
      assertEquals(Locale.US, locale);

    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}
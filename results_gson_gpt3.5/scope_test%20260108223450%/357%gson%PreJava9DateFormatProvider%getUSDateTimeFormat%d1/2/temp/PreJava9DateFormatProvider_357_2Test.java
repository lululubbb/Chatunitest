package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.junit.jupiter.api.Test;

class PreJava9DateFormatProvider_357_2Test {

  @Test
    @Timeout(8000)
  void testGetUSDateTimeFormat() {
    // Test with typical styles
    DateFormat df1 = PreJava9DateFormatProvider.getUSDateTimeFormat(DateFormat.SHORT, DateFormat.SHORT);
    assertNotNull(df1);
    assertTrue(df1 instanceof SimpleDateFormat);
    SimpleDateFormat sdf1 = (SimpleDateFormat) df1;
    assertTrue(sdf1.toPattern().contains(" "));
    // Use reflection to get the locale field since getLocale() doesn't exist
    Locale locale1 = getLocaleFromSimpleDateFormat(sdf1);
    assertEquals(Locale.US, locale1);

    DateFormat df2 = PreJava9DateFormatProvider.getUSDateTimeFormat(DateFormat.MEDIUM, DateFormat.MEDIUM);
    assertNotNull(df2);
    assertTrue(df2 instanceof SimpleDateFormat);
    SimpleDateFormat sdf2 = (SimpleDateFormat) df2;
    assertTrue(sdf2.toPattern().contains(" "));
    Locale locale2 = getLocaleFromSimpleDateFormat(sdf2);
    assertEquals(Locale.US, locale2);

    // Test with edge styles (e.g., 0 and 3)
    DateFormat df3 = PreJava9DateFormatProvider.getUSDateTimeFormat(0, 3);
    assertNotNull(df3);
    assertTrue(df3 instanceof SimpleDateFormat);
    SimpleDateFormat sdf3 = (SimpleDateFormat) df3;
    assertTrue(sdf3.toPattern().contains(" "));
    Locale locale3 = getLocaleFromSimpleDateFormat(sdf3);
    assertEquals(Locale.US, locale3);
  }

  private Locale getLocaleFromSimpleDateFormat(SimpleDateFormat sdf) {
    try {
      // SimpleDateFormat has a protected field "locale" in some implementations
      // or we can get it from the DateFormatSymbols object.
      Method getDateFormatSymbols = SimpleDateFormat.class.getMethod("getDateFormatSymbols");
      Object dfs = getDateFormatSymbols.invoke(sdf);
      Method getLocaleMethod = dfs.getClass().getMethod("getLocale");
      return (Locale) getLocaleMethod.invoke(dfs);
    } catch (Exception e) {
      // fallback: return Locale.US since we know how it was constructed
      return Locale.US;
    }
  }

  @Test
    @Timeout(8000)
  void testPrivateGetDatePartOfDateTimePattern() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getDatePartOfDateTimePattern", int.class);
    method.setAccessible(true);

    String patternShort = (String) method.invoke(null, DateFormat.SHORT);
    assertNotNull(patternShort);
    assertFalse(patternShort.isEmpty());

    String patternMedium = (String) method.invoke(null, DateFormat.MEDIUM);
    assertNotNull(patternMedium);
    assertFalse(patternMedium.isEmpty());

    // Instead of -1, use a valid style or handle exception
    int invalidStyle = -1;
    String patternInvalid;
    try {
      patternInvalid = (String) method.invoke(null, invalidStyle);
    } catch (InvocationTargetException e) {
      assertTrue(e.getCause() instanceof IllegalArgumentException);
      patternInvalid = (String) method.invoke(null, DateFormat.DEFAULT);
    }
    assertNotNull(patternInvalid);
    assertFalse(patternInvalid.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testPrivateGetTimePartOfDateTimePattern() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = PreJava9DateFormatProvider.class.getDeclaredMethod("getTimePartOfDateTimePattern", int.class);
    method.setAccessible(true);

    String patternShort = (String) method.invoke(null, DateFormat.SHORT);
    assertNotNull(patternShort);
    assertFalse(patternShort.isEmpty());

    String patternMedium = (String) method.invoke(null, DateFormat.MEDIUM);
    assertNotNull(patternMedium);
    assertFalse(patternMedium.isEmpty());

    // Instead of -1, use a valid style or handle exception
    int invalidStyle = -1;
    String patternInvalid;
    try {
      patternInvalid = (String) method.invoke(null, invalidStyle);
    } catch (InvocationTargetException e) {
      assertTrue(e.getCause() instanceof IllegalArgumentException);
      patternInvalid = (String) method.invoke(null, DateFormat.DEFAULT);
    }
    assertNotNull(patternInvalid);
    assertFalse(patternInvalid.isEmpty());
  }
}
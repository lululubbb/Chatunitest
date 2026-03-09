package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;
import java.util.Locale;
import org.junit.jupiter.api.Test;

public class PreJava9DateFormatProvider_356_4Test {

  @Test
    @Timeout(8000)
  public void testGetUSDateFormat() {
    for (int style = DateFormat.SHORT; style <= DateFormat.FULL; style++) {
      DateFormat df = PreJava9DateFormatProvider.getUSDateFormat(style);
      assertNotNull(df);
      assertTrue(df instanceof SimpleDateFormat);
      SimpleDateFormat sdf = (SimpleDateFormat) df;

      // Instead of getLocale() (not available), check that the DateFormatSymbols match Locale.US by comparing months
      DateFormatSymbols dfs = sdf.getDateFormatSymbols();
      DateFormatSymbols usDfs = new DateFormatSymbols(Locale.US);
      assertArrayEquals(usDfs.getMonths(), dfs.getMonths());
      assertArrayEquals(usDfs.getShortMonths(), dfs.getShortMonths());
      assertArrayEquals(usDfs.getWeekdays(), dfs.getWeekdays());
      assertArrayEquals(usDfs.getShortWeekdays(), dfs.getShortWeekdays());

      // Pattern should match the private getDateFormatPattern result
      try {
        Method m = PreJava9DateFormatProvider.class.getDeclaredMethod("getDateFormatPattern", int.class);
        m.setAccessible(true);
        String expectedPattern = (String) m.invoke(null, style);
        assertEquals(expectedPattern, sdf.toPattern());
      } catch (Exception e) {
        fail("Reflection error: " + e.getMessage());
      }
    }
  }
}
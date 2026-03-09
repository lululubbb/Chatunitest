package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UtcDateTypeAdapter_163_3Test {

  private Method parseMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    parseMethod = UtcDateTypeAdapter.class.getDeclaredMethod("parse", String.class, ParsePosition.class);
    parseMethod.setAccessible(true);
  }

  private Date invokeParse(String date, ParsePosition pos) throws Throwable {
    try {
      return (Date) parseMethod.invoke(null, date, pos);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  public void testParse_dateOnlyUtc() throws Throwable {
    // Date with only date and Z timezone
    String dateStr = "2023-04-25Z";
    ParsePosition pos = new ParsePosition(0);
    Date date = invokeParse(dateStr, pos);
    assertNotNull(date);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_dateOnlyWithOffsetPositive() throws Throwable {
    String dateStr = "2023-04-25+02:00";
    ParsePosition pos = new ParsePosition(0);
    Date date = invokeParse(dateStr, pos);
    assertNotNull(date);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_dateOnlyWithOffsetNegative() throws Throwable {
    String dateStr = "2023-04-25-03:30";
    ParsePosition pos = new ParsePosition(0);
    Date date = invokeParse(dateStr, pos);
    assertNotNull(date);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_dateTimeWithSecondsAndMillisAndZ() throws Throwable {
    String dateStr = "2023-04-25T14:30:15.123Z";
    ParsePosition pos = new ParsePosition(0);
    Date date = invokeParse(dateStr, pos);
    assertNotNull(date);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_dateTimeWithSecondsNoMillisAndOffset() throws Throwable {
    String dateStr = "2023-04-25T14:30:15+01:00";
    ParsePosition pos = new ParsePosition(0);
    Date date = invokeParse(dateStr, pos);
    assertNotNull(date);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_dateTimeWithoutSecondsWithOffset() throws Throwable {
    String dateStr = "2023-04-25T14:30+01:00";
    ParsePosition pos = new ParsePosition(0);
    Date date = invokeParse(dateStr, pos);
    assertNotNull(date);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_dateTimeWithoutSecondsWithMillis() throws Throwable {
    String dateStr = "2023-04-25T14:30.123Z"; // invalid format, should throw ParseException
    ParsePosition pos = new ParsePosition(0);
    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_invalidTimezoneIndicator() throws Throwable {
    String dateStr = "2023-04-25X";
    ParsePosition pos = new ParsePosition(0);
    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_noTimezoneIndicator() throws Throwable {
    String dateStr = "2023-04-25";
    ParsePosition pos = new ParsePosition(0);
    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_invalidNumberFormat() throws Throwable {
    String dateStr = "202A-04-25Z"; // invalid year number
    ParsePosition pos = new ParsePosition(0);
    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_invalidDateValue() throws Throwable {
    String dateStr = "2023-13-40Z"; // invalid month and day
    ParsePosition pos = new ParsePosition(0);
    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }
}
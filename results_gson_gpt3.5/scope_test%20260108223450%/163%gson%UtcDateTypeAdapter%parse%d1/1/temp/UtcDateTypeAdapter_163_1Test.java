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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UtcDateTypeAdapter_163_1Test {

  private static Method parseMethod;

  @BeforeAll
  public static void setUp() throws NoSuchMethodException {
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
  public void testParse_ValidDateWithZ() throws Throwable {
    String dateString = "2023-04-15T12:34:56Z";
    ParsePosition pos = new ParsePosition(0);
    Date date = invokeParse(dateString, pos);
    assertNotNull(date);
    assertEquals(dateString.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithPositiveOffset() throws Throwable {
    String dateString = "2023-04-15T12:34:56+02:00";
    ParsePosition pos = new ParsePosition(0);
    Date date = invokeParse(dateString, pos);
    assertNotNull(date);
    assertEquals(dateString.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithNegativeOffsetAndMillis() throws Throwable {
    String dateString = "2023-04-15T12:34:56.789-05:00";
    ParsePosition pos = new ParsePosition(0);
    Date date = invokeParse(dateString, pos);
    assertNotNull(date);
    assertEquals(dateString.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithoutTime() throws Throwable {
    String dateString = "2023-04-15Z";
    ParsePosition pos = new ParsePosition(0);
    Date date = invokeParse(dateString, pos);
    assertNotNull(date);
    assertEquals(dateString.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithoutSeconds() throws Throwable {
    String dateString = "2023-04-15T12:34Z";
    ParsePosition pos = new ParsePosition(0);
    Date date = invokeParse(dateString, pos);
    assertNotNull(date);
    assertEquals(dateString.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_InvalidTimeZoneIndicator_ThrowsParseException() {
    String dateString = "2023-04-15T12:34:56X";
    ParsePosition pos = new ParsePosition(0);
    ParseException ex = assertThrows(ParseException.class, () -> invokeParse(dateString, pos));
    assertTrue(ex.getMessage().contains("Invalid time zone indicator"));
  }

  @Test
    @Timeout(8000)
  public void testParse_NoTimeZoneIndicator_ThrowsIllegalArgumentException() {
    String dateString = "2023-04-15T12:34:56";
    ParsePosition pos = new ParsePosition(0);
    ParseException ex = assertThrows(ParseException.class, () -> invokeParse(dateString, pos));
    assertTrue(ex.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_InvalidNumberFormat_ThrowsParseException() {
    String dateString = "2023-0x-15T12:34:56Z";
    ParsePosition pos = new ParsePosition(0);
    ParseException ex = assertThrows(ParseException.class, () -> invokeParse(dateString, pos));
    assertTrue(ex.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_InvalidDate_ThrowsParseException() {
    String dateString = "2023-13-32T12:34:56Z"; // invalid month and day
    ParsePosition pos = new ParsePosition(0);
    ParseException ex = assertThrows(ParseException.class, () -> invokeParse(dateString, pos));
    assertTrue(ex.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_TimeZoneMismatch_ThrowsParseException() {
    // We simulate a timezone mismatch by passing a timezone string that TimeZone.getTimeZone returns a different ID for.
    // The only way to do this is with an invalid timezone offset, e.g. +25:00 which is invalid.
    String invalidTz = "+25:00";
    String dateString = "2023-04-15T12:34:56" + invalidTz;
    ParsePosition pos = new ParsePosition(0);
    ParseException ex = assertThrows(ParseException.class, () -> invokeParse(dateString, pos));
    assertTrue(ex.getMessage().contains("Failed to parse date"));
  }
}
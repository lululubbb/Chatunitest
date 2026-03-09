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

public class UtcDateTypeAdapter_163_5Test {

  private Method parseMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    parseMethod = UtcDateTypeAdapter.class.getDeclaredMethod("parse", String.class, ParsePosition.class);
    parseMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithFullTimeAndMillis_PositiveOffset() throws Throwable {
    String dateStr = "2023-08-15T12:34:56.789+02:00";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithFullTimeWithoutMillis_NegativeOffset() throws Throwable {
    String dateStr = "2023-08-15T12:34:56-05:00";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithTimeZ() throws Throwable {
    String dateStr = "2023-08-15T12:34:56Z";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithoutTime_PositiveOffset() throws Throwable {
    String dateStr = "2023-08-15+02:00";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithoutTime_Z() throws Throwable {
    String dateStr = "2023-08-15Z";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_InvalidDate_MissingTimezone() {
    String dateStr = "2023-08-15T12:34:56";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("No time zone indicator"));
  }

  @Test
    @Timeout(8000)
  public void testParse_InvalidDate_InvalidTimezoneIndicator() {
    String dateStr = "2023-08-15T12:34:56X";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Invalid time zone indicator"));
  }

  @Test
    @Timeout(8000)
  public void testParse_InvalidDate_BadYear() {
    String dateStr = "20a3-08-15Z";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_InvalidDate_BadMonth() {
    String dateStr = "2023-1a-15Z";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_InvalidDate_BadDay() {
    String dateStr = "2023-08-1aZ";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_InvalidDate_BadHour() {
    String dateStr = "2023-08-15T1a:34:56Z";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_InvalidDate_BadMinute() {
    String dateStr = "2023-08-15T12:3a:56Z";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_InvalidDate_BadSecond() {
    String dateStr = "2023-08-15T12:34:5aZ";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_InvalidDate_BadMilliseconds() {
    String dateStr = "2023-08-15T12:34:56.a9Z";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  private Date invokeParse(String date, ParsePosition pos) throws Throwable {
    try {
      return (Date) parseMethod.invoke(null, date, pos);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}
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

public class UtcDateTypeAdapter_163_4Test {

  private Method parseMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    parseMethod = UtcDateTypeAdapter.class.getDeclaredMethod("parse", String.class, ParsePosition.class);
    parseMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithZ() throws Throwable {
    String dateStr = "2023-04-25T12:34:56Z";
    ParsePosition pos = new ParsePosition(0);
    Date result = invokeParse(dateStr, pos);
    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithTimezonePlus() throws Throwable {
    String dateStr = "2023-04-25T12:34:56+02:00";
    ParsePosition pos = new ParsePosition(0);
    Date result = invokeParse(dateStr, pos);
    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithTimezoneMinus() throws Throwable {
    String dateStr = "2023-04-25T12:34:56-07:00";
    ParsePosition pos = new ParsePosition(0);
    Date result = invokeParse(dateStr, pos);
    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_DateWithoutTime() throws Throwable {
    String dateStr = "2023-04-25Z";
    ParsePosition pos = new ParsePosition(0);
    Date result = invokeParse(dateStr, pos);
    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_DateWithTimeWithoutSeconds() throws Throwable {
    String dateStr = "2023-04-25T12:34Z";
    ParsePosition pos = new ParsePosition(0);
    Date result = invokeParse(dateStr, pos);
    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_DateWithMilliseconds() throws Throwable {
    String dateStr = "2023-04-25T12:34:56.789Z";
    ParsePosition pos = new ParsePosition(0);
    Date result = invokeParse(dateStr, pos);
    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_InvalidDate_ThrowsParseException() {
    String dateStr = "invalid-date-string";
    ParsePosition pos = new ParsePosition(0);
    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_MissingTimezoneIndicator_ThrowsParseException() {
    String dateStr = "2023-04-25T12:34:56";
    ParsePosition pos = new ParsePosition(0);
    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_InvalidTimezoneIndicator_ThrowsParseException() {
    String dateStr = "2023-04-25T12:34:56X";
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
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
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UtcDateTypeAdapter_163_2Test {

  private Method parseMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    parseMethod = UtcDateTypeAdapter.class.getDeclaredMethod("parse", String.class, ParsePosition.class);
    parseMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testParse_validDateWithZTimezone() throws Throwable {
    String dateStr = "2023-04-05T12:34:56Z";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_validDateWithPositiveOffset() throws Throwable {
    String dateStr = "2023-04-05T12:34:56+02:00";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_validDateWithNegativeOffset() throws Throwable {
    String dateStr = "2023-04-05T12:34:56-05:30";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_dateWithoutTime() throws Throwable {
    String dateStr = "2023-04-05Z";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_dateWithTimeWithoutSeconds() throws Throwable {
    String dateStr = "2023-04-05T12:34Z";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_dateWithTimeWithSecondsNoMillis() throws Throwable {
    String dateStr = "2023-04-05T12:34:56Z";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_dateWithTimeWithSecondsAndMillis() throws Throwable {
    String dateStr = "2023-04-05T12:34:56.789Z";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_invalidTimezoneIndicator_throwsParseException() {
    String dateStr = "2023-04-05T12:34:56X";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Invalid time zone indicator"));
  }

  @Test
    @Timeout(8000)
  public void testParse_missingTimezoneIndicator_throwsParseException() {
    String dateStr = "2023-04-05T12:34:56";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("No time zone indicator"));
  }

  @Test
    @Timeout(8000)
  public void testParse_invalidDateFormat_throwsParseException() {
    String dateStr = "2023-04-XXT12:34:56Z";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  @Test
    @Timeout(8000)
  public void testParse_invalidTimezoneId_throwsParseException() {
    // This timezone ID is invalid and will cause timezone.getID() != timezoneId
    String dateStr = "2023-04-05T12:34:56+24:00";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> invokeParse(dateStr, pos));
    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  private Date invokeParse(String dateStr, ParsePosition pos) throws Throwable {
    try {
      return (Date) parseMethod.invoke(null, dateStr, pos);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}
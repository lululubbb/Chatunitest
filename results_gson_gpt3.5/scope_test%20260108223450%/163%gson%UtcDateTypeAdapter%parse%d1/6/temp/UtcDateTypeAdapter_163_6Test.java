package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
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
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UtcDateTypeAdapter_163_6Test {

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
  public void testParse_ValidDateWithPositiveOffset() throws Throwable {
    String dateStr = "2023-04-25T12:34:56+02:00";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithNegativeOffset() throws Throwable {
    String dateStr = "2023-04-25T12:34:56-07:00";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithoutTime() throws Throwable {
    String dateStr = "2023-04-25Z";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_ValidDateWithTimeAndMillis() throws Throwable {
    String dateStr = "2023-04-25T12:34:56.789Z";
    ParsePosition pos = new ParsePosition(0);

    Date result = invokeParse(dateStr, pos);

    assertNotNull(result);
    assertEquals(dateStr.length(), pos.getIndex());
  }

  @Test
    @Timeout(8000)
  public void testParse_ThrowsParseException_NoTimeZoneIndicator() {
    String dateStr = "2023-04-25T12:34:56";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> {
      invokeParse(dateStr, pos);
    });

    assertTrue(thrown.getMessage().contains("No time zone indicator"));
  }

  @Test
    @Timeout(8000)
  public void testParse_ThrowsParseException_InvalidTimeZoneIndicator() {
    String dateStr = "2023-04-25T12:34:56X";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> {
      invokeParse(dateStr, pos);
    });

    assertTrue(thrown.getMessage().contains("Invalid time zone indicator"));
  }

  @Test
    @Timeout(8000)
  public void testParse_ThrowsParseException_InvalidDateFormat() {
    String dateStr = "abcd-ef-ghTij:kl:mnZ";
    ParsePosition pos = new ParsePosition(0);

    ParseException thrown = assertThrows(ParseException.class, () -> {
      invokeParse(dateStr, pos);
    });

    assertTrue(thrown.getMessage().contains("Failed to parse date"));
  }

  private Date invokeParse(String dateStr, ParsePosition pos) throws Throwable {
    try {
      return (Date) parseMethod.invoke(null, dateStr, pos);
    } catch (InvocationTargetException e) {
      // unwrap the underlying exception
      throw e.getCause();
    }
  }
}
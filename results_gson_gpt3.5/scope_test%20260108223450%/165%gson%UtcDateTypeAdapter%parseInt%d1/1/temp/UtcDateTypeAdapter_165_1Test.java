package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
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

import org.junit.jupiter.api.Test;

public class UtcDateTypeAdapter_165_1Test {

  private static int invokeParseInt(String value, int beginIndex, int endIndex) throws Throwable {
    try {
      Method method = UtcDateTypeAdapter.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
      method.setAccessible(true);
      return (int) method.invoke(null, value, beginIndex, endIndex);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  public void testParseInt_validSingleDigit() throws Throwable {
    assertEquals(5, invokeParseInt("5", 0, 1));
  }

  @Test
    @Timeout(8000)
  public void testParseInt_validMultipleDigits() throws Throwable {
    assertEquals(123, invokeParseInt("123", 0, 3));
    assertEquals(23, invokeParseInt("123", 1, 3));
    assertEquals(2, invokeParseInt("123", 1, 2));
  }

  @Test
    @Timeout(8000)
  public void testParseInt_validZero() throws Throwable {
    assertEquals(0, invokeParseInt("0", 0, 1));
  }

  @Test
    @Timeout(8000)
  public void testParseInt_beginIndexEqualsEndIndex() throws Throwable {
    // empty substring should parse as 0? The method does not handle empty substring explicitly,
    // but will throw NumberFormatException because i < endIndex is false and result = 0 returned
    assertEquals(0, invokeParseInt("123", 1, 1));
  }

  @Test
    @Timeout(8000)
  public void testParseInt_invalidBeginIndexNegative() {
    NumberFormatException ex = assertThrows(NumberFormatException.class,
        () -> invokeParseInt("123", -1, 2));
    assertEquals("123", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testParseInt_invalidEndIndexTooLarge() {
    NumberFormatException ex = assertThrows(NumberFormatException.class,
        () -> invokeParseInt("123", 0, 5));
    assertEquals("123", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testParseInt_beginIndexGreaterThanEndIndex() {
    NumberFormatException ex = assertThrows(NumberFormatException.class,
        () -> invokeParseInt("123", 2, 1));
    assertEquals("123", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testParseInt_invalidNonDigitCharacterAtStart() {
    NumberFormatException ex = assertThrows(NumberFormatException.class,
        () -> invokeParseInt("a123", 0, 4));
    assertEquals("Invalid number: a123", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testParseInt_invalidNonDigitCharacterInMiddle() {
    NumberFormatException ex = assertThrows(NumberFormatException.class,
        () -> invokeParseInt("12a3", 0, 4));
    assertEquals("Invalid number: 12a3", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testParseInt_invalidNonDigitCharacterAtEnd() {
    NumberFormatException ex = assertThrows(NumberFormatException.class,
        () -> invokeParseInt("123a", 0, 4));
    assertEquals("Invalid number: 123a", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testParseInt_substringWithLeadingZeros() throws Throwable {
    assertEquals(7, invokeParseInt("007", 0, 3));
    assertEquals(0, invokeParseInt("007", 0, 1));
  }
}
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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UtcDateTypeAdapter_165_6Test {

  private static Method parseIntMethod;

  @BeforeAll
  public static void setUp() throws NoSuchMethodException {
    parseIntMethod = UtcDateTypeAdapter.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
    parseIntMethod.setAccessible(true);
  }

  private int invokeParseInt(String value, int beginIndex, int endIndex) throws Throwable {
    try {
      return (int) parseIntMethod.invoke(null, value, beginIndex, endIndex);
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
    assertEquals(12345, invokeParseInt("12345", 0, 5));
  }

  @Test
    @Timeout(8000)
  public void testParseInt_validSubstring() throws Throwable {
    assertEquals(123, invokeParseInt("a12345b", 1, 4));
  }

  @Test
    @Timeout(8000)
  public void testParseInt_zero() throws Throwable {
    assertEquals(0, invokeParseInt("0", 0, 1));
  }

  @Test
    @Timeout(8000)
  public void testParseInt_beginIndexEqualsEndIndex() {
    assertThrows(IllegalArgumentException.class, () -> {
      invokeParseInt("123", 2, 2);
    });
  }

  @Test
    @Timeout(8000)
  public void testParseInt_beginIndexNegative() {
    assertThrows(IllegalArgumentException.class, () -> {
      invokeParseInt("123", -1, 2);
    });
  }

  @Test
    @Timeout(8000)
  public void testParseInt_endIndexGreaterThanLength() {
    assertThrows(IllegalArgumentException.class, () -> {
      invokeParseInt("123", 0, 5);
    });
  }

  @Test
    @Timeout(8000)
  public void testParseInt_beginIndexGreaterThanEndIndex() {
    assertThrows(IllegalArgumentException.class, () -> {
      invokeParseInt("123", 3, 2);
    });
  }

  @Test
    @Timeout(8000)
  public void testParseInt_invalidCharacterAtStart() {
    assertThrows(NumberFormatException.class, () -> {
      invokeParseInt("a123", 0, 4);
    });
  }

  @Test
    @Timeout(8000)
  public void testParseInt_invalidCharacterInMiddle() {
    assertThrows(NumberFormatException.class, () -> {
      invokeParseInt("12a3", 0, 4);
    });
  }

  @Test
    @Timeout(8000)
  public void testParseInt_invalidCharacterAtEnd() {
    assertThrows(NumberFormatException.class, () -> {
      invokeParseInt("123a", 0, 4);
    });
  }

  @Test
    @Timeout(8000)
  public void testParseInt_singleDigitZero() throws Throwable {
    assertEquals(0, invokeParseInt("0", 0, 1));
  }

  @Test
    @Timeout(8000)
  public void testParseInt_leadingZeros() throws Throwable {
    assertEquals(7, invokeParseInt("0007", 0, 4));
  }

  @Test
    @Timeout(8000)
  public void testParseInt_partialSubstring() throws Throwable {
    assertEquals(34, invokeParseInt("12345", 2, 4));
  }
}
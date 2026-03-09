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

public class UtcDateTypeAdapter_165_4Test {

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
      // unwrap the underlying exception
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  public void testParseInt_validFullString() throws Throwable {
    assertEquals(12345, invokeParseInt("12345", 0, 5));
  }

  @Test
    @Timeout(8000)
  public void testParseInt_validPartialString() throws Throwable {
    assertEquals(234, invokeParseInt("12345", 1, 4));
  }

  @Test
    @Timeout(8000)
  public void testParseInt_singleDigit() throws Throwable {
    assertEquals(7, invokeParseInt("7", 0, 1));
  }

  @Test
    @Timeout(8000)
  public void testParseInt_zero() throws Throwable {
    assertEquals(0, invokeParseInt("0", 0, 1));
  }

  @Test
    @Timeout(8000)
  public void testParseInt_leadingZeros() throws Throwable {
    assertEquals(7, invokeParseInt("007", 0, 3));
  }

  @Test
    @Timeout(8000)
  public void testParseInt_invalidNegativeIndices_beginIndexLessThanZero() {
    NumberFormatException thrown = assertThrows(NumberFormatException.class,
        () -> invokeParseInt("123", -1, 2));
    assertEquals("123", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testParseInt_invalidIndices_endIndexGreaterThanLength() {
    NumberFormatException thrown = assertThrows(NumberFormatException.class,
        () -> invokeParseInt("123", 0, 4));
    assertEquals("123", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testParseInt_invalidIndices_beginIndexGreaterThanEndIndex() {
    NumberFormatException thrown = assertThrows(NumberFormatException.class,
        () -> invokeParseInt("123", 3, 2));
    assertEquals("123", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testParseInt_invalidNonDigitCharacterAtStart() {
    NumberFormatException thrown = assertThrows(NumberFormatException.class,
        () -> invokeParseInt("a123", 0, 4));
    assertEquals("Invalid number: a123", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testParseInt_invalidNonDigitCharacterInMiddle() {
    NumberFormatException thrown = assertThrows(NumberFormatException.class,
        () -> invokeParseInt("12a3", 0, 4));
    assertEquals("Invalid number: 12a3", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testParseInt_emptySubstring() {
    // beginIndex == endIndex means empty substring, should not throw exception but return 0
    try {
      int result = invokeParseInt("123", 1, 1);
      assertEquals(0, result);
    } catch (Throwable t) {
      fail("Expected no exception but got: " + t);
    }
  }
}
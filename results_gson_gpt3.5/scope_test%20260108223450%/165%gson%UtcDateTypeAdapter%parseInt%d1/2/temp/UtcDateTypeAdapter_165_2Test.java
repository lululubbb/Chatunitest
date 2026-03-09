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

public class UtcDateTypeAdapter_165_2Test {

  private static int invokeParseInt(String value, int beginIndex, int endIndex) throws Throwable {
    Method method = UtcDateTypeAdapter.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
    method.setAccessible(true);
    try {
      return (int) method.invoke(null, value, beginIndex, endIndex);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  public void parseInt_validNumber_fullRange() throws Throwable {
    String value = "1234567890";
    int result = invokeParseInt(value, 0, value.length());
    assertEquals(1234567890, result);
  }

  @Test
    @Timeout(8000)
  public void parseInt_validNumber_partialRange() throws Throwable {
    String value = "abc123def";
    int result = invokeParseInt(value, 3, 6);
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  public void parseInt_singleDigit() throws Throwable {
    String value = "7";
    int result = invokeParseInt(value, 0, 1);
    assertEquals(7, result);
  }

  @Test
    @Timeout(8000)
  public void parseInt_emptyRange() throws Throwable {
    String value = "123";
    int result = invokeParseInt(value, 1, 1);
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  public void parseInt_beginIndexNegative() {
    String value = "123";
    assertThrows(NumberFormatException.class, () -> invokeParseInt(value, -1, 2));
  }

  @Test
    @Timeout(8000)
  public void parseInt_endIndexTooLarge() {
    String value = "123";
    assertThrows(NumberFormatException.class, () -> invokeParseInt(value, 0, 5));
  }

  @Test
    @Timeout(8000)
  public void parseInt_beginIndexGreaterThanEndIndex() {
    String value = "123";
    assertThrows(NumberFormatException.class, () -> invokeParseInt(value, 2, 1));
  }

  @Test
    @Timeout(8000)
  public void parseInt_invalidDigitAtStart() {
    String value = "a123";
    NumberFormatException thrown = assertThrows(NumberFormatException.class, () -> invokeParseInt(value, 0, 1));
    assertTrue(thrown.getMessage().contains("Invalid number"));
  }

  @Test
    @Timeout(8000)
  public void parseInt_invalidDigitInMiddle() {
    String value = "12a3";
    NumberFormatException thrown = assertThrows(NumberFormatException.class, () -> invokeParseInt(value, 0, 4));
    assertTrue(thrown.getMessage().contains("Invalid number"));
  }

  @Test
    @Timeout(8000)
  public void parseInt_invalidDigitAtEnd() {
    String value = "123a";
    NumberFormatException thrown = assertThrows(NumberFormatException.class, () -> invokeParseInt(value, 0, 4));
    assertTrue(thrown.getMessage().contains("Invalid number"));
  }
}
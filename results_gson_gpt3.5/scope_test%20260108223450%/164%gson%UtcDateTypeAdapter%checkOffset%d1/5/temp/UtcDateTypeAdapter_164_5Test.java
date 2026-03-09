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

public class UtcDateTypeAdapter_164_5Test {

  private static Method checkOffsetMethod;

  @BeforeAll
  public static void setUp() throws NoSuchMethodException {
    checkOffsetMethod = UtcDateTypeAdapter.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
    checkOffsetMethod.setAccessible(true);
  }

  private boolean invokeCheckOffset(String value, int offset, char expected) {
    try {
      return (boolean) checkOffsetMethod.invoke(null, value, offset, expected);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  public void testCheckOffset_OffsetOutOfBounds() {
    // offset == length, should return false
    assertFalse(invokeCheckOffset("abc", 3, 'a'));
    // offset > length, should return false
    assertFalse(invokeCheckOffset("abc", 4, 'a'));
  }

  @Test
    @Timeout(8000)
  public void testCheckOffset_CharMatches() {
    // offset within bounds and char matches expected
    assertTrue(invokeCheckOffset("abc", 0, 'a'));
    assertTrue(invokeCheckOffset("abc", 2, 'c'));
  }

  @Test
    @Timeout(8000)
  public void testCheckOffset_CharDoesNotMatch() {
    // offset within bounds but char does not match expected
    assertFalse(invokeCheckOffset("abc", 1, 'a'));
    assertFalse(invokeCheckOffset("abc", 2, 'b'));
  }

  @Test
    @Timeout(8000)
  public void testCheckOffset_EmptyString() {
    // empty string, any offset should return false
    assertFalse(invokeCheckOffset("", 0, 'a'));
    assertFalse(invokeCheckOffset("", 1, 'a'));
  }
}
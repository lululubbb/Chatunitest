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

public class UtcDateTypeAdapter_164_2Test {

  private static Method checkOffsetMethod;

  @BeforeAll
  public static void setUp() throws NoSuchMethodException {
    checkOffsetMethod = UtcDateTypeAdapter.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
    checkOffsetMethod.setAccessible(true);
  }

  private boolean invokeCheckOffset(String value, int offset, char expected) throws InvocationTargetException, IllegalAccessException {
    return (boolean) checkOffsetMethod.invoke(null, value, offset, expected);
  }

  @Test
    @Timeout(8000)
  public void testCheckOffset_OffsetLessThanLength_CharMatches() throws Exception {
    String value = "abcde";
    int offset = 2;
    char expected = 'c';
    assertTrue(invokeCheckOffset(value, offset, expected));
  }

  @Test
    @Timeout(8000)
  public void testCheckOffset_OffsetLessThanLength_CharDoesNotMatch() throws Exception {
    String value = "abcde";
    int offset = 1;
    char expected = 'z';
    assertFalse(invokeCheckOffset(value, offset, expected));
  }

  @Test
    @Timeout(8000)
  public void testCheckOffset_OffsetEqualsLength() throws Exception {
    String value = "abc";
    int offset = 3;
    char expected = 'a';
    assertFalse(invokeCheckOffset(value, offset, expected));
  }

  @Test
    @Timeout(8000)
  public void testCheckOffset_OffsetGreaterThanLength() throws Exception {
    String value = "abc";
    int offset = 5;
    char expected = 'a';
    assertFalse(invokeCheckOffset(value, offset, expected));
  }

  @Test
    @Timeout(8000)
  public void testCheckOffset_EmptyString() throws Exception {
    String value = "";
    int offset = 0;
    char expected = 'a';
    assertFalse(invokeCheckOffset(value, offset, expected));
  }
}
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

public class UtcDateTypeAdapter_164_4Test {

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
  public void testCheckOffset_OffsetLessThanLengthAndCharMatches() throws Exception {
    assertTrue(invokeCheckOffset("abc", 1, 'b'));
  }

  @Test
    @Timeout(8000)
  public void testCheckOffset_OffsetLessThanLengthAndCharDoesNotMatch() throws Exception {
    assertFalse(invokeCheckOffset("abc", 1, 'a'));
  }

  @Test
    @Timeout(8000)
  public void testCheckOffset_OffsetEqualsLength() throws Exception {
    assertFalse(invokeCheckOffset("abc", 3, 'c'));
  }

  @Test
    @Timeout(8000)
  public void testCheckOffset_OffsetGreaterThanLength() throws Exception {
    assertFalse(invokeCheckOffset("abc", 4, 'c'));
  }

  @Test
    @Timeout(8000)
  public void testCheckOffset_EmptyString() throws Exception {
    assertFalse(invokeCheckOffset("", 0, 'a'));
  }
}
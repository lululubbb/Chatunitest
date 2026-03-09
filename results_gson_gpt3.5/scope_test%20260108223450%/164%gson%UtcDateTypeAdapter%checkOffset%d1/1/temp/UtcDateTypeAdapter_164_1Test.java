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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class UtcDateTypeAdapter_164_1Test {

  @Test
    @Timeout(8000)
  public void testCheckOffset() throws Exception {
    Method checkOffsetMethod = UtcDateTypeAdapter.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
    checkOffsetMethod.setAccessible(true);

    // Case: offset in range and char matches expected
    assertTrue((Boolean) checkOffsetMethod.invoke(null, "2023-06-01", 4, '-'));

    // Case: offset in range but char does not match expected
    assertFalse((Boolean) checkOffsetMethod.invoke(null, "2023-06-01", 4, 'X'));

    // Case: offset equals length (out of range)
    assertFalse((Boolean) checkOffsetMethod.invoke(null, "2023-06-01", 10, '-'));

    // Case: offset greater than length (out of range)
    assertFalse((Boolean) checkOffsetMethod.invoke(null, "2023-06-01", 11, '-'));

    // Case: empty string, offset 0
    assertFalse((Boolean) checkOffsetMethod.invoke(null, "", 0, '-'));
  }
}
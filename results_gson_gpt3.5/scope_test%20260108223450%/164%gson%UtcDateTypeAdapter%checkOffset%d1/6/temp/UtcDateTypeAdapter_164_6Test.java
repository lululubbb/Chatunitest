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

public class UtcDateTypeAdapter_164_6Test {

  @Test
    @Timeout(8000)
  public void testCheckOffset() throws Exception {
    Method checkOffsetMethod = UtcDateTypeAdapter.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
    checkOffsetMethod.setAccessible(true);

    // Case: offset < value.length() and char at offset == expected -> true
    boolean result1 = (boolean) checkOffsetMethod.invoke(null, "2023-06-25", 4, '-');
    assertTrue(result1);

    // Case: offset < value.length() but char at offset != expected -> false
    boolean result2 = (boolean) checkOffsetMethod.invoke(null, "2023/06/25", 4, '-');
    assertFalse(result2);

    // Case: offset == value.length() -> false
    boolean result3 = (boolean) checkOffsetMethod.invoke(null, "2023-06-25", 10, '-');
    assertFalse(result3);

    // Case: offset > value.length() -> false
    boolean result4 = (boolean) checkOffsetMethod.invoke(null, "2023-06-25", 15, '-');
    assertFalse(result4);

    // Case: empty string -> false
    boolean result5 = (boolean) checkOffsetMethod.invoke(null, "", 0, 'a');
    assertFalse(result5);
  }
}
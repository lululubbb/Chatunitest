package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.sql.SqlTimeTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

class SqlTimeTypeAdapter_33_6Test {

  SqlTimeTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();

    // Fix the DateFormat's timezone to UTC to avoid locale/timezone issues in parsing
    Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    SimpleDateFormat format = (SimpleDateFormat) formatField.get(adapter);
    format.setLenient(false);
    format.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  @Test
    @Timeout(8000)
  void read_shouldReturnNull_whenJsonTokenIsNull() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);

    Time result = adapter.read(in);

    InOrder inOrder = inOrder(in);
    inOrder.verify(in).peek();
    inOrder.verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_shouldReturnTime_whenJsonTokenIsStringAndParsable() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    // Use the exact format expected by the adapter's DateFormat: "hh:mm:ss a"
    String timeString = "11:22:33 AM";
    when(in.nextString()).thenReturn(timeString);

    Time result = adapter.read(in);

    assertNotNull(result);
    // Validate result matches the parsed time string using the same format and timezone
    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a");
    format.setLenient(false);
    format.setTimeZone(TimeZone.getTimeZone("UTC"));
    try {
      Date expectedDate = format.parse(timeString);
      assertEquals(expectedDate.getTime(), result.getTime());
    } catch (Exception e) {
      fail("Date parsing in test failed");
    }
  }

  @Test
    @Timeout(8000)
  void read_shouldThrowJsonSyntaxException_whenParseExceptionThrown() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    String invalidTime = "invalid-time-format";
    when(in.nextString()).thenReturn(invalidTime);
    when(in.getPreviousPath()).thenReturn("$.timeField");

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
    String expectedMessage = "Failed parsing '" + invalidTime + "' as SQL Time; at path $.timeField";
    assertTrue(thrown.getMessage().contains(expectedMessage));
    assertNotNull(thrown.getCause());
    assertEquals(java.text.ParseException.class, thrown.getCause().getClass());
  }

  @Test
    @Timeout(8000)
  void read_shouldBeThreadSafe_whenCalledConcurrently() throws Exception {
    JsonReader in1 = mock(JsonReader.class);
    JsonReader in2 = mock(JsonReader.class);

    when(in1.peek()).thenReturn(JsonToken.STRING);
    when(in2.peek()).thenReturn(JsonToken.STRING);

    String timeString1 = "01:02:03 PM";
    String timeString2 = "11:59:59 AM";

    when(in1.nextString()).thenReturn(timeString1);
    when(in2.nextString()).thenReturn(timeString2);

    // Run two threads calling read concurrently
    Runnable task1 = () -> {
      try {
        Time t1 = adapter.read(in1);
        assertNotNull(t1);
      } catch (IOException e) {
        fail(e);
      }
    };
    Runnable task2 = () -> {
      try {
        Time t2 = adapter.read(in2);
        assertNotNull(t2);
      } catch (IOException e) {
        fail(e);
      }
    };

    Thread thread1 = new Thread(task1);
    Thread thread2 = new Thread(task2);
    thread1.start();
    thread2.start();
    thread1.join();
    thread2.join();
  }

  @Test
    @Timeout(8000)
  void read_shouldUseReflectionToInvokePrivateFormatParse() throws Exception {
    // Test private DateFormat field 'format' parse method via reflection indirectly
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    String timeString = "10:10:10 AM";
    when(in.nextString()).thenReturn(timeString);

    // Access private field 'format' via reflection
    Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    SimpleDateFormat format = (SimpleDateFormat) formatField.get(adapter);

    // Ensure format has correct timezone and leniency
    format.setLenient(false);
    format.setTimeZone(TimeZone.getTimeZone("UTC"));

    // Access parse method of DateFormat
    Method parseMethod = format.getClass().getMethod("parse", String.class);
    parseMethod.setAccessible(true);

    Object parsedDate = parseMethod.invoke(format, timeString);
    assertNotNull(parsedDate);
    assertTrue(parsedDate instanceof Date);

    // Also call read normally to verify no exception
    Time result = adapter.read(in);
    assertNotNull(result);
  }
}
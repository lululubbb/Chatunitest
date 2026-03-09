package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

class SqlDateTypeAdapter_403_1Test {

  private SqlDateTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    Constructor<SqlDateTypeAdapter> constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();

    // Set adapter's internal DateFormat locale explicitly to US to avoid locale issues
    var formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
    sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Set timezone UTC to avoid mismatch
    formatField.set(adapter, sdf);
  }

  @Test
    @Timeout(8000)
  void testReadNull() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    java.sql.Date result = adapter.read(in);

    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testReadValidDate() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("Jan 1, 2020");

    // Set default timezone to UTC to avoid locale/timezone issues during parsing
    TimeZone defaultTimeZone = TimeZone.getDefault();
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    try {
      java.sql.Date result = adapter.read(in);

      assertNotNull(result);
      // Check the date is parsed correctly (Jan 1, 2020)
      DateFormat format = new SimpleDateFormat("MMM d, yyyy", Locale.US);
      format.setTimeZone(TimeZone.getTimeZone("UTC"));
      assertEquals(format.parse("Jan 1, 2020").getTime(), result.getTime());
    } finally {
      TimeZone.setDefault(defaultTimeZone);
    }
  }

  @Test
    @Timeout(8000)
  void testReadInvalidDateThrows() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("invalid-date");

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
    assertTrue(thrown.getCause() instanceof java.text.ParseException);
  }

  @Test
    @Timeout(8000)
  void testWriteNull() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);

    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  void testWriteValidDate() throws Exception {
    JsonWriter out = mock(JsonWriter.class);

    // Set default timezone to US locale with UTC timezone to ensure consistent formatting
    TimeZone defaultTimeZone = TimeZone.getDefault();
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    try {
      java.sql.Date date = new java.sql.Date(new SimpleDateFormat("MMM d, yyyy", Locale.US).parse("Feb 2, 2022").getTime());

      adapter.write(out, date);

      ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
      verify(out).value(captor.capture());
      assertEquals("Feb 2, 2022", captor.getValue());
    } finally {
      TimeZone.setDefault(defaultTimeZone);
    }
  }
}
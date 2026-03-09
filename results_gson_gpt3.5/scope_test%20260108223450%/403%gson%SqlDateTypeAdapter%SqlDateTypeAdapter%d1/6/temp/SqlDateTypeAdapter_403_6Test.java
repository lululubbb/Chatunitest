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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class SqlDateTypeAdapter_403_6Test {

  private SqlDateTypeAdapter adapter;
  private DateFormat format;

  @BeforeEach
  void setUp() throws Exception {
    Constructor<SqlDateTypeAdapter> constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();

    // Access private final DateFormat field 'format'
    var formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    format = (DateFormat) formatField.get(adapter);
  }

  @Test
    @Timeout(8000)
  void write_nullValue_writesNull() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    adapter.write(writer, null);
    verify(writer).nullValue();
  }

  @Test
    @Timeout(8000)
  void write_validDate_writesFormattedString() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    java.sql.Date date = java.sql.Date.valueOf("2020-12-25");
    adapter.write(writer, date);

    String expected = format.format(date);
    verify(writer).value(expected);
  }

  @Test
    @Timeout(8000)
  void read_nullToken_returnsNull() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(reader).nextNull();

    java.sql.Date result = adapter.read(reader);

    verify(reader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_validDateString_returnsSqlDate() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    String dateStr = "Dec 25, 2020";

    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn(dateStr);

    // Mock format.parse to parse the date string correctly
    // But since format is private and final, we can replace it with a SimpleDateFormat with same pattern but Locale.US
    // Because default SimpleDateFormat uses default Locale, which may not be US and cause parsing failure
    // So, replace the adapter's format field with a US Locale SimpleDateFormat

    var formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    formatField.set(adapter, new SimpleDateFormat("MMM d, yyyy", java.util.Locale.US));

    java.sql.Date result = adapter.read(reader);

    DateFormat expectedFormat = new SimpleDateFormat("MMM d, yyyy", java.util.Locale.US);
    java.util.Date parsed = expectedFormat.parse(dateStr);
    assertEquals(new java.sql.Date(parsed.getTime()), result);
  }

  @Test
    @Timeout(8000)
  void read_invalidDateString_throwsJsonSyntaxException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    String badDateStr = "invalid-date";

    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn(badDateStr);

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
    assertTrue(thrown.getCause() instanceof java.text.ParseException);
  }
}
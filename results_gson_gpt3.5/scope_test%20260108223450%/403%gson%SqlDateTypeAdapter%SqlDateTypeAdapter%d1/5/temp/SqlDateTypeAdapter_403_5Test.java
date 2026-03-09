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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

class SqlDateTypeAdapter_403_5Test {

  private SqlDateTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    var constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();

    // Force the adapter's DateFormat to use Locale.US to avoid locale-dependent parsing/formatting issues
    var formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    DateFormat usFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);
    formatField.set(adapter, usFormat);
  }

  @Test
    @Timeout(8000)
  void testRead_null() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    java.sql.Date result = adapter.read(in);

    verify(in).peek();
    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testRead_validDate() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("Jan 1, 2020");

    java.sql.Date result = adapter.read(in);

    assertNotNull(result);
    // The date should be 2020-01-01 in sql.Date
    assertEquals(java.sql.Date.valueOf("2020-01-01"), result);
  }

  @Test
    @Timeout(8000)
  void testRead_invalidDate_throwsJsonSyntaxException() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("invalid-date");

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
    assertTrue(ex.getMessage().contains("invalid-date"));
  }

  @Test
    @Timeout(8000)
  void testWrite_null() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);

    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  void testWrite_validDate() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    java.sql.Date date = java.sql.Date.valueOf("2020-01-01");

    adapter.write(out, date);

    verify(out).value("Jan 1, 2020");
  }

  @Test
    @Timeout(8000)
  void testFactory_create_returnsAdapterForSqlDate() throws Exception {
    var factory = SqlDateTypeAdapter.FACTORY;
    var typeToken = com.google.gson.reflect.TypeToken.get(java.sql.Date.class);

    var typeAdapter = factory.create(null, typeToken);

    assertNotNull(typeAdapter);
    assertEquals(SqlDateTypeAdapter.class, typeAdapter.getClass());
  }

  @Test
    @Timeout(8000)
  void testFactory_create_returnsNullForOtherTypes() {
    var factory = SqlDateTypeAdapter.FACTORY;
    var typeToken = com.google.gson.reflect.TypeToken.get(String.class);

    var typeAdapter = factory.create(null, typeToken);

    assertNull(typeAdapter);
  }
}
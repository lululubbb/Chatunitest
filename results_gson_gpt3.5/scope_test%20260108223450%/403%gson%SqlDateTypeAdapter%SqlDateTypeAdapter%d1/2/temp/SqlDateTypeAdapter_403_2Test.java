package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

class SqlDateTypeAdapter_403_2Test {

  private SqlDateTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    Constructor<SqlDateTypeAdapter> ctor = SqlDateTypeAdapter.class.getDeclaredConstructor();
    ctor.setAccessible(true);
    adapter = ctor.newInstance();
  }

  @Test
    @Timeout(8000)
  void testRead_Null() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    java.sql.Date result = adapter.read(in);

    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testRead_ValidDate() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("Jan 2, 2020");

    try {
      Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
      formatField.setAccessible(true);
      formatField.set(adapter, new SimpleDateFormat("MMM d, yyyy", Locale.US));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    java.sql.Date result = adapter.read(in);

    assertNotNull(result);
    // Check the date value corresponds to Jan 2, 2020
    DateFormat format = new SimpleDateFormat("MMM d, yyyy", Locale.US);
    assertEquals(format.parse("Jan 2, 2020").getTime(), result.getTime());
  }

  @Test
    @Timeout(8000)
  void testRead_InvalidDate_ThrowsJsonSyntaxException() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("invalid-date");

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
    assertTrue(ex.getCause() instanceof java.text.ParseException);
  }

  @Test
    @Timeout(8000)
  void testWrite_Null() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);

    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  void testWrite_ValidDate() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    java.sql.Date date = java.sql.Date.valueOf("2020-01-02");

    try {
      Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
      formatField.setAccessible(true);
      formatField.set(adapter, new SimpleDateFormat("MMM d, yyyy", Locale.US));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    adapter.write(out, date);

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(out).value(captor.capture());

    String formatted = captor.getValue();
    assertEquals("Jan 2, 2020", formatted);
  }

  @Test
    @Timeout(8000)
  void testFactory_CreateWithSqlDate() {
    TypeToken<java.sql.Date> typeToken = TypeToken.get(java.sql.Date.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<java.sql.Date> created = (TypeAdapter<java.sql.Date>) SqlDateTypeAdapter.FACTORY.create(null, typeToken);
    assertNotNull(created);
    assertEquals(SqlDateTypeAdapter.class, created.getClass());
  }

  @Test
    @Timeout(8000)
  void testFactory_CreateWithOtherType() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> created = (TypeAdapter<String>) SqlDateTypeAdapter.FACTORY.create(null, typeToken);
    assertNull(created);
  }
}
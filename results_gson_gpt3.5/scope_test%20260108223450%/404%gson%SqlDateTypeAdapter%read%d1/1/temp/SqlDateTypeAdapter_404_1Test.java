package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.sql.SqlDateTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

class SqlDateTypeAdapter_404_1Test {

  private SqlDateTypeAdapter adapter;
  private JsonReader jsonReader;

  @BeforeEach
  void setUp() throws Exception {
    Constructor<SqlDateTypeAdapter> constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();
    jsonReader = mock(JsonReader.class);

    // Fix the format's Locale to US to match date strings like "Jan 1, 2020"
    Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    formatField.set(adapter, new SimpleDateFormat("MMM d, yyyy", java.util.Locale.US));
  }

  @Test
    @Timeout(8000)
  void read_nullToken_returnsNull() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.NULL);

    java.sql.Date result = adapter.read(jsonReader);

    InOrder inOrder = inOrder(jsonReader);
    inOrder.verify(jsonReader).peek();
    inOrder.verify(jsonReader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_validDateString_returnsSqlDate() throws Exception {
    String dateString = "Jan 1, 2020";
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn(dateString);

    java.sql.Date result = adapter.read(jsonReader);

    assertNotNull(result);
    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", java.util.Locale.US);
    java.util.Date parsed = sdf.parse(dateString);
    assertEquals(new java.sql.Date(parsed.getTime()), result);
  }

  @Test
    @Timeout(8000)
  void read_invalidDateString_throwsJsonSyntaxException() throws IOException {
    String invalidDate = "invalid-date";
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn(invalidDate);
    when(jsonReader.getPreviousPath()).thenReturn("$.date");

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(jsonReader));
    assertTrue(ex.getMessage().contains("Failed parsing"));
    assertTrue(ex.getMessage().contains(invalidDate));
    assertTrue(ex.getMessage().contains("$.date"));
    assertNotNull(ex.getCause());
    assertTrue(ex.getCause() instanceof java.text.ParseException);
  }

  @Test
    @Timeout(8000)
  void read_synchronizedBlock_withReflection() throws Exception {
    String dateString = "Feb 2, 2021";
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn(dateString);

    // Set the format field's Locale to US again for reflection call
    Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    formatField.set(adapter, new SimpleDateFormat("MMM d, yyyy", java.util.Locale.US));

    Method readMethod = SqlDateTypeAdapter.class.getDeclaredMethod("read", JsonReader.class);
    readMethod.setAccessible(true);

    java.sql.Date result = (java.sql.Date) readMethod.invoke(adapter, jsonReader);

    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", java.util.Locale.US);
    java.util.Date parsed = sdf.parse(dateString);
    assertEquals(new java.sql.Date(parsed.getTime()), result);
  }
}
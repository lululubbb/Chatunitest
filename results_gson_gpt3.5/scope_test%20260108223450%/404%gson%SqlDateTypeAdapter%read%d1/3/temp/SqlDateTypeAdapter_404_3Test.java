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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;

class SqlDateTypeAdapter_404_3Test {

  private SqlDateTypeAdapter adapter;

  @BeforeEach
  void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<SqlDateTypeAdapter> constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();
  }

  @Test
    @Timeout(8000)
  void read_shouldReturnNull_whenJsonTokenIsNull() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);

    java.sql.Date result = adapter.read(in);

    InOrder inOrder = inOrder(in);
    inOrder.verify(in).peek();
    inOrder.verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_shouldParseDateAndReturnSqlDate_whenJsonTokenIsString() throws Exception {
    JsonReader in = mock(JsonReader.class);
    String dateString = "Jan 1, 2020"; // Single space between "Jan" and "1"
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn(dateString);
    when(in.getPreviousPath()).thenReturn(null);

    java.sql.Date result = adapter.read(in);

    assertNotNull(result);
    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy"); // Single space in format
    java.util.Date expectedDate = sdf.parse(dateString);
    assertEquals(new java.sql.Date(expectedDate.getTime()), result);
  }

  @Test
    @Timeout(8000)
  void read_shouldThrowJsonSyntaxException_whenParseExceptionOccurs() throws IOException {
    JsonReader in = mock(JsonReader.class);
    String invalidDate = "invalid-date";
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn(invalidDate);
    when(in.getPreviousPath()).thenReturn("$.date");

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
    assertTrue(thrown.getMessage().contains("Failed parsing 'invalid-date' as SQL Date; at path $.date"));
    assertNotNull(thrown.getCause());
    assertEquals(java.text.ParseException.class, thrown.getCause().getClass());
  }
}
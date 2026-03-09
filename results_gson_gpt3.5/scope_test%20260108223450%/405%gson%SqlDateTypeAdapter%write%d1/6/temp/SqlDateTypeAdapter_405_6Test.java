package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static org.mockito.Mockito.*;

import com.google.gson.internal.sql.SqlDateTypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

class SqlDateTypeAdapter_405_6Test {

  private SqlDateTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    // SqlDateTypeAdapter has a private constructor, use reflection to instantiate
    var constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();
  }

  @Test
    @Timeout(8000)
  void write_nullValue_callsNullValueOnJsonWriter() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);

    verify(out).nullValue();
    verifyNoMoreInteractions(out);
  }

  @Test
    @Timeout(8000)
  void write_nonNullValue_callsValueWithFormattedDate() throws Exception {
    JsonWriter out = mock(JsonWriter.class);

    // Create a java.util.Date instance for a known date
    java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse("2023-06-15");
    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

    // Use reflection to get the private format field and format the date for expected string
    var formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    var format = (SimpleDateFormat) formatField.get(adapter);
    String expectedString;
    synchronized (adapter) {
      expectedString = format.format(sqlDate);
    }

    adapter.write(out, sqlDate);

    InOrder inOrder = inOrder(out);
    inOrder.verify(out).value(expectedString);
    verifyNoMoreInteractions(out);
  }
}
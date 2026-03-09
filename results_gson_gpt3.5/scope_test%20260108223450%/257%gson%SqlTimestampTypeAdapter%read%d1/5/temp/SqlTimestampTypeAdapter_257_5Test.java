package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SqlTimestampTypeAdapter_257_5Test {

  private SqlTimestampTypeAdapter adapter;
  private com.google.gson.TypeAdapter<Date> mockDateTypeAdapter;

  @BeforeEach
  void setUp() throws Exception {
    // Mock the Date TypeAdapter
    mockDateTypeAdapter = mock(com.google.gson.TypeAdapter.class);

    // Use reflection to get the private constructor
    Constructor<SqlTimestampTypeAdapter> constructor =
        SqlTimestampTypeAdapter.class.getDeclaredConstructor(com.google.gson.TypeAdapter.class);
    constructor.setAccessible(true);

    adapter = constructor.newInstance(mockDateTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void read_shouldReturnNull_whenDateTypeAdapterReturnsNull() throws IOException {
    JsonReader in = mock(JsonReader.class);

    when(mockDateTypeAdapter.read(in)).thenReturn(null);

    Timestamp result = adapter.read(in);

    assertNull(result);
    verify(mockDateTypeAdapter).read(in);
  }

  @Test
    @Timeout(8000)
  void read_shouldReturnTimestamp_whenDateTypeAdapterReturnsDate() throws IOException {
    JsonReader in = mock(JsonReader.class);
    Date date = new Date(123456789L);

    when(mockDateTypeAdapter.read(in)).thenReturn(date);

    Timestamp result = adapter.read(in);

    assertNotNull(result);
    assertEquals(date.getTime(), result.getTime());
    verify(mockDateTypeAdapter).read(in);
  }
}
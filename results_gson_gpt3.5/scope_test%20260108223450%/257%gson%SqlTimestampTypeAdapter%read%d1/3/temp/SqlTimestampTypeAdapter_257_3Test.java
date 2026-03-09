package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SqlTimestampTypeAdapter_257_3Test {

  private TypeAdapter<Date> mockDateTypeAdapter;
  private SqlTimestampTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    mockDateTypeAdapter = mock(TypeAdapter.class);
    Constructor<SqlTimestampTypeAdapter> constructor = SqlTimestampTypeAdapter.class.getDeclaredConstructor(TypeAdapter.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(mockDateTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void read_nullDate_returnsNull() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(mockDateTypeAdapter.read(in)).thenReturn(null);

    Timestamp result = adapter.read(in);

    assertNull(result);
    verify(mockDateTypeAdapter).read(in);
  }

  @Test
    @Timeout(8000)
  void read_nonNullDate_returnsTimestamp() throws IOException {
    JsonReader in = mock(JsonReader.class);
    Date date = new Date(123456789L);
    when(mockDateTypeAdapter.read(in)).thenReturn(date);

    Timestamp result = adapter.read(in);

    assertNotNull(result);
    assertEquals(date.getTime(), result.getTime());
    verify(mockDateTypeAdapter).read(in);
  }
}
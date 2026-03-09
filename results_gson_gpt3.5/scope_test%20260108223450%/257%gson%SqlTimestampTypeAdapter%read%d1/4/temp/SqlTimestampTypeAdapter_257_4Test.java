package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;

class SqlTimestampTypeAdapter_257_4Test {

  private TypeAdapter<Date> mockDateTypeAdapter;
  private SqlTimestampTypeAdapter adapter;

  @BeforeEach
  void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    mockDateTypeAdapter = mock(TypeAdapter.class);
    Constructor<SqlTimestampTypeAdapter> constructor = SqlTimestampTypeAdapter.class.getDeclaredConstructor(TypeAdapter.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(mockDateTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void read_returnsTimestamp_whenDateIsNotNull() throws IOException {
    JsonReader mockReader = mock(JsonReader.class);
    Date date = new Date(123456789L);
    when(mockDateTypeAdapter.read(mockReader)).thenReturn(date);

    Timestamp result = adapter.read(mockReader);

    assertNotNull(result);
    assertEquals(date.getTime(), result.getTime());
  }

  @Test
    @Timeout(8000)
  void read_returnsNull_whenDateIsNull() throws IOException {
    JsonReader mockReader = mock(JsonReader.class);
    when(mockDateTypeAdapter.read(mockReader)).thenReturn(null);

    Timestamp result = adapter.read(mockReader);

    assertNull(result);
  }
}
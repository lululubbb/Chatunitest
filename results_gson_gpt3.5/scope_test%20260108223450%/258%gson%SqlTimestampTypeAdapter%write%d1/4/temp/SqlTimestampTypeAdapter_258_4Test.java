package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SqlTimestampTypeAdapter_258_4Test {

  private SqlTimestampTypeAdapter adapter;
  private TypeAdapter<Date> mockDateTypeAdapter;
  private JsonWriter mockJsonWriter;

  @BeforeEach
  void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    mockDateTypeAdapter = mock(TypeAdapter.class);
    Constructor<SqlTimestampTypeAdapter> constructor = SqlTimestampTypeAdapter.class.getDeclaredConstructor(TypeAdapter.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(mockDateTypeAdapter);
    mockJsonWriter = mock(JsonWriter.class);
  }

  @Test
    @Timeout(8000)
  void write_shouldDelegateToDateTypeAdapter_whenValueIsNonNull() throws IOException {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    adapter.write(mockJsonWriter, timestamp);

    verify(mockDateTypeAdapter, times(1)).write(mockJsonWriter, timestamp);
  }

  @Test
    @Timeout(8000)
  void write_shouldDelegateToDateTypeAdapter_whenValueIsNull() throws IOException {
    adapter.write(mockJsonWriter, null);

    verify(mockDateTypeAdapter, times(1)).write(mockJsonWriter, null);
  }
}
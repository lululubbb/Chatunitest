package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class SqlTimestampTypeAdapter_258_5Test {

  private SqlTimestampTypeAdapter adapter;
  private TypeAdapter<Date> mockDateTypeAdapter;

  @BeforeEach
  void setUp() throws Exception {
    mockDateTypeAdapter = mock(TypeAdapter.class);
    Constructor<SqlTimestampTypeAdapter> constructor = SqlTimestampTypeAdapter.class.getDeclaredConstructor(TypeAdapter.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(mockDateTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void write_delegatesToDateTypeAdapter() throws IOException {
    JsonWriter mockWriter = mock(JsonWriter.class);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    adapter.write(mockWriter, timestamp);

    verify(mockDateTypeAdapter).write(mockWriter, timestamp);
  }

  @Test
    @Timeout(8000)
  void write_withNullValue_delegatesToDateTypeAdapter() throws IOException {
    JsonWriter mockWriter = mock(JsonWriter.class);
    Timestamp timestamp = null;

    adapter.write(mockWriter, timestamp);

    verify(mockDateTypeAdapter).write(mockWriter, null);
  }
}
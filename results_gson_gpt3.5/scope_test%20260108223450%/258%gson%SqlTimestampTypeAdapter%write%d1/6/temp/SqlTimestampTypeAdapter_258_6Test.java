package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class SqlTimestampTypeAdapter_258_6Test {

  private TypeAdapter<Date> mockDateTypeAdapter;
  private SqlTimestampTypeAdapter sqlTimestampTypeAdapter;
  private JsonWriter mockJsonWriter;

  @BeforeEach
  void setUp() throws Exception {
    mockDateTypeAdapter = mock(TypeAdapter.class);
    mockJsonWriter = mock(JsonWriter.class);

    Constructor<SqlTimestampTypeAdapter> constructor = SqlTimestampTypeAdapter.class.getDeclaredConstructor(TypeAdapter.class);
    constructor.setAccessible(true);
    sqlTimestampTypeAdapter = constructor.newInstance(mockDateTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void write_callsDateTypeAdapterWrite_withTimestampValue() throws IOException {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    sqlTimestampTypeAdapter.write(mockJsonWriter, timestamp);

    verify(mockDateTypeAdapter).write(mockJsonWriter, timestamp);
  }

  @Test
    @Timeout(8000)
  void write_callsDateTypeAdapterWrite_withNullValue() throws IOException {
    sqlTimestampTypeAdapter.write(mockJsonWriter, null);

    verify(mockDateTypeAdapter).write(mockJsonWriter, null);
  }
}
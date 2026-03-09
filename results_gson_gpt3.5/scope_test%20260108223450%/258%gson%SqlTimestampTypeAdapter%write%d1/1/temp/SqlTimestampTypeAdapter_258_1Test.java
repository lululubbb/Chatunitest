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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SqlTimestampTypeAdapter_258_1Test {

  private SqlTimestampTypeAdapter adapter;
  private TypeAdapter<Date> dateTypeAdapter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    dateTypeAdapter = mock(TypeAdapter.class);
    jsonWriter = mock(JsonWriter.class);

    Constructor<SqlTimestampTypeAdapter> constructor =
        SqlTimestampTypeAdapter.class.getDeclaredConstructor(TypeAdapter.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(dateTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void write_callsDateTypeAdapterWrite_withSameParameters() throws IOException {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    adapter.write(jsonWriter, timestamp);

    verify(dateTypeAdapter).write(jsonWriter, timestamp);
  }

  @Test
    @Timeout(8000)
  void write_callsDateTypeAdapterWrite_withNullValue() throws IOException {
    adapter.write(jsonWriter, null);

    verify(dateTypeAdapter).write(jsonWriter, null);
  }
}
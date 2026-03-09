package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.internal.sql.SqlTimestampTypeAdapter;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

class SqlTimestampTypeAdapter_257_2Test {

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
  void read_returnsTimestamp_whenDateNotNull() throws Exception {
    JsonReader jsonReader = mock(JsonReader.class);
    Date date = new Date(123456789L);
    when(mockDateTypeAdapter.read(jsonReader)).thenReturn(date);

    Method readMethod = SqlTimestampTypeAdapter.class.getDeclaredMethod("read", JsonReader.class);
    readMethod.setAccessible(true);
    Timestamp result = (Timestamp) readMethod.invoke(adapter, jsonReader);

    assertNotNull(result);
    assertEquals(date.getTime(), result.getTime());
  }

  @Test
    @Timeout(8000)
  void read_returnsNull_whenDateIsNull() throws Exception {
    JsonReader jsonReader = mock(JsonReader.class);
    when(mockDateTypeAdapter.read(jsonReader)).thenReturn(null);

    Method readMethod = SqlTimestampTypeAdapter.class.getDeclaredMethod("read", JsonReader.class);
    readMethod.setAccessible(true);
    Timestamp result = (Timestamp) readMethod.invoke(adapter, jsonReader);

    assertNull(result);
  }
}
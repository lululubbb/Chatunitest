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
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SqlTimestampTypeAdapter_257_1Test {

  private TypeAdapter<Date> dateTypeAdapterMock;
  private SqlTimestampTypeAdapter sqlTimestampTypeAdapter;

  @BeforeEach
  void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    dateTypeAdapterMock = mock(TypeAdapter.class);
    Constructor<SqlTimestampTypeAdapter> constructor = SqlTimestampTypeAdapter.class.getDeclaredConstructor(TypeAdapter.class);
    constructor.setAccessible(true);
    sqlTimestampTypeAdapter = constructor.newInstance(dateTypeAdapterMock);
  }

  @Test
    @Timeout(8000)
  void read_shouldReturnTimestamp_whenDateIsNotNull() throws IOException {
    JsonReader jsonReaderMock = mock(JsonReader.class);
    Date date = new Date(123456789L);
    when(dateTypeAdapterMock.read(jsonReaderMock)).thenReturn(date);

    Timestamp result = sqlTimestampTypeAdapter.read(jsonReaderMock);

    assertNotNull(result);
    assertEquals(date.getTime(), result.getTime());
    verify(dateTypeAdapterMock).read(jsonReaderMock);
  }

  @Test
    @Timeout(8000)
  void read_shouldReturnNull_whenDateIsNull() throws IOException {
    JsonReader jsonReaderMock = mock(JsonReader.class);
    when(dateTypeAdapterMock.read(jsonReaderMock)).thenReturn(null);

    Timestamp result = sqlTimestampTypeAdapter.read(jsonReaderMock);

    assertNull(result);
    verify(dateTypeAdapterMock).read(jsonReaderMock);
  }
}
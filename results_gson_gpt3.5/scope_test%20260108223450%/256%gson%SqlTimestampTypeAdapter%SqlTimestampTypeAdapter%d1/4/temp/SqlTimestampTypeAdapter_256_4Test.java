package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

class SqlTimestampTypeAdapter_256_4Test {

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
  void testRead_nullValue() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(mockDateTypeAdapter.read(reader)).thenReturn(null);

    Timestamp result = adapter.read(reader);

    assertNull(result);
    verify(mockDateTypeAdapter).read(reader);
  }

  @Test
    @Timeout(8000)
  void testRead_nonNullValue() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    Date date = new Date(123456789L);
    when(mockDateTypeAdapter.read(reader)).thenReturn(date);

    Timestamp result = adapter.read(reader);

    assertNotNull(result);
    assertEquals(date.getTime(), result.getTime());
    verify(mockDateTypeAdapter).read(reader);
  }

  @Test
    @Timeout(8000)
  void testWrite_nullValue() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);

    adapter.write(writer, null);

    verify(mockDateTypeAdapter).write(writer, null);
  }

  @Test
    @Timeout(8000)
  void testWrite_nonNullValue() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    Timestamp timestamp = new Timestamp(987654321L);

    adapter.write(writer, timestamp);

    ArgumentCaptor<Date> captor = ArgumentCaptor.forClass(Date.class);
    verify(mockDateTypeAdapter).write(eq(writer), captor.capture());
    Date passedDate = captor.getValue();
    assertEquals(timestamp.getTime(), passedDate.getTime());
  }

  @Test
    @Timeout(8000)
  void testFactoryCreatesAdapterForTimestamp() {
    Gson gson = mock(Gson.class);
    TypeAdapter<Date> dateAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(Date.class)).thenReturn(dateAdapter);

    TypeAdapter<?> adapter = SqlTimestampTypeAdapter.FACTORY.create(gson, TypeToken.get(Timestamp.class));

    assertNotNull(adapter);
    assertTrue(adapter instanceof SqlTimestampTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void testFactoryReturnsNullForOtherTypes() {
    Gson gson = mock(Gson.class);
    TypeAdapter<?> adapter = SqlTimestampTypeAdapter.FACTORY.create(gson, TypeToken.get(String.class));
    assertNull(adapter);
  }
}
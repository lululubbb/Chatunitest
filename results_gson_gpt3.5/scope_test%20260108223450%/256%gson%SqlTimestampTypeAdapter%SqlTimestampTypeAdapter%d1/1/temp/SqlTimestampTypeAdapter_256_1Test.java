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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class SqlTimestampTypeAdapter_256_1Test {

  private TypeAdapter<Date> mockDateAdapter;
  private SqlTimestampTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    mockDateAdapter = mock(TypeAdapter.class);
    Constructor<SqlTimestampTypeAdapter> constructor = SqlTimestampTypeAdapter.class.getDeclaredConstructor(TypeAdapter.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(mockDateAdapter);
  }

  @Test
    @Timeout(8000)
  void testRead_withValidDate_returnsTimestamp() throws Exception {
    JsonReader mockReader = mock(JsonReader.class);
    Date date = new Date(123456789L);
    when(mockDateAdapter.read(mockReader)).thenReturn(date);

    Timestamp result = adapter.read(mockReader);

    assertNotNull(result);
    assertEquals(date.getTime(), result.getTime());
    verify(mockDateAdapter).read(mockReader);
  }

  @Test
    @Timeout(8000)
  void testRead_withNullDate_returnsNull() throws Exception {
    JsonReader mockReader = mock(JsonReader.class);
    when(mockDateAdapter.read(mockReader)).thenReturn(null);

    Timestamp result = adapter.read(mockReader);

    assertNull(result);
    verify(mockDateAdapter).read(mockReader);
  }

  @Test
    @Timeout(8000)
  void testWrite_withNonNullTimestamp_writesDate() throws Exception {
    JsonWriter mockWriter = mock(JsonWriter.class);
    Timestamp timestamp = new Timestamp(987654321L);

    adapter.write(mockWriter, timestamp);

    InOrder inOrder = inOrder(mockDateAdapter);
    inOrder.verify(mockDateAdapter).write(mockWriter, timestamp);
  }

  @Test
    @Timeout(8000)
  void testWrite_withNullTimestamp_writesNull() throws Exception {
    JsonWriter mockWriter = mock(JsonWriter.class);

    adapter.write(mockWriter, null);

    verify(mockDateAdapter).write(mockWriter, null);
  }

  @Test
    @Timeout(8000)
  void testFactoryCreatesAdapterForTimestamp() {
    Gson gson = mock(Gson.class);
    TypeAdapter<Date> dateAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(Date.class)).thenReturn(dateAdapter);

    TypeAdapter<?> adapterCreated = SqlTimestampTypeAdapter.FACTORY.create(gson, TypeToken.get(Timestamp.class));

    assertNotNull(adapterCreated);
    assertTrue(adapterCreated instanceof SqlTimestampTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void testFactoryReturnsNullForOtherType() {
    Gson gson = mock(Gson.class);

    TypeAdapter<?> adapterCreated = SqlTimestampTypeAdapter.FACTORY.create(gson, TypeToken.get(String.class));

    assertNull(adapterCreated);
  }
}
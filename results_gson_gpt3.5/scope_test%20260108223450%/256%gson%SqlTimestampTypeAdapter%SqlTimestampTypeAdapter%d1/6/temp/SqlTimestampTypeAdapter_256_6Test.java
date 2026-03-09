package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

class SqlTimestampTypeAdapter_256_6Test {

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
  void testFactoryCreatesAdapterForTimestamp() {
    Gson gson = mock(Gson.class);
    TypeAdapter<Date> dateAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(Date.class)).thenReturn(dateAdapter);

    TypeAdapterFactory factory = SqlTimestampTypeAdapter.FACTORY;

    TypeAdapter<?> adapterCreated = factory.create(gson, TypeToken.get(Timestamp.class));
    assertNotNull(adapterCreated);
    assertEquals(SqlTimestampTypeAdapter.class, adapterCreated.getClass());

    TypeAdapter<?> nullAdapter = factory.create(gson, TypeToken.get(String.class));
    assertNull(nullAdapter);
  }

  @Test
    @Timeout(8000)
  void testReadReturnsNullWhenDateAdapterReturnsNull() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(mockDateAdapter.read(reader)).thenReturn(null);

    Timestamp result = adapter.read(reader);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testReadReturnsTimestampWhenDateAdapterReturnsDate() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    Date date = new Date(123456789L);
    when(mockDateAdapter.read(reader)).thenReturn(date);

    Timestamp result = adapter.read(reader);
    assertNotNull(result);
    assertEquals(date.getTime(), result.getTime());
    assertEquals(Timestamp.class, result.getClass());
  }

  @Test
    @Timeout(8000)
  void testWriteDelegatesToDateAdapter() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    Timestamp timestamp = new Timestamp(987654321L);

    adapter.write(writer, timestamp);

    ArgumentCaptor<Date> captor = ArgumentCaptor.forClass(Date.class);
    verify(mockDateAdapter).write(eq(writer), captor.capture());
    Date datePassed = captor.getValue();
    assertEquals(timestamp.getTime(), datePassed.getTime());
  }

  @Test
    @Timeout(8000)
  void testWriteWithNullValue() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);

    adapter.write(writer, null);

    verify(mockDateAdapter).write(writer, null);
  }
}
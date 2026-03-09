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
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SqlTimestampTypeAdapter_256_2Test {

  private TypeAdapter<Date> mockDateAdapter;
  private SqlTimestampTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    mockDateAdapter = mock(TypeAdapter.class);
    // Use reflection to invoke the private constructor
    Constructor<SqlTimestampTypeAdapter> ctor = SqlTimestampTypeAdapter.class.getDeclaredConstructor(TypeAdapter.class);
    ctor.setAccessible(true);
    adapter = ctor.newInstance(mockDateAdapter);
  }

  @Test
    @Timeout(8000)
  void testRead_null() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(mockDateAdapter.read(reader)).thenReturn(null);

    Timestamp result = adapter.read(reader);

    assertNull(result);
    verify(mockDateAdapter).read(reader);
  }

  @Test
    @Timeout(8000)
  void testRead_nonNull() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    Date date = new Date(123456789L);
    when(mockDateAdapter.read(reader)).thenReturn(date);

    Timestamp result = adapter.read(reader);

    assertNotNull(result);
    assertEquals(date.getTime(), result.getTime());
    verify(mockDateAdapter).read(reader);
  }

  @Test
    @Timeout(8000)
  void testWrite_null() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);

    adapter.write(writer, null);

    verify(mockDateAdapter).write(writer, null);
  }

  @Test
    @Timeout(8000)
  void testWrite_nonNull() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    Timestamp timestamp = new Timestamp(987654321L);

    adapter.write(writer, timestamp);

    ArgumentCaptor<Date> captor = ArgumentCaptor.forClass(Date.class);
    verify(mockDateAdapter).write(eq(writer), captor.capture());
    Date passedDate = captor.getValue();
    assertEquals(timestamp.getTime(), passedDate.getTime());
  }

  @Test
    @Timeout(8000)
  void testFactoryCreate_withTimestamp() {
    Gson gson = mock(Gson.class);
    TypeAdapter<Date> dateAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(Date.class)).thenReturn(dateAdapter);

    TypeAdapterFactory factory = SqlTimestampTypeAdapter.FACTORY;
    TypeAdapter<?> adapterCreated = factory.create(gson, TypeToken.get(Timestamp.class));

    assertNotNull(adapterCreated);
    assertTrue(adapterCreated instanceof SqlTimestampTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void testFactoryCreate_withOtherType() {
    Gson gson = mock(Gson.class);
    TypeAdapterFactory factory = SqlTimestampTypeAdapter.FACTORY;
    TypeAdapter<?> adapterCreated = factory.create(gson, TypeToken.get(String.class));

    assertNull(adapterCreated);
  }
}
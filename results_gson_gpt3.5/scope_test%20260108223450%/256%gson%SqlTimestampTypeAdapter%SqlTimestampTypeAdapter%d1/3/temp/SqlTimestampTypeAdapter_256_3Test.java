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
import java.sql.Timestamp;
import java.util.Date;

class SqlTimestampTypeAdapter_256_3Test {

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
  void testRead_nullJson_returnsNull() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(com.google.gson.stream.JsonToken.NULL);

    // Stub nextNull() to do nothing (no need to use doNothing() explicitly)
    // Just allow the call without stubbing as it's a void method
    // But to avoid the "wanted but not invoked" error, we verify after call

    Timestamp result = adapter.read(reader);

    assertNull(result);
    verify(reader).nextNull();
    verifyNoMoreInteractions(mockDateAdapter);
  }

  @Test
    @Timeout(8000)
  void testRead_nonNullJson_delegatesToDateAdapter() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(com.google.gson.stream.JsonToken.STRING);
    Date date = new Date(123456789L);
    when(mockDateAdapter.read(reader)).thenReturn(date);

    Timestamp result = adapter.read(reader);

    assertNotNull(result);
    assertEquals(date.getTime(), result.getTime());
    verify(mockDateAdapter).read(reader);
  }

  @Test
    @Timeout(8000)
  void testWrite_nullValue_writesNull() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);

    // No need to stub nullValue() since it's a void method and Mockito allows calling it on mocks

    adapter.write(writer, null);

    verify(writer).nullValue();
    verifyNoMoreInteractions(mockDateAdapter);
  }

  @Test
    @Timeout(8000)
  void testWrite_nonNullValue_delegatesToDateAdapter() throws IOException {
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
  void testFactory_create_withTimestampType_returnsAdapter() {
    Gson gson = mock(Gson.class);
    TypeToken<Timestamp> timestampTypeToken = TypeToken.get(Timestamp.class);
    TypeAdapter<Date> dateAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(Date.class)).thenReturn(dateAdapter);

    TypeAdapter<?> result = SqlTimestampTypeAdapter.FACTORY.create(gson, timestampTypeToken);

    assertNotNull(result);
    assertTrue(result instanceof SqlTimestampTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void testFactory_create_withNonTimestampType_returnsNull() {
    Gson gson = mock(Gson.class);
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);

    TypeAdapter<?> result = SqlTimestampTypeAdapter.FACTORY.create(gson, stringTypeToken);

    assertNull(result);
  }
}
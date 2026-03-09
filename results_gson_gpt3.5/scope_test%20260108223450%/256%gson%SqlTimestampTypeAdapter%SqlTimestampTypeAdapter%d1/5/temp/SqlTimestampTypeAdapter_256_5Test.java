package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
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
import org.mockito.InOrder;

class SqlTimestampTypeAdapter_256_5Test {

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
  void testRead_nullValue_returnsNull() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(mockDateTypeAdapter.read(reader)).thenReturn(null);

    Timestamp result = adapter.read(reader);

    assertNull(result);
    verify(mockDateTypeAdapter).read(reader);
  }

  @Test
    @Timeout(8000)
  void testRead_nonNullValue_returnsTimestamp() throws IOException {
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
  void testWrite_nullValue_writesNull() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);

    adapter.write(writer, null);

    verify(mockDateTypeAdapter).write(writer, null);
  }

  @Test
    @Timeout(8000)
  void testWrite_nonNullValue_writesDate() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    Timestamp timestamp = new Timestamp(987654321L);

    adapter.write(writer, timestamp);

    verify(mockDateTypeAdapter).write(writer, timestamp);
  }

  @Test
    @Timeout(8000)
  void testFactory_create_withTimestamp() {
    Gson gson = mock(Gson.class);
    TypeToken<Timestamp> typeToken = TypeToken.get(Timestamp.class);
    TypeAdapter<Date> dateAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(Date.class)).thenReturn(dateAdapter);

    @SuppressWarnings("unchecked")
    TypeAdapter<Timestamp> created = (TypeAdapter<Timestamp>) SqlTimestampTypeAdapter.FACTORY.create(gson, typeToken);

    assertNotNull(created);
    assertEquals(SqlTimestampTypeAdapter.class, created.getClass());
  }

  @Test
    @Timeout(8000)
  void testFactory_create_withOtherType_returnsNull() {
    Gson gson = mock(Gson.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    TypeAdapter<?> created = SqlTimestampTypeAdapter.FACTORY.create(gson, typeToken);

    assertNull(created);
  }
}
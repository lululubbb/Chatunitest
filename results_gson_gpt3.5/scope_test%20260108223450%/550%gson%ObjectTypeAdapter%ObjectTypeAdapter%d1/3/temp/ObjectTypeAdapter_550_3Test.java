package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Map;

public class ObjectTypeAdapter_550_3Test {

  private Gson gsonMock;
  private ToNumberStrategy toNumberStrategyMock;
  private ObjectTypeAdapter adapter;

  @BeforeEach
  public void setUp() throws Exception {
    gsonMock = mock(Gson.class);
    toNumberStrategyMock = mock(ToNumberStrategy.class);

    Constructor<ObjectTypeAdapter> constructor =
        ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, ToNumberStrategy.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(gsonMock, toNumberStrategyMock);
  }

  @Test
    @Timeout(8000)
  public void testRead_nullValue() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(reader).nextNull();

    Object result = adapter.read(reader);

    verify(reader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testRead_beginArray() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
    doNothing().when(reader).beginArray();
    doNothing().when(reader).endArray();

    Method tryBeginNesting = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    tryBeginNesting.setAccessible(true);

    Object result = tryBeginNesting.invoke(adapter, reader, JsonToken.BEGIN_ARRAY);

    assertTrue(result instanceof ArrayDeque);
  }

  @Test
    @Timeout(8000)
  public void testRead_beginObject() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    doNothing().when(reader).beginObject();
    doNothing().when(reader).endObject();

    Method tryBeginNesting = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    tryBeginNesting.setAccessible(true);

    Object result = tryBeginNesting.invoke(adapter, reader, JsonToken.BEGIN_OBJECT);

    assertTrue(result instanceof Map);
  }

  @Test
    @Timeout(8000)
  public void testRead_terminalValue() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);

    Method readTerminal = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    readTerminal.setAccessible(true);

    when(reader.nextString()).thenReturn("testString");

    Object result = readTerminal.invoke(adapter, reader, JsonToken.STRING);

    assertEquals("testString", result);
  }

  @Test
    @Timeout(8000)
  public void testRead_unsupportedToken_throws() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.END_DOCUMENT);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> adapter.read(reader));
    assertTrue(thrown.getMessage().contains("Unexpected token"));
  }

  @Test
    @Timeout(8000)
  public void testWrite_nullValue() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);

    adapter.write(writer, null);

    verify(writer).nullValue();
  }

  @Test
    @Timeout(8000)
  public void testWrite_nonNullValue() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    Object value = new Object();

    @SuppressWarnings({"unchecked", "rawtypes"})
    TypeAdapter delegateAdapter = mock(TypeAdapter.class);
    when(gsonMock.getAdapter(TypeToken.get(value.getClass()))).thenReturn(delegateAdapter);

    // Mock delegateAdapter.write to do nothing to avoid NPE
    doNothing().when(delegateAdapter).write(writer, value);

    adapter.write(writer, value);

    verify(delegateAdapter).write(writer, value);
  }

  @Test
    @Timeout(8000)
  public void testNewFactory_returnsFactory() throws Exception {
    Method newFactory = ObjectTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactory.setAccessible(true);

    Object factory = newFactory.invoke(null, ToNumberPolicy.DOUBLE);

    assertNotNull(factory);
    assertTrue(factory instanceof TypeAdapterFactory);
  }

  @Test
    @Timeout(8000)
  public void testGetFactory_returnsFactory() {
    TypeAdapterFactory factory = ObjectTypeAdapter.getFactory(ToNumberPolicy.DOUBLE);
    assertNotNull(factory);
  }
}
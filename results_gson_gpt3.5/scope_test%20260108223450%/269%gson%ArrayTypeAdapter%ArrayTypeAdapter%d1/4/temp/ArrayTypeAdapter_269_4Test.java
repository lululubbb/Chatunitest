package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ArrayTypeAdapter_269_4Test {
  @Mock Gson mockGson;
  @Mock TypeAdapter<String> mockComponentTypeAdapter;
  @Mock JsonReader mockJsonReader;
  @Mock JsonWriter mockJsonWriter;

  ArrayTypeAdapter<String> adapter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    adapter = new ArrayTypeAdapter<>(mockGson, mockComponentTypeAdapter, String.class);
  }

  @Test
    @Timeout(8000)
  void read_nullJson_returnsNull() throws IOException {
    when(mockJsonReader.peek()).thenReturn(JsonToken.NULL);
    // no need to stub nextNull() with doNothing(), it is void and does nothing by default

    Object result = adapter.read(mockJsonReader);

    verify(mockJsonReader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_emptyArray_returnsEmptyArray() throws IOException {
    when(mockJsonReader.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.END_ARRAY);
    // no need to stub beginArray() and endArray(), they are void and do nothing by default

    Object result = adapter.read(mockJsonReader);

    verify(mockJsonReader).beginArray();
    verify(mockJsonReader).endArray();
    assertNotNull(result);
    assertEquals(0, Array.getLength(result));
  }

  @Test
    @Timeout(8000)
  void read_arrayWithElements_readsElements() throws IOException {
    // Adjusted to reflect actual peek() calls: BEGIN_ARRAY, STRING, STRING, END_ARRAY
    when(mockJsonReader.peek())
        .thenReturn(
            JsonToken.BEGIN_ARRAY,
            JsonToken.STRING,
            JsonToken.STRING,
            JsonToken.END_ARRAY);
    // no need to stub beginArray() and endArray()

    when(mockComponentTypeAdapter.read(mockJsonReader)).thenReturn("one", "two");

    Object result = adapter.read(mockJsonReader);

    verify(mockJsonReader).beginArray();
    // The adapter calls peek() 4 times: BEGIN_ARRAY, STRING, STRING, END_ARRAY
    verify(mockJsonReader, times(4)).peek();
    verify(mockComponentTypeAdapter, times(2)).read(mockJsonReader);
    verify(mockJsonReader).endArray();

    assertNotNull(result);
    assertEquals(2, Array.getLength(result));
    assertEquals("one", Array.get(result, 0));
    assertEquals("two", Array.get(result, 1));
  }

  @Test
    @Timeout(8000)
  void write_nullArray_writesNull() throws IOException {
    adapter.write(mockJsonWriter, null);

    verify(mockJsonWriter).nullValue();
  }

  @Test
    @Timeout(8000)
  void write_emptyArray_writesEmptyArray() throws IOException {
    Object emptyArray = Array.newInstance(String.class, 0);

    // no need to stub beginArray() and endArray()

    adapter.write(mockJsonWriter, emptyArray);

    InOrder inOrder = inOrder(mockJsonWriter);
    inOrder.verify(mockJsonWriter).beginArray();
    inOrder.verify(mockJsonWriter).endArray();
  }

  @Test
    @Timeout(8000)
  void write_arrayWithElements_writesElements() throws IOException {
    String[] array = new String[] {"one", "two"};
    // no need to stub beginArray() and endArray()
    // no need to stub componentTypeAdapter.write(), it is void and does nothing by default

    adapter.write(mockJsonWriter, array);

    InOrder inOrder = inOrder(mockJsonWriter, mockComponentTypeAdapter);
    inOrder.verify(mockJsonWriter).beginArray();
    inOrder.verify(mockComponentTypeAdapter).write(mockJsonWriter, "one");
    inOrder.verify(mockComponentTypeAdapter).write(mockJsonWriter, "two");
    inOrder.verify(mockJsonWriter).endArray();
  }

  @Test
    @Timeout(8000)
  void factory_create_withNonArrayType_returnsNull() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    TypeAdapter<?> adapter = ArrayTypeAdapter.FACTORY.create(mockGson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void factory_create_withArrayType_returnsArrayTypeAdapter() {
    TypeToken<String[]> typeToken = TypeToken.get(String[].class);

    Gson gson = new Gson();
    TypeAdapter<?> componentAdapter = gson.getAdapter(String.class);
    Gson spyGson = spy(gson);
    doReturn(componentAdapter).when(spyGson).getAdapter(TypeToken.get(String.class));

    TypeAdapter<?> adapter = ArrayTypeAdapter.FACTORY.create(spyGson, typeToken);

    assertNotNull(adapter);
    assertEquals(ArrayTypeAdapter.class, adapter.getClass());
  }
}
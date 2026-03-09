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

class ArrayTypeAdapter_269_1Test {

  private Gson mockGson;
  private TypeAdapter<String> mockComponentAdapter;
  private ArrayTypeAdapter<String> arrayTypeAdapter;

  @BeforeEach
  void setUp() {
    mockGson = mock(Gson.class);
    mockComponentAdapter = mock(TypeAdapter.class);
    arrayTypeAdapter = new ArrayTypeAdapter<>(mockGson, mockComponentAdapter, String.class);
  }

  @Test
    @Timeout(8000)
  void testRead_nullJson_returnsNull() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);

    Object result = arrayTypeAdapter.read(in);

    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testRead_emptyArray_returnsEmptyArray() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.END_ARRAY);
    doNothing().when(in).beginArray();
    doNothing().when(in).endArray();

    Object result = arrayTypeAdapter.read(in);

    assertNotNull(result);
    assertEquals(0, Array.getLength(result));
    verify(in).beginArray();
    verify(in).endArray();
  }

  @Test
    @Timeout(8000)
  void testRead_arrayWithElements_returnsArray() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(
        JsonToken.BEGIN_ARRAY,
        JsonToken.STRING,
        JsonToken.STRING,
        JsonToken.END_ARRAY
    );
    doNothing().when(in).beginArray();
    doNothing().when(in).endArray();

    when(mockComponentAdapter.read(in)).thenReturn("one", "two");

    // Mock in.nextString() to consume the STRING tokens
    when(in.nextString()).thenReturn("one", "two");

    Object result = arrayTypeAdapter.read(in);

    assertNotNull(result);
    assertEquals(2, Array.getLength(result));
    assertEquals("one", Array.get(result, 0));
    assertEquals("two", Array.get(result, 1));
    verify(in).beginArray();
    verify(in).endArray();
  }

  @Test
    @Timeout(8000)
  void testWrite_nullArray_writesNull() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    arrayTypeAdapter.write(out, null);

    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  void testWrite_emptyArray_writesEmptyArray() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    Object emptyArray = Array.newInstance(String.class, 0);

    // Removed incorrect doNothing stubbing on non-void method
    // beginArray() and endArray() are void methods, no need to stub

    arrayTypeAdapter.write(out, emptyArray);

    verify(out).beginArray();
    verify(out).endArray();
  }

  @Test
    @Timeout(8000)
  void testWrite_arrayWithElements_writesElements() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    Object array = Array.newInstance(String.class, 2);
    Array.set(array, 0, "one");
    Array.set(array, 1, "two");

    // Removed incorrect doNothing stubbing on non-void method

    doNothing().when(mockComponentAdapter).write(any(JsonWriter.class), any());

    arrayTypeAdapter.write(out, array);

    verify(out).beginArray();
    verify(mockComponentAdapter).write(out, "one");
    verify(mockComponentAdapter).write(out, "two");
    verify(out).endArray();
  }

  @Test
    @Timeout(8000)
  void testFactory_create_returnsNullForNonArrayType() {
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
    TypeAdapter<?> adapter = ArrayTypeAdapter.FACTORY.create(mockGson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void testFactory_create_returnsArrayTypeAdapterForArray() {
    TypeToken<String[]> typeToken = TypeToken.get(String[].class);
    Gson gson = mock(Gson.class);
    @SuppressWarnings({"unchecked", "rawtypes"})
    TypeAdapter componentAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(TypeToken.get(String.class))).thenReturn(componentAdapter);

    TypeAdapter<?> adapter = ArrayTypeAdapter.FACTORY.create(gson, typeToken);

    assertNotNull(adapter);
    assertTrue(adapter instanceof ArrayTypeAdapter);
  }
}
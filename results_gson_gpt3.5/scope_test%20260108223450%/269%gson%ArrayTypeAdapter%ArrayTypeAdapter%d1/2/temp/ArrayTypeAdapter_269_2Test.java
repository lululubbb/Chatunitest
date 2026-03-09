package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
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
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArrayTypeAdapter_269_2Test {

  Gson gson;
  TypeAdapter<String> componentAdapter;
  ArrayTypeAdapter<String> arrayTypeAdapter;

  @BeforeEach
  void setUp() {
    gson = mock(Gson.class);
    componentAdapter = mock(TypeAdapter.class);
    arrayTypeAdapter = new ArrayTypeAdapter<>(gson, componentAdapter, String.class);
  }

  @Test
    @Timeout(8000)
  void testRead_nullJson_returnsNull() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

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

    verify(in).beginArray();
    verify(in).endArray();
    assertNotNull(result);
    assertEquals(0, Array.getLength(result));
  }

  @Test
    @Timeout(8000)
  void testRead_arrayWithElements_returnsArray() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.STRING, JsonToken.STRING, JsonToken.END_ARRAY);
    doNothing().when(in).beginArray();
    doNothing().when(in).endArray();
    when(componentAdapter.read(in)).thenReturn("first").thenReturn("second");

    Object result = arrayTypeAdapter.read(in);

    verify(in).beginArray();
    // Changed from times(3) to times(1) because peek is called once in actual implementation
    verify(in, times(1)).peek();
    verify(componentAdapter, times(2)).read(in);
    verify(in).endArray();
    assertNotNull(result);
    assertEquals(2, Array.getLength(result));
    assertEquals("first", Array.get(result, 0));
    assertEquals("second", Array.get(result, 1));
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
  void testWrite_emptyArray_writesBeginEndArray() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    String[] array = new String[0];

    arrayTypeAdapter.write(out, array);

    verify(out).beginArray();
    verify(out).endArray();
  }

  @Test
    @Timeout(8000)
  void testWrite_arrayWithElements_writesAllElements() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    String[] array = new String[]{"a", "b"};
    doNothing().when(componentAdapter).write(any(JsonWriter.class), any());

    arrayTypeAdapter.write(out, array);

    verify(out).beginArray();
    verify(componentAdapter).write(out, "a");
    verify(componentAdapter).write(out, "b");
    verify(out).endArray();
  }

  @Test
    @Timeout(8000)
  void testFactory_create_withArrayType_returnsArrayTypeAdapter() {
    Gson gson = mock(Gson.class);
    TypeToken<String[]> typeToken = TypeToken.get(String[].class);
    Type componentType = String.class;

    @SuppressWarnings({"unchecked", "rawtypes"})
    TypeAdapter<String> componentAdapter = mock(TypeAdapter.class);

    when(gson.getAdapter((TypeToken<String>) any())).thenReturn(componentAdapter);

    TypeAdapter<?> adapter = ArrayTypeAdapter.FACTORY.create(gson, typeToken);

    assertNotNull(adapter);
    assertTrue(adapter instanceof ArrayTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void testFactory_create_withNonArrayType_returnsNull() {
    Gson gson = mock(Gson.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    TypeAdapter<?> adapter = ArrayTypeAdapter.FACTORY.create(gson, typeToken);

    assertNull(adapter);
  }
}
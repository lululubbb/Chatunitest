package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArrayTypeAdapter_270_3Test {

  private Gson gson;
  private TypeAdapter<Integer> componentTypeAdapter;
  private ArrayTypeAdapter<Integer> arrayTypeAdapter;

  @BeforeEach
  void setUp() {
    gson = mock(Gson.class);
    componentTypeAdapter = mock(TypeAdapter.class);
    arrayTypeAdapter = new ArrayTypeAdapter<>(gson, componentTypeAdapter, Integer.class);
  }

  @Test
    @Timeout(8000)
  void read_nullJson_returnsNull() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);

    Object result = arrayTypeAdapter.read(in);

    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_emptyArray_returnsEmptyArray() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.END_ARRAY);
    when(in.hasNext()).thenReturn(false);

    Object result = arrayTypeAdapter.read(in);

    verify(in).beginArray();
    verify(in).hasNext();
    verify(in).endArray();
    assertTrue(result instanceof Integer[]);
    assertEquals(0, Array.getLength(result));
  }

  @Test
    @Timeout(8000)
  void read_arrayOfIntegers_returnsArray() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
    when(in.hasNext()).thenReturn(true, true, false);
    when(componentTypeAdapter.read(in)).thenReturn(1, 2);
    doNothing().when(in).beginArray();
    doNothing().when(in).endArray();

    // Arrange peek calls for hasNext and peek in the original method
    // but only peek() is called once at start, so we mock only that.
    // hasNext() controls the loop.

    Object result = arrayTypeAdapter.read(in);

    InOrder inOrder = inOrder(in, componentTypeAdapter);
    inOrder.verify(in).peek();
    inOrder.verify(in).beginArray();
    inOrder.verify(in, times(3)).hasNext();
    inOrder.verify(componentTypeAdapter, times(2)).read(in);
    inOrder.verify(in).endArray();

    assertTrue(result instanceof Integer[]);
    Integer[] array = (Integer[]) result;
    assertArrayEquals(new Integer[]{1, 2}, array);
  }

  @Test
    @Timeout(8000)
  void read_arrayOfPrimitiveInts_returnsPrimitiveIntArray() throws IOException {
    // Create adapter for int primitive type
    TypeAdapter<Integer> intComponentAdapter = mock(TypeAdapter.class);
    ArrayTypeAdapter<Integer> adapter = new ArrayTypeAdapter<>(gson, intComponentAdapter, int.class);

    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
    when(in.hasNext()).thenReturn(true, true, false);
    when(intComponentAdapter.read(in)).thenReturn(10, 20);
    doNothing().when(in).beginArray();
    doNothing().when(in).endArray();

    Object result = adapter.read(in);

    InOrder inOrder = inOrder(in, intComponentAdapter);
    inOrder.verify(in).peek();
    inOrder.verify(in).beginArray();
    inOrder.verify(in, times(3)).hasNext();
    inOrder.verify(intComponentAdapter, times(2)).read(in);
    inOrder.verify(in).endArray();

    assertTrue(result.getClass().isArray());
    assertEquals(int.class, result.getClass().getComponentType());
    assertEquals(2, Array.getLength(result));
    assertEquals(10, Array.get(result, 0));
    assertEquals(20, Array.get(result, 1));
  }
}
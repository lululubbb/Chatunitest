package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;

public class ArrayTypeAdapter_270_6Test {

  private ArrayTypeAdapter<Integer> adapter;
  private TypeAdapter<Integer> componentTypeAdapter;
  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    componentTypeAdapter = mock(TypeAdapter.class);
    adapter = new ArrayTypeAdapter<>(null, componentTypeAdapter, int.class);
    jsonReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  public void read_shouldReturnNull_whenJsonTokenIsNull() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.NULL);

    Object result = adapter.read(jsonReader);

    verify(jsonReader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void read_shouldReturnPrimitiveArray_whenComponentTypeIsPrimitive() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
    doNothing().when(jsonReader).beginArray();
    when(jsonReader.hasNext()).thenReturn(true, true, false);
    when(componentTypeAdapter.read(jsonReader)).thenReturn(1).thenReturn(2);
    doNothing().when(jsonReader).endArray();

    Object result = adapter.read(jsonReader);

    assertTrue(result.getClass().isArray());
    assertEquals(int.class, result.getClass().getComponentType());
    int[] intArray = (int[]) result;
    assertArrayEquals(new int[]{1, 2}, intArray);

    InOrder inOrder = inOrder(jsonReader, componentTypeAdapter);
    inOrder.verify(jsonReader).peek();
    inOrder.verify(jsonReader).beginArray();
    inOrder.verify(jsonReader, times(3)).hasNext();
    inOrder.verify(componentTypeAdapter, times(2)).read(jsonReader);
    inOrder.verify(jsonReader).endArray();
  }

  @Test
    @Timeout(8000)
  public void read_shouldReturnObjectArray_whenComponentTypeIsObject() throws IOException {
    // Use Integer.class which is not primitive
    ArrayTypeAdapter<Integer> objectAdapter = new ArrayTypeAdapter<>(null, componentTypeAdapter, Integer.class);
    JsonReader reader = mock(JsonReader.class);

    when(reader.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
    doNothing().when(reader).beginArray();
    when(reader.hasNext()).thenReturn(true, true, false);
    when(componentTypeAdapter.read(reader)).thenReturn(1).thenReturn(2);
    doNothing().when(reader).endArray();

    Object result = objectAdapter.read(reader);

    assertTrue(result.getClass().isArray());
    assertEquals(Integer.class, result.getClass().getComponentType());
    Integer[] arr = (Integer[]) result;
    assertArrayEquals(new Integer[]{1, 2}, arr);

    InOrder inOrder = inOrder(reader, componentTypeAdapter);
    inOrder.verify(reader).peek();
    inOrder.verify(reader).beginArray();
    inOrder.verify(reader, times(3)).hasNext();
    inOrder.verify(componentTypeAdapter, times(2)).read(reader);
    inOrder.verify(reader).endArray();
  }
}
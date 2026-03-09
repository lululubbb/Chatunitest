package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class ArrayTypeAdapter_270_5Test {

  private Gson mockGson;
  private TypeAdapter<Integer> mockComponentTypeAdapter;
  private ArrayTypeAdapter<Integer> arrayTypeAdapter;

  @BeforeEach
  void setUp() {
    mockGson = mock(Gson.class);
    mockComponentTypeAdapter = mock(TypeAdapter.class);
    arrayTypeAdapter = new ArrayTypeAdapter<>(mockGson, mockComponentTypeAdapter, Integer.class);
  }

  @Test
    @Timeout(8000)
  void read_nullJsonToken_returnsNull() throws IOException {
    JsonReader mockJsonReader = mock(JsonReader.class);
    when(mockJsonReader.peek()).thenReturn(JsonToken.NULL);

    Object result = arrayTypeAdapter.read(mockJsonReader);

    verify(mockJsonReader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_arrayOfObjects_returnsArray() throws IOException {
    JsonReader mockJsonReader = mock(JsonReader.class);

    when(mockJsonReader.peek())
        .thenReturn(JsonToken.BEGIN_ARRAY) // initial peek
        .thenReturn(JsonToken.NUMBER) // first element
        .thenReturn(JsonToken.NUMBER) // second element
        .thenReturn(JsonToken.END_ARRAY); // after second element

    when(mockJsonReader.hasNext())
        .thenReturn(true, true, false);

    when(mockComponentTypeAdapter.read(mockJsonReader))
        .thenReturn(1)
        .thenReturn(2);

    doNothing().when(mockJsonReader).beginArray();
    doNothing().when(mockJsonReader).endArray();

    Object result = arrayTypeAdapter.read(mockJsonReader);

    assertNotNull(result);
    assertTrue(result.getClass().isArray());
    assertEquals(Integer.class, result.getClass().getComponentType());
    Integer[] resultArray = (Integer[]) result;
    assertArrayEquals(new Integer[] {1, 2}, resultArray);

    InOrder inOrder = inOrder(mockJsonReader, mockComponentTypeAdapter);
    inOrder.verify(mockJsonReader).peek();
    inOrder.verify(mockJsonReader).beginArray();
    inOrder.verify(mockJsonReader, times(3)).hasNext();
    inOrder.verify(mockComponentTypeAdapter).read(mockJsonReader);
    inOrder.verify(mockComponentTypeAdapter).read(mockJsonReader);
    inOrder.verify(mockJsonReader).endArray();
  }

  @Test
    @Timeout(8000)
  void read_arrayOfPrimitives_returnsPrimitiveArray() throws IOException {
    TypeAdapter<Integer> intAdapter = mock(TypeAdapter.class);
    ArrayTypeAdapter<Integer> primitiveArrayAdapter =
        new ArrayTypeAdapter<>(mockGson, intAdapter, int.class);

    JsonReader mockJsonReader = mock(JsonReader.class);

    when(mockJsonReader.peek())
        .thenReturn(JsonToken.BEGIN_ARRAY) // initial peek
        .thenReturn(JsonToken.NUMBER) // first element
        .thenReturn(JsonToken.NUMBER) // second element
        .thenReturn(JsonToken.END_ARRAY); // after second element

    when(mockJsonReader.hasNext())
        .thenReturn(true, true, false);

    when(intAdapter.read(mockJsonReader))
        .thenReturn(1)
        .thenReturn(2);

    doNothing().when(mockJsonReader).beginArray();
    doNothing().when(mockJsonReader).endArray();

    Object result = primitiveArrayAdapter.read(mockJsonReader);

    assertNotNull(result);
    assertTrue(result.getClass().isArray());
    assertEquals(int.class, result.getClass().getComponentType());

    // Cast to int[] using reflection because result is Object
    int length = Array.getLength(result);
    assertEquals(2, length);
    assertEquals(1, Array.get(result, 0));
    assertEquals(2, Array.get(result, 1));
  }
}
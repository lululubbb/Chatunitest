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

class ArrayTypeAdapter_270_2Test {

  private Gson gson;
  private TypeAdapter<Object> componentTypeAdapter;
  private ArrayTypeAdapter<Object> arrayTypeAdapter;

  @BeforeEach
  void setUp() {
    gson = mock(Gson.class);
    componentTypeAdapter = mock(TypeAdapter.class);
    // Using Object class for componentType for testing
    arrayTypeAdapter = new ArrayTypeAdapter<>(gson, componentTypeAdapter, Object.class);
  }

  @Test
    @Timeout(8000)
  void read_nullJsonToken_returnsNull() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);

    doAnswer(invocation -> {
      return null;
    }).when(in).nextNull();

    Object result = arrayTypeAdapter.read(in);

    verify(in).peek();
    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_emptyArray_returnsEmptyArray() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
    when(in.hasNext()).thenReturn(false);

    doNothing().when(in).beginArray();
    doNothing().when(in).endArray();

    Object[] emptyArray = new Object[0];
    when(componentTypeAdapter.read(in)).thenReturn(null); // won't be called

    Object result = arrayTypeAdapter.read(in);

    verify(in).peek();
    verify(in).beginArray();
    verify(in).hasNext();
    verify(in).endArray();
    assertTrue(result instanceof Object[]);
    assertEquals(0, Array.getLength(result));
  }

  @Test
    @Timeout(8000)
  void read_arrayOfObjects_returnsArrayWithObjects() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
    when(in.hasNext()).thenReturn(true, true, false);

    doNothing().when(in).beginArray();
    doNothing().when(in).endArray();

    Object element1 = "first";
    Object element2 = "second";

    when(componentTypeAdapter.read(in)).thenReturn(element1, element2);

    Object result = arrayTypeAdapter.read(in);

    verify(in).peek();
    verify(in).beginArray();
    verify(in, times(3)).hasNext();
    verify(in).endArray();
    verify(componentTypeAdapter, times(2)).read(in);

    assertTrue(result instanceof Object[]);
    Object[] arrayResult = (Object[]) result;
    assertEquals(2, arrayResult.length);
    assertEquals(element1, arrayResult[0]);
    assertEquals(element2, arrayResult[1]);
  }

  @Test
    @Timeout(8000)
  void read_arrayOfPrimitiveType_returnsPrimitiveArray() throws IOException {
    // For primitive type, create a new ArrayTypeAdapter with componentType int.class
    @SuppressWarnings("unchecked")
    TypeAdapter<Integer> intAdapter = mock(TypeAdapter.class);
    ArrayTypeAdapter<Integer> adapter = new ArrayTypeAdapter<>(gson, intAdapter, int.class);

    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
    when(in.hasNext()).thenReturn(true, true, false);

    doNothing().when(in).beginArray();
    doNothing().when(in).endArray();

    when(intAdapter.read(in)).thenReturn(1, 2);

    Object result = adapter.read(in);

    verify(in).peek();
    verify(in).beginArray();
    verify(in, times(3)).hasNext();
    verify(in).endArray();
    verify(intAdapter, times(2)).read(in);

    assertNotNull(result);
    assertTrue(result.getClass().isArray());
    assertEquals(int.class, result.getClass().getComponentType());
    assertEquals(2, Array.getLength(result));
    assertEquals(1, Array.get(result, 0));
    assertEquals(2, Array.get(result, 1));
  }
}
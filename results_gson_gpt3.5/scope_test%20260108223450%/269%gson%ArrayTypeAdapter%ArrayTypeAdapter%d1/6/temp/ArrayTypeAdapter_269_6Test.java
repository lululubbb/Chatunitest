package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Array;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class ArrayTypeAdapter_269_6Test {

  private Gson mockGson;
  private TypeAdapter<String> mockComponentAdapter;
  private ArrayTypeAdapter<String> arrayTypeAdapter;

  @BeforeEach
  public void setUp() {
    mockGson = mock(Gson.class);
    mockComponentAdapter = mock(TypeAdapter.class);
    arrayTypeAdapter = new ArrayTypeAdapter<>(mockGson, mockComponentAdapter, String.class);
  }

  @Test
    @Timeout(8000)
  public void testRead_nullJson_returnsNull() throws IOException {
    JsonReader mockReader = mock(JsonReader.class);
    when(mockReader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(mockReader).nextNull();

    Object result = arrayTypeAdapter.read(mockReader);

    assertNull(result);
    verify(mockReader).nextNull();
    verifyNoMoreInteractions(mockComponentAdapter);
  }

  @Test
    @Timeout(8000)
  public void testRead_nonNullArray_readsElements() throws IOException {
    JsonReader mockReader = mock(JsonReader.class);

    // Setup peek() to return BEGIN_ARRAY, STRING, STRING, END_ARRAY in order
    when(mockReader.peek()).thenReturn(
        JsonToken.BEGIN_ARRAY,
        JsonToken.STRING,
        JsonToken.STRING,
        JsonToken.END_ARRAY);

    doNothing().when(mockReader).beginArray();
    doNothing().when(mockReader).endArray();

    // Setup component adapter to return "a" and "b" on successive read calls
    when(mockComponentAdapter.read(mockReader)).thenReturn("a", "b");

    Object result = arrayTypeAdapter.read(mockReader);

    assertNotNull(result);
    assertTrue(result.getClass().isArray());
    assertEquals(String[].class, result.getClass());
    String[] arr = (String[]) result;
    assertArrayEquals(new String[]{"a", "b"}, arr);

    InOrder inOrder = inOrder(mockReader, mockComponentAdapter);
    inOrder.verify(mockReader).peek();
    inOrder.verify(mockReader).beginArray();

    // First element
    inOrder.verify(mockReader).peek();
    inOrder.verify(mockComponentAdapter, times(1)).read(mockReader);

    // Second element
    inOrder.verify(mockReader).peek();
    inOrder.verify(mockComponentAdapter, times(1)).read(mockReader);

    inOrder.verify(mockReader).peek();
    inOrder.verify(mockReader).endArray();
  }

  @Test
    @Timeout(8000)
  public void testWrite_nullArray_writesNull() throws IOException {
    JsonWriter mockWriter = mock(JsonWriter.class);
    arrayTypeAdapter.write(mockWriter, null);
    verify(mockWriter).nullValue();
    verifyNoMoreInteractions(mockComponentAdapter);
  }

  @Test
    @Timeout(8000)
  public void testWrite_nonNullArray_writesElements() throws IOException {
    JsonWriter mockWriter = mock(JsonWriter.class);
    String[] input = new String[]{"x", "y"};

    // No need to stub doNothing() on void methods, just verify calls
    arrayTypeAdapter.write(mockWriter, input);

    InOrder inOrder = inOrder(mockWriter, mockComponentAdapter);
    inOrder.verify(mockWriter).beginArray();
    inOrder.verify(mockComponentAdapter).write(eq(mockWriter), eq("x"));
    inOrder.verify(mockComponentAdapter).write(eq(mockWriter), eq("y"));
    inOrder.verify(mockWriter).endArray();
  }

  @Test
    @Timeout(8000)
  public void testFactory_create_withArrayType_returnsArrayTypeAdapter() {
    TypeToken<String[]> typeToken = TypeToken.get(String[].class);
    Gson gson = mock(Gson.class);
    TypeAdapter<String> componentAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(TypeToken.get(String.class))).thenReturn(componentAdapter);

    TypeAdapter<?> adapter = ArrayTypeAdapter.FACTORY.create(gson, typeToken);

    assertNotNull(adapter);
    assertTrue(adapter instanceof ArrayTypeAdapter);
  }

  @Test
    @Timeout(8000)
  public void testFactory_create_withNonArrayType_returnsNull() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    Gson gson = mock(Gson.class);

    TypeAdapter<?> adapter = ArrayTypeAdapter.FACTORY.create(gson, typeToken);

    assertNull(adapter);
  }
}
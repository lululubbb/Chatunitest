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

class ArrayTypeAdapter_269_5Test {

  private Gson gson;
  private TypeAdapter<String> componentTypeAdapter;
  private ArrayTypeAdapter<String> arrayTypeAdapter;
  private Class<String> componentType;

  @BeforeEach
  void setUp() {
    gson = mock(Gson.class);
    componentTypeAdapter = mock(TypeAdapter.class);
    componentType = String.class;
    arrayTypeAdapter = new ArrayTypeAdapter<>(gson, componentTypeAdapter, componentType);
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
    when(componentTypeAdapter.read(in)).thenThrow(new AssertionError("Should not be called"));

    Object result = arrayTypeAdapter.read(in);

    verify(in).beginArray();
    verify(in).endArray();
    assertEquals(0, Array.getLength(result));
    assertEquals(componentType, result.getClass().getComponentType());
  }

  @Test
    @Timeout(8000)
  void testRead_arrayWithElements_returnsArray() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(
        JsonToken.BEGIN_ARRAY,
        JsonToken.STRING,
        JsonToken.STRING,
        JsonToken.END_ARRAY);
    when(componentTypeAdapter.read(in)).thenReturn("first", "second");

    Object result = arrayTypeAdapter.read(in);

    InOrder inOrder = inOrder(in, componentTypeAdapter);
    inOrder.verify(in).beginArray();
    inOrder.verify(componentTypeAdapter).read(in);
    inOrder.verify(componentTypeAdapter).read(in);
    inOrder.verify(in).endArray();

    assertEquals(2, Array.getLength(result));
    assertEquals("first", Array.get(result, 0));
    assertEquals("second", Array.get(result, 1));
    assertEquals(componentType, result.getClass().getComponentType());
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
    Object array = Array.newInstance(componentType, 0);

    arrayTypeAdapter.write(out, array);

    InOrder inOrder = inOrder(out);
    inOrder.verify(out).beginArray();
    inOrder.verify(out).endArray();
  }

  @Test
    @Timeout(8000)
  void testWrite_arrayWithElements_writesElements() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    Object array = Array.newInstance(componentType, 2);
    Array.set(array, 0, "foo");
    Array.set(array, 1, "bar");

    doAnswer(invocation -> {
      String value = invocation.getArgument(1);
      assertTrue(value.equals("foo") || value.equals("bar"));
      return null;
    }).when(componentTypeAdapter).write(any(JsonWriter.class), anyString());

    arrayTypeAdapter.write(out, array);

    InOrder inOrder = inOrder(out, componentTypeAdapter);
    inOrder.verify(out).beginArray();
    inOrder.verify(componentTypeAdapter).write(out, "foo");
    inOrder.verify(componentTypeAdapter).write(out, "bar");
    inOrder.verify(out).endArray();
  }

  @Test
    @Timeout(8000)
  void testFactory_create_withArrayType_returnsArrayTypeAdapter() {
    TypeToken<String[]> typeToken = TypeToken.get(String[].class);
    Gson gsonInstance = mock(Gson.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> stringAdapter = mock(TypeAdapter.class);
    when(gsonInstance.getAdapter(TypeToken.get(String.class))).thenReturn(stringAdapter);

    TypeAdapter<?> adapter = ArrayTypeAdapter.FACTORY.create(gsonInstance, typeToken);

    assertNotNull(adapter);
    assertTrue(adapter instanceof ArrayTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void testFactory_create_withNonArrayType_returnsNull() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    Gson gsonInstance = mock(Gson.class);

    TypeAdapter<?> adapter = ArrayTypeAdapter.FACTORY.create(gsonInstance, typeToken);

    assertNull(adapter);
  }
}
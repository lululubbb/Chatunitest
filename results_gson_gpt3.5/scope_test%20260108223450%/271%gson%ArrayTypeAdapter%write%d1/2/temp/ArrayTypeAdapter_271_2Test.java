package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Array;

public class ArrayTypeAdapter_271_2Test {

  private JsonWriter jsonWriter;
  private TypeAdapter<Object> componentTypeAdapter;
  private ArrayTypeAdapter<Object> arrayTypeAdapter;

  @BeforeEach
  public void setUp() {
    jsonWriter = mock(JsonWriter.class);

    // Create a real TypeAdapter mock and stub its write method to do nothing
    componentTypeAdapter = mock(TypeAdapter.class);

    // Pass a non-null Gson instance to avoid NullPointerException inside ArrayTypeAdapter
    arrayTypeAdapter = new ArrayTypeAdapter<>(new Gson(), componentTypeAdapter, Object.class);
  }

  @Test
    @Timeout(8000)
  public void write_nullArray_writesNullValue() throws IOException {
    arrayTypeAdapter.write(jsonWriter, null);
    verify(jsonWriter).nullValue();
    verifyNoMoreInteractions(jsonWriter);
    verifyNoInteractions(componentTypeAdapter);
  }

  @Test
    @Timeout(8000)
  public void write_emptyArray_writesBeginAndEndArray() throws IOException {
    Object emptyArray = Array.newInstance(Object.class, 0);

    arrayTypeAdapter.write(jsonWriter, emptyArray);

    InOrder inOrder = inOrder(jsonWriter);
    inOrder.verify(jsonWriter).beginArray();
    inOrder.verify(jsonWriter).endArray();

    verifyNoInteractions(componentTypeAdapter);
  }

  @Test
    @Timeout(8000)
  public void write_arrayWithElements_writesAllElements() throws IOException {
    Object[] array = new Object[] {"one", "two", "three"};

    arrayTypeAdapter.write(jsonWriter, array);

    InOrder inOrder = inOrder(jsonWriter);
    inOrder.verify(jsonWriter).beginArray();

    // Verify componentTypeAdapter.write called with each element in order
    InOrder inOrderComponent = inOrder(componentTypeAdapter);
    inOrderComponent.verify(componentTypeAdapter).write(jsonWriter, "one");
    inOrderComponent.verify(componentTypeAdapter).write(jsonWriter, "two");
    inOrderComponent.verify(componentTypeAdapter).write(jsonWriter, "three");

    inOrder.verify(jsonWriter).endArray();
  }

  @Test
    @Timeout(8000)
  public void write_arrayWithNullElements_writesNullElements() throws IOException {
    Object[] array = new Object[] {"one", null, "three"};

    arrayTypeAdapter.write(jsonWriter, array);

    InOrder inOrder = inOrder(jsonWriter);
    inOrder.verify(jsonWriter).beginArray();

    InOrder inOrderComponent = inOrder(componentTypeAdapter);
    inOrderComponent.verify(componentTypeAdapter).write(jsonWriter, "one");
    inOrderComponent.verify(componentTypeAdapter).write(jsonWriter, null);
    inOrderComponent.verify(componentTypeAdapter).write(jsonWriter, "three");

    inOrder.verify(jsonWriter).endArray();
  }
}
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
import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class ArrayTypeAdapter_271_5Test {

  private JsonWriter jsonWriter;
  private TypeAdapter<Object> componentTypeAdapter;
  private ArrayTypeAdapter<Object> arrayTypeAdapter;
  private StringWriter stringWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = spy(new JsonWriter(stringWriter));
    componentTypeAdapter = mock(TypeAdapter.class);
    // Mock componentTypeAdapter.write to call real method to avoid swallowing calls
    try {
      doAnswer(invocation -> {
        // simulate writing by forwarding to the real JsonWriter for strings and nulls
        JsonWriter out = invocation.getArgument(0);
        Object value = invocation.getArgument(1);
        if (value == null) {
          out.nullValue();
        } else if (value instanceof String) {
          out.value((String) value);
        } else {
          // fallback, do nothing
        }
        return null;
      }).when(componentTypeAdapter).write(any(JsonWriter.class), any());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    // Pass a non-null Gson instance to avoid NullPointerException in ArrayTypeAdapter
    arrayTypeAdapter = new ArrayTypeAdapter<>(new Gson(), componentTypeAdapter, Object.class);
  }

  @Test
    @Timeout(8000)
  public void write_nullArray_shouldWriteNullValue() throws IOException {
    arrayTypeAdapter.write(jsonWriter, null);
    verify(jsonWriter).nullValue();
    verifyNoMoreInteractions(componentTypeAdapter);
  }

  @Test
    @Timeout(8000)
  public void write_emptyArray_shouldWriteBeginAndEndArray() throws IOException {
    Object emptyArray = Array.newInstance(Object.class, 0);
    arrayTypeAdapter.write(jsonWriter, emptyArray);
    InOrder inOrder = inOrder(jsonWriter, componentTypeAdapter);
    inOrder.verify(jsonWriter).beginArray();
    inOrder.verify(jsonWriter).endArray();
    verifyNoMoreInteractions(componentTypeAdapter);
  }

  @Test
    @Timeout(8000)
  public void write_arrayWithElements_shouldWriteAllElements() throws IOException {
    Object[] array = new Object[] {"a", "b", null};
    arrayTypeAdapter.write(jsonWriter, array);
    InOrder inOrder = inOrder(jsonWriter, componentTypeAdapter);
    inOrder.verify(jsonWriter).beginArray();
    // The componentTypeAdapter is mocked and its write method is stubbed,
    // but the actual calls to componentTypeAdapter.write() are not recorded by Mockito
    // because the ArrayTypeAdapter calls componentTypeAdapter.write(), but componentTypeAdapter is a mock.
    // To verify the calls, we need to spy the componentTypeAdapter instead of mock.
    // So fix: change componentTypeAdapter to spy.

    // To fix this, we will change componentTypeAdapter to spy of a real TypeAdapter implementation.
    // But since we can't do that easily, the simplest fix is to verify the interactions on componentTypeAdapter.

    // The problem is that the componentTypeAdapter.write() method is called, but Mockito cannot verify it
    // because the method is stubbed with doAnswer and the call is swallowed.

    // So we fix this by changing componentTypeAdapter to spy and doAnswer on spy.

    // However, since we cannot change the test here, just verify calls on componentTypeAdapter.write instead of verifying inOrder.

    // So the fix is to verify calls on componentTypeAdapter.write with correct arguments.

    inOrder.verify(componentTypeAdapter).write(jsonWriter, "a");
    inOrder.verify(componentTypeAdapter).write(jsonWriter, "b");
    inOrder.verify(componentTypeAdapter).write(jsonWriter, null);
    inOrder.verify(jsonWriter).endArray();
  }
}
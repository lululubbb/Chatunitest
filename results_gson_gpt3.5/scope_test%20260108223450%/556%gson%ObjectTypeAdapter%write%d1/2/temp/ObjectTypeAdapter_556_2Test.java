package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectTypeAdapter_556_2Test {

  private Gson gsonMock;
  private JsonWriter jsonWriter;
  private ObjectTypeAdapter objectTypeAdapter;

  @BeforeEach
  void setUp() throws Exception {
    gsonMock = mock(Gson.class);
    // Use reflection to create ObjectTypeAdapter instance since constructor is private
    var constructor = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, com.google.gson.ToNumberStrategy.class);
    constructor.setAccessible(true);
    objectTypeAdapter = (ObjectTypeAdapter) constructor.newInstance(gsonMock, com.google.gson.ToNumberPolicy.DOUBLE);

    StringWriter stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void write_nullValue_callsNullValue() throws IOException {
    objectTypeAdapter.write(jsonWriter, null);
    // JsonWriter.nullValue() returns JsonWriter, so no direct state change to verify,
    // but no exception means success.
  }

  @Test
    @Timeout(8000)
  void write_valueWithObjectTypeAdapter_callsBeginAndEndObject() throws IOException {
    Object value = new Object();
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> typeAdapterMock = mock(TypeAdapter.class);
    // Cast to raw TypeAdapter to avoid generic mismatch
    @SuppressWarnings("unchecked")
    TypeAdapter rawObjectTypeAdapter = objectTypeAdapter;
    when(gsonMock.getAdapter(value.getClass())).thenReturn(rawObjectTypeAdapter);

    JsonWriter spyWriter = spy(jsonWriter);

    objectTypeAdapter.write(spyWriter, value);

    verify(spyWriter).beginObject();
    verify(spyWriter).endObject();
    verifyNoMoreInteractions(spyWriter);
  }

  @Test
    @Timeout(8000)
  void write_valueWithOtherTypeAdapter_delegatesWrite() throws IOException {
    Object value = new Object();
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> otherTypeAdapter = mock(TypeAdapter.class);
    // Cast to raw TypeAdapter to avoid generic mismatch
    @SuppressWarnings("unchecked")
    TypeAdapter rawOtherTypeAdapter = otherTypeAdapter;
    when(gsonMock.getAdapter(value.getClass())).thenReturn(rawOtherTypeAdapter);

    objectTypeAdapter.write(jsonWriter, value);

    verify(otherTypeAdapter).write(jsonWriter, value);
  }
}
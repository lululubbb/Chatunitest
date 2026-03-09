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

class ObjectTypeAdapter_556_6Test {

  private Gson gsonMock;
  private JsonWriter jsonWriter;
  private StringWriter stringWriter;
  private ObjectTypeAdapter objectTypeAdapter;

  @BeforeEach
  void setUp() throws Exception {
    gsonMock = mock(Gson.class);
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
    // Use reflection to create ObjectTypeAdapter instance since constructor is private
    var constructor = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, com.google.gson.ToNumberStrategy.class);
    constructor.setAccessible(true);
    // The second param is ToNumberStrategy, pass null for test since it's not used in write()
    objectTypeAdapter = (ObjectTypeAdapter) constructor.newInstance(gsonMock, null);
  }

  @Test
    @Timeout(8000)
  void write_nullValue_callsNullValue() throws IOException {
    objectTypeAdapter.write(jsonWriter, null);
    String output = stringWriter.toString();
    // JsonWriter.nullValue() writes "null"
    assert output.equals("null");
  }

  @Test
    @Timeout(8000)
  void write_valueWithObjectTypeAdapterAdapter_writesEmptyObject() throws IOException {
    Object value = new Object();
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> adapterMock = (TypeAdapter<Object>) (TypeAdapter<?>) objectTypeAdapter;
    when(gsonMock.<Object>getAdapter((Class<Object>) value.getClass())).thenReturn(adapterMock);

    objectTypeAdapter.write(jsonWriter, value);

    String output = stringWriter.toString();
    // Should write {}
    assert output.equals("{}");
  }

  @Test
    @Timeout(8000)
  void write_valueWithDifferentAdapter_delegatesWrite() throws IOException {
    Object value = "testString";
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> adapterMock = mock(TypeAdapter.class);
    when(gsonMock.<Object>getAdapter((Class<Object>) value.getClass())).thenReturn(adapterMock);

    objectTypeAdapter.write(jsonWriter, value);

    verify(adapterMock).write(jsonWriter, value);
  }
}
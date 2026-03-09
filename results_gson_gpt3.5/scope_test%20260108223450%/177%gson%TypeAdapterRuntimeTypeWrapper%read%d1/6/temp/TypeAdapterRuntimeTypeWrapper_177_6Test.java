package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.TypeVariable;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapterRuntimeTypeWrapper_177_6Test {

  private Gson context;
  private TypeAdapter<String> delegate;
  private Type type;
  private TypeAdapterRuntimeTypeWrapper<String> wrapper;
  private JsonReader jsonReader;

  @BeforeEach
  void setup() {
    context = mock(Gson.class);
    delegate = mock(TypeAdapter.class);
    type = String.class;
    wrapper = new TypeAdapterRuntimeTypeWrapper<>(context, delegate, type);
    jsonReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  void read_delegatesToDelegate() throws IOException {
    String expected = "testValue";
    when(delegate.read(jsonReader)).thenReturn(expected);

    String actual = wrapper.read(jsonReader);

    assertSame(expected, actual);
  }
}
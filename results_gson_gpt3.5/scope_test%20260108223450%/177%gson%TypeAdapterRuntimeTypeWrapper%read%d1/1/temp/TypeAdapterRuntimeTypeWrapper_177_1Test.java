package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.TypeVariable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapterRuntimeTypeWrapper_177_1Test {

  Gson context;
  TypeAdapter<Object> delegate;
  TypeAdapterRuntimeTypeWrapper<Object> wrapper;
  JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    context = mock(Gson.class);
    delegate = mock(TypeAdapter.class);
    jsonReader = mock(JsonReader.class);
    wrapper = new TypeAdapterRuntimeTypeWrapper<>(context, delegate, Object.class);
  }

  @Test
    @Timeout(8000)
  void read_delegatesToDelegateRead() throws IOException {
    Object expected = new Object();
    when(delegate.read(jsonReader)).thenReturn(expected);

    Object actual = wrapper.read(jsonReader);

    verify(delegate).read(jsonReader);
    assertSame(expected, actual);
  }
}
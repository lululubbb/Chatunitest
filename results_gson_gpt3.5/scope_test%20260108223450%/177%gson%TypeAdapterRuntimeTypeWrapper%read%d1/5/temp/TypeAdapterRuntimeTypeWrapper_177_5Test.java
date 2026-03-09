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

class TypeAdapterRuntimeTypeWrapper_177_5Test {

  Gson mockContext;
  TypeAdapter<Object> mockDelegate;
  JsonReader mockJsonReader;
  TypeAdapterRuntimeTypeWrapper<Object> wrapper;
  Type mockType;

  @BeforeEach
  void setUp() {
    mockContext = mock(Gson.class);
    mockDelegate = mock(TypeAdapter.class);
    mockJsonReader = mock(JsonReader.class);
    mockType = Object.class;
    wrapper = new TypeAdapterRuntimeTypeWrapper<>(mockContext, mockDelegate, mockType);
  }

  @Test
    @Timeout(8000)
  void read_delegatesToDelegateRead() throws IOException {
    Object expected = new Object();
    when(mockDelegate.read(mockJsonReader)).thenReturn(expected);

    Object actual = wrapper.read(mockJsonReader);

    verify(mockDelegate).read(mockJsonReader);
    assertSame(expected, actual);
  }
}
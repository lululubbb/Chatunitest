package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.TypeVariable;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.lang.reflect.Type;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TypeAdapterRuntimeTypeWrapper_177_3Test {

  private Gson mockGson;
  private TypeAdapter<Object> mockDelegate;
  private Type mockType;
  private JsonReader mockJsonReader;
  private TypeAdapterRuntimeTypeWrapper<Object> wrapper;

  @BeforeEach
  void setUp() {
    mockGson = mock(Gson.class);
    mockDelegate = mock(TypeAdapter.class);
    mockType = Object.class;
    mockJsonReader = mock(JsonReader.class);
    wrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, mockDelegate, mockType);
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
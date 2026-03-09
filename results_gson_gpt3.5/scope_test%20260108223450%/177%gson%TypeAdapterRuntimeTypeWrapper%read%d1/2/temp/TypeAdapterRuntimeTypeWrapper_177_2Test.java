package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.TypeVariable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;

class TypeAdapterRuntimeTypeWrapper_177_2Test {

  @Mock
  private Gson mockGson;

  @Mock
  private TypeAdapter<Object> mockDelegate;

  @Mock
  private JsonReader mockJsonReader;

  private TypeAdapterRuntimeTypeWrapper<Object> wrapper;

  private final Type mockType = Object.class;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    wrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, mockDelegate, mockType);
  }

  @Test
    @Timeout(8000)
  void read_delegatesToDelegateRead() throws IOException {
    Object expected = new Object();
    when(mockDelegate.read(mockJsonReader)).thenReturn(expected);

    Object actual = wrapper.read(mockJsonReader);

    assertSame(expected, actual);
    verify(mockDelegate).read(mockJsonReader);
  }

  @Test
    @Timeout(8000)
  void read_delegateThrowsIOException_propagates() throws IOException {
    when(mockDelegate.read(mockJsonReader)).thenThrow(IOException.class);

    assertThrows(IOException.class, () -> wrapper.read(mockJsonReader));
    verify(mockDelegate).read(mockJsonReader);
  }
}
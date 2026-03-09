package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.TypeVariable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

final class TypeAdapterRuntimeTypeWrapper_178_2Test {

  Gson mockGson;
  TypeAdapter<Object> mockDelegate;
  JsonWriter mockJsonWriter;

  static class DummyReflectiveAdapter extends Adapter<Object, Object> {
    @SuppressWarnings("unchecked")
    DummyReflectiveAdapter() {
      // The Adapter constructor requires Map<String, BoundField>
      super((Map<String, BoundField>) (Map<?, ?>) Collections.emptyMap());
    }

    @Override
    public void write(JsonWriter out, Object value) throws IOException {}

    @Override
    public Object read(com.google.gson.stream.JsonReader in) throws IOException {
      return null;
    }

    @Override
    public void finalize(Object value) {
      // Provide empty implementation to satisfy abstract method
    }
  }

  @BeforeEach
  void setUp() {
    mockGson = mock(Gson.class);
    mockDelegate = mock(TypeAdapter.class);
    mockJsonWriter = mock(JsonWriter.class);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeEqualsDeclaredType_usesDelegateWrite() throws IOException {
    Object value = new Object();
    Type declaredType = Object.class;

    TypeAdapterRuntimeTypeWrapper<Object> wrapper =
        new TypeAdapterRuntimeTypeWrapper<>(mockGson, mockDelegate, declaredType);

    // runtimeType == declaredType, so getAdapter should not be called
    wrapper.write(mockJsonWriter, value);

    verify(mockDelegate).write(mockJsonWriter, value);
    verifyNoInteractions(mockGson);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferent_nonReflectiveRuntimeTypeAdapter_usesRuntimeTypeAdapter() throws IOException {
    Object value = "test";
    Type declaredType = Object.class;
    Type runtimeType = String.class;

    @SuppressWarnings("unchecked")
    TypeAdapter<String> mockRuntimeTypeAdapter = mock(TypeAdapter.class);

    when(mockGson.getAdapter(TypeToken.get(runtimeType))).thenReturn(mockRuntimeTypeAdapter);

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> delegate = mock(TypeAdapter.class);
    TypeAdapterRuntimeTypeWrapper<Object> wrapper =
        new TypeAdapterRuntimeTypeWrapper<>(mockGson, delegate, declaredType);

    wrapper.write(mockJsonWriter, value);

    InOrder inOrder = inOrder(mockGson, mockRuntimeTypeAdapter, delegate);
    inOrder.verify(mockGson).getAdapter(TypeToken.get(runtimeType));
    inOrder.verify(mockRuntimeTypeAdapter).write(mockJsonWriter, value);
    verifyNoMoreInteractions(delegate);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferent_reflectiveRuntimeTypeAdapter_delegateIsNotReflective_usesDelegate() throws IOException {
    Object value = "test";
    Type declaredType = Object.class;
    Type runtimeType = String.class;

    DummyReflectiveAdapter reflectiveAdapter = new DummyReflectiveAdapter();
    when(mockGson.getAdapter(TypeToken.get(runtimeType))).thenReturn(reflectiveAdapter);

    // delegate is NOT reflective - use a mock TypeAdapter whose class is not ReflectiveTypeAdapterFactory.Adapter
    TypeAdapter<Object> nonReflectiveDelegate = mock(TypeAdapter.class);

    TypeAdapterRuntimeTypeWrapper<Object> wrapper =
        new TypeAdapterRuntimeTypeWrapper<>(mockGson, nonReflectiveDelegate, declaredType);

    wrapper.write(mockJsonWriter, value);

    verify(mockGson).getAdapter(TypeToken.get(runtimeType));
    verify(nonReflectiveDelegate).write(mockJsonWriter, value);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferent_reflectiveRuntimeTypeAdapter_delegateIsReflective_usesRuntimeTypeAdapter() throws IOException {
    Object value = "test";
    Type declaredType = Object.class;
    Type runtimeType = String.class;

    DummyReflectiveAdapter reflectiveAdapter = new DummyReflectiveAdapter();
    when(mockGson.getAdapter(TypeToken.get(runtimeType))).thenReturn(reflectiveAdapter);

    // delegate is reflective - use spy of reflectiveAdapter
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> reflectiveDelegate = spy(reflectiveAdapter);

    TypeAdapterRuntimeTypeWrapper<Object> wrapper =
        new TypeAdapterRuntimeTypeWrapper<>(mockGson, reflectiveDelegate, declaredType);

    wrapper.write(mockJsonWriter, value);

    verify(mockGson).getAdapter(TypeToken.get(runtimeType));
    verify(reflectiveDelegate, never()).write(any(), any());
    verify(reflectiveAdapter).write(mockJsonWriter, value);
  }

  @Test
    @Timeout(8000)
  void write_valueNull_usesDelegateWrite() throws IOException {
    Object value = null;
    Type declaredType = Object.class;

    TypeAdapterRuntimeTypeWrapper<Object> wrapper =
        new TypeAdapterRuntimeTypeWrapper<>(mockGson, mockDelegate, declaredType);

    wrapper.write(mockJsonWriter, value);

    verify(mockDelegate).write(mockJsonWriter, null);
    verifyNoInteractions(mockGson);
  }
}
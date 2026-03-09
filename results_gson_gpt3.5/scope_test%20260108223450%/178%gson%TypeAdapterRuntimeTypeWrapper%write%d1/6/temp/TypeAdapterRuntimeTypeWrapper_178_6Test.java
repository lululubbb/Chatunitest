package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.TypeVariable;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.stream.JsonWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;

class TypeAdapterRuntimeTypeWrapper_178_6Test {

  @Mock Gson mockGson;
  @Mock TypeAdapter<Object> mockDelegateAdapter;
  @Mock JsonWriter mockJsonWriter;

  TypeAdapterRuntimeTypeWrapper<Object> wrapper;
  Type declaredType = Object.class;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    wrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, mockDelegateAdapter, declaredType);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeSameAsDeclaredType_usesDelegateWrite() throws IOException {
    Object value = new Object();

    // runtimeType == declaredType, so no call to mockGson.getAdapter expected
    wrapper.write(mockJsonWriter, value);

    verify(mockDelegateAdapter).write(mockJsonWriter, value);
    verifyNoMoreInteractions(mockGson);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferentAndRuntimeAdapterNotReflective_usesRuntimeAdapter() throws IOException {
    Object value = "string"; // runtimeType = String.class, declaredType = Object.class
    Class<?> runtimeType = String.class;

    @SuppressWarnings("unchecked")
    TypeAdapter<String> mockRuntimeTypeAdapter = (TypeAdapter<String>) mock(TypeAdapter.class);

    when(mockGson.getAdapter((TypeToken) TypeToken.get(runtimeType))).thenReturn(mockRuntimeTypeAdapter);

    wrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, mockDelegateAdapter, declaredType);

    wrapper.write(mockJsonWriter, value);

    verify(mockRuntimeTypeAdapter).write(mockJsonWriter, "string");
    verify(mockGson).getAdapter((TypeToken) TypeToken.get(runtimeType));
    verifyNoMoreInteractions(mockDelegateAdapter);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferentRuntimeAdapterReflectiveDelegateNotReflective_usesDelegate() throws IOException {
    Object value = "string"; // runtimeType = String.class, declaredType = Object.class
    Class<?> runtimeType = String.class;

    @SuppressWarnings("unchecked")
    TypeAdapter<String> reflectiveRuntimeAdapter = (TypeAdapter<String>) mock(Adapter.class);

    when(mockGson.getAdapter((TypeToken) TypeToken.get(runtimeType))).thenReturn(reflectiveRuntimeAdapter);

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> nonReflectiveDelegate = mock(TypeAdapter.class);
    wrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, nonReflectiveDelegate, declaredType);

    wrapper.write(mockJsonWriter, value);

    verify(nonReflectiveDelegate).write(mockJsonWriter, value);
    verify(mockGson).getAdapter((TypeToken) TypeToken.get(runtimeType));
    verifyNoMoreInteractions(reflectiveRuntimeAdapter);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferentRuntimeAdapterReflectiveDelegateReflective_usesRuntimeAdapter() throws IOException {
    Object value = "string"; // runtimeType = String.class, declaredType = Object.class
    Class<?> runtimeType = String.class;

    @SuppressWarnings("unchecked")
    TypeAdapter<String> reflectiveRuntimeAdapter = (TypeAdapter<String>) mock(Adapter.class);

    when(mockGson.getAdapter((TypeToken) TypeToken.get(runtimeType))).thenReturn(reflectiveRuntimeAdapter);

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> reflectiveDelegate = (TypeAdapter<Object>) mock(Adapter.class);
    wrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, reflectiveDelegate, declaredType);

    wrapper.write(mockJsonWriter, value);

    verify(reflectiveRuntimeAdapter).write(mockJsonWriter, "string");
    verify(mockGson).getAdapter((TypeToken) TypeToken.get(runtimeType));
    verifyNoMoreInteractions(reflectiveDelegate);
  }

  @Test
    @Timeout(8000)
  void write_nullValue_usesDelegateWrite() throws IOException {
    Object value = null;

    wrapper.write(mockJsonWriter, value);

    verify(mockDelegateAdapter).write(mockJsonWriter, null);
    verifyNoMoreInteractions(mockGson);
  }
}
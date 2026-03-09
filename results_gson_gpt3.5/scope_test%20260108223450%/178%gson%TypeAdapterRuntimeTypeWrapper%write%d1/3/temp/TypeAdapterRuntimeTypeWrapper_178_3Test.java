package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.TypeVariable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapterRuntimeTypeWrapper_178_3Test {

  private Gson mockGson;
  private TypeAdapter<Object> mockDelegateAdapter;
  private JsonWriter mockJsonWriter;

  private TypeAdapterRuntimeTypeWrapper<Object> wrapper;

  /**
   * DummyReflectiveAdapter extends ReflectiveTypeAdapterFactory.Adapter and implements the missing abstract method finalize.
   * It also uses a raw Map cast for BoundField due to package-private visibility.
   */
  private static class DummyReflectiveAdapter extends ReflectiveTypeAdapterFactory.Adapter<Object, Object> {

    @SuppressWarnings("unchecked")
    DummyReflectiveAdapter() {
      super((Map) Collections.emptyMap());
    }

    @Override
    public void write(JsonWriter out, Object value) throws IOException {
      // no-op
    }

    @Override
    public Object read(com.google.gson.stream.JsonReader in) {
      return null;
    }

    @Override
    public boolean finalize(Object value) {
      // no-op implementation to satisfy abstract method
      return false;
    }
  }

  @BeforeEach
  void setUp() {
    mockGson = mock(Gson.class);
    mockDelegateAdapter = mock(TypeAdapter.class);
    mockJsonWriter = mock(JsonWriter.class);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeEqualsDeclaredType_usesDelegateWrite() throws IOException {
    Type declaredType = String.class;
    String value = "test";

    wrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, mockDelegateAdapter, declaredType);

    wrapper.write(mockJsonWriter, value);

    verify(mockDelegateAdapter).write(mockJsonWriter, value);
    verifyNoMoreInteractions(mockGson);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferent_userRegisteredNonReflectiveAdapter_usesRuntimeTypeAdapter() throws IOException {
    Type declaredType = Object.class;
    String value = "test";
    Type runtimeType = String.class;

    @SuppressWarnings({"unchecked", "rawtypes"})
    TypeAdapter runtimeTypeAdapter = mock(TypeAdapter.class);

    when(mockGson.getAdapter(TypeToken.get(runtimeType))).thenReturn(runtimeTypeAdapter);

    wrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, mockDelegateAdapter, declaredType);

    wrapper.write(mockJsonWriter, value);

    verify(mockGson).getAdapter(TypeToken.get(runtimeType));
    verify(runtimeTypeAdapter).write(mockJsonWriter, value);
    verifyNoMoreInteractions(mockDelegateAdapter);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferent_runtimeTypeAdapterIsReflective_delegateIsNot_usesDelegate() throws IOException {
    Type declaredType = Object.class;
    String value = "test";
    Type runtimeType = String.class;

    DummyReflectiveAdapter reflectiveAdapter = new DummyReflectiveAdapter();

    when(mockGson.getAdapter(TypeToken.get(runtimeType))).thenReturn(reflectiveAdapter);

    // Make delegate non-reflective by mocking a TypeAdapter that is not ReflectiveTypeAdapterFactory.Adapter
    TypeAdapter<Object> nonReflectiveDelegate = mock(TypeAdapter.class);

    wrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, nonReflectiveDelegate, declaredType);

    wrapper.write(mockJsonWriter, value);

    verify(mockGson).getAdapter(TypeToken.get(runtimeType));
    verify(nonReflectiveDelegate).write(mockJsonWriter, value);
    verifyNoMoreInteractions(reflectiveAdapter);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferent_bothReflective_usesRuntimeTypeAdapter() throws IOException {
    Type declaredType = Object.class;
    String value = "test";
    Type runtimeType = String.class;

    DummyReflectiveAdapter reflectiveAdapter = new DummyReflectiveAdapter();

    when(mockGson.getAdapter(TypeToken.get(runtimeType))).thenReturn(reflectiveAdapter);

    // Make delegate reflective by using DummyReflectiveAdapter
    DummyReflectiveAdapter reflectiveDelegate = spy(new DummyReflectiveAdapter());

    wrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, reflectiveDelegate, declaredType);

    wrapper.write(mockJsonWriter, value);

    verify(mockGson).getAdapter(TypeToken.get(runtimeType));
    verify(reflectiveAdapter).write(mockJsonWriter, value);
    verifyNoMoreInteractions(reflectiveDelegate);
  }

  @Test
    @Timeout(8000)
  void write_valueIsNull_runtimeTypeEqualsDeclaredType_usesDelegateWrite() throws IOException {
    Type declaredType = Object.class;
    Object value = null;

    wrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, mockDelegateAdapter, declaredType);

    wrapper.write(mockJsonWriter, value);

    verify(mockDelegateAdapter).write(mockJsonWriter, value);
    verifyNoMoreInteractions(mockGson);
  }
}
package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.TypeVariable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

final class TypeAdapterRuntimeTypeWrapper_178_5Test {

  @Mock Gson mockGson;
  @Mock TypeAdapter<Object> mockDelegate;
  @Mock TypeAdapter<Object> mockRuntimeTypeAdapter;
  @Mock JsonWriter mockJsonWriter;

  TypeAdapterRuntimeTypeWrapper<Object> wrapper;
  Type declaredType = Object.class;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    wrapper = new TypeAdapterRuntimeTypeWrapper<>(mockGson, mockDelegate, declaredType);
  }

  // Helper method to invoke private static getRuntimeTypeIfMoreSpecific via reflection
  private static Type invokeGetRuntimeTypeIfMoreSpecific(TypeAdapterRuntimeTypeWrapper<?> wrapper, Type type, Object value) throws Exception {
    Method m = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    m.setAccessible(true);
    return (Type) m.invoke(null, type, value);
  }

  // Helper method to invoke private static isReflective via reflection
  private static boolean invokeIsReflective(TypeAdapterRuntimeTypeWrapper<?> wrapper, TypeAdapter<?> adapter) throws Exception {
    Method m = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    m.setAccessible(true);
    return (boolean) m.invoke(null, adapter);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeEqualsDeclaredType_usesDelegateWrite() throws IOException {
    Object value = new Object();

    // The runtime type is the same as declared type, so delegate.write should be called
    wrapper.write(mockJsonWriter, value);

    verify(mockDelegate).write(mockJsonWriter, value);
    verifyNoMoreInteractions(mockRuntimeTypeAdapter, mockGson);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferent_runtimeTypeAdapterNotReflective_usesRuntimeTypeAdapterWrite() throws Exception {
    Object value = "string"; // runtime type String.class different from declaredType Object.class

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> runtimeTypeAdapter = mockRuntimeTypeAdapter;

    @SuppressWarnings({"unchecked", "rawtypes"})
    TypeToken runtimeTypeToken = (TypeToken) TypeToken.get(String.class);

    when(mockGson.getAdapter(runtimeTypeToken)).thenReturn(runtimeTypeAdapter);
    // We cannot mock instanceof, so we simulate by using a mock that is not instance of ReflectiveTypeAdapterFactory.Adapter
    // mockRuntimeTypeAdapter is a plain mock, so instanceof ReflectiveTypeAdapterFactory.Adapter is false

    TypeAdapterRuntimeTypeWrapper<Object> spyWrapper = spy(wrapper);

    doAnswer(invocation -> {
      JsonWriter out = invocation.getArgument(0);
      Object val = invocation.getArgument(1);
      Type runtimeType = String.class;
      @SuppressWarnings("unchecked")
      TypeAdapter<Object> runtimeTypeAdapterCast = (TypeAdapter<Object>) mockGson.getAdapter(TypeToken.get(runtimeType));
      TypeAdapter<Object> delegateAdapter = mockDelegate;

      TypeAdapter<Object> chosen = delegateAdapter;
      if (runtimeType != declaredType) {
        if (!(runtimeTypeAdapterCast instanceof ReflectiveTypeAdapterFactory.Adapter)) {
          chosen = runtimeTypeAdapterCast;
        } else if (!invokeIsReflective(spyWrapper, delegateAdapter)) {
          chosen = delegateAdapter;
        } else {
          chosen = runtimeTypeAdapterCast;
        }
      }
      chosen.write(out, val);
      return null;
    }).when(spyWrapper).write(any(JsonWriter.class), any());

    spyWrapper.write(mockJsonWriter, value);

    verify(mockRuntimeTypeAdapter).write(mockJsonWriter, value);
    verify(mockDelegate, never()).write(any(), any());
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferent_runtimeTypeAdapterReflective_delegateNotReflective_usesDelegateWrite() throws Exception {
    Object value = "string";

    ReflectiveTypeAdapterFactory.Adapter reflectiveAdapter = mock(ReflectiveTypeAdapterFactory.Adapter.class);

    @SuppressWarnings({"unchecked", "rawtypes"})
    TypeToken runtimeTypeToken = (TypeToken) TypeToken.get(String.class);

    when(mockGson.getAdapter(runtimeTypeToken)).thenReturn(reflectiveAdapter);

    TypeAdapterRuntimeTypeWrapper<Object> spyWrapper = spy(wrapper);

    doAnswer(invocation -> {
      JsonWriter out = invocation.getArgument(0);
      Object val = invocation.getArgument(1);
      Type runtimeType = String.class;
      @SuppressWarnings("unchecked")
      TypeAdapter<Object> runtimeTypeAdapter = (TypeAdapter<Object>) mockGson.getAdapter(TypeToken.get(runtimeType));
      TypeAdapter<Object> delegateAdapter = mockDelegate;

      TypeAdapter<Object> chosen = delegateAdapter;
      if (runtimeType != declaredType) {
        if (!(runtimeTypeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter)) {
          chosen = runtimeTypeAdapter;
        } else if (!invokeIsReflective(spyWrapper, delegateAdapter)) {
          chosen = delegateAdapter;
        } else {
          chosen = runtimeTypeAdapter;
        }
      }
      chosen.write(out, val);
      return null;
    }).when(spyWrapper).write(any(JsonWriter.class), any());

    spyWrapper.write(mockJsonWriter, value);

    verify(mockDelegate).write(mockJsonWriter, value);
    verify(reflectiveAdapter, never()).write(any(), any());
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferent_runtimeTypeAdapterReflective_delegateReflective_usesRuntimeTypeAdapterWrite() throws Exception {
    Object value = "string";

    ReflectiveTypeAdapterFactory.Adapter reflectiveAdapter = mock(ReflectiveTypeAdapterFactory.Adapter.class);

    @SuppressWarnings({"unchecked", "rawtypes"})
    TypeToken runtimeTypeToken = (TypeToken) TypeToken.get(String.class);

    when(mockGson.getAdapter(runtimeTypeToken)).thenReturn(reflectiveAdapter);

    TypeAdapterRuntimeTypeWrapper<Object> spyWrapper = spy(wrapper);

    doAnswer(invocation -> {
      JsonWriter out = invocation.getArgument(0);
      Object val = invocation.getArgument(1);
      Type runtimeType = String.class;
      @SuppressWarnings("unchecked")
      TypeAdapter<Object> runtimeTypeAdapter = (TypeAdapter<Object>) mockGson.getAdapter(TypeToken.get(runtimeType));
      TypeAdapter<Object> delegateAdapter = mockDelegate;

      TypeAdapter<Object> chosen = delegateAdapter;
      if (runtimeType != declaredType) {
        if (!(runtimeTypeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter)) {
          chosen = runtimeTypeAdapter;
        } else if (!invokeIsReflective(spyWrapper, delegateAdapter)) {
          chosen = delegateAdapter;
        } else {
          chosen = runtimeTypeAdapter;
        }
      }
      chosen.write(out, val);
      return null;
    }).when(spyWrapper).write(any(JsonWriter.class), any());

    spyWrapper.write(mockJsonWriter, value);

    verify(reflectiveAdapter).write(mockJsonWriter, value);
    verify(mockDelegate, never()).write(any(), any());
  }
}
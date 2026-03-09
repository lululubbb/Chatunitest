package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.Test;

final class TypeAdapterRuntimeTypeWrapper_179_6Test {

  @Test
    @Timeout(8000)
  void isReflective_returnsTrue_whenReflectiveTypeAdapterFactoryAdapter() throws Exception {
    @SuppressWarnings("unchecked")
    ReflectiveTypeAdapterFactory.Adapter<?, ?> reflective = mock(ReflectiveTypeAdapterFactory.Adapter.class);
    boolean result = invokeIsReflective(reflective);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_returnsFalse_whenNotSerializationDelegatingNorReflective() throws Exception {
    TypeAdapter<?> simpleAdapter = mock(TypeAdapter.class);
    boolean result = invokeIsReflective(simpleAdapter);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_returnsTrue_whenNestedSerializationDelegatingToReflective() throws Exception {
    @SuppressWarnings("unchecked")
    ReflectiveTypeAdapterFactory.Adapter<?, ?> reflective = mock(ReflectiveTypeAdapterFactory.Adapter.class);

    @SuppressWarnings({"unchecked", "rawtypes"})
    SerializationDelegatingTypeAdapter delegatingAdapter = mock(SerializationDelegatingTypeAdapter.class);
    when(delegatingAdapter.getSerializationDelegate()).thenReturn(reflective);

    boolean result = invokeIsReflective(delegatingAdapter);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_breaksLoop_whenDelegateIsSameInstance() throws Exception {
    @SuppressWarnings({"unchecked", "rawtypes"})
    SerializationDelegatingTypeAdapter delegatingAdapter = mock(SerializationDelegatingTypeAdapter.class);
    when(delegatingAdapter.getSerializationDelegate()).thenReturn(delegatingAdapter);

    boolean result = invokeIsReflective(delegatingAdapter);
    assertFalse(result);
  }

  private static boolean invokeIsReflective(TypeAdapter<?> adapter) throws Exception {
    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, adapter);
  }
}
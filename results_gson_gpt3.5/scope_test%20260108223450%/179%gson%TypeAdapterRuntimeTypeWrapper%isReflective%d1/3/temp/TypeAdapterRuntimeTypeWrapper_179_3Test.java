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

final class TypeAdapterRuntimeTypeWrapper_179_3Test {

  @Test
    @Timeout(8000)
  void testIsReflective_withNonSerializationDelegatingTypeAdapter() throws Exception {
    TypeAdapter<?> mockAdapter = mock(TypeAdapter.class);

    // Access private static method isReflective via reflection
    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    method.setAccessible(true);

    // Should return false because mockAdapter is neither SerializationDelegatingTypeAdapter nor ReflectiveTypeAdapterFactory.Adapter
    boolean result = (boolean) method.invoke(null, mockAdapter);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsReflective_withReflectiveTypeAdapterFactoryAdapter() throws Exception {
    TypeAdapter<?> reflectiveAdapter = mock(ReflectiveTypeAdapterFactory.Adapter.class);

    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    method.setAccessible(true);

    // Should return true because instance of ReflectiveTypeAdapterFactory.Adapter
    boolean result = (boolean) method.invoke(null, reflectiveAdapter);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsReflective_withSerializationDelegatingTypeAdapterDelegatesToReflective() throws Exception {
    // Create mocks
    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<?> delegatingAdapter = mock(SerializationDelegatingTypeAdapter.class);
    ReflectiveTypeAdapterFactory.Adapter reflectiveAdapter = mock(ReflectiveTypeAdapterFactory.Adapter.class);

    // Setup delegation chain: delegatingAdapter -> reflectiveAdapter
    when(delegatingAdapter.getSerializationDelegate()).thenReturn(reflectiveAdapter);

    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, delegatingAdapter);
    assertTrue(result);

    verify(delegatingAdapter, atLeastOnce()).getSerializationDelegate();
  }

  @Test
    @Timeout(8000)
  void testIsReflective_withSerializationDelegatingTypeAdapterDelegatesToItself() throws Exception {
    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<TypeAdapter<?>> delegatingAdapter = mock(SerializationDelegatingTypeAdapter.class);

    // Setup delegation chain: delegatingAdapter -> delegatingAdapter (break loop)
    when(delegatingAdapter.getSerializationDelegate()).thenReturn(delegatingAdapter);

    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, delegatingAdapter);
    // delegatingAdapter is not instance of ReflectiveTypeAdapterFactory.Adapter, so false
    assertFalse(result);

    verify(delegatingAdapter, atLeastOnce()).getSerializationDelegate();
  }
}
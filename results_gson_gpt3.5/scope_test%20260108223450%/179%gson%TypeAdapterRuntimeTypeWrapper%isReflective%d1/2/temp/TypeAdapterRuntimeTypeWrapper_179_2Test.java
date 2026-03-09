package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.TypeVariable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

class TypeAdapterRuntimeTypeWrapper_179_2Test {

  @Test
    @Timeout(8000)
  void isReflective_withNonDelegatingTypeAdapter_returnsFalse() throws Exception {
    TypeAdapter<?> mockAdapter = mock(TypeAdapter.class);
    boolean result = invokeIsReflective(mockAdapter);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_withReflectiveTypeAdapterFactoryAdapter_returnsTrue() throws Exception {
    TypeAdapter<?> reflectiveAdapter = mock(ReflectiveTypeAdapterFactory.Adapter.class);
    boolean result = invokeIsReflective(reflectiveAdapter);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_withNestedSerializationDelegatingTypeAdapter_delegatesUntilNonDelegating() throws Exception {
    // Mock the innermost delegate which is ReflectiveTypeAdapterFactory.Adapter
    TypeAdapter<?> reflectiveAdapter = mock(ReflectiveTypeAdapterFactory.Adapter.class);

    // Use raw types and cast to avoid generic mismatch issues
    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter rawMiddleDelegate = mock(SerializationDelegatingTypeAdapter.class);
    when(rawMiddleDelegate.getSerializationDelegate()).thenReturn(reflectiveAdapter);

    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter rawOuterDelegate = mock(SerializationDelegatingTypeAdapter.class);
    when(rawOuterDelegate.getSerializationDelegate()).thenReturn(rawMiddleDelegate);

    boolean result = invokeIsReflective(rawOuterDelegate);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_withSerializationDelegatingTypeAdapterThatDelegatesToItself_breaksLoopAndReturnsFalse() throws Exception {
    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter selfDelegatingAdapter = mock(SerializationDelegatingTypeAdapter.class);
    when(selfDelegatingAdapter.getSerializationDelegate()).thenReturn(selfDelegatingAdapter);

    boolean result = invokeIsReflective(selfDelegatingAdapter);
    assertFalse(result);
  }

  // Helper method to invoke private static isReflective method via reflection
  private boolean invokeIsReflective(TypeAdapter<?> adapter) throws Exception {
    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, adapter);
  }
}
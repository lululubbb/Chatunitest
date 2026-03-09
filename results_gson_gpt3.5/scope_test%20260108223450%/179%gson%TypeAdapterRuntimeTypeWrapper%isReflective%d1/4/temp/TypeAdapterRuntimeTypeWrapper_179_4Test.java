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

final class TypeAdapterRuntimeTypeWrapper_179_4Test {

  @Test
    @Timeout(8000)
  void isReflective_withNonSerializationDelegatingTypeAdapter_returnsFalse() throws Exception {
    TypeAdapter<?> adapter = mock(TypeAdapter.class);

    boolean result = invokeIsReflective(adapter);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_withReflectiveTypeAdapterFactoryAdapter_returnsTrue() throws Exception {
    TypeAdapter<?> reflectiveAdapter = mockReflectiveTypeAdapterFactoryAdapter();

    boolean result = invokeIsReflective(reflectiveAdapter);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_withSerializationDelegatingTypeAdapter_delegatesUntilNonDelegating() throws Exception {
    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<Object> delegatingAdapter1 = mock(SerializationDelegatingTypeAdapter.class);
    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<Object> delegatingAdapter2 = mock(SerializationDelegatingTypeAdapter.class);
    TypeAdapter<?> reflectiveAdapter = mockReflectiveTypeAdapterFactoryAdapter();

    // delegatingAdapter1 delegates to delegatingAdapter2
    when(delegatingAdapter1.getSerializationDelegate()).thenAnswer(invocation -> delegatingAdapter2);
    // delegatingAdapter2 delegates to reflectiveAdapter
    when(delegatingAdapter2.getSerializationDelegate()).thenAnswer(invocation -> reflectiveAdapter);

    boolean result = invokeIsReflective(delegatingAdapter1);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_withSerializationDelegatingTypeAdapter_delegateIsSelf_breaksLoop() throws Exception {
    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<Object> delegatingAdapter = mock(SerializationDelegatingTypeAdapter.class);

    when(delegatingAdapter.getSerializationDelegate()).thenAnswer(invocation -> delegatingAdapter);

    boolean result = invokeIsReflective(delegatingAdapter);

    // delegatingAdapter is not ReflectiveTypeAdapterFactory.Adapter, so false
    assertFalse(result);
  }

  private static boolean invokeIsReflective(TypeAdapter<?> adapter) throws Exception {
    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, adapter);
  }

  private static TypeAdapter<?> mockReflectiveTypeAdapterFactoryAdapter() {
    // Create a mock that is instance of ReflectiveTypeAdapterFactory.Adapter interface
    TypeAdapter<?> reflectiveAdapter = mock(TypeAdapter.class, withSettings().extraInterfaces(ReflectiveTypeAdapterFactory.Adapter.class));
    return reflectiveAdapter;
  }
}
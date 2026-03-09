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

final class TypeAdapterRuntimeTypeWrapper_179_5Test {

  // Dummy subclass to instantiate ReflectiveTypeAdapterFactory.Adapter
  static class DummyReflectiveAdapter extends ReflectiveTypeAdapterFactory.Adapter<Object> {
    DummyReflectiveAdapter() {
      super(null, null, null);
    }
  }

  @Test
    @Timeout(8000)
  void isReflective_withNonSerializationDelegatingTypeAdapter_returnsFalse() throws Exception {
    TypeAdapter<?> mockAdapter = mock(TypeAdapter.class);

    boolean result = invokeIsReflective(mockAdapter);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_withSerializationDelegatingTypeAdapterDelegatesToReflective_returnsTrue() throws Exception {
    // Create a dummy instance of ReflectiveTypeAdapterFactory.Adapter
    TypeAdapter<?> reflective = new DummyReflectiveAdapter();

    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<Object> delegatingAdapter = mock(SerializationDelegatingTypeAdapter.class);
    when(delegatingAdapter.getSerializationDelegate()).thenReturn(reflective);

    boolean result = invokeIsReflective(delegatingAdapter);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_withNestedSerializationDelegatingAdapters_returnsTrue() throws Exception {
    DummyReflectiveAdapter reflectiveAdapter = new DummyReflectiveAdapter();

    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<Object> delegatingAdapter2 = mock(SerializationDelegatingTypeAdapter.class);
    when(delegatingAdapter2.getSerializationDelegate()).thenReturn(reflectiveAdapter);

    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<Object> delegatingAdapter1 = mock(SerializationDelegatingTypeAdapter.class);
    when(delegatingAdapter1.getSerializationDelegate()).thenReturn(delegatingAdapter2);

    boolean result = invokeIsReflective(delegatingAdapter1);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_withSerializationDelegatingAdapterThatDelegatesToItself_returnsFalse() throws Exception {
    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<Object> delegatingAdapter = mock(SerializationDelegatingTypeAdapter.class);
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
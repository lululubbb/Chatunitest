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

class TypeAdapterRuntimeTypeWrapper_179_1Test {

  @Test
    @Timeout(8000)
  void isReflective_withNonDelegatingReflectiveAdapter_returnsTrue() throws Exception {
    // Arrange
    @SuppressWarnings("unchecked")
    ReflectiveTypeAdapterFactory.Adapter<Object, Object> reflectiveAdapter = mock(ReflectiveTypeAdapterFactory.Adapter.class);

    // Act
    boolean result = invokeIsReflective(reflectiveAdapter);

    // Assert
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_withNonDelegatingNonReflectiveAdapter_returnsFalse() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> nonReflectiveAdapter = mock(TypeAdapter.class);

    boolean result = invokeIsReflective(nonReflectiveAdapter);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_withSingleLevelDelegatingAdapter_returnsReflectiveDelegateResult() throws Exception {
    // Arrange
    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<Object> delegatingAdapter = mock(SerializationDelegatingTypeAdapter.class);
    @SuppressWarnings("unchecked")
    ReflectiveTypeAdapterFactory.Adapter<Object, Object> reflectiveDelegate = mock(ReflectiveTypeAdapterFactory.Adapter.class);

    when(delegatingAdapter.getSerializationDelegate()).thenReturn(reflectiveDelegate);

    // Act
    boolean result = invokeIsReflective(delegatingAdapter);

    // Assert
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_withSingleLevelDelegatingAdapterDelegatesToSelf_breaksLoopAndReturnsFalse() throws Exception {
    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<Object> delegatingAdapter = mock(SerializationDelegatingTypeAdapter.class);

    when(delegatingAdapter.getSerializationDelegate()).thenReturn(delegatingAdapter);

    boolean result = invokeIsReflective(delegatingAdapter);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_withMultiLevelDelegatingAdapters_returnsReflectiveDelegateResult() throws Exception {
    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<Object> firstDelegating = mock(SerializationDelegatingTypeAdapter.class);
    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<Object> secondDelegating = mock(SerializationDelegatingTypeAdapter.class);
    @SuppressWarnings("unchecked")
    ReflectiveTypeAdapterFactory.Adapter<Object, Object> reflectiveAdapter = mock(ReflectiveTypeAdapterFactory.Adapter.class);

    when(firstDelegating.getSerializationDelegate()).thenReturn(secondDelegating);
    when(secondDelegating.getSerializationDelegate()).thenReturn(reflectiveAdapter);

    boolean result = invokeIsReflective(firstDelegating);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isReflective_withMultiLevelDelegatingAdaptersDelegatingToSelf_breaksLoopAndReturnsFalse() throws Exception {
    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<Object> firstDelegating = mock(SerializationDelegatingTypeAdapter.class);
    @SuppressWarnings("unchecked")
    SerializationDelegatingTypeAdapter<Object> secondDelegating = mock(SerializationDelegatingTypeAdapter.class);

    when(firstDelegating.getSerializationDelegate()).thenReturn(secondDelegating);
    when(secondDelegating.getSerializationDelegate()).thenReturn(secondDelegating);

    boolean result = invokeIsReflective(firstDelegating);

    assertFalse(result);
  }

  private static boolean invokeIsReflective(TypeAdapter<?> adapter) throws Exception {
    var method = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, adapter);
  }
}
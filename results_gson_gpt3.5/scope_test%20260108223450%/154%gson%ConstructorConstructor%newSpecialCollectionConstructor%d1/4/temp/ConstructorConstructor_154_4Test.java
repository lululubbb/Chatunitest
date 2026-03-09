package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.InstanceCreator;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonIOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import org.junit.jupiter.api.Test;

class ConstructorConstructor_154_4Test {

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumSetWithValidParameterizedType_constructsEnumSet() throws Exception {
    // Arrange
    Type enumSetType = mock(ParameterizedType.class);
    Type[] actualTypeArguments = new Type[] {SampleEnum.class};
    when(((ParameterizedType) enumSetType).getActualTypeArguments()).thenReturn(actualTypeArguments);

    // Act
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(enumSetType, EnumSet.class);

    // Assert
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof EnumSet);
    @SuppressWarnings("unchecked")
    EnumSet<SampleEnum> enumSet = (EnumSet<SampleEnum>) instance;
    assertTrue(enumSet.isEmpty());
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumSetWithInvalidParameterizedType_throwsJsonIOException() throws Exception {
    // Arrange
    Type enumSetType = mock(ParameterizedType.class);
    Type[] actualTypeArguments = new Type[] {mock(Type.class)}; // not Class
    when(((ParameterizedType) enumSetType).getActualTypeArguments()).thenReturn(actualTypeArguments);

    // Act & Assert
    JsonIOException ex = assertThrows(JsonIOException.class, () -> {
      ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(enumSetType, EnumSet.class);
      constructor.construct();
    });
    assertTrue(ex.getMessage().contains("Invalid EnumSet type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumSetWithNonParameterizedType_throwsJsonIOException() throws Exception {
    // Arrange
    Type nonParameterizedType = mock(Type.class); // not ParameterizedType

    // Act
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(nonParameterizedType, EnumSet.class);

    // Assert
    assertNotNull(constructor);
    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("Invalid EnumSet type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumMapWithValidParameterizedType_constructsEnumMap() throws Exception {
    // Arrange
    Type enumMapType = mock(ParameterizedType.class);
    Type[] actualTypeArguments = new Type[] {SampleEnum.class, String.class};
    when(((ParameterizedType) enumMapType).getActualTypeArguments()).thenReturn(actualTypeArguments);

    // Act
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(enumMapType, EnumMap.class);

    // Assert
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof EnumMap);
    @SuppressWarnings("unchecked")
    EnumMap<SampleEnum, String> enumMap = (EnumMap<SampleEnum, String>) instance;
    assertTrue(enumMap.isEmpty());
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumMapWithInvalidParameterizedType_throwsJsonIOException() throws Exception {
    // Arrange
    Type enumMapType = mock(ParameterizedType.class);
    Type[] actualTypeArguments = new Type[] {mock(Type.class), String.class}; // first arg not Class
    when(((ParameterizedType) enumMapType).getActualTypeArguments()).thenReturn(actualTypeArguments);

    // Act
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(enumMapType, EnumMap.class);

    // Assert
    assertNotNull(constructor);
    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("Invalid EnumMap type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumMapWithNonParameterizedType_throwsJsonIOException() throws Exception {
    // Arrange
    Type nonParameterizedType = mock(Type.class); // not ParameterizedType

    // Act
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(nonParameterizedType, EnumMap.class);

    // Assert
    assertNotNull(constructor);
    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("Invalid EnumMap type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_nonSpecialCollectionType_returnsNull() throws Exception {
    // Arrange
    Type someType = mock(Type.class);

    // Act
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(someType, ArrayList.class);

    // Assert
    assertNull(constructor);
  }

  // Helper method to invoke private static newSpecialCollectionConstructor via reflection
  @SuppressWarnings("unchecked")
  private static <T> ObjectConstructor<T> invokeNewSpecialCollectionConstructor(Type type, Class<? super T> rawType)
      throws Exception {
    Class<?> constructorConstructorClass = Class.forName("com.google.gson.internal.ConstructorConstructor");
    Method method = constructorConstructorClass.getDeclaredMethod("newSpecialCollectionConstructor", Type.class, Class.class);
    method.setAccessible(true);
    Object result = method.invoke(null, type, rawType);

    if (result == null) {
      return null;
    }

    // Wrap the returned ObjectConstructor in a proxy that implements our local ObjectConstructor interface
    return new ObjectConstructor<T>() {
      @Override
      public T construct() {
        try {
          Method constructMethod = result.getClass().getMethod("construct");
          @SuppressWarnings("unchecked")
          T constructed = (T) constructMethod.invoke(result);
          return constructed;
        } catch (Exception e) {
          // Unwrap InvocationTargetException if present
          Throwable cause = e.getCause() != null ? e.getCause() : e;
          if (cause instanceof RuntimeException) {
            throw (RuntimeException) cause;
          }
          throw new RuntimeException(cause);
        }
      }
    };
  }

  // Sample enum for testing EnumSet and EnumMap
  private enum SampleEnum {
    A, B, C
  }

  // Interface to match ObjectConstructor in ConstructorConstructor
  private interface ObjectConstructor<T> {
    T construct();
  }
}
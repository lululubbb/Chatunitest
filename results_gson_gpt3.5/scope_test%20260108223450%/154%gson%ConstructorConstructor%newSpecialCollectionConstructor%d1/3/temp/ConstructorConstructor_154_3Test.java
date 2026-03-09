package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.InstanceCreator;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.internal.reflect.ReflectionHelper;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
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
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.EnumSet;
import org.junit.jupiter.api.Test;

class ConstructorConstructor_154_3Test {

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumSet_withParameterizedTypeAndClassElementType_constructsEnumSet() throws Exception {
    // Arrange
    ParameterizedType enumSetType = mock(ParameterizedType.class);
    Type elementType = TestEnum.class;
    when(enumSetType.getActualTypeArguments()).thenReturn(new Type[] {elementType});

    // Act
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(enumSetType, EnumSet.class);

    // Assert
    assertNotNull(constructor);
    Object result = constructor.construct();
    assertTrue(result instanceof EnumSet);
    assertTrue(((EnumSet<?>) result).isEmpty());
    assertEquals(TestEnum.class, ((EnumSet<?>) result).iterator().next().getClass());
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumSet_withNonClassElementType_throwsJsonIOException() throws Exception {
    // Arrange
    ParameterizedType enumSetType = mock(ParameterizedType.class);
    Type elementType = mock(Type.class);
    when(enumSetType.getActualTypeArguments()).thenReturn(new Type[] {elementType});

    // Act & Assert
    JsonIOException ex = assertThrows(JsonIOException.class,
        () -> invokeNewSpecialCollectionConstructor(enumSetType, EnumSet.class));
    assertTrue(ex.getMessage().contains("Invalid EnumSet type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumSet_withNonParameterizedType_throwsJsonIOException() throws Exception {
    // Arrange
    Type nonParameterizedType = String.class;

    // Act & Assert
    JsonIOException ex = assertThrows(JsonIOException.class,
        () -> invokeNewSpecialCollectionConstructor(nonParameterizedType, EnumSet.class));
    assertTrue(ex.getMessage().contains("Invalid EnumSet type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumMap_withParameterizedTypeAndClassElementType_constructsEnumMap() throws Exception {
    // Arrange
    ParameterizedType enumMapType = mock(ParameterizedType.class);
    Type elementType = TestEnum.class;
    when(enumMapType.getActualTypeArguments()).thenReturn(new Type[] {elementType});

    // Act
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(enumMapType, EnumMap.class);

    // Assert
    assertNotNull(constructor);
    Object result = constructor.construct();
    assertTrue(result instanceof EnumMap);
    assertTrue(((EnumMap<?, ?>) result).isEmpty());
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumMap_withNonClassElementType_throwsJsonIOException() throws Exception {
    // Arrange
    ParameterizedType enumMapType = mock(ParameterizedType.class);
    Type elementType = mock(Type.class);
    when(enumMapType.getActualTypeArguments()).thenReturn(new Type[] {elementType});

    // Act & Assert
    JsonIOException ex = assertThrows(JsonIOException.class,
        () -> invokeNewSpecialCollectionConstructor(enumMapType, EnumMap.class));
    assertTrue(ex.getMessage().contains("Invalid EnumMap type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumMap_withNonParameterizedType_throwsJsonIOException() throws Exception {
    // Arrange
    Type nonParameterizedType = String.class;

    // Act & Assert
    JsonIOException ex = assertThrows(JsonIOException.class,
        () -> invokeNewSpecialCollectionConstructor(nonParameterizedType, EnumMap.class));
    assertTrue(ex.getMessage().contains("Invalid EnumMap type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_otherRawType_returnsNull() throws Exception {
    // Arrange
    Type someType = String.class;

    // Act
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(someType, String.class);

    // Assert
    assertNull(constructor);
  }

  // Helper method to invoke private static newSpecialCollectionConstructor via reflection
  @SuppressWarnings("unchecked")
  private static <T> ObjectConstructor<T> invokeNewSpecialCollectionConstructor(Type type, Class<? super T> rawType) throws Exception {
    var method = ConstructorConstructor.class.getDeclaredMethod("newSpecialCollectionConstructor", Type.class, Class.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(null, type, rawType);
  }

  private enum TestEnum {
    A, B;
  }
}
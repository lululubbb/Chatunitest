package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.InstanceCreator;
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
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.EnumSet;
import org.junit.jupiter.api.Test;

class ConstructorConstructor_154_2Test {

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumSetWithValidParameterizedType_constructsEnumSet() throws Exception {
    // Arrange
    Type enumSetType = mock(ParameterizedType.class);
    Type enumType = TestEnum.class;
    when(((ParameterizedType) enumSetType).getActualTypeArguments()).thenReturn(new Type[] {enumType});

    Class<?> rawType = EnumSet.class;

    // Act
    ObjectConstructor<EnumSet<TestEnum>> constructor = invokeNewSpecialCollectionConstructor(enumSetType, (Class<? super EnumSet<TestEnum>>) rawType);
    EnumSet<TestEnum> result = constructor.construct();

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
    assertEquals(EnumSet.noneOf(TestEnum.class), result);
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumSetWithNonClassType_throwsJsonIOException() {
    // Arrange
    Type enumSetType = mock(ParameterizedType.class);
    Type nonClassType = mock(Type.class);
    when(((ParameterizedType) enumSetType).getActualTypeArguments()).thenReturn(new Type[] {nonClassType});

    Class<?> rawType = EnumSet.class;

    // Act & Assert
    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(enumSetType, rawType);
      constructor.construct();
    });
    assertTrue(thrown.getMessage().contains("Invalid EnumSet type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumSetWithNonParameterizedType_throwsJsonIOException() {
    // Arrange
    Type nonParameterizedType = mock(Type.class);
    Class<?> rawType = EnumSet.class;

    // Act & Assert
    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(nonParameterizedType, rawType);
      constructor.construct();
    });
    assertTrue(thrown.getMessage().contains("Invalid EnumSet type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumMapWithValidParameterizedType_constructsEnumMap() throws Exception {
    // Arrange
    Type enumMapType = mock(ParameterizedType.class);
    Type enumType = TestEnum.class;
    when(((ParameterizedType) enumMapType).getActualTypeArguments()).thenReturn(new Type[] {enumType});

    Class<?> rawType = EnumMap.class;

    // Act
    ObjectConstructor<EnumMap<TestEnum, ?>> constructor = invokeNewSpecialCollectionConstructor(enumMapType, (Class<? super EnumMap<TestEnum, ?>>) rawType);
    EnumMap<TestEnum, ?> result = constructor.construct();

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
    assertEquals(new EnumMap<>(TestEnum.class), result);
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumMapWithNonClassType_throwsJsonIOException() {
    // Arrange
    Type enumMapType = mock(ParameterizedType.class);
    Type nonClassType = mock(Type.class);
    when(((ParameterizedType) enumMapType).getActualTypeArguments()).thenReturn(new Type[] {nonClassType});

    Class<?> rawType = EnumMap.class;

    // Act & Assert
    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(enumMapType, rawType);
      constructor.construct();
    });
    assertTrue(thrown.getMessage().contains("Invalid EnumMap type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_enumMapWithNonParameterizedType_throwsJsonIOException() {
    // Arrange
    Type nonParameterizedType = mock(Type.class);
    Class<?> rawType = EnumMap.class;

    // Act & Assert
    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(nonParameterizedType, rawType);
      constructor.construct();
    });
    assertTrue(thrown.getMessage().contains("Invalid EnumMap type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_nonSpecialCollectionType_returnsNull() throws Exception {
    // Arrange
    Type anyType = String.class;
    Class<?> rawType = String.class;

    // Act
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(anyType, rawType);

    // Assert
    assertNull(constructor);
  }

  @SuppressWarnings("unchecked")
  private <T> ObjectConstructor<T> invokeNewSpecialCollectionConstructor(Type type, Class<? super T> rawType)
      throws Exception {
    var method = ConstructorConstructor.class.getDeclaredMethod("newSpecialCollectionConstructor", Type.class, Class.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(null, type, rawType);
  }

  private enum TestEnum {
    A, B
  }
}
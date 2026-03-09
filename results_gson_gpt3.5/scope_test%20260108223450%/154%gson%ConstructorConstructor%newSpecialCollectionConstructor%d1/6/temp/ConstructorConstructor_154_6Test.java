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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.EnumSet;
import org.junit.jupiter.api.Test;

class ConstructorConstructor_154_6Test {

  @Test
    @Timeout(8000)
  void testNewSpecialCollectionConstructor_enumSetWithValidParameterizedType() throws Exception {
    // Arrange
    ParameterizedType enumSetType = mock(ParameterizedType.class);
    Class<TestEnum> enumClass = TestEnum.class;
    when(enumSetType.getActualTypeArguments()).thenReturn(new Type[] {enumClass});
    when(enumSetType.getRawType()).thenReturn(EnumSet.class);

    // Act
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(enumSetType, EnumSet.class);

    // Assert
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof EnumSet);
    assertEquals(0, ((EnumSet<?>) instance).size());
  }

  @Test
    @Timeout(8000)
  void testNewSpecialCollectionConstructor_enumSetWithInvalidParameterizedType() throws Exception {
    // Arrange
    ParameterizedType invalidType = mock(ParameterizedType.class);
    Type invalidTypeArg = mock(Type.class);
    when(invalidType.getActualTypeArguments()).thenReturn(new Type[] {invalidTypeArg});
    when(invalidType.getRawType()).thenReturn(EnumSet.class);

    // Act & Assert
    JsonIOException exception = assertThrows(JsonIOException.class,
        () -> invokeNewSpecialCollectionConstructor(invalidType, EnumSet.class).construct());
    assertTrue(exception.getMessage().contains("Invalid EnumSet type"));
  }

  @Test
    @Timeout(8000)
  void testNewSpecialCollectionConstructor_enumSetWithNonParameterizedType() throws Exception {
    // Arrange
    Type nonParameterizedType = String.class;

    // Act & Assert
    JsonIOException exception = assertThrows(JsonIOException.class,
        () -> invokeNewSpecialCollectionConstructor(nonParameterizedType, EnumSet.class).construct());
    assertTrue(exception.getMessage().contains("Invalid EnumSet type"));
  }

  @Test
    @Timeout(8000)
  void testNewSpecialCollectionConstructor_enumMapWithValidParameterizedType() throws Exception {
    // Arrange
    ParameterizedType enumMapType = mock(ParameterizedType.class);
    Class<TestEnum> enumClass = TestEnum.class;
    when(enumMapType.getActualTypeArguments()).thenReturn(new Type[] {enumClass});
    when(enumMapType.getRawType()).thenReturn(EnumMap.class);

    // Act
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(enumMapType, EnumMap.class);

    // Assert
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof EnumMap);
    assertEquals(0, ((EnumMap<?, ?>) instance).size());
  }

  @Test
    @Timeout(8000)
  void testNewSpecialCollectionConstructor_enumMapWithInvalidParameterizedType() throws Exception {
    // Arrange
    ParameterizedType invalidType = mock(ParameterizedType.class);
    Type invalidTypeArg = mock(Type.class);
    when(invalidType.getActualTypeArguments()).thenReturn(new Type[] {invalidTypeArg});
    when(invalidType.getRawType()).thenReturn(EnumMap.class);

    // Act & Assert
    JsonIOException exception = assertThrows(JsonIOException.class,
        () -> invokeNewSpecialCollectionConstructor(invalidType, EnumMap.class).construct());
    assertTrue(exception.getMessage().contains("Invalid EnumMap type"));
  }

  @Test
    @Timeout(8000)
  void testNewSpecialCollectionConstructor_enumMapWithNonParameterizedType() throws Exception {
    // Arrange
    Type nonParameterizedType = String.class;

    // Act & Assert
    JsonIOException exception = assertThrows(JsonIOException.class,
        () -> invokeNewSpecialCollectionConstructor(nonParameterizedType, EnumMap.class).construct());
    assertTrue(exception.getMessage().contains("Invalid EnumMap type"));
  }

  @Test
    @Timeout(8000)
  void testNewSpecialCollectionConstructor_otherRawTypeReturnsNull() throws Exception {
    // Act
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(String.class, String.class);

    // Assert
    assertNull(constructor);
  }

  // Helper method to invoke the private static method via reflection
  @SuppressWarnings("unchecked")
  private static <T> ObjectConstructor<T> invokeNewSpecialCollectionConstructor(Type type, Class<? super T> rawType) throws Exception {
    java.lang.reflect.Method method = ConstructorConstructor.class.getDeclaredMethod(
        "newSpecialCollectionConstructor", Type.class, Class.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(null, type, rawType);
  }

  private enum TestEnum {
    VALUE1, VALUE2
  }
}
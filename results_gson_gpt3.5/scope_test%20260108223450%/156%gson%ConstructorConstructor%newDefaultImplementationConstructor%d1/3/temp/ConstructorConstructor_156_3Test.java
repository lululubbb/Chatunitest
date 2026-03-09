package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
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
import java.util.EnumMap;
import java.util.EnumSet;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

class ConstructorConstructor_newDefaultImplementationConstructorTest {

  @Test
    @Timeout(8000)
  void testCollectionSortedSet() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Object.class, SortedSet.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof TreeSet);
  }

  @Test
    @Timeout(8000)
  void testCollectionSet() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Object.class, Set.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof LinkedHashSet);
  }

  @Test
    @Timeout(8000)
  void testCollectionQueue() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Object.class, Queue.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ArrayDeque);
  }

  @Test
    @Timeout(8000)
  void testCollectionOther() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Object.class, Collection.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ArrayList);
  }

  @Test
    @Timeout(8000)
  void testMapConcurrentNavigableMap() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Object.class, ConcurrentNavigableMap.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ConcurrentSkipListMap);
  }

  @Test
    @Timeout(8000)
  void testMapConcurrentMap() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Object.class, ConcurrentMap.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ConcurrentHashMap);
  }

  @Test
    @Timeout(8000)
  void testMapSortedMap() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Object.class, SortedMap.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof TreeMap);
  }

  @Test
    @Timeout(8000)
  void testMapParameterizedTypeNonStringKey() throws Exception {
    ParameterizedType paramType = mock(ParameterizedType.class);
    Type[] typeArgs = new Type[] {Integer.class};
    when(paramType.getActualTypeArguments()).thenReturn(typeArgs);

    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        paramType, Map.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof LinkedHashMap);
  }

  @Test
    @Timeout(8000)
  void testMapParameterizedTypeStringKey() throws Exception {
    ParameterizedType paramType = mock(ParameterizedType.class);
    Type[] typeArgs = new Type[] {String.class};
    when(paramType.getActualTypeArguments()).thenReturn(typeArgs);

    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        paramType, Map.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof LinkedTreeMap);
  }

  @Test
    @Timeout(8000)
  void testMapRawType() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Object.class, Map.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof LinkedTreeMap);
  }

  @Test
    @Timeout(8000)
  void testNonCollectionNonMap() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Object.class, Object.class);
    assertNull(constructor);
  }

  @SuppressWarnings("unchecked")
  private <T> ObjectConstructor<T> invokeNewDefaultImplementationConstructor(Type type, Class<? super T> rawType) throws Exception {
    java.lang.reflect.Method method = ConstructorConstructor.class.getDeclaredMethod(
        "newDefaultImplementationConstructor", Type.class, Class.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(null, type, rawType);
  }

  // Import ObjectConstructor to avoid compilation error
  private interface ObjectConstructor<T> {
    T construct();
  }
}
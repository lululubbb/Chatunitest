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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.Test;

class ConstructorConstructor_156_4Test {

  @SuppressWarnings("unchecked")
  private <T> ObjectConstructor<T> invokeNewDefaultImplementationConstructor(Type type, Class<T> rawType)
      throws Exception {
    var method = ConstructorConstructor.class.getDeclaredMethod("newDefaultImplementationConstructor", Type.class, Class.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(null, type, rawType);
  }

  @Test
    @Timeout(8000)
  void testCollectionSortedSet() throws Exception {
    ObjectConstructor<SortedSet> constructor =
        invokeNewDefaultImplementationConstructor(SortedSet.class, SortedSet.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof TreeSet);
  }

  @Test
    @Timeout(8000)
  void testCollectionSet() throws Exception {
    ObjectConstructor<Set> constructor =
        invokeNewDefaultImplementationConstructor(Set.class, Set.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof LinkedHashSet);
  }

  @Test
    @Timeout(8000)
  void testCollectionQueue() throws Exception {
    ObjectConstructor<Queue> constructor =
        invokeNewDefaultImplementationConstructor(Queue.class, Queue.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ArrayDeque);
  }

  @Test
    @Timeout(8000)
  void testCollectionOther() throws Exception {
    ObjectConstructor<Collection> constructor =
        invokeNewDefaultImplementationConstructor(Collection.class, Collection.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ArrayList);
  }

  @Test
    @Timeout(8000)
  void testMapConcurrentNavigableMap() throws Exception {
    ObjectConstructor<ConcurrentNavigableMap> constructor =
        invokeNewDefaultImplementationConstructor(ConcurrentNavigableMap.class, ConcurrentNavigableMap.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ConcurrentSkipListMap);
  }

  @Test
    @Timeout(8000)
  void testMapConcurrentMap() throws Exception {
    ObjectConstructor<ConcurrentMap> constructor =
        invokeNewDefaultImplementationConstructor(ConcurrentMap.class, ConcurrentMap.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ConcurrentHashMap);
  }

  @Test
    @Timeout(8000)
  void testMapSortedMap() throws Exception {
    ObjectConstructor<SortedMap> constructor =
        invokeNewDefaultImplementationConstructor(SortedMap.class, SortedMap.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof TreeMap);
  }

  @Test
    @Timeout(8000)
  void testMapParameterizedTypeNonStringKey() throws Exception {
    // Create a ParameterizedType with a non-String key type to trigger LinkedHashMap branch
    ParameterizedType paramType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[]{Integer.class, String.class};
      }

      @Override
      public Type getRawType() {
        return Map.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    ObjectConstructor<Map> constructor =
        invokeNewDefaultImplementationConstructor(paramType, Map.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof LinkedHashMap);
  }

  @Test
    @Timeout(8000)
  void testMapParameterizedTypeStringKey() throws Exception {
    // Create a ParameterizedType with String key type to trigger LinkedTreeMap branch
    ParameterizedType paramType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[]{String.class, Integer.class};
      }

      @Override
      public Type getRawType() {
        return Map.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    ObjectConstructor<Map> constructor =
        invokeNewDefaultImplementationConstructor(paramType, Map.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    // LinkedTreeMap is a Gson internal class, just check non-null and class name
    assertEquals("com.google.gson.internal.LinkedTreeMap", instance.getClass().getName());
  }

  @Test
    @Timeout(8000)
  void testNonCollectionNonMapReturnsNull() throws Exception {
    ObjectConstructor<String> constructor =
        invokeNewDefaultImplementationConstructor(String.class, String.class);
    assertNull(constructor);
  }
}
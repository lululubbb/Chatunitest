package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.internal.reflect.ReflectionHelper;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

class ConstructorConstructor_156_6Test {

  @SuppressWarnings("unchecked")
  private <T> ObjectConstructor<T> invokeNewDefaultImplementationConstructor(Type type, Class<? super T> rawType) throws Exception {
    Method method = ConstructorConstructor.class.getDeclaredMethod("newDefaultImplementationConstructor", Type.class, Class.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(null, type, rawType);
  }

  interface ObjectConstructor<T> {
    T construct();
  }

  @Test
    @Timeout(8000)
  void testCollectionSortedSet() throws Exception {
    ObjectConstructor<SortedSet> constructor = invokeNewDefaultImplementationConstructor(SortedSet.class, SortedSet.class);
    assertNotNull(constructor);
    SortedSet<?> instance = constructor.construct();
    assertTrue(instance instanceof TreeSet);
  }

  @Test
    @Timeout(8000)
  void testCollectionSet() throws Exception {
    ObjectConstructor<Set> constructor = invokeNewDefaultImplementationConstructor(Set.class, Set.class);
    assertNotNull(constructor);
    Set<?> instance = constructor.construct();
    assertTrue(instance instanceof LinkedHashSet);
  }

  @Test
    @Timeout(8000)
  void testCollectionQueue() throws Exception {
    ObjectConstructor<Queue> constructor = invokeNewDefaultImplementationConstructor(Queue.class, Queue.class);
    assertNotNull(constructor);
    Queue<?> instance = constructor.construct();
    assertTrue(instance instanceof ArrayDeque);
  }

  @Test
    @Timeout(8000)
  void testCollectionOther() throws Exception {
    ObjectConstructor<Collection> constructor = invokeNewDefaultImplementationConstructor(Collection.class, Collection.class);
    assertNotNull(constructor);
    Collection<?> instance = constructor.construct();
    assertTrue(instance instanceof ArrayList);
  }

  @Test
    @Timeout(8000)
  void testMapConcurrentNavigableMap() throws Exception {
    ObjectConstructor<ConcurrentNavigableMap> constructor = invokeNewDefaultImplementationConstructor(ConcurrentNavigableMap.class, ConcurrentNavigableMap.class);
    assertNotNull(constructor);
    ConcurrentNavigableMap<?, ?> instance = constructor.construct();
    assertTrue(instance instanceof ConcurrentSkipListMap);
  }

  @Test
    @Timeout(8000)
  void testMapConcurrentMap() throws Exception {
    ObjectConstructor<ConcurrentMap> constructor = invokeNewDefaultImplementationConstructor(ConcurrentMap.class, ConcurrentMap.class);
    assertNotNull(constructor);
    ConcurrentMap<?, ?> instance = constructor.construct();
    assertTrue(instance instanceof ConcurrentHashMap);
  }

  @Test
    @Timeout(8000)
  void testMapSortedMap() throws Exception {
    ObjectConstructor<SortedMap> constructor = invokeNewDefaultImplementationConstructor(SortedMap.class, SortedMap.class);
    assertNotNull(constructor);
    SortedMap<?, ?> instance = constructor.construct();
    assertTrue(instance instanceof TreeMap);
  }

  @Test
    @Timeout(8000)
  void testMapParameterizedTypeKeyNotString() throws Exception {
    // Create a ParameterizedType with key type not assignable from String
    ParameterizedType paramType = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() {
        return new Type[] {Integer.class, String.class};
      }
      @Override public Type getRawType() {
        return Map.class;
      }
      @Override public Type getOwnerType() {
        return null;
      }
    };

    ObjectConstructor<Map> constructor = invokeNewDefaultImplementationConstructor(paramType, Map.class);
    assertNotNull(constructor);
    Map<?, ?> instance = constructor.construct();
    assertTrue(instance instanceof LinkedHashMap);
  }

  @Test
    @Timeout(8000)
  void testMapDefault() throws Exception {
    // type not ParameterizedType
    ObjectConstructor<Map> constructor = invokeNewDefaultImplementationConstructor(Map.class, Map.class);
    assertNotNull(constructor);
    Map<?, ?> instance = constructor.construct();
    assertTrue(instance instanceof com.google.gson.internal.LinkedTreeMap);
  }

  @Test
    @Timeout(8000)
  void testNonCollectionNonMapReturnsNull() throws Exception {
    ObjectConstructor<String> constructor = invokeNewDefaultImplementationConstructor(String.class, String.class);
    assertNull(constructor);
  }
}
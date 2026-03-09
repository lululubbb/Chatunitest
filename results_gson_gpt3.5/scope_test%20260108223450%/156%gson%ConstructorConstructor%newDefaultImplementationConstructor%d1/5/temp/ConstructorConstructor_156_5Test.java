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

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.Test;

class ConstructorConstructor_156_5Test {

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withSortedSet_returnsTreeSet() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        null, SortedSet.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof TreeSet);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withSet_returnsLinkedHashSet() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        null, Set.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof LinkedHashSet);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withQueue_returnsArrayDeque() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        null, Queue.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ArrayDeque);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withCollection_returnsArrayList() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        null, Collection.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ArrayList);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withConcurrentNavigableMap_returnsConcurrentSkipListMap() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        null, ConcurrentNavigableMap.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ConcurrentSkipListMap);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withConcurrentMap_returnsConcurrentHashMap() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        null, ConcurrentMap.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ConcurrentHashMap);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withSortedMap_returnsTreeMap() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        null, SortedMap.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof TreeMap);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withMapParameterizedTypeKeyNotString_returnsLinkedHashMap() throws Exception {
    ParameterizedType parameterizedType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { Integer.class };
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

    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(parameterizedType, Map.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof LinkedHashMap);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withMapKeyString_returnsLinkedTreeMap() throws Exception {
    ParameterizedType parameterizedType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { String.class };
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

    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(parameterizedType, Map.class);
    Object instance = constructor.construct();
    assertTrue(instance instanceof LinkedTreeMap);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withNonCollectionNonMap_returnsNull() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(null, String.class);
    assertNull(constructor);
  }

  @SuppressWarnings("unchecked")
  private <T> ObjectConstructor<T> invokeNewDefaultImplementationConstructor(Type type, Class<? super T> rawType) throws Exception {
    var method = ConstructorConstructor.class.getDeclaredMethod("newDefaultImplementationConstructor", Type.class, Class.class);
    method.setAccessible(true);
    Object result = method.invoke(null, type, rawType);
    if (result == null) {
      return null;
    }
    return new ObjectConstructor<T>() {
      @Override
      public T construct() {
        try {
          return (T) result.getClass().getMethod("construct").invoke(result);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  interface ObjectConstructor<T> {
    T construct();
  }
}
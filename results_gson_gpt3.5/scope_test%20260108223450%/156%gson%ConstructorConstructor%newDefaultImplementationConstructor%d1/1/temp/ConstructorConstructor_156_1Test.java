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
import static org.mockito.Mockito.*;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.Test;

class ConstructorConstructor_156_1Test {

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withSortedSet_returnsTreeSet() throws Exception {
    Type type = Set.class;
    Class<?> rawType = SortedSet.class;

    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(type, rawType);

    Object instance = constructor.construct();
    assertTrue(instance instanceof TreeSet);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withSet_returnsLinkedHashSet() throws Exception {
    Type type = Set.class;
    Class<?> rawType = LinkedHashSet.class;

    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(type, rawType);

    Object instance = constructor.construct();
    assertTrue(instance instanceof LinkedHashSet);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withQueue_returnsArrayDeque() throws Exception {
    Type type = Collection.class;
    Class<?> rawType = Queue.class;

    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(type, rawType);

    Object instance = constructor.construct();
    assertTrue(instance instanceof ArrayDeque);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withCollection_returnsArrayList() throws Exception {
    Type type = Collection.class;
    Class<?> rawType = Collection.class;

    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(type, rawType);

    Object instance = constructor.construct();
    assertTrue(instance instanceof ArrayList);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withConcurrentNavigableMap_returnsConcurrentSkipListMap() throws Exception {
    Type type = Map.class;
    Class<?> rawType = ConcurrentNavigableMap.class;

    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(type, rawType);

    Object instance = constructor.construct();
    assertTrue(instance instanceof ConcurrentSkipListMap);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withConcurrentMap_returnsConcurrentHashMap() throws Exception {
    Type type = Map.class;
    Class<?> rawType = ConcurrentMap.class;

    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(type, rawType);

    Object instance = constructor.construct();
    assertTrue(instance instanceof ConcurrentHashMap);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withSortedMap_returnsTreeMap() throws Exception {
    Type type = Map.class;
    Class<?> rawType = SortedMap.class;

    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(type, rawType);

    Object instance = constructor.construct();
    assertTrue(instance instanceof TreeMap);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withParameterizedTypeNonStringKey_returnsLinkedHashMap() throws Exception {
    // Create a ParameterizedType with non-String key type
    ParameterizedType parameterizedType = mock(ParameterizedType.class);
    Type nonStringKeyType = Integer.class;
    Type[] actualTypeArguments = new Type[] {nonStringKeyType};
    when(parameterizedType.getActualTypeArguments()).thenReturn(actualTypeArguments);

    Class<?> rawType = Map.class;

    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(parameterizedType, rawType);

    Object instance = constructor.construct();
    assertTrue(instance instanceof LinkedHashMap);
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withMap_returnsLinkedTreeMap() throws Exception {
    Type type = Map.class;
    Class<?> rawType = Map.class;

    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(type, rawType);

    Object instance = constructor.construct();
    assertEquals("com.google.gson.internal.LinkedTreeMap", instance.getClass().getName());
  }

  @Test
    @Timeout(8000)
  void newDefaultImplementationConstructor_withNonCollectionOrMap_returnsNull() throws Exception {
    Type type = String.class;
    Class<?> rawType = String.class;

    ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(type, rawType);

    assertNull(constructor);
  }

  @SuppressWarnings("unchecked")
  private static <T> ObjectConstructor<T> invokeNewDefaultImplementationConstructor(Type type, Class<? super T> rawType) throws Exception {
    var method = ConstructorConstructor.class.getDeclaredMethod("newDefaultImplementationConstructor", Type.class, Class.class);
    method.setAccessible(true);
    Object result = method.invoke(null, type, rawType);
    if (result == null) {
      return null;
    }
    // Wrap the returned ObjectConstructor from ConstructorConstructor into our test's ObjectConstructor interface
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

  // Define ObjectConstructor interface here to fix import error
  private interface ObjectConstructor<T> {
    T construct();
  }
}
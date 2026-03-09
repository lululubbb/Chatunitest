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

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import org.junit.jupiter.api.Test;

class ConstructorConstructor_156_2Test {

  @Test
    @Timeout(8000)
  void testNewDefaultImplementationConstructor_withSortedSet() throws Exception {
    ConstructorConstructor.ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Set.class, TreeSet.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof TreeSet);
  }

  @Test
    @Timeout(8000)
  void testNewDefaultImplementationConstructor_withSet() throws Exception {
    ConstructorConstructor.ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Collection.class, LinkedHashSet.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof LinkedHashSet);
  }

  @Test
    @Timeout(8000)
  void testNewDefaultImplementationConstructor_withQueue() throws Exception {
    ConstructorConstructor.ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Collection.class, ArrayDeque.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ArrayDeque);
  }

  @Test
    @Timeout(8000)
  void testNewDefaultImplementationConstructor_withCollection_other() throws Exception {
    ConstructorConstructor.ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Collection.class, ArrayList.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ArrayList);
  }

  @Test
    @Timeout(8000)
  void testNewDefaultImplementationConstructor_withConcurrentNavigableMap() throws Exception {
    ConstructorConstructor.ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Map.class, ConcurrentSkipListMap.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ConcurrentSkipListMap);
  }

  @Test
    @Timeout(8000)
  void testNewDefaultImplementationConstructor_withConcurrentMap() throws Exception {
    ConstructorConstructor.ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Map.class, ConcurrentHashMap.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ConcurrentHashMap);
  }

  @Test
    @Timeout(8000)
  void testNewDefaultImplementationConstructor_withSortedMap() throws Exception {
    ConstructorConstructor.ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        Map.class, TreeMap.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof TreeMap);
  }

  @Test
    @Timeout(8000)
  void testNewDefaultImplementationConstructor_withParameterizedType_nonStringKey() throws Exception {
    ParameterizedType parameterizedType = mock(ParameterizedType.class);
    Type[] actualTypeArguments = new Type[] {Integer.class};
    when(parameterizedType.getActualTypeArguments()).thenReturn(actualTypeArguments);

    ConstructorConstructor.ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        parameterizedType, LinkedHashMap.class);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof LinkedHashMap);
  }

  @Test
    @Timeout(8000)
  void testNewDefaultImplementationConstructor_withMap_other() throws Exception {
    Class<?> linkedTreeMapClass = Class.forName("com.google.gson.internal.LinkedTreeMap");
    ConstructorConstructor.ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        String.class, linkedTreeMapClass);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertEquals("com.google.gson.internal.LinkedTreeMap", instance.getClass().getName());
  }

  @Test
    @Timeout(8000)
  void testNewDefaultImplementationConstructor_withNonCollectionNonMap() throws Exception {
    ConstructorConstructor.ObjectConstructor<?> constructor = invokeNewDefaultImplementationConstructor(
        String.class, String.class);
    assertNull(constructor);
  }

  @SuppressWarnings("unchecked")
  private <T> ConstructorConstructor.ObjectConstructor<T> invokeNewDefaultImplementationConstructor(Type type, Class<? super T> rawType) throws Exception {
    java.lang.reflect.Method method = ConstructorConstructor.class.getDeclaredMethod(
        "newDefaultImplementationConstructor", Type.class, Class.class);
    method.setAccessible(true);
    return (ConstructorConstructor.ObjectConstructor<T>) method.invoke(null, type, rawType);
  }
}
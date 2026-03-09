package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.reflect.ReflectionHelper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
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

import com.google.gson.InstanceCreator;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ConstructorConstructor_151_2Test {

  private Map<Type, InstanceCreator<?>> instanceCreators;
  private List<ReflectionAccessFilter> reflectionFilters;

  @BeforeEach
  void setUp() {
    instanceCreators = new HashMap<>();
    reflectionFilters = new ArrayList<>();
  }

  @Test
    @Timeout(8000)
  void constructorStoresFields() throws Exception {
    boolean useJdkUnsafe = true;
    ConstructorConstructor cc =
        new ConstructorConstructor(instanceCreators, useJdkUnsafe, reflectionFilters);
    // Use reflection to check private fields
    var instanceCreatorsField =
        ConstructorConstructor.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    var useJdkUnsafeField = ConstructorConstructor.class.getDeclaredField("useJdkUnsafe");
    useJdkUnsafeField.setAccessible(true);
    var reflectionFiltersField = ConstructorConstructor.class.getDeclaredField("reflectionFilters");
    reflectionFiltersField.setAccessible(true);

    assertSame(instanceCreators, instanceCreatorsField.get(cc));
    assertEquals(useJdkUnsafe, useJdkUnsafeField.getBoolean(cc));
    assertSame(reflectionFilters, reflectionFiltersField.get(cc));
  }

  @Test
    @Timeout(8000)
  void constructorWithEmptyParams() throws Exception {
    ConstructorConstructor cc = new ConstructorConstructor(Collections.emptyMap(), false, Collections.emptyList());

    var instanceCreatorsField =
        ConstructorConstructor.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    var useJdkUnsafeField = ConstructorConstructor.class.getDeclaredField("useJdkUnsafe");
    useJdkUnsafeField.setAccessible(true);
    var reflectionFiltersField = ConstructorConstructor.class.getDeclaredField("reflectionFilters");
    reflectionFiltersField.setAccessible(true);

    assertTrue(((Map<?, ?>) instanceCreatorsField.get(cc)).isEmpty());
    assertFalse(useJdkUnsafeField.getBoolean(cc));
    assertTrue(((List<?>) reflectionFiltersField.get(cc)).isEmpty());
  }
}
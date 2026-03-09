package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ConstructorConstructor_158_4Test {

  @Test
    @Timeout(8000)
  public void testToString_withEmptyInstanceCreators() {
    Map<Type, InstanceCreator<?>> instanceCreators = Collections.emptyMap();
    boolean useJdkUnsafe = false;
    List<ReflectionAccessFilter> reflectionFilters = Collections.emptyList();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators, useJdkUnsafe, reflectionFilters);

    String expected = instanceCreators.toString();
    String actual = constructorConstructor.toString();

    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  public void testToString_withNonEmptyInstanceCreators() {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    Type typeMock = String.class;
    InstanceCreator<?> instanceCreatorMock = Mockito.mock(InstanceCreator.class);
    instanceCreators.put(typeMock, instanceCreatorMock);

    boolean useJdkUnsafe = true;
    List<ReflectionAccessFilter> reflectionFilters = Collections.emptyList();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators, useJdkUnsafe, reflectionFilters);

    String expected = instanceCreators.toString();
    String actual = constructorConstructor.toString();

    assertEquals(expected, actual);
  }
}
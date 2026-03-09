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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ConstructorConstructor_158_2Test {

  @Test
    @Timeout(8000)
  public void testToString_emptyInstanceCreators() {
    ConstructorConstructor constructorConstructor =
        new ConstructorConstructor(Collections.emptyMap(), false, Collections.emptyList());
    assertEquals("{}", constructorConstructor.toString());
  }

  @Test
    @Timeout(8000)
  public void testToString_nonEmptyInstanceCreators() {
    @SuppressWarnings("unchecked")
    InstanceCreator<String> instanceCreator = mock(InstanceCreator.class);
    HashMap<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    instanceCreators.put(String.class, instanceCreator);

    ConstructorConstructor constructorConstructor =
        new ConstructorConstructor(instanceCreators, false, Collections.emptyList());

    assertEquals(instanceCreators.toString(), constructorConstructor.toString());
  }

  @Test
    @Timeout(8000)
  public void testToString_multipleInstanceCreators() {
    @SuppressWarnings("unchecked")
    InstanceCreator<String> instanceCreator1 = mock(InstanceCreator.class);
    @SuppressWarnings("unchecked")
    InstanceCreator<Integer> instanceCreator2 = mock(InstanceCreator.class);
    HashMap<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    instanceCreators.put(String.class, instanceCreator1);
    instanceCreators.put(Integer.class, instanceCreator2);

    ConstructorConstructor constructorConstructor =
        new ConstructorConstructor(instanceCreators, true, Collections.emptyList());

    assertEquals(instanceCreators.toString(), constructorConstructor.toString());
  }
}
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ConstructorConstructor_158_1Test {

  @Test
    @Timeout(8000)
  void toString_returnsInstanceCreatorsToString() {
    // Arrange
    @SuppressWarnings("unchecked")
    Map<Type, InstanceCreator<?>> mockInstanceCreators = mock(Map.class);
    boolean useJdkUnsafe = false;
    @SuppressWarnings("unchecked")
    List<ReflectionAccessFilter> mockReflectionFilters = mock(List.class);

    ConstructorConstructor constructorConstructor =
        new ConstructorConstructor(mockInstanceCreators, useJdkUnsafe, mockReflectionFilters);

    String expected = "mockedToString";
    when(mockInstanceCreators.toString()).thenReturn(expected);

    // Act
    String actual = constructorConstructor.toString();

    // Assert
    assertEquals(expected, actual);
  }
}
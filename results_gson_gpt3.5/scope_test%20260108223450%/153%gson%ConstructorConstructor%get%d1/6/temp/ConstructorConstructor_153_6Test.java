package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.reflect.ReflectionHelper;
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
import com.google.gson.JsonIOException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Type;
import java.util.*;

class ConstructorConstructor_153_6Test {

  private Map<Type, InstanceCreator<?>> instanceCreators;
  private List<ReflectionAccessFilter> reflectionFilters;
  private ConstructorConstructor constructorConstructor;

  @BeforeEach
  void setUp() {
    instanceCreators = mock(Map.class);
    reflectionFilters = mock(List.class);
    constructorConstructor = new ConstructorConstructor(instanceCreators, true, reflectionFilters);
  }

  @Test
    @Timeout(8000)
  void get_withInstanceCreatorForType_returnsObjectConstructorUsingTypeCreator() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    InstanceCreator<?> typeCreator = mock(InstanceCreator.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(typeCreator);

    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
    String instance = "testInstance";
    when(typeCreator.createInstance(typeToken.getType())).thenReturn(instance);

    assertEquals(instance, objectConstructor.construct());
    verify(typeCreator).createInstance(typeToken.getType());
  }

  @Test
    @Timeout(8000)
  void get_withInstanceCreatorForRawType_returnsObjectConstructorUsingRawTypeCreator() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    @SuppressWarnings("unchecked")
    InstanceCreator<?> rawTypeCreator = mock(InstanceCreator.class);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(rawTypeCreator);

    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
    String instance = "rawTypeInstance";
    when(rawTypeCreator.createInstance(typeToken.getType())).thenReturn(instance);

    assertEquals(instance, objectConstructor.construct());
    verify(rawTypeCreator).createInstance(typeToken.getType());
  }

  @Test
    @Timeout(8000)
  void get_withSpecialCollectionConstructor_returnsSpecialConstructor() throws Exception {
    @SuppressWarnings("unchecked")
    TypeToken<Set<String>> typeToken = (TypeToken<Set<String>>) (TypeToken<?>) TypeToken.getParameterized(Set.class, String.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    // Use reflection to invoke private static newSpecialCollectionConstructor
    ObjectConstructor<?> specialConstructor;
    try (MockedStatic<ConstructorConstructor> mockedStatic = Mockito.mockStatic(ConstructorConstructor.class, invocation -> {
      if ("newSpecialCollectionConstructor".equals(invocation.getMethod().getName())) {
        return new ObjectConstructor<Set<String>>() {
          @Override
          public Set<String> construct() {
            return new LinkedHashSet<>();
          }
        };
      }
      return invocation.callRealMethod();
    })) {
      specialConstructor = constructorConstructor.get(typeToken);
    }
    assertNotNull(specialConstructor);
    Object instance = specialConstructor.construct();
    assertTrue(instance instanceof Set);
  }

  @Test
    @Timeout(8000)
  void get_withDefaultConstructor_returnsDefaultConstructor() {
    TypeToken<SimpleClass> typeToken = TypeToken.get(SimpleClass.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    when(reflectionFilters.isEmpty()).thenReturn(true);

    ObjectConstructor<SimpleClass> objectConstructor = constructorConstructor.get(typeToken);
    SimpleClass instance = objectConstructor.construct();
    assertNotNull(instance);
    assertEquals("default", instance.getName());
  }

  @Test
    @Timeout(8000)
  void get_withDefaultImplementationConstructor_returnsDefaultImplementation() {
    @SuppressWarnings("unchecked")
    TypeToken<Map<String, String>> typeToken = (TypeToken<Map<String, String>>) (TypeToken<?>) TypeToken.getParameterized(Map.class, String.class, String.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    ObjectConstructor<?> objectConstructor = constructorConstructor.get(typeToken);
    Object instance = objectConstructor.construct();
    assertTrue(instance instanceof LinkedHashMap);
  }

  @Test
    @Timeout(8000)
  void get_withNonInstantiableClass_throwsJsonIOException() {
    TypeToken<AbstractList> typeToken = TypeToken.get(AbstractList.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    ObjectConstructor<AbstractList> objectConstructor = constructorConstructor.get(typeToken);
    JsonIOException thrown = assertThrows(JsonIOException.class, objectConstructor::construct);
    assertTrue(thrown.getMessage().contains("Unable to create instance"));
  }

  @Test
    @Timeout(8000)
  void get_withFilterBlockAll_throwsJsonIOException() {
    TypeToken<SimpleClass> typeToken = TypeToken.get(SimpleClass.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    // Mock ReflectionAccessFilterHelper.getFilterResult to return BLOCK_ALL
    try (MockedStatic<ReflectionAccessFilterHelper> mockedStatic = Mockito.mockStatic(ReflectionAccessFilterHelper.class)) {
      mockedStatic.when(() -> ReflectionAccessFilterHelper.getFilterResult(any(), eq(typeToken.getRawType())))
          .thenReturn(FilterResult.BLOCK_ALL);

      ObjectConstructor<SimpleClass> objectConstructor = constructorConstructor.get(typeToken);
      JsonIOException thrown = assertThrows(JsonIOException.class, objectConstructor::construct);
      assertTrue(thrown.getMessage().contains("Unable to create instance of"));
      assertTrue(thrown.getMessage().contains("ReflectionAccessFilter"));
    }
  }

  // Helper class with default constructor
  static class SimpleClass {
    private final String name;

    SimpleClass() {
      this.name = "default";
    }

    String getName() {
      return name;
    }
  }
}
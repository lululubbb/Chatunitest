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
import java.lang.reflect.Type;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

class ConstructorConstructor_153_2Test {

  private Map<Type, InstanceCreator<?>> instanceCreators;
  private List<ReflectionAccessFilter> reflectionFilters;
  private ConstructorConstructor constructorConstructor;

  @BeforeEach
  void setUp() {
    instanceCreators = mock(Map.class);
    reflectionFilters = new ArrayList<>();
    constructorConstructor = new ConstructorConstructor(instanceCreators, true, reflectionFilters);
  }

  @Test
    @Timeout(8000)
  void get_WithInstanceCreatorForType_ReturnsObjectConstructorUsingTypeCreator() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    InstanceCreator<String> typeCreator = mock(InstanceCreator.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(typeCreator);

    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    String instance = "test";
    when(typeCreator.createInstance(typeToken.getType())).thenReturn(instance);
    assertEquals(instance, objectConstructor.construct());
    verify(typeCreator).createInstance(typeToken.getType());
  }

  @Test
    @Timeout(8000)
  void get_WithInstanceCreatorForRawType_ReturnsObjectConstructorUsingRawTypeCreator() {
    TypeToken<ArrayList> typeToken = TypeToken.get(ArrayList.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    @SuppressWarnings("unchecked")
    InstanceCreator<ArrayList> rawTypeCreator = mock(InstanceCreator.class);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(rawTypeCreator);

    ObjectConstructor<ArrayList> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    ArrayList<String> instance = new ArrayList<>();
    when(rawTypeCreator.createInstance(typeToken.getType())).thenReturn(instance);
    assertEquals(instance, objectConstructor.construct());
    verify(rawTypeCreator).createInstance(typeToken.getType());
  }

  @Test
    @Timeout(8000)
  void get_WithSpecialCollectionConstructor_ReturnsThatConstructor() {
    TypeToken<LinkedHashSet> typeToken = TypeToken.get(LinkedHashSet.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    ObjectConstructor<LinkedHashSet> specialConstructor = constructorConstructor.get(typeToken);

    assertNotNull(specialConstructor);
    LinkedHashSet<String> instance = specialConstructor.construct();
    assertTrue(instance instanceof LinkedHashSet);
  }

  @Test
    @Timeout(8000)
  void get_WithDefaultConstructor_ReturnsThatConstructor() throws Exception {
    TypeToken<DefaultCtorClass> typeToken = TypeToken.get(DefaultCtorClass.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    // Add a filter that returns ALLOW to allow default constructor usage
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(filter.apply(ArgumentMatchers.any())).thenReturn(FilterResult.ALLOW);
    reflectionFilters.add(filter);

    ObjectConstructor<DefaultCtorClass> constructor = constructorConstructor.get(typeToken);

    assertNotNull(constructor);
    DefaultCtorClass instance = constructor.construct();
    assertNotNull(instance);
    assertEquals("default", instance.value);
  }

  @Test
    @Timeout(8000)
  void get_WithDefaultImplementationConstructor_ReturnsThatConstructor() {
    TypeToken<Map> typeToken = TypeToken.get(Map.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    // Add a filter that returns ALLOW to allow default implementation constructor usage
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(filter.apply(ArgumentMatchers.any())).thenReturn(FilterResult.ALLOW);
    reflectionFilters.add(filter);

    ObjectConstructor<Map> constructor = constructorConstructor.get(typeToken);

    assertNotNull(constructor);
    Map<?, ?> instance = constructor.construct();
    assertTrue(instance instanceof LinkedHashMap);
  }

  @Test
    @Timeout(8000)
  void get_WithNonInstantiableType_ReturnsObjectConstructorThrowingJsonIOException() {
    TypeToken<AbstractList> typeToken = TypeToken.get(AbstractList.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(filter.apply(ArgumentMatchers.any())).thenReturn(FilterResult.ALLOW);
    reflectionFilters.add(filter);

    ObjectConstructor<AbstractList> constructor = constructorConstructor.get(typeToken);

    assertNotNull(constructor);
    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("abstract"));
  }

  @Test
    @Timeout(8000)
  void get_WithFilterBlockAll_ReturnsObjectConstructorThrowingJsonIOException() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(filter.apply(typeToken.getRawType())).thenReturn(FilterResult.BLOCK_ALL);
    reflectionFilters.add(filter);

    ObjectConstructor<String> constructor = constructorConstructor.get(typeToken);

    assertNotNull(constructor);
    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("Unable to create instance"));
  }

  @Test
    @Timeout(8000)
  void get_WithFilterBlockInaccessible_DoesNotUseUnsafeAllocator() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(filter.apply(typeToken.getRawType())).thenReturn(FilterResult.BLOCK_INACCESSIBLE);
    reflectionFilters.add(filter);

    ObjectConstructor<String> constructor = constructorConstructor.get(typeToken);

    assertNotNull(constructor);
    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("Unable to create instance"));
  }

  // Helper class with default constructor
  static class DefaultCtorClass {
    String value = "default";
  }
}
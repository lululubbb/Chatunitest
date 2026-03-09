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

class ConstructorConstructor_153_4Test {

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
  void get_WithInstanceCreatorForType_ReturnsObjectConstructorThatUsesTypeCreator() {
    TypeToken<ArrayList> typeToken = TypeToken.get(ArrayList.class);
    @SuppressWarnings("unchecked")
    InstanceCreator<?> typeCreator = mock(InstanceCreator.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(typeCreator);

    ObjectConstructor<ArrayList> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    ArrayList expectedInstance = new ArrayList();
    when(typeCreator.createInstance(typeToken.getType())).thenReturn(expectedInstance);
    ArrayList actualInstance = objectConstructor.construct();
    assertSame(expectedInstance, actualInstance);
  }

  @Test
    @Timeout(8000)
  void get_WithInstanceCreatorForRawType_ReturnsObjectConstructorThatUsesRawTypeCreator() {
    TypeToken<ArrayList> typeToken = TypeToken.get(ArrayList.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    @SuppressWarnings("unchecked")
    InstanceCreator<?> rawTypeCreator = mock(InstanceCreator.class);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(rawTypeCreator);

    ObjectConstructor<ArrayList> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    ArrayList expectedInstance = new ArrayList();
    when(rawTypeCreator.createInstance(typeToken.getType())).thenReturn(expectedInstance);
    ArrayList actualInstance = objectConstructor.construct();
    assertSame(expectedInstance, actualInstance);
  }

  @Test
    @Timeout(8000)
  void get_WithSpecialCollectionConstructor_ReturnsThatConstructor() {
    TypeToken<ArrayDeque> typeToken = TypeToken.get(ArrayDeque.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    ObjectConstructor<ArrayDeque> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    ArrayDeque<?> instance = (ArrayDeque<?>) objectConstructor.construct();
    assertTrue(instance.isEmpty());
  }

  @Test
    @Timeout(8000)
  void get_WithDefaultConstructor_ReturnsThatConstructor() {
    class HasNoArgsConstructor {
      public HasNoArgsConstructor() {}
    }
    TypeToken<HasNoArgsConstructor> typeToken = TypeToken.get(HasNoArgsConstructor.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    // We mock reflectionFilters to allow reflection
    when(reflectionFilters.isEmpty()).thenReturn(true);

    ObjectConstructor<HasNoArgsConstructor> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    HasNoArgsConstructor instance = objectConstructor.construct();
    assertNotNull(instance);
  }

  @Test
    @Timeout(8000)
  void get_WithDefaultImplementationConstructor_ReturnsThatConstructor() {
    TypeToken<Set> typeToken = TypeToken.get(Set.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    // We mock reflectionFilters to allow reflection
    when(reflectionFilters.isEmpty()).thenReturn(true);

    ObjectConstructor<Set> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    Set<?> instance = objectConstructor.construct();
    assertTrue(instance.isEmpty());
  }

  @Test
    @Timeout(8000)
  void get_WithNonInstantiableType_ReturnsConstructorThatThrowsJsonIOException() {
    abstract class AbstractClass {}
    TypeToken<AbstractClass> typeToken = TypeToken.get(AbstractClass.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    // We mock reflectionFilters to allow reflection
    when(reflectionFilters.isEmpty()).thenReturn(true);

    ObjectConstructor<AbstractClass> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    JsonIOException ex = assertThrows(JsonIOException.class, objectConstructor::construct);
    assertTrue(ex.getMessage().contains("Unable to create instance"));
  }

  @Test
    @Timeout(8000)
  void get_WithReflectionAccessFilterBlockAll_ReturnsConstructorThatThrowsJsonIOException() {
    class SomeClass {
      public SomeClass() {}
    }
    TypeToken<SomeClass> typeToken = TypeToken.get(SomeClass.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(reflectionFilters.isEmpty()).thenReturn(false);
    when(reflectionFilters.iterator()).thenReturn(Collections.singleton(filter).iterator());
    when(ReflectionAccessFilterHelper.getFilterResult(reflectionFilters, SomeClass.class))
        .thenReturn(FilterResult.BLOCK_ALL);

    ObjectConstructor<SomeClass> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    JsonIOException ex = assertThrows(JsonIOException.class, objectConstructor::construct);
    assertTrue(ex.getMessage().contains("does not permit using reflection or Unsafe"));
  }

  @Test
    @Timeout(8000)
  void get_WithReflectionAccessFilterAllow_UsesUnsafeAllocator() {
    class SomeClass {
      private SomeClass() {
        throw new RuntimeException("Constructor should not be called");
      }
    }
    TypeToken<SomeClass> typeToken = TypeToken.get(SomeClass.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(reflectionFilters.isEmpty()).thenReturn(false);
    when(reflectionFilters.iterator()).thenReturn(Collections.singleton(filter).iterator());
    when(ReflectionAccessFilterHelper.getFilterResult(reflectionFilters, SomeClass.class))
        .thenReturn(FilterResult.ALLOW);

    ObjectConstructor<SomeClass> objectConstructor = constructorConstructor.get(typeToken);

    assertNotNull(objectConstructor);
    SomeClass instance = objectConstructor.construct();
    assertNotNull(instance);
  }
}
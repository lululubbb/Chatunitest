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

import java.lang.reflect.Type;
import java.util.*;

class ConstructorConstructor_153_3Test {
  private Map<Type, InstanceCreator<?>> instanceCreators;
  private List<ReflectionAccessFilter> reflectionFilters;
  private ConstructorConstructor constructorConstructor;

  @BeforeEach
  void setUp() {
    instanceCreators = new HashMap<>();
    reflectionFilters = new ArrayList<>();
    constructorConstructor = new ConstructorConstructor(instanceCreators, true, reflectionFilters);
  }

  static class Dummy {}

  static class DummyWithNoDefaultConstructor {
    private DummyWithNoDefaultConstructor() {}
  }

  @Test
    @Timeout(8000)
  void get_returnsInstanceCreatorForExactType() {
    InstanceCreator<Dummy> creator = mock(InstanceCreator.class);
    instanceCreators.put(Dummy.class, creator);
    TypeToken<Dummy> token = TypeToken.get(Dummy.class);

    ObjectConstructor<Dummy> objectConstructor = constructorConstructor.get(token);
    objectConstructor.construct();

    verify(creator).createInstance(Dummy.class);
  }

  @Test
    @Timeout(8000)
  void get_returnsInstanceCreatorForRawType() {
    InstanceCreator<Dummy> creator = mock(InstanceCreator.class);
    instanceCreators.put(Dummy.class, creator);
    // Use a wildcard TypeToken to avoid incompatible type error
    @SuppressWarnings("rawtypes")
    TypeToken<?> token = TypeToken.get((Type) Dummy.class);

    // Remove exact type to force raw type lookup
    instanceCreators.clear();
    instanceCreators.put(Dummy.class, creator);

    @SuppressWarnings("unchecked")
    ObjectConstructor<Dummy> objectConstructor = (ObjectConstructor<Dummy>) constructorConstructor.get(token);
    objectConstructor.construct();

    verify(creator).createInstance(Dummy.class);
  }

  @Test
    @Timeout(8000)
  void get_returnsSpecialCollectionConstructor_whenApplicable() throws Exception {
    TypeToken<Set> token = TypeToken.get(Set.class);

    ObjectConstructor<?> constructor = constructorConstructor.get(token);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof Set);
  }

  @Test
    @Timeout(8000)
  void get_returnsDefaultConstructor_whenAvailable() throws Exception {
    TypeToken<Dummy> token = TypeToken.get(Dummy.class);

    ObjectConstructor<Dummy> constructor = constructorConstructor.get(token);
    assertNotNull(constructor);
    Dummy instance = constructor.construct();
    assertNotNull(instance);
    assertTrue(instance instanceof Dummy);
  }

  @Test
    @Timeout(8000)
  void get_returnsDefaultImplementationConstructor_whenNoDefaultConstructor() {
    TypeToken<Set> token = TypeToken.get(Set.class);

    ObjectConstructor<?> constructor = constructorConstructor.get(token);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof Set);
  }

  @Test
    @Timeout(8000)
  void get_returnsExceptionThrowingConstructor_whenNotInstantiable() {
    class AbstractClass {}

    TypeToken<AbstractClass> token = TypeToken.get(AbstractClass.class);

    ObjectConstructor<AbstractClass> constructor = constructorConstructor.get(token);
    assertNotNull(constructor);
    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("Unable to create instance"));
  }

  @Test
    @Timeout(8000)
  void get_usesUnsafeAllocator_whenFilterAllows() throws Exception {
    // Prepare a class with no accessible constructor
    class NoDefaultConstructor {
      private NoDefaultConstructor() {}
    }
    TypeToken<NoDefaultConstructor> token = TypeToken.get(NoDefaultConstructor.class);

    // Add a filter that returns ALLOW
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(filter.apply(any())).thenAnswer(invocation -> {
      Class<?> clazz = invocation.getArgument(0);
      if (clazz == NoDefaultConstructor.class) {
        return ReflectionAccessFilter.FilterResult.ALLOW;
      }
      return ReflectionAccessFilter.FilterResult.UNDECIDED;
    });
    reflectionFilters.add(filter);

    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, reflectionFilters);

    ObjectConstructor<NoDefaultConstructor> constructor = cc.get(token);
    assertNotNull(constructor);
    NoDefaultConstructor instance = constructor.construct();
    assertNotNull(instance);
    assertEquals(NoDefaultConstructor.class, instance.getClass());
  }

  @Test
    @Timeout(8000)
  void get_throwsJsonIOException_whenFilterBlocksReflectionAndUnsafe() {
    class NoDefaultConstructor {
      private NoDefaultConstructor() {}
    }
    TypeToken<NoDefaultConstructor> token = TypeToken.get(NoDefaultConstructor.class);

    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(filter.apply(any())).thenAnswer(invocation -> {
      Class<?> clazz = invocation.getArgument(0);
      if (clazz == NoDefaultConstructor.class) {
        return ReflectionAccessFilter.FilterResult.BLOCK_ALL;
      }
      return ReflectionAccessFilter.FilterResult.UNDECIDED;
    });
    reflectionFilters.add(filter);

    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, reflectionFilters);

    ObjectConstructor<NoDefaultConstructor> constructor = cc.get(token);
    assertNotNull(constructor);
    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("Unable to create instance of"));
  }

  @Test
    @Timeout(8000)
  void get_throwsJsonIOException_whenFilterBlocksInaccessible() {
    class NoDefaultConstructor {
      private NoDefaultConstructor() {}
    }
    TypeToken<NoDefaultConstructor> token = TypeToken.get(NoDefaultConstructor.class);

    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(filter.apply(any())).thenAnswer(invocation -> {
      Class<?> clazz = invocation.getArgument(0);
      if (clazz == NoDefaultConstructor.class) {
        return ReflectionAccessFilter.FilterResult.BLOCK_INACCESSIBLE;
      }
      return ReflectionAccessFilter.FilterResult.UNDECIDED;
    });
    reflectionFilters.add(filter);

    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, reflectionFilters);

    ObjectConstructor<NoDefaultConstructor> constructor = cc.get(token);
    assertNotNull(constructor);
    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("Unable to create instance of"));
  }
}
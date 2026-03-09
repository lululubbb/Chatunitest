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

class ConstructorConstructor_153_1Test {
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
  void get_returnsInstanceCreatorForExactType() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    InstanceCreator<String> creator = mock(InstanceCreator.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(creator);

    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    String expected = "created";
    when(creator.createInstance(typeToken.getType())).thenReturn(expected);
    assertEquals(expected, objectConstructor.construct());

    verify(instanceCreators).get(typeToken.getType());
  }

  @Test
    @Timeout(8000)
  void get_returnsInstanceCreatorForRawType() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    InstanceCreator<String> creator = mock(InstanceCreator.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(creator);

    ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
    assertNotNull(objectConstructor);
    String expected = "rawCreated";
    when(creator.createInstance(typeToken.getType())).thenReturn(expected);
    assertEquals(expected, objectConstructor.construct());

    verify(instanceCreators).get(typeToken.getType());
    verify(instanceCreators).get(typeToken.getRawType());
  }

  @Test
    @Timeout(8000)
  void get_returnsSpecialCollectionConstructor_whenApplicable() throws Exception {
    TypeToken<Set<String>> typeToken = TypeToken.getParameterized(Set.class, String.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    // Use reflection to invoke private static method newSpecialCollectionConstructor
    var method = ConstructorConstructor.class.getDeclaredMethod("newSpecialCollectionConstructor", Type.class, Class.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    ObjectConstructor<Set<String>> specialConstructor = (ObjectConstructor<Set<String>>) method.invoke(null, typeToken.getType(), typeToken.getRawType());
    if (specialConstructor == null) {
      // fallback: test get returns non-null specialConstructor
      ObjectConstructor<Set<String>> constructor = constructorConstructor.get(typeToken);
      assertNotNull(constructor);
      // Construct an instance and check it is instance of Set
      Object instance = constructor.construct();
      assertTrue(instance instanceof Set);
    } else {
      Object instance = specialConstructor.construct();
      assertTrue(instance instanceof Set);
    }
  }

  @Test
    @Timeout(8000)
  void get_returnsDefaultConstructor_whenAvailable() throws Exception {
    class Dummy {
      public Dummy() {}
    }
    TypeToken<Dummy> typeToken = TypeToken.get(Dummy.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    // Mock ReflectionAccessFilterHelper.getFilterResult to return ALLOW
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(reflectionFilters.iterator()).thenReturn(Collections.emptyIterator());

    // Use reflection to invoke private static method newDefaultConstructor
    var method = ConstructorConstructor.class.getDeclaredMethod("newDefaultConstructor", Class.class, FilterResult.class);
    method.setAccessible(true);
    ObjectConstructor<Dummy> defaultConstructor = (ObjectConstructor<Dummy>) method.invoke(null, Dummy.class, FilterResult.ALLOW);
    if (defaultConstructor != null) {
      Object instance = defaultConstructor.construct();
      assertNotNull(instance);
      assertTrue(instance instanceof Dummy);
    } else {
      // fallback: test get returns non-null and construct returns Dummy
      ObjectConstructor<Dummy> constructor = constructorConstructor.get(typeToken);
      assertNotNull(constructor);
      Object instance = constructor.construct();
      assertNotNull(instance);
      assertTrue(instance instanceof Dummy);
    }
  }

  @Test
    @Timeout(8000)
  void get_returnsDefaultImplementationConstructor_forInterface() {
    TypeToken<Map<String, String>> typeToken = TypeToken.getParameterized(Map.class, String.class, String.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    ObjectConstructor<Map<String, String>> constructor = constructorConstructor.get(typeToken);
    assertNotNull(constructor);
    Map<String, String> map = constructor.construct();
    assertTrue(map instanceof Map);
  }

  @Test
    @Timeout(8000)
  void get_returnsErrorConstructor_whenNotInstantiable() throws Exception {
    class AbstractClass {
      AbstractClass() {}
    }
    TypeToken<AbstractClass> typeToken = TypeToken.get(AbstractClass.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    // Use reflection to invoke private static method checkInstantiable
    var checkMethod = ConstructorConstructor.class.getDeclaredMethod("checkInstantiable", Class.class);
    checkMethod.setAccessible(true);
    String message = (String) checkMethod.invoke(null, AbstractClass.class);
    assertNotNull(message);

    ObjectConstructor<AbstractClass> constructor = constructorConstructor.get(typeToken);
    assertNotNull(constructor);
    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("Unable to create instance") || ex.getMessage().contains(message));
  }

  @Test
    @Timeout(8000)
  void get_returnsUnsafeAllocator_whenAllowed() {
    class NoDefaultCtor {
      private NoDefaultCtor(String s) {}
    }
    TypeToken<NoDefaultCtor> typeToken = TypeToken.get(NoDefaultCtor.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    // Spy on reflectionFilters to simulate FilterResult.ALLOW
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(reflectionFilters.iterator()).thenReturn(Collections.emptyIterator());

    ObjectConstructor<NoDefaultCtor> constructor = constructorConstructor.get(typeToken);
    assertNotNull(constructor);
    NoDefaultCtor instance = constructor.construct();
    assertNotNull(instance);
    assertEquals(NoDefaultCtor.class, instance.getClass());
  }

  @Test
    @Timeout(8000)
  void get_throwsJsonIOException_whenReflectionNotAllowed() {
    class NoDefaultCtor {
      private NoDefaultCtor() {}
    }
    TypeToken<NoDefaultCtor> typeToken = TypeToken.get(NoDefaultCtor.class);
    when(instanceCreators.get(typeToken.getType())).thenReturn(null);
    when(instanceCreators.get(typeToken.getRawType())).thenReturn(null);

    // Create ConstructorConstructor with reflectionFilters that cause BLOCK_ALL or BLOCK_INACCESSIBLE
    List<ReflectionAccessFilter> filters = List.of(new ReflectionAccessFilter() {
      @Override
      public FilterResult checkAccess(Class<?> clazz) {
        return FilterResult.BLOCK_ALL;
      }
    });
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, filters);

    ObjectConstructor<NoDefaultCtor> constructor = cc.get(typeToken);
    assertNotNull(constructor);
    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("ReflectionAccessFilter does not permit"));
  }
}
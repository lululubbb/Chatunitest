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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ConstructorConstructor_153_5Test {

  private Map<Type, InstanceCreator<?>> instanceCreators;
  private List<ReflectionAccessFilter> reflectionFilters;

  @BeforeEach
  void setUp() {
    instanceCreators = new HashMap<>();
    reflectionFilters = new ArrayList<>();
  }

  @Test
    @Timeout(8000)
  void get_withInstanceCreatorForExactType_returnsObjectConstructorUsingInstanceCreator() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    InstanceCreator<String> creator = mock(InstanceCreator.class);
    instanceCreators.put(typeToken.getType(), creator);

    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, reflectionFilters);
    ObjectConstructor<String> objectConstructor = cc.get(typeToken);

    String expected = "test";
    when(creator.createInstance(typeToken.getType())).thenReturn(expected);

    String result = objectConstructor.construct();
    assertEquals(expected, result);
    verify(creator).createInstance(typeToken.getType());
  }

  @Test
    @Timeout(8000)
  void get_withInstanceCreatorForRawType_returnsObjectConstructorUsingRawTypeInstanceCreator() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    InstanceCreator<String> creator = mock(InstanceCreator.class);
    instanceCreators.put(String.class, creator);

    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, reflectionFilters);
    ObjectConstructor<String> objectConstructor = cc.get(typeToken);

    String expected = "rawTypeInstance";
    when(creator.createInstance(typeToken.getType())).thenReturn(expected);

    String result = objectConstructor.construct();
    assertEquals(expected, result);
    verify(creator).createInstance(typeToken.getType());
  }

  @Test
    @Timeout(8000)
  void get_withSpecialCollectionConstructor_returnsSpecialCollectionConstructor() throws Exception {
    TypeToken<Set<String>> typeToken = TypeToken.get(Set.class);

    ConstructorConstructor cc = spy(new ConstructorConstructor(instanceCreators, true, reflectionFilters));
    // Mock newSpecialCollectionConstructor to return a specific ObjectConstructor
    ObjectConstructor<Set<String>> specialConstructor = new ObjectConstructor<Set<String>>() {
      @Override
      public Set<String> construct() {
        return new HashSet<>();
      }
    };
    try (MockedStatic<ConstructorConstructor> mockedStatic = Mockito.mockStatic(ConstructorConstructor.class)) {
      mockedStatic.when(() -> ConstructorConstructor.newSpecialCollectionConstructor(typeToken.getType(), typeToken.getRawType()))
          .thenReturn(specialConstructor);

      ObjectConstructor<Set<String>> result = cc.get(typeToken);
      assertNotNull(result);
      assertTrue(result.construct() instanceof Set);
    }
  }

  @Test
    @Timeout(8000)
  void get_withDefaultConstructor_returnsDefaultConstructor() throws Exception {
    TypeToken<ArrayList<String>> typeToken = TypeToken.get(ArrayList.class);

    ConstructorConstructor cc = spy(new ConstructorConstructor(instanceCreators, true, reflectionFilters));
    FilterResult filterResult = FilterResult.ALLOW;
    try (MockedStatic<ReflectionAccessFilterHelper> filterHelperMock = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ConstructorConstructor> ccStatic = Mockito.mockStatic(ConstructorConstructor.class)) {
      filterHelperMock.when(() -> ReflectionAccessFilterHelper.getFilterResult(reflectionFilters, typeToken.getRawType()))
          .thenReturn(filterResult);

      ObjectConstructor<ArrayList<String>> defaultConstructor = new ObjectConstructor<ArrayList<String>>() {
        @Override
        public ArrayList<String> construct() {
          return new ArrayList<>();
        }
      };

      ccStatic.when(() -> ConstructorConstructor.newDefaultConstructor(typeToken.getRawType(), filterResult))
          .thenReturn(defaultConstructor);

      ObjectConstructor<ArrayList<String>> result = cc.get(typeToken);
      assertNotNull(result);
      assertTrue(result.construct() instanceof ArrayList);
    }
  }

  @Test
    @Timeout(8000)
  void get_withDefaultImplementationConstructor_returnsDefaultImplementationConstructor() throws Exception {
    TypeToken<Map<String, String>> typeToken = TypeToken.get(Map.class);

    ConstructorConstructor cc = spy(new ConstructorConstructor(instanceCreators, true, reflectionFilters));
    FilterResult filterResult = FilterResult.ALLOW;
    try (MockedStatic<ReflectionAccessFilterHelper> filterHelperMock = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ConstructorConstructor> ccStatic = Mockito.mockStatic(ConstructorConstructor.class)) {
      filterHelperMock.when(() -> ReflectionAccessFilterHelper.getFilterResult(reflectionFilters, typeToken.getRawType()))
          .thenReturn(filterResult);

      ccStatic.when(() -> ConstructorConstructor.newDefaultConstructor(typeToken.getRawType(), filterResult))
          .thenReturn(null);

      ObjectConstructor<Map<String, String>> defaultImplConstructor = new ObjectConstructor<Map<String, String>>() {
        @Override
        public Map<String, String> construct() {
          return new LinkedHashMap<>();
        }
      };

      ccStatic.when(() -> ConstructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), typeToken.getRawType()))
          .thenReturn(defaultImplConstructor);

      ObjectConstructor<Map<String, String>> result = cc.get(typeToken);
      assertNotNull(result);
      assertTrue(result.construct() instanceof LinkedHashMap);
    }
  }

  @Test
    @Timeout(8000)
  void get_withNonInstantiableType_returnsObjectConstructorThrowingJsonIOException() {
    TypeToken<Class<?>> typeToken = TypeToken.get(AbstractList.class);

    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, reflectionFilters);

    ObjectConstructor<Class<?>> objectConstructor = cc.get(typeToken);

    JsonIOException thrown = assertThrows(JsonIOException.class, objectConstructor::construct);
    assertTrue(thrown.getMessage().contains("Unable to instantiate"));
  }

  @Test
    @Timeout(8000)
  void get_withReflectionFilterBlockAll_returnsObjectConstructorThrowingJsonIOException() throws Exception {
    TypeToken<String> typeToken = TypeToken.get(String.class);

    ConstructorConstructor cc = spy(new ConstructorConstructor(instanceCreators, true, reflectionFilters));
    try (MockedStatic<ReflectionAccessFilterHelper> filterHelperMock = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ConstructorConstructor> ccStatic = Mockito.mockStatic(ConstructorConstructor.class)) {

      filterHelperMock.when(() -> ReflectionAccessFilterHelper.getFilterResult(reflectionFilters, typeToken.getRawType()))
          .thenReturn(FilterResult.BLOCK_ALL);

      ccStatic.when(() -> ConstructorConstructor.newDefaultConstructor(typeToken.getRawType(), FilterResult.BLOCK_ALL))
          .thenReturn(null);
      ccStatic.when(() -> ConstructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), typeToken.getRawType()))
          .thenReturn(null);

      ObjectConstructor<String> objectConstructor = cc.get(typeToken);
      JsonIOException thrown = assertThrows(JsonIOException.class, objectConstructor::construct);
      assertTrue(thrown.getMessage().contains("ReflectionAccessFilter does not permit"));
    }
  }

  @Test
    @Timeout(8000)
  void get_withReflectionFilterAllowAndUseJdkUnsafe_returnsUnsafeAllocator() throws Exception {
    TypeToken<String> typeToken = TypeToken.get(String.class);

    ConstructorConstructor cc = spy(new ConstructorConstructor(instanceCreators, true, reflectionFilters));
    try (MockedStatic<ReflectionAccessFilterHelper> filterHelperMock = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ConstructorConstructor> ccStatic = Mockito.mockStatic(ConstructorConstructor.class)) {

      filterHelperMock.when(() -> ReflectionAccessFilterHelper.getFilterResult(reflectionFilters, typeToken.getRawType()))
          .thenReturn(FilterResult.ALLOW);

      ccStatic.when(() -> ConstructorConstructor.newDefaultConstructor(typeToken.getRawType(), FilterResult.ALLOW))
          .thenReturn(null);
      ccStatic.when(() -> ConstructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), typeToken.getRawType()))
          .thenReturn(null);

      doReturn(mock(ObjectConstructor.class)).when(cc).newUnsafeAllocator(typeToken.getRawType());

      ObjectConstructor<String> objectConstructor = cc.get(typeToken);
      assertNotNull(objectConstructor);
      verify(cc).newUnsafeAllocator(typeToken.getRawType());
    }
  }
}
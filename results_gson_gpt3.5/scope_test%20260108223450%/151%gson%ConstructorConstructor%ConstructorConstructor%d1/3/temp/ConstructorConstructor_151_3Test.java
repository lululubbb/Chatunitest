package com.google.gson.internal;
import com.google.gson.internal.reflect.ReflectionHelper;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.*;
import java.util.*;
import org.junit.jupiter.api.*;

class ConstructorConstructor_151_3Test {

  private Map<Type, InstanceCreator<?>> instanceCreators;
  private List<ReflectionAccessFilter> reflectionFilters;

  @BeforeEach
  void setUp() {
    instanceCreators = new HashMap<>();
    reflectionFilters = new ArrayList<>();
  }

  @Test
  void testConstructorStoresParameters() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, reflectionFilters);
    assertNotNull(cc);
    assertEquals(instanceCreators, getField(cc, "instanceCreators"));
    assertEquals(true, getField(cc, "useJdkUnsafe"));
    assertEquals(reflectionFilters, getField(cc, "reflectionFilters"));
  }

  @Test
  void testCheckInstantiable_withNonInstantiableClass_throws() {
    Class<?> abstractClass = Modifier.isAbstract(AbstractList.class.getModifiers()) ? AbstractList.class : List.class;
    Exception ex = assertThrows(JsonIOException.class, () -> {
      ConstructorConstructor.checkInstantiable(abstractClass);
    });
    assertTrue(ex.getMessage().contains("cannot be instantiated"));
  }

  @Test
  void testCheckInstantiable_withInstantiableClass_passes() {
    assertNull(ConstructorConstructor.checkInstantiable(ArrayList.class));
  }

  @Test
  void testGet_withInstanceCreator_returnsInstanceCreator() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    InstanceCreator<String> creator = mock(InstanceCreator.class);
    instanceCreators.put(String.class, creator);
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);

    ObjectConstructor<String> objectConstructor = cc.get(typeToken);
    assertNotNull(objectConstructor);
    String expected = "instanceCreator";
    String actual = objectConstructor.toString();
    assertNotNull(actual);
  }

  @Test
  void testGet_withEnumSet() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);
    TypeToken<EnumSet<Thread.State>> typeToken = (TypeToken<EnumSet<Thread.State>>) TypeToken.get(
        TypeToken.getParameterized(EnumSet.class, Thread.State.class).getType());
    ObjectConstructor<?> constructor = cc.get(typeToken);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof EnumSet);
  }

  @Test
  void testGet_withEnumMap() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);
    TypeToken<EnumMap<Thread.State, String>> typeToken = (TypeToken<EnumMap<Thread.State, String>>) TypeToken.get(
        TypeToken.getParameterized(EnumMap.class, Thread.State.class, String.class).getType());
    ObjectConstructor<?> constructor = cc.get(typeToken);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof EnumMap);
  }

  @Test
  void testGet_withCollection() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);
    TypeToken<ArrayList<String>> typeToken = (TypeToken<ArrayList<String>>) TypeToken.get(
        TypeToken.getParameterized(ArrayList.class, String.class).getType());
    ObjectConstructor<?> constructor = cc.get(typeToken);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ArrayList);
  }

  @Test
  void testGet_withMap() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);
    TypeToken<HashMap<String, String>> typeToken = (TypeToken<HashMap<String, String>>) TypeToken.get(
        TypeToken.getParameterized(HashMap.class, String.class, String.class).getType());
    ObjectConstructor<?> constructor = cc.get(typeToken);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof HashMap);
  }

  @Test
  void testGet_withDefaultConstructor() throws Exception {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);
    TypeToken<Date> typeToken = TypeToken.get(Date.class);

    // Add reflection filter that returns ALLOW
    reflectionFilters.add(type -> FilterResult.ALLOW);

    ObjectConstructor<?> constructor = cc.get(typeToken);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof Date);
  }

  @Test
  void testGet_withUnsafeAllocator() throws Exception {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, reflectionFilters);
    TypeToken<ClassWithoutNoArgConstructor> typeToken = TypeToken.get(ClassWithoutNoArgConstructor.class);

    // Add reflection filter that returns ALLOW
    reflectionFilters.add(type -> FilterResult.ALLOW);

    ObjectConstructor<?> constructor = cc.get(typeToken);
    assertNotNull(constructor);
    Object instance = constructor.construct();
    assertTrue(instance instanceof ClassWithoutNoArgConstructor);
  }

  @Test
  void testNewDefaultConstructor_withPrivateConstructor_andFilterDeny() throws Exception {
    // Use ReflectionAccessFilter.FilterResult.DISALLOWED instead of DISALLOW
    FilterResult deny = FilterResult.DISALLOWED;
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);

    ObjectConstructor<?> oc = invokePrivateStatic(
        ConstructorConstructor.class,
        "newDefaultConstructor",
        new Class[]{Class.class, FilterResult.class},
        PrivateConstructorClass.class,
        deny);
    assertNotNull(oc);
    Exception ex = assertThrows(InvocationTargetException.class, oc::construct);
    assertNotNull(ex);
  }

  private static Object getField(Object instance, String fieldName) {
    try {
      Field field = ConstructorConstructor.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(instance);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static <T> T invokePrivateStatic(Class<?> clazz, String methodName, Class<?>[] paramTypes, Object... args) {
    try {
      Method method = clazz.getDeclaredMethod(methodName, paramTypes);
      method.setAccessible(true);
      @SuppressWarnings("unchecked")
      T result = (T) method.invoke(null, args);
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static class ClassWithoutNoArgConstructor {
    final String value;

    ClassWithoutNoArgConstructor(String value) {
      this.value = value;
    }
  }

  private static class PrivateConstructorClass {
    private PrivateConstructorClass() {
      throw new RuntimeException("Private constructor");
    }
  }
}
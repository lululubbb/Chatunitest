package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.ReflectionAccessFilter.FilterResult;
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
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.*;

class ConstructorConstructor_151_4Test {

  private Map<Type, InstanceCreator<?>> instanceCreators;
  private List<ReflectionAccessFilter> reflectionFilters;

  @BeforeEach
  void setUp() {
    instanceCreators = new HashMap<>();
    reflectionFilters = new ArrayList<>();
  }

  @Test
    @Timeout(8000)
  void constructor_initializesFields() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, reflectionFilters);
    assertSame(instanceCreators, getField(cc, "instanceCreators"));
    assertTrue((Boolean) getField(cc, "useJdkUnsafe"));
    assertSame(reflectionFilters, getField(cc, "reflectionFilters"));
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_publicConcreteClass_returnsNull() throws Exception {
    String result = ConstructorConstructor.checkInstantiable(ArrayList.class);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_abstractClass_returnsError() throws Exception {
    String result = ConstructorConstructor.checkInstantiable(AbstractList.class);
    assertNotNull(result);
    assertTrue(result.contains("abstract"));
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_interface_returnsError() throws Exception {
    String result = ConstructorConstructor.checkInstantiable(List.class);
    assertNotNull(result);
    assertTrue(result.contains("interface"));
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_nonStaticMemberClass_returnsError() throws Exception {
    class NonStaticMember {}
    String result = ConstructorConstructor.checkInstantiable(NonStaticMember.class);
    assertNotNull(result);
    assertTrue(result.contains("non-static"));
  }

  @Test
    @Timeout(8000)
  void get_returnsInstanceCreatorFromMap() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    InstanceCreator<String> creator = mock(InstanceCreator.class);
    instanceCreators.put(String.class, creator);
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);

    ObjectConstructor<String> objectConstructor = cc.get(typeToken);
    assertNotNull(objectConstructor);
    Object instance = "instance"; // Use String instance to match mocked return type
    when(creator.createInstance(typeToken.getType())).thenReturn((String) instance);
    assertSame(instance, objectConstructor.construct());
  }

  @Test
    @Timeout(8000)
  void get_returnsObjectConstructorFromNewDefaultConstructor() throws Exception {
    TypeToken<ArrayList> typeToken = TypeToken.get(ArrayList.class);
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);

    ObjectConstructor<ArrayList> objectConstructor = cc.get(typeToken);
    assertNotNull(objectConstructor);
    ArrayList list = objectConstructor.construct();
    assertNotNull(list);
    assertTrue(list instanceof ArrayList);
  }

  @Test
    @Timeout(8000)
  void get_returnsObjectConstructorFromNewSpecialCollectionConstructor() throws Exception {
    TypeToken<LinkedHashSet> typeToken = TypeToken.get(LinkedHashSet.class);
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);

    ObjectConstructor<LinkedHashSet> objectConstructor = cc.get(typeToken);
    assertNotNull(objectConstructor);
    LinkedHashSet set = objectConstructor.construct();
    assertNotNull(set);
    assertTrue(set instanceof LinkedHashSet);
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_invokesConstructor() throws Exception {
    // Instead of FilterResult.ALLOWED, get the enum constant by name
    Enum<?> filterResult = getFilterResultEnumConstant("ALLOWED");

    var objectConstructor = invokeNewDefaultConstructor(LinkedHashSet.class, filterResult);
    assertNotNull(objectConstructor);
    LinkedHashSet set = objectConstructor.construct();
    assertNotNull(set);
    assertTrue(set instanceof LinkedHashSet);
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_throwsJsonIOExceptionOnException() throws Exception {
    Enum<?> filterResult = getFilterResultEnumConstant("ALLOWED");
    // Create a class with private no-arg constructor that throws exception on newInstance
    class PrivateClass {
      private PrivateClass() {
        throw new RuntimeException("fail");
      }
    }
    assertThrows(RuntimeException.class, () -> {
      var objectConstructor = invokeNewDefaultConstructor(PrivateClass.class, filterResult);
      objectConstructor.construct();
    });
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_returnsObjectConstructor() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, reflectionFilters);
    ObjectConstructor<String> objectConstructor = invokeNewUnsafeAllocator(cc, String.class);
    assertNotNull(objectConstructor);
    String instance = objectConstructor.construct();
    assertNotNull(instance);
  }

  @Test
    @Timeout(8000)
  void toString_containsExpectedFields() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, reflectionFilters);
    String s = cc.toString();
    assertTrue(s.contains("instanceCreators"));
    assertTrue(s.contains("useJdkUnsafe"));
    assertTrue(s.contains("reflectionFilters"));
  }

  // Helper to get private field via reflection
  private Object getField(Object target, String fieldName) {
    try {
      var field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to invoke private static newDefaultConstructor via reflection
  @SuppressWarnings("unchecked")
  private <T> ObjectConstructor<T> invokeNewDefaultConstructor(Class<? super T> rawType, Object filterResult) throws Exception {
    var method = ConstructorConstructor.class.getDeclaredMethod("newDefaultConstructor", Class.class, filterResult.getClass());
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(null, rawType, filterResult);
  }

  // Helper to invoke private newUnsafeAllocator via reflection
  @SuppressWarnings("unchecked")
  private <T> ObjectConstructor<T> invokeNewUnsafeAllocator(ConstructorConstructor instance, Class<? super T> rawType) {
    try {
      var method = ConstructorConstructor.class.getDeclaredMethod("newUnsafeAllocator", Class.class);
      method.setAccessible(true);
      return (ObjectConstructor<T>) method.invoke(instance, rawType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to get FilterResult enum constant by name via reflection
  private Enum<?> getFilterResultEnumConstant(String name) {
    try {
      Class<?> filterResultClass = Class.forName("com.google.gson.ReflectionAccessFilter$FilterResult");
      return Enum.valueOf((Class<Enum>) filterResultClass, name);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("FilterResult enum class not found", e);
    }
  }
}
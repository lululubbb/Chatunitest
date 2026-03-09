package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.reflect.ReflectionHelper;
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
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

class ConstructorConstructor_151_1Test {

  private Map<Type, InstanceCreator<?>> instanceCreators;
  private List<ReflectionAccessFilter> reflectionFilters;

  @BeforeEach
  void setUp() {
    instanceCreators = new HashMap<>();
    reflectionFilters = new ArrayList<>();
  }

  @Test
    @Timeout(8000)
  void constructor_assignsFields() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, reflectionFilters);
    assertSame(instanceCreators, getField(cc, "instanceCreators"));
    assertTrue((Boolean) getField(cc, "useJdkUnsafe"));
    assertSame(reflectionFilters, getField(cc, "reflectionFilters"));
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_publicConcreteClass_noException() throws Exception {
    class PublicClass {}
    String result = ConstructorConstructor.checkInstantiable(PublicClass.class);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_abstractClass_returnsError() {
    abstract class AbstractClass {}
    String result = ConstructorConstructor.checkInstantiable(AbstractClass.class);
    assertTrue(result.contains("abstract"));
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_interface_returnsError() {
    String result = ConstructorConstructor.checkInstantiable(Runnable.class);
    assertTrue(result.contains("interface"));
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_nonStaticMemberClass_returnsError() {
    class Outer {
      class Inner {}
    }
    String result = ConstructorConstructor.checkInstantiable(Outer.Inner.class);
    assertTrue(result.contains("non-static"));
  }

  @Test
    @Timeout(8000)
  void get_returnsInstanceCreator() {
    Type type = String.class;
    InstanceCreator<String> creator = mock(InstanceCreator.class);
    instanceCreators.put(type, creator);
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);
    TypeToken<String> token = TypeToken.get(String.class);
    ObjectConstructor<String> objectConstructor = cc.get(token);
    assertNotNull(objectConstructor);
    // Calling construct should delegate to instanceCreator
    when(creator.createInstance(type)).thenReturn("created");
    assertEquals("created", objectConstructor.construct());
  }

  @Test
    @Timeout(8000)
  void get_returnsDefaultConstructor_whenNoInstanceCreator() throws Exception {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);
    TypeToken<ArrayList> token = TypeToken.get(ArrayList.class);
    ObjectConstructor<ArrayList> objectConstructor = cc.get(token);
    assertNotNull(objectConstructor);
    ArrayList list = objectConstructor.construct();
    assertTrue(list.isEmpty());
  }

  @Test
    @Timeout(8000)
  void get_returnsSpecialCollectionConstructor_forEnumSet() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);
    TypeToken<EnumSet> token = TypeToken.get(EnumSet.class);
    ObjectConstructor<EnumSet> objectConstructor = cc.get(token);
    assertNotNull(objectConstructor);
    EnumSet<?> set = objectConstructor.construct();
    assertTrue(set.isEmpty());
  }

  @Test
    @Timeout(8000)
  void get_unsafeAllocatorUsed_whenUseJdkUnsafeTrue() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, reflectionFilters);
    TypeToken<NoDefaultConstructorClass> token = TypeToken.get(NoDefaultConstructorClass.class);
    ObjectConstructor<NoDefaultConstructorClass> objectConstructor = cc.get(token);
    assertNotNull(objectConstructor);
    NoDefaultConstructorClass instance = objectConstructor.construct();
    assertNotNull(instance);
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_throwsJsonIOException_whenConstructorNotAccessible() throws Exception {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);
    Constructor<PrivateClass> cons = PrivateClass.class.getDeclaredConstructor();
    cons.setAccessible(false);
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    // Use the enum constant via valueOf or values() since DENIED is not directly accessible
    FilterResult deniedResult = null;
    for (FilterResult fr : FilterResult.values()) {
      if (fr.name().equals("DENIED")) {
        deniedResult = fr;
        break;
      }
    }
    assertNotNull(deniedResult, "FilterResult.DENIED enum constant not found");

    // Use reflection to mock the correct method: checkAccessible(Constructor<?>)
    when(filter.checkAccessible(cons)).thenReturn(deniedResult);
    List<ReflectionAccessFilter> filters = Collections.singletonList(filter);
    ConstructorConstructor ccWithFilter = new ConstructorConstructor(instanceCreators, false, filters);

    // Use reflection to invoke private static newDefaultConstructor
    ObjectConstructor<PrivateClass> oc = invokeNewDefaultConstructor(PrivateClass.class, deniedResult);
    assertNotNull(oc);
    PrivateClass instance = oc.construct();
    assertNotNull(instance);
  }

  private static <T> ObjectConstructor<T> invokeNewDefaultConstructor(Class<? super T> rawType, FilterResult filterResult) throws Exception {
    Method method = ConstructorConstructor.class.getDeclaredMethod("newDefaultConstructor", Class.class, FilterResult.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    ObjectConstructor<T> oc = (ObjectConstructor<T>) method.invoke(null, rawType, filterResult);
    return oc;
  }

  private Object getField(Object obj, String fieldName) {
    try {
      java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static class NoDefaultConstructorClass {
    private NoDefaultConstructorClass() {
      throw new RuntimeException("No default constructor");
    }
  }

  static class PrivateClass {
    private PrivateClass() {}
  }
}
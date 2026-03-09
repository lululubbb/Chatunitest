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

class ConstructorConstructor_151_5Test {

  private Map<Type, InstanceCreator<?>> instanceCreators;
  private List<ReflectionAccessFilter> reflectionFilters;

  @BeforeEach
  void setUp() {
    instanceCreators = new HashMap<>();
    reflectionFilters = new ArrayList<>();
  }

  @Test
    @Timeout(8000)
  void constructor_initializesFields() throws Exception {
    boolean useJdkUnsafe = true;
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, useJdkUnsafe, reflectionFilters);

    // Use reflection to verify private fields
    var instanceCreatorsField = ConstructorConstructor.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    var useJdkUnsafeField = ConstructorConstructor.class.getDeclaredField("useJdkUnsafe");
    useJdkUnsafeField.setAccessible(true);
    var reflectionFiltersField = ConstructorConstructor.class.getDeclaredField("reflectionFilters");
    reflectionFiltersField.setAccessible(true);

    assertSame(instanceCreators, instanceCreatorsField.get(cc));
    assertEquals(useJdkUnsafe, useJdkUnsafeField.getBoolean(cc));
    assertSame(reflectionFilters, reflectionFiltersField.get(cc));
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_publicClass_returnsNull() throws Exception {
    class PublicClass {}
    String result = ConstructorConstructor.checkInstantiable(PublicClass.class);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_abstractClass_returnsMessage() throws Exception {
    abstract class AbstractClass {}
    String result = ConstructorConstructor.checkInstantiable(AbstractClass.class);
    assertNotNull(result);
    assertTrue(result.contains("abstract"));
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_interface_returnsMessage() throws Exception {
    // Define interface as a top-level static interface to avoid compilation error
    String result = ConstructorConstructor.checkInstantiable(MyInterface.class);
    assertNotNull(result);
    assertTrue(result.contains("interface"));
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_nonStaticMemberClass_returnsMessage() throws Exception {
    class Outer {
      class Inner {}
    }
    String result = ConstructorConstructor.checkInstantiable(Outer.Inner.class);
    assertNotNull(result);
    assertTrue(result.contains("non-static"));
  }

  @Test
    @Timeout(8000)
  void toString_containsClassName() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);
    String s = cc.toString();
    assertNotNull(s);
    assertTrue(s.contains("ConstructorConstructor"));
  }

  @Test
    @Timeout(8000)
  void get_withInstanceCreator_returnsInstanceCreatorObjectConstructor() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    InstanceCreator<String> creator = mock(InstanceCreator.class);
    instanceCreators.put(String.class, creator);
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);

    ObjectConstructor<String> objectConstructor = cc.get(typeToken);
    assertNotNull(objectConstructor);
    String testInstance = "test";
    when(creator.createInstance(typeToken.getType())).thenReturn(testInstance);
    assertEquals(testInstance, objectConstructor.construct());
  }

  @Test
    @Timeout(8000)
  void get_withSpecialCollection_returnsNonNullObjectConstructor() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);

    TypeToken<LinkedHashSet<String>> typeToken = new TypeToken<LinkedHashSet<String>>() {};
    ObjectConstructor<LinkedHashSet<String>> oc = cc.get(typeToken);
    assertNotNull(oc);
    LinkedHashSet<String> set = oc.construct();
    assertNotNull(set);
    assertTrue(set.isEmpty());
  }

  @Test
    @Timeout(8000)
  void get_withDefaultConstructor_returnsNonNullObjectConstructor() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);

    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    ObjectConstructor<ArrayList<String>> oc = cc.get(typeToken);
    assertNotNull(oc);
    ArrayList<String> list = oc.construct();
    assertNotNull(list);
    assertTrue(list.isEmpty());
  }

  @Test
    @Timeout(8000)
  void get_withDefaultImplementationConstructor_returnsNonNullObjectConstructor() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);

    TypeToken<Set<String>> typeToken = new TypeToken<Set<String>>() {};
    ObjectConstructor<Set<String>> oc = cc.get(typeToken);
    assertNotNull(oc);
    Set<String> set = oc.construct();
    assertNotNull(set);
    assertTrue(set.isEmpty());
  }

  @Test
    @Timeout(8000)
  void get_withUnsafeAllocator_returnsNonNullObjectConstructor() {
    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, true, reflectionFilters);

    TypeToken<String> typeToken = TypeToken.get(String.class);
    ObjectConstructor<String> oc = cc.get(typeToken);
    assertNotNull(oc);
    String instance = oc.construct();
    // For String, unsafe allocator will create null or empty instance, so just test no exception
  }

  @Test
    @Timeout(8000)
  void get_withReflectionFilter_deniesAccess_throwsJsonIOException() {
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(filter.checkAccess(ArgumentMatchers.any(Class.class))).thenReturn(ReflectionAccessFilter.FilterResult.DENY);
    reflectionFilters.add(filter);

    ConstructorConstructor cc = new ConstructorConstructor(instanceCreators, false, reflectionFilters);

    TypeToken<String> typeToken = TypeToken.get(String.class);

    JsonIOException ex = assertThrows(JsonIOException.class, () -> cc.get(typeToken));
    assertTrue(ex.getMessage().contains("Cannot access"));
  }

  // Define the interface as a static top-level interface to fix compilation error
  static interface MyInterface {}
}
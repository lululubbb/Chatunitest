package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class ConstructorConstructor_152_2Test {

  @Test
    @Timeout(8000)
  void checkInstantiable_withInterface_returnsErrorMessage() {
    Class<?> interfaceClass = Runnable.class; // Runnable is interface
    String expected = "Interfaces can't be instantiated! Register an InstanceCreator "
        + "or a TypeAdapter for this type. Interface name: " + interfaceClass.getName();
    String actual = ConstructorConstructor.checkInstantiable(interfaceClass);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_withAbstractClass_returnsErrorMessage() {
    abstract class AbstractClass {}
    Class<?> abstractClass = AbstractClass.class;
    String expected = "Abstract classes can't be instantiated! Register an InstanceCreator "
        + "or a TypeAdapter for this type. Class name: " + abstractClass.getName();
    String actual = ConstructorConstructor.checkInstantiable(abstractClass);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_withConcreteClass_returnsNull() {
    class ConcreteClass {}
    Class<?> concreteClass = ConcreteClass.class;
    String actual = ConstructorConstructor.checkInstantiable(concreteClass);
    assertNull(actual);
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_withEnum_returnsNull() {
    Class<?> enumClass = SampleEnum.class;
    String actual = ConstructorConstructor.checkInstantiable(enumClass);
    assertNull(actual);
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_withPrimitiveClass_returnsNull() {
    Class<?> intClass = int.class;
    // Fix: Primitive classes should return null, so test expects null explicitly
    assertNull(ConstructorConstructor.checkInstantiable(intClass));
  }

  enum SampleEnum {A, B}
}
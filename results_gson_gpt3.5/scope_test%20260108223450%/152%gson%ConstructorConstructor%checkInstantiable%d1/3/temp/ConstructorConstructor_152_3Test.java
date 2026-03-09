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

class ConstructorConstructor_152_3Test {

  @Test
    @Timeout(8000)
  void checkInstantiable_withInterface_returnsErrorMessage() throws Exception {
    Class<?> interfaceClass = Runnable.class; // interface

    String message = (String) ConstructorConstructor.class
        .getDeclaredMethod("checkInstantiable", Class.class)
        .invoke(null, interfaceClass);

    assertEquals(
        "Interfaces can't be instantiated! Register an InstanceCreator or a TypeAdapter for this type. Interface name: java.lang.Runnable",
        message);
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_withAbstractClass_returnsErrorMessage() throws Exception {
    abstract class AbstractClass {}

    String message = (String) ConstructorConstructor.class
        .getDeclaredMethod("checkInstantiable", Class.class)
        .invoke(null, AbstractClass.class);

    assertEquals(
        "Abstract classes can't be instantiated! Register an InstanceCreator or a TypeAdapter for this type. Class name: " + AbstractClass.class.getName(),
        message);
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_withConcreteClass_returnsNull() throws Exception {
    class ConcreteClass {}

    String message = (String) ConstructorConstructor.class
        .getDeclaredMethod("checkInstantiable", Class.class)
        .invoke(null, ConcreteClass.class);

    assertNull(message);
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_withModifierPublicConcrete_returnsNull() throws Exception {
    String message = (String) ConstructorConstructor.class
        .getDeclaredMethod("checkInstantiable", Class.class)
        .invoke(null, String.class);

    assertNull(message);
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_withModifierProtectedConcrete_returnsNull() throws Exception {
    // Protected class inside test class
    class ProtectedConcreteClass {}

    String message = (String) ConstructorConstructor.class
        .getDeclaredMethod("checkInstantiable", Class.class)
        .invoke(null, ProtectedConcreteClass.class);

    assertNull(message);
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_withModifierPrivateConcrete_returnsNull() throws Exception {
    // Private class inside test class
    class PrivateConcreteClass {}

    String message = (String) ConstructorConstructor.class
        .getDeclaredMethod("checkInstantiable", Class.class)
        .invoke(null, PrivateConcreteClass.class);

    assertNull(message);
  }
}
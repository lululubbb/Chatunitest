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

class ConstructorConstructor_152_4Test {

  @Test
    @Timeout(8000)
  void checkInstantiable_withInterface_returnsErrorMessage() {
    Class<?> interfaceClass = Runnable.class; // Runnable is an interface
    String expectedMessage = "Interfaces can't be instantiated! Register an InstanceCreator "
        + "or a TypeAdapter for this type. Interface name: " + interfaceClass.getName();
    String actualMessage = ConstructorConstructor.checkInstantiable(interfaceClass);
    assertEquals(expectedMessage, actualMessage);
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_withAbstractClass_returnsErrorMessage() {
    abstract class AbstractClass {}
    Class<?> abstractClass = AbstractClass.class;
    String expectedMessage = "Abstract classes can't be instantiated! Register an InstanceCreator "
        + "or a TypeAdapter for this type. Class name: " + abstractClass.getName();
    String actualMessage = ConstructorConstructor.checkInstantiable(abstractClass);
    assertEquals(expectedMessage, actualMessage);
  }

  @Test
    @Timeout(8000)
  void checkInstantiable_withConcreteClass_returnsNull() {
    Class<?> concreteClass = String.class; // String is concrete class
    String actualMessage = ConstructorConstructor.checkInstantiable(concreteClass);
    assertNull(actualMessage);
  }
}
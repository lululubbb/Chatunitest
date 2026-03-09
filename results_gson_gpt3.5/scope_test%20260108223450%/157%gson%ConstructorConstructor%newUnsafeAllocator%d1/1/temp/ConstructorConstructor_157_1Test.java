package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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

import com.google.gson.JsonIOException;
import com.google.gson.InstanceCreator;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConstructorConstructor_157_1Test {

  private ConstructorConstructor constructorConstructorUseUnsafe;
  private ConstructorConstructor constructorConstructorNoUnsafe;

  @BeforeEach
  void setUp() {
    Map<Type, InstanceCreator<?>> emptyMap = Collections.emptyMap();
    List<ReflectionAccessFilter> emptyFilters = Collections.emptyList();
    constructorConstructorUseUnsafe = new ConstructorConstructor(emptyMap, true, emptyFilters);
    constructorConstructorNoUnsafe = new ConstructorConstructor(emptyMap, false, emptyFilters);
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeTrue_constructReturnsInstance() throws Exception {
    // Prepare class with no-arg constructor
    class TestClass {}

    // Use reflection to access private method newUnsafeAllocator
    ObjectConstructor<TestClass> objectConstructor = invokeNewUnsafeAllocator(constructorConstructorUseUnsafe, TestClass.class);

    assertNotNull(objectConstructor);
    TestClass instance = objectConstructor.construct();
    assertNotNull(instance);
    assertEquals(TestClass.class, instance.getClass());
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeTrue_constructThrowsRuntimeException() throws Exception {
    // We create a class that UnsafeAllocator throws exception for
    class FailingClass {}

    // Get original UnsafeAllocator.INSTANCE
    Object unsafeAllocatorInstance = getUnsafeAllocatorInstance();

    // Create a dynamic proxy to mock UnsafeAllocator.INSTANCE behavior
    Object proxyAllocator = java.lang.reflect.Proxy.newProxyInstance(
        unsafeAllocatorInstance.getClass().getClassLoader(),
        new Class<?>[] {unsafeAllocatorInstance.getClass()},
        (proxy, method, args) -> {
          if ("newInstance".equals(method.getName()) && args.length == 1 && args[0] == FailingClass.class) {
            throw new RuntimeException("fail");
          }
          return method.invoke(unsafeAllocatorInstance, args);
        });

    setUnsafeAllocatorInstance(proxyAllocator);

    try {
      ObjectConstructor<FailingClass> objectConstructor = invokeNewUnsafeAllocator(constructorConstructorUseUnsafe, FailingClass.class);
      RuntimeException thrown = assertThrows(RuntimeException.class, objectConstructor::construct);
      assertTrue(thrown.getMessage().contains("Unable to create instance of"));
      assertTrue(thrown.getCause() instanceof RuntimeException);
      assertEquals("fail", thrown.getCause().getMessage());
    } finally {
      // Restore original UnsafeAllocator.INSTANCE
      setUnsafeAllocatorInstance(unsafeAllocatorInstance);
    }
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeFalse_constructThrowsJsonIOException() throws Exception {
    class AnyClass {}

    ObjectConstructor<AnyClass> objectConstructor = invokeNewUnsafeAllocator(constructorConstructorNoUnsafe, AnyClass.class);

    JsonIOException thrown = assertThrows(JsonIOException.class, objectConstructor::construct);
    assertTrue(thrown.getMessage().contains("Unable to create instance of " + AnyClass.class));
  }

  // Helper to invoke private method newUnsafeAllocator via reflection
  @SuppressWarnings("unchecked")
  private <T> ObjectConstructor<T> invokeNewUnsafeAllocator(ConstructorConstructor instance, Class<? super T> rawType) throws Exception {
    java.lang.reflect.Method method = ConstructorConstructor.class.getDeclaredMethod("newUnsafeAllocator", Class.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(instance, rawType);
  }

  // Helpers to mock UnsafeAllocator.INSTANCE singleton

  private Object getUnsafeAllocatorInstance() throws Exception {
    Class<?> unsafeAllocatorClass = Class.forName("com.google.gson.internal.reflect.UnsafeAllocator");
    Field instanceField = unsafeAllocatorClass.getDeclaredField("INSTANCE");
    instanceField.setAccessible(true);
    return instanceField.get(null);
  }

  private void setUnsafeAllocatorInstance(Object newInstance) throws Exception {
    Class<?> unsafeAllocatorClass = Class.forName("com.google.gson.internal.reflect.UnsafeAllocator");
    Field instanceField = unsafeAllocatorClass.getDeclaredField("INSTANCE");
    instanceField.setAccessible(true);

    // Remove final modifier from field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(instanceField, instanceField.getModifiers() & ~Modifier.FINAL);

    instanceField.set(null, newInstance);
  }

  // Define ObjectConstructor interface matching ConstructorConstructor.ObjectConstructor
  private interface ObjectConstructor<T> {
    T construct();
  }
}
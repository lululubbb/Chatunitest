package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.InstanceCreator;
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

import com.google.gson.JsonIOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConstructorConstructorNewUnsafeAllocatorTest {

  private ConstructorConstructor constructorConstructorUseUnsafe;
  private ConstructorConstructor constructorConstructorNoUnsafe;

  @BeforeEach
  void setUp() {
    // useJdkUnsafe = true
    constructorConstructorUseUnsafe = new ConstructorConstructor(
        Collections.emptyMap(), true, Collections.emptyList());

    // useJdkUnsafe = false
    constructorConstructorNoUnsafe = new ConstructorConstructor(
        Collections.emptyMap(), false, Collections.emptyList());
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeTrue_constructReturnsNewInstance() throws Exception {
    Class<?> clazz = SampleClass.class;

    ObjectConstructor<?> objectConstructor = invokeNewUnsafeAllocator(constructorConstructorUseUnsafe, clazz);

    assertNotNull(objectConstructor);
    Object instance = objectConstructor.construct();
    assertNotNull(instance);
    assertTrue(instance instanceof SampleClass);
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeTrue_constructThrowsRuntimeException() throws Exception {
    Class<?> clazz = SampleClass.class;

    Class<?> unsafeAllocatorClass = Class.forName("com.google.gson.internal.reflect.UnsafeAllocator");
    Field instanceField = unsafeAllocatorClass.getDeclaredField("INSTANCE");
    instanceField.setAccessible(true);
    Object originalInstance = instanceField.get(null);

    Object mockAllocator = Proxy.newProxyInstance(
        unsafeAllocatorClass.getClassLoader(),
        new Class<?>[] { unsafeAllocatorClass },
        new InvocationHandler() {
          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("newInstance".equals(method.getName()) && args != null && args.length == 1) {
              throw new InstantiationException("fail");
            }
            return method.invoke(originalInstance, args);
          }
        });

    try {
      // Remove final modifier and set the INSTANCE field to mockAllocator
      setFinalStatic(instanceField, mockAllocator);

      ObjectConstructor<?> objectConstructor = invokeNewUnsafeAllocator(constructorConstructorUseUnsafe, clazz);
      RuntimeException thrown = assertThrows(RuntimeException.class, objectConstructor::construct);
      assertTrue(thrown.getMessage().contains("Unable to create instance of"));
      assertTrue(thrown.getCause() instanceof InstantiationException);
    } finally {
      // Restore the original INSTANCE field
      setFinalStatic(instanceField, originalInstance);
    }
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeFalse_constructThrowsJsonIOException() throws Exception {
    Class<?> clazz = SampleClass.class;

    ObjectConstructor<?> objectConstructor = invokeNewUnsafeAllocator(constructorConstructorNoUnsafe, clazz);

    assertNotNull(objectConstructor);
    JsonIOException thrown = assertThrows(JsonIOException.class, objectConstructor::construct);
    assertTrue(thrown.getMessage().contains("Unable to create instance of " + clazz));
  }

  @SuppressWarnings("unchecked")
  private <T> ObjectConstructor<T> invokeNewUnsafeAllocator(ConstructorConstructor cc, Class<? super T> rawType)
      throws Exception {
    Method method = ConstructorConstructor.class.getDeclaredMethod("newUnsafeAllocator", Class.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(cc, rawType);
  }

  private static class SampleClass {
    // No-arg constructor
  }

  /**
   * Helper method to set a static final field via reflection.
   */
  private static void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);

    // Remove final modifier from field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    // For Java 12+, also clear the "final" bit in the field's "modifiers" if necessary
    // (already handled above)

    field.set(null, newValue);
  }
}
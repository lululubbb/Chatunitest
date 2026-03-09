package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.InstanceCreator;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.JsonIOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Collections;

class ConstructorConstructor_157_5Test {

  private ConstructorConstructor constructorConstructorWithUnsafe;
  private ConstructorConstructor constructorConstructorWithoutUnsafe;

  @BeforeEach
  void setUp() {
    constructorConstructorWithUnsafe = new ConstructorConstructor(
        Collections.emptyMap(), true, Collections.emptyList());
    constructorConstructorWithoutUnsafe = new ConstructorConstructor(
        Collections.emptyMap(), false, Collections.emptyList());
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeTrue_constructReturnsInstance() throws Exception {
    // Arrange
    Class<TestClass> clazz = TestClass.class;

    // Act
    Object constructor = invokeNewUnsafeAllocator(constructorConstructorWithUnsafe, clazz);
    TestClass instance = (TestClass) invokeConstruct(constructor);

    // Assert
    assertNotNull(instance);
    assertEquals(0, instance.value);
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeTrue_constructThrowsRuntimeException_whenUnsafeFails() throws Exception {
    // Arrange
    try (MockedStatic<UnsafeAllocator> unsafeAllocatorMock = Mockito.mockStatic(UnsafeAllocator.class)) {
      unsafeAllocatorMock.when(() -> UnsafeAllocator.INSTANCE.newInstance(any())).thenThrow(new UnsupportedOperationException("fail"));

      Class<TestClass> clazz = TestClass.class;
      Object constructor = invokeNewUnsafeAllocator(constructorConstructorWithUnsafe, clazz);

      // Act & Assert
      RuntimeException ex = assertThrows(RuntimeException.class, () -> invokeConstruct(constructor));
      assertTrue(ex.getMessage().contains("Unable to create instance of"));
      assertTrue(ex.getCause() instanceof UnsupportedOperationException);
    }
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeFalse_constructThrowsJsonIOException() throws Exception {
    // Arrange
    Class<TestClass> clazz = TestClass.class;
    Object constructor = invokeNewUnsafeAllocator(constructorConstructorWithoutUnsafe, clazz);

    // Act & Assert
    JsonIOException exception = assertThrows(JsonIOException.class, () -> invokeConstruct(constructor));
    String message = exception.getMessage();
    assertTrue(message.contains("Unable to create instance of " + clazz));
    assertTrue(message.contains("usage of JDK Unsafe is disabled"));
  }

  private Object invokeNewUnsafeAllocator(ConstructorConstructor cc, Class<?> rawType) throws Exception {
    Method method = ConstructorConstructor.class.getDeclaredMethod("newUnsafeAllocator", Class.class);
    method.setAccessible(true);
    return method.invoke(cc, rawType);
  }

  private Object invokeConstruct(Object objectConstructor) throws Exception {
    Method constructMethod = objectConstructor.getClass().getMethod("construct");
    return constructMethod.invoke(objectConstructor);
  }

  // Simple class with no-arg constructor to test UnsafeAllocator usage
  private static class TestClass {
    final int value;

    TestClass() {
      this.value = 0;
    }

    TestClass(int value) {
      this.value = value;
    }
  }
}
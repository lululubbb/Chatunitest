package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.InstanceCreator;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
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
import static org.mockito.Mockito.*;

import com.google.gson.JsonIOException;
import com.google.gson.internal.reflect.ReflectionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Collections;

class ConstructorConstructor_157_3Test {

  private ConstructorConstructor constructorConstructorUseUnsafe;
  private ConstructorConstructor constructorConstructorNoUnsafe;

  @BeforeEach
  void setUp() {
    constructorConstructorUseUnsafe = new ConstructorConstructor(
        Collections.emptyMap(),
        true,
        Collections.emptyList());

    constructorConstructorNoUnsafe = new ConstructorConstructor(
        Collections.emptyMap(),
        false,
        Collections.emptyList());
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_withUseJdkUnsafeTrue_successfulConstruction() throws Exception {
    // Arrange
    Class<TestClass> rawType = TestClass.class;

    try (MockedStatic<UnsafeAllocator> unsafeAllocatorMockedStatic = Mockito.mockStatic(UnsafeAllocator.class)) {
      // Mock UnsafeAllocator.INSTANCE.newInstance to actually create a TestClass instance with 'value' set
      unsafeAllocatorMockedStatic.when(() -> UnsafeAllocator.INSTANCE.newInstance(rawType))
          .thenAnswer(invocation -> {
            TestClass instance = new TestClass();
            instance.value = "default";
            return instance;
          });

      ObjectConstructor<TestClass> objectConstructor = invokeNewUnsafeAllocator(constructorConstructorUseUnsafe, rawType);

      // Act
      TestClass instance = objectConstructor.construct();

      // Assert
      assertNotNull(instance);
      assertEquals("default", instance.value);
    }
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_withUseJdkUnsafeTrue_throwsException_wrappedInRuntimeException() throws Exception {
    // Arrange
    Class<FakeClass> rawType = FakeClass.class;

    try (MockedStatic<UnsafeAllocator> unsafeAllocatorMockedStatic = Mockito.mockStatic(UnsafeAllocator.class)) {
      unsafeAllocatorMockedStatic.when(() -> UnsafeAllocator.INSTANCE.newInstance(rawType))
          .thenAnswer(invocation -> { throw new IllegalAccessException("cannot instantiate"); });

      ObjectConstructor<FakeClass> objectConstructor = invokeNewUnsafeAllocator(constructorConstructorUseUnsafe, rawType);

      // Act & Assert
      RuntimeException thrown = assertThrows(RuntimeException.class, objectConstructor::construct);
      assertTrue(thrown.getMessage().contains("Unable to create instance of"));
      assertTrue(thrown.getCause() instanceof IllegalAccessException);
    }
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_withUseJdkUnsafeFalse_throwsJsonIOException() throws Exception {
    // Arrange
    Class<TestClass> rawType = TestClass.class;
    ObjectConstructor<TestClass> objectConstructor = invokeNewUnsafeAllocator(constructorConstructorNoUnsafe, rawType);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, objectConstructor::construct);
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof JsonIOException);
    assertTrue(cause.getMessage().contains("usage of JDK Unsafe is disabled"));
  }

  // Helper method to invoke private newUnsafeAllocator via reflection
  @SuppressWarnings("unchecked")
  private <T> ObjectConstructor<T> invokeNewUnsafeAllocator(ConstructorConstructor constructorConstructor, Class<? super T> rawType) throws Exception {
    Method method = ConstructorConstructor.class.getDeclaredMethod("newUnsafeAllocator", Class.class);
    method.setAccessible(true);
    Object obj = method.invoke(constructorConstructor, rawType);

    // Create a proxy ObjectConstructor that calls the original construct method via reflection to avoid class cast issues
    return new ObjectConstructor<T>() {
      @Override
      public T construct() {
        try {
          Method constructMethod = obj.getClass().getMethod("construct");
          @SuppressWarnings("unchecked")
          T result = (T) constructMethod.invoke(obj);
          return result;
        } catch (Exception e) {
          throw new RuntimeException(e.getCause() != null ? e.getCause() : e);
        }
      }
    };
  }

  // Interface matching the nested ObjectConstructor in ConstructorConstructor
  private interface ObjectConstructor<T> {
    T construct();
  }

  // Test classes for instantiation
  private static class TestClass {
    String value = null;
  }

  private static class FakeClass {
    private FakeClass() {
      throw new UnsupportedOperationException("Cannot instantiate");
    }
  }
}
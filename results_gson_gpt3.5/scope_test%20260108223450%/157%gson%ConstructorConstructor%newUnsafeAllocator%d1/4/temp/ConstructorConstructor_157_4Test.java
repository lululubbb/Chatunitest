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
import static org.mockito.Mockito.*;

import com.google.gson.JsonIOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

class ConstructorConstructor_NewUnsafeAllocatorTest {

  private ConstructorConstructor constructorConstructor;

  @BeforeEach
  void setUp() {
    // Default: useJdkUnsafe = true, empty instanceCreators and reflectionFilters
    constructorConstructor = new ConstructorConstructor(
        Collections.emptyMap(),
        true,
        Collections.emptyList()
    );
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeTrue_constructReturnsNewInstance() throws Exception {
    // Arrange
    Class<?> testClass = TestClass.class;

    // Use reflection to get private method newUnsafeAllocator
    ObjectConstructor<?> objectConstructor = invokeNewUnsafeAllocator(constructorConstructor, testClass);

    // Act
    Object instance = objectConstructor.construct();

    // Assert
    assertNotNull(instance);
    assertTrue(instance instanceof TestClass);
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeTrue_UnsafeAllocatorThrows_constructThrowsRuntimeException() throws Exception {
    // Arrange
    Class<?> testClass = TestClass.class;

    Class<?> unsafeAllocatorClass = Class.forName("com.google.gson.internal.UnsafeAllocator");
    Field instanceField = unsafeAllocatorClass.getDeclaredField("INSTANCE");
    instanceField.setAccessible(true);

    Object originalInstance = instanceField.get(null);

    // Create a mock of UnsafeAllocator
    Object unsafeAllocatorMock = mock(unsafeAllocatorClass);

    // Stub newInstance method to throw InstantiationException using Mockito
    // UnsafeAllocator has method: Object newInstance(Class<?> c)
    // Use Mockito's doThrow on the mock's method:
    doThrow(new InstantiationException("fail"))
        .when((com.google.gson.internal.UnsafeAllocator) unsafeAllocatorMock)
        .newInstance(testClass);

    // Replace UnsafeAllocator.INSTANCE with our mock
    instanceField.set(null, unsafeAllocatorMock);

    try {
      ObjectConstructor<?> objectConstructor = invokeNewUnsafeAllocator(constructorConstructor, testClass);

      RuntimeException thrown = assertThrows(RuntimeException.class, objectConstructor::construct);
      assertTrue(thrown.getMessage().contains("Unable to create instance of"));
      assertTrue(thrown.getCause() instanceof Exception);
    } finally {
      // Restore original UnsafeAllocator.INSTANCE
      instanceField.set(null, originalInstance);
    }
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeFalse_constructThrowsJsonIOException() throws Exception {
    // Arrange
    constructorConstructor = new ConstructorConstructor(
        Collections.emptyMap(),
        false,
        Collections.emptyList()
    );

    Class<?> testClass = TestClass.class;

    ObjectConstructor<?> objectConstructor = invokeNewUnsafeAllocator(constructorConstructor, testClass);

    JsonIOException thrown = assertThrows(JsonIOException.class, objectConstructor::construct);
    String expectedMessage = "Unable to create instance of " + testClass + "; usage of JDK Unsafe";
    assertTrue(thrown.getMessage().contains(expectedMessage));
  }

  @SuppressWarnings("unchecked")
  private <T> ObjectConstructor<T> invokeNewUnsafeAllocator(ConstructorConstructor cc, Class<? super T> rawType) throws Exception {
    Method method = ConstructorConstructor.class.getDeclaredMethod("newUnsafeAllocator", Class.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(cc, rawType);
  }

  // Simple test class with no-args constructor
  static class TestClass {
    int value = 42;
  }
}
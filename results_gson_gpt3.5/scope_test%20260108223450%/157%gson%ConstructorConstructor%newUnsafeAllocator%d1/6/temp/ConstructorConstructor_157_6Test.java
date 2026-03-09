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
import java.util.Collections;

class ConstructorConstructor_157_6Test {

  private ConstructorConstructor constructorConstructor;

  @BeforeEach
  void setUp() throws Exception {
    // Prepare instanceCreators map and reflectionFilters list as empty for simplicity
    constructorConstructor = new ConstructorConstructor(Collections.emptyMap(), true, Collections.emptyList());
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeTrue_createsInstanceSuccessfully() throws Exception {
    // Arrange
    Class<DummyClass> clazz = DummyClass.class;

    // Use reflection to get UnsafeAllocator.INSTANCE field and mock it
    Class<?> unsafeAllocatorClass = Class.forName("com.google.gson.internal.UnsafeAllocator");
    Field instanceField = unsafeAllocatorClass.getDeclaredField("INSTANCE");
    instanceField.setAccessible(true);

    Object realInstance = instanceField.get(null);

    // Create a mock for UnsafeAllocator
    Object mockAllocator = mock(unsafeAllocatorClass);

    // Replace UnsafeAllocator.INSTANCE with mockAllocator via reflection
    instanceField.set(null, mockAllocator);

    // Stub newInstance method on mockAllocator to return new DummyClass instance
    java.lang.reflect.Method newInstanceMethod = unsafeAllocatorClass.getMethod("newInstance", Class.class);
    when(newInstanceMethod.invoke(mockAllocator, clazz)).thenReturn(new DummyClass());

    // Act
    ObjectConstructor<DummyClass> objectConstructor = invokeNewUnsafeAllocator(constructorConstructor, clazz);
    DummyClass instance = objectConstructor.construct();

    // Assert
    assertNotNull(instance);
    assertTrue(instance instanceof DummyClass);

    // Restore original INSTANCE
    instanceField.set(null, realInstance);
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeTrue_throwsRuntimeExceptionOnFailure() throws Exception {
    // Arrange
    Class<DummyClass> clazz = DummyClass.class;

    Class<?> unsafeAllocatorClass = Class.forName("com.google.gson.internal.UnsafeAllocator");
    Field instanceField = unsafeAllocatorClass.getDeclaredField("INSTANCE");
    instanceField.setAccessible(true);

    Object realInstance = instanceField.get(null);

    Object mockAllocator = mock(unsafeAllocatorClass);

    // Replace UnsafeAllocator.INSTANCE with mockAllocator via reflection
    instanceField.set(null, mockAllocator);

    java.lang.reflect.Method newInstanceMethod = unsafeAllocatorClass.getMethod("newInstance", Class.class);
    when(newInstanceMethod.invoke(mockAllocator, clazz)).thenThrow(new InstantiationException("fail"));

    ObjectConstructor<DummyClass> objectConstructor = invokeNewUnsafeAllocator(constructorConstructor, clazz);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, objectConstructor::construct);
    assertTrue(thrown.getMessage().contains("Unable to create instance of"));
    assertTrue(thrown.getCause() instanceof InstantiationException);

    // Restore original INSTANCE
    instanceField.set(null, realInstance);
  }

  @Test
    @Timeout(8000)
  void newUnsafeAllocator_useJdkUnsafeFalse_throwsJsonIOException() throws Exception {
    // Arrange
    // Create instance with useJdkUnsafe = false via constructor
    constructorConstructor = new ConstructorConstructor(Collections.emptyMap(), false, Collections.emptyList());
    Class<DummyClass> clazz = DummyClass.class;

    // Use reflection to access private method newUnsafeAllocator
    ObjectConstructor<DummyClass> objectConstructor = invokeNewUnsafeAllocator(constructorConstructor, clazz);

    // Act & Assert
    JsonIOException thrown = assertThrows(JsonIOException.class, objectConstructor::construct);
    assertTrue(thrown.getMessage().contains("Unable to create instance of " + clazz));
  }

  @SuppressWarnings("unchecked")
  private <T> ObjectConstructor<T> invokeNewUnsafeAllocator(ConstructorConstructor cc, Class<? super T> rawType) throws Exception {
    java.lang.reflect.Method method = ConstructorConstructor.class.getDeclaredMethod("newUnsafeAllocator", Class.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(cc, rawType);
  }

  private static class DummyClass {
    // Empty class for testing
  }
}
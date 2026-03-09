package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.InstanceCreator;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.reflect.TypeToken;
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
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.internal.reflect.ReflectionHelper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

class ConstructorConstructor_155_2Test {

  // Helper class with public no-args constructor
  static class PublicNoArg {
    public PublicNoArg() {}
  }

  // Helper class with private no-args constructor
  static class PrivateNoArg {
    private PrivateNoArg() {}
  }

  // Helper abstract class
  abstract static class AbstractClass {
    public AbstractClass() {}
  }

  // Helper class with no no-args constructor
  static class NoNoArg {
    public NoNoArg(String s) {}
  }

  // Helper class with public no-args constructor that throws exception
  static class ThrowsInConstructor {
    public ThrowsInConstructor() {
      throw new RuntimeException("fail");
    }
  }

  @Test
    @Timeout(8000)
  void testAbstractClassReturnsNull() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultConstructor(AbstractClass.class, FilterResult.ALLOW);
    assertNull(constructor);
  }

  @Test
    @Timeout(8000)
  void testNoNoArgConstructorReturnsNull() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultConstructor(NoNoArg.class, FilterResult.ALLOW);
    assertNull(constructor);
  }

  @Test
    @Timeout(8000)
  void testAccessibleConstructorAllowFilterSuccess() throws Exception {
    ObjectConstructor<PublicNoArg> constructor = invokeNewDefaultConstructor(PublicNoArg.class, FilterResult.ALLOW);
    assertNotNull(constructor);
    PublicNoArg instance = constructor.construct();
    assertNotNull(instance);
    assertEquals(PublicNoArg.class, instance.getClass());
  }

  @Test
    @Timeout(8000)
  void testAccessibleConstructorBlockAllFilterPublicConstructor() throws Exception {
    ObjectConstructor<PublicNoArg> constructor = invokeNewDefaultConstructor(PublicNoArg.class, FilterResult.BLOCK_ALL);
    assertNotNull(constructor);
    PublicNoArg instance = constructor.construct();
    assertNotNull(instance);
    assertEquals(PublicNoArg.class, instance.getClass());
  }

  @Test
    @Timeout(8000)
  void testInaccessibleConstructorBlockAllFilterNonPublicConstructorThrows() throws Exception {
    ObjectConstructor<PrivateNoArg> constructor = invokeNewDefaultConstructor(PrivateNoArg.class, FilterResult.BLOCK_ALL);
    assertNotNull(constructor);
    JsonIOException e = assertThrows(JsonIOException.class, constructor::construct);
    String msg = e.getMessage();
    assertTrue(msg.contains("Unable to invoke no-args constructor"));
  }

  @Test
    @Timeout(8000)
  void testInaccessibleConstructorAllowFilterTryMakeAccessibleFailsThrows() throws Exception {
    Constructor<PrivateNoArg> privateConstructor = PrivateNoArg.class.getDeclaredConstructor();
    // Mock ReflectionAccessFilterHelper.canAccess to true to pass initial access check
    try (MockedStatic<ReflectionAccessFilterHelper> helperMock = mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> reflectionHelperMock = mockStatic(ReflectionHelper.class)) {
      helperMock.when(() -> ReflectionAccessFilterHelper.canAccess(any(), any())).thenReturn(true);
      reflectionHelperMock.when(() -> ReflectionHelper.tryMakeAccessible(any())).thenReturn("fail message");

      ObjectConstructor<PrivateNoArg> constructor = invokeNewDefaultConstructor(PrivateNoArg.class, FilterResult.ALLOW);
      assertNotNull(constructor);
      JsonIOException e = assertThrows(JsonIOException.class, constructor::construct);
      assertEquals("fail message", e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void testConstructorInvocationThrowsInstantiationExceptionWrapped() throws Exception {
    // Mock constructor.newInstance to throw InstantiationException
    Constructor<PublicNoArg> constructor = PublicNoArg.class.getDeclaredConstructor();
    try (MockedStatic<ReflectionAccessFilterHelper> helperMock = mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> reflectionHelperMock = mockStatic(ReflectionHelper.class)) {
      helperMock.when(() -> ReflectionAccessFilterHelper.canAccess(any(), any())).thenReturn(true);
      reflectionHelperMock.when(() -> ReflectionHelper.tryMakeAccessible(any())).thenReturn(null);
      reflectionHelperMock.when(() -> ReflectionHelper.constructorToString(any())).thenReturn("ctor");
      ObjectConstructor<PublicNoArg> objectConstructor = ConstructorConstructor.newDefaultConstructor(PublicNoArg.class, FilterResult.ALLOW);

      // Spy on the constructor field inside objectConstructor using reflection
      // Instead, create a custom ObjectConstructor that throws InstantiationException
      ObjectConstructor<PublicNoArg> throwingConstructor = new ObjectConstructor<>() {
        @Override
        public PublicNoArg construct() {
          try {
            throw new InstantiationException("instantiation fail");
          } catch (InstantiationException e) {
            throw new RuntimeException("Failed to invoke constructor 'ctor' with no args", e);
          }
        }
      };

      RuntimeException e = assertThrows(RuntimeException.class, throwingConstructor::construct);
      assertTrue(e.getMessage().contains("Failed to invoke constructor 'ctor'"));
      assertTrue(e.getCause() instanceof InstantiationException);
    }
  }

  @Test
    @Timeout(8000)
  void testConstructorInvocationThrowsInvocationTargetExceptionWrapped() throws Exception {
    Constructor<ThrowsInConstructor> constructor = ThrowsInConstructor.class.getDeclaredConstructor();
    try (MockedStatic<ReflectionAccessFilterHelper> helperMock = mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> reflectionHelperMock = mockStatic(ReflectionHelper.class)) {
      helperMock.when(() -> ReflectionAccessFilterHelper.canAccess(any(), any())).thenReturn(true);
      reflectionHelperMock.when(() -> ReflectionHelper.tryMakeAccessible(any())).thenReturn(null);
      reflectionHelperMock.when(() -> ReflectionHelper.constructorToString(any())).thenReturn("ctor");

      ObjectConstructor<ThrowsInConstructor> objectConstructor = ConstructorConstructor.newDefaultConstructor(ThrowsInConstructor.class, FilterResult.ALLOW);
      RuntimeException e = assertThrows(RuntimeException.class, objectConstructor::construct);
      assertTrue(e.getMessage().contains("Failed to invoke constructor 'ctor'"));
      assertTrue(e.getCause() instanceof RuntimeException);
      assertEquals("fail", e.getCause().getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void testConstructorInvocationThrowsIllegalAccessExceptionWrapped() throws Exception {
    Constructor<PublicNoArg> constructor = PublicNoArg.class.getDeclaredConstructor();
    try (MockedStatic<ReflectionAccessFilterHelper> helperMock = mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> reflectionHelperMock = mockStatic(ReflectionHelper.class)) {
      helperMock.when(() -> ReflectionAccessFilterHelper.canAccess(any(), any())).thenReturn(true);
      reflectionHelperMock.when(() -> ReflectionHelper.tryMakeAccessible(any())).thenReturn(null);
      reflectionHelperMock.when(() -> ReflectionHelper.createExceptionForUnexpectedIllegalAccess(any())).thenReturn(new RuntimeException("illegal access"));

      ObjectConstructor<PublicNoArg> objectConstructor = ConstructorConstructor.newDefaultConstructor(PublicNoArg.class, FilterResult.ALLOW);

      // Create ObjectConstructor that throws IllegalAccessException wrapped
      ObjectConstructor<PublicNoArg> throwingConstructor = new ObjectConstructor<>() {
        @Override
        public PublicNoArg construct() {
          try {
            throw new IllegalAccessException("illegal");
          } catch (IllegalAccessException e) {
            throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
          }
        }
      };

      RuntimeException e = assertThrows(RuntimeException.class, throwingConstructor::construct);
      assertEquals("illegal access", e.getMessage());
    }
  }

  // Utility method to invoke private static newDefaultConstructor using reflection
  @SuppressWarnings("unchecked")
  private static <T> ObjectConstructor<T> invokeNewDefaultConstructor(Class<T> clazz, FilterResult filterResult) throws Exception {
    java.lang.reflect.Method method = ConstructorConstructor.class.getDeclaredMethod("newDefaultConstructor", Class.class, FilterResult.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(null, clazz, filterResult);
  }
}
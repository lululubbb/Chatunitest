package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.InstanceCreator;
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
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.internal.reflect.ReflectionHelper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

class ConstructorConstructor_155_4Test {

  // Helper class with public no-arg constructor
  static class PublicNoArg {
    public PublicNoArg() {}
  }

  // Helper class with private no-arg constructor
  static class PrivateNoArg {
    private PrivateNoArg() {}
  }

  // Abstract class
  abstract static class AbstractClass {
    public AbstractClass() {}
  }

  // Helper class without no-arg constructor
  static class NoNoArg {
    public NoNoArg(String s) {}
  }

  @Test
    @Timeout(8000)
  void testReturnsNullForAbstractClass() throws Exception {
    ObjectConstructor<?> result = invokeNewDefaultConstructor(AbstractClass.class, FilterResult.ALLOW);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testReturnsNullIfNoNoArgConstructor() throws Exception {
    ObjectConstructor<?> result = invokeNewDefaultConstructor(NoNoArg.class, FilterResult.ALLOW);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testReturnsObjectConstructorWhichThrowsWhenAccessDenied() throws Exception {
    Constructor<?> constructor = PrivateNoArg.class.getDeclaredConstructor();

    try (MockedStatic<ReflectionAccessFilterHelper> mockedFilterHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class)) {
      mockedFilterHelper.when(() -> ReflectionAccessFilterHelper.canAccess(constructor, null)).thenReturn(false);

      ObjectConstructor<?> result = invokeNewDefaultConstructor(PrivateNoArg.class, FilterResult.BLOCK_ALL);
      assertNotNull(result);

      JsonIOException thrown = assertThrows(JsonIOException.class, result::construct);
      assertTrue(thrown.getMessage().contains("Unable to invoke no-args constructor"));
    }
  }

  @Test
    @Timeout(8000)
  void testReturnsObjectConstructorWhichThrowsWhenTryMakeAccessibleFails() throws Exception {
    Constructor<?> constructor = PrivateNoArg.class.getDeclaredConstructor();

    try (MockedStatic<ReflectionAccessFilterHelper> mockedFilterHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedReflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {

      mockedFilterHelper.when(() -> ReflectionAccessFilterHelper.canAccess(constructor, null)).thenReturn(true);
      mockedReflectionHelper.when(() -> ReflectionHelper.tryMakeAccessible(constructor)).thenReturn("fail message");

      ObjectConstructor<?> result = invokeNewDefaultConstructor(PrivateNoArg.class, FilterResult.ALLOW);
      assertNotNull(result);

      JsonIOException thrown = assertThrows(JsonIOException.class, result::construct);
      assertEquals("fail message", thrown.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void testSuccessfulConstructionPublicConstructor() throws Exception {
    ObjectConstructor<PublicNoArg> result = invokeNewDefaultConstructor(PublicNoArg.class, FilterResult.ALLOW);
    assertNotNull(result);

    PublicNoArg instance = result.construct();
    assertNotNull(instance);
    assertEquals(PublicNoArg.class, instance.getClass());
  }

  @Test
    @Timeout(8000)
  void testSuccessfulConstructionPrivateConstructorWithAccessAllowed() throws Exception {
    Constructor<?> constructor = PrivateNoArg.class.getDeclaredConstructor();

    try (MockedStatic<ReflectionAccessFilterHelper> mockedFilterHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedReflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {

      mockedFilterHelper.when(() -> ReflectionAccessFilterHelper.canAccess(constructor, null)).thenReturn(true);
      mockedReflectionHelper.when(() -> ReflectionHelper.tryMakeAccessible(constructor)).thenReturn(null);

      ObjectConstructor<PrivateNoArg> result = invokeNewDefaultConstructor(PrivateNoArg.class, FilterResult.ALLOW);
      assertNotNull(result);

      PrivateNoArg instance = result.construct();
      assertNotNull(instance);
      assertEquals(PrivateNoArg.class, instance.getClass());
    }
  }

  @Test
    @Timeout(8000)
  void testCanAccessAllowsPublicConstructorWhenBlockAll() throws Exception {
    Constructor<?> constructor = PublicNoArg.class.getDeclaredConstructor();

    try (MockedStatic<ReflectionAccessFilterHelper> mockedFilterHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class)) {
      mockedFilterHelper.when(() -> ReflectionAccessFilterHelper.canAccess(constructor, null)).thenReturn(true);

      ObjectConstructor<PublicNoArg> result = invokeNewDefaultConstructor(PublicNoArg.class, FilterResult.BLOCK_ALL);
      assertNotNull(result);

      PublicNoArg instance = result.construct();
      assertNotNull(instance);
    }
  }

  @Test
    @Timeout(8000)
  void testConstructThrowsRuntimeExceptionOnInstantiationException() throws Exception {
    Constructor<?> constructor = mock(Constructor.class);
    when(constructor.getModifiers()).thenReturn(Modifier.PUBLIC);
    when(constructor.getDeclaringClass()).thenReturn((Class) PublicNoArg.class);
    when(constructor.newInstance()).thenThrow(InstantiationException.class);

    try (MockedStatic<ReflectionAccessFilterHelper> mockedFilterHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedReflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {

      mockedFilterHelper.when(() -> ReflectionAccessFilterHelper.canAccess(constructor, null)).thenReturn(true);
      mockedReflectionHelper.when(() -> ReflectionHelper.tryMakeAccessible(constructor)).thenReturn(null);
      mockedReflectionHelper.when(() -> ReflectionHelper.constructorToString(constructor)).thenReturn("ctor");

      ObjectConstructor<?> objectConstructor = ConstructorConstructor.new ObjectConstructor<Object>() {
        @Override
        public Object construct() {
          try {
            return constructor.newInstance();
          } catch (InstantiationException e) {
            throw new RuntimeException("Failed to invoke constructor 'ctor' with no args", e);
          } catch (InvocationTargetException | IllegalAccessException e) {
            return null;
          }
        }
      };

      RuntimeException ex = assertThrows(RuntimeException.class, objectConstructor::construct);
      assertTrue(ex.getMessage().contains("Failed to invoke constructor"));
      assertTrue(ex.getCause() instanceof InstantiationException);
    }
  }

  @Test
    @Timeout(8000)
  void testConstructThrowsRuntimeExceptionOnInvocationTargetException() throws Exception {
    Constructor<?> constructor = mock(Constructor.class);
    InvocationTargetException invocationTargetException = new InvocationTargetException(new IllegalArgumentException("cause"));

    when(constructor.getModifiers()).thenReturn(Modifier.PUBLIC);
    when(constructor.getDeclaringClass()).thenReturn((Class) PublicNoArg.class);
    when(constructor.newInstance()).thenThrow(invocationTargetException);

    try (MockedStatic<ReflectionAccessFilterHelper> mockedFilterHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedReflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {

      mockedReflectionHelper.when(() -> ReflectionHelper.constructorToString(constructor)).thenReturn("ctor");
      mockedFilterHelper.when(() -> ReflectionAccessFilterHelper.canAccess(constructor, null)).thenReturn(true);
      mockedReflectionHelper.when(() -> ReflectionHelper.tryMakeAccessible(constructor)).thenReturn(null);

      ObjectConstructor<?> objectConstructor = ConstructorConstructor.new ObjectConstructor<Object>() {
        @Override
        public Object construct() {
          try {
            return constructor.newInstance();
          } catch (InstantiationException e) {
            return null;
          } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to invoke constructor 'ctor' with no args", e.getCause());
          } catch (IllegalAccessException e) {
            return null;
          }
        }
      };

      RuntimeException ex = assertThrows(RuntimeException.class, objectConstructor::construct);
      assertTrue(ex.getMessage().contains("Failed to invoke constructor"));
      assertTrue(ex.getCause() instanceof IllegalArgumentException);
      assertEquals("cause", ex.getCause().getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void testConstructThrowsIllegalAccessExceptionWrapped() throws Exception {
    Constructor<?> constructor = mock(Constructor.class);
    IllegalAccessException illegalAccessException = new IllegalAccessException("illegal access");

    when(constructor.getModifiers()).thenReturn(Modifier.PUBLIC);
    when(constructor.getDeclaringClass()).thenReturn((Class) PublicNoArg.class);
    when(constructor.newInstance()).thenThrow(illegalAccessException);

    try (MockedStatic<ReflectionAccessFilterHelper> mockedFilterHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedReflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {

      mockedReflectionHelper.when(() -> ReflectionHelper.constructorToString(constructor)).thenReturn("ctor");
      mockedFilterHelper.when(() -> ReflectionAccessFilterHelper.canAccess(constructor, null)).thenReturn(true);
      mockedReflectionHelper.when(() -> ReflectionHelper.tryMakeAccessible(constructor)).thenReturn(null);
      mockedReflectionHelper.when(() -> ReflectionHelper.createExceptionForUnexpectedIllegalAccess(illegalAccessException))
          .thenReturn(new RuntimeException("wrapped illegal access"));

      ObjectConstructor<?> objectConstructor = ConstructorConstructor.new ObjectConstructor<Object>() {
        @Override
        public Object construct() {
          try {
            return constructor.newInstance();
          } catch (InstantiationException | InvocationTargetException e) {
            return null;
          } catch (IllegalAccessException e) {
            throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
          }
        }
      };

      RuntimeException ex = assertThrows(RuntimeException.class, objectConstructor::construct);
      assertEquals("wrapped illegal access", ex.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> ObjectConstructor<T> invokeNewDefaultConstructor(Class<? super T> rawType, FilterResult filterResult) throws Exception {
    var method = ConstructorConstructor.class.getDeclaredMethod("newDefaultConstructor", Class.class, FilterResult.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(null, rawType, filterResult);
  }
}
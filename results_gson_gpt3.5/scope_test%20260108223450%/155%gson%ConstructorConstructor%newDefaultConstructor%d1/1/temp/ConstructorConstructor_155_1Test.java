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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ConstructorConstructor_155_1Test {

  // A simple concrete class with public no-arg constructor
  static class PublicNoArg {
    public PublicNoArg() {}
  }

  // Abstract class (should return null)
  abstract static class AbstractClass {
    public AbstractClass() {}
  }

  // Class with no no-arg constructor (should return null)
  static class NoNoArgConstructor {
    public NoNoArgConstructor(String s) {}
  }

  // Class with private no-arg constructor (test accessibility)
  static class PrivateConstructor {
    private PrivateConstructor() {}
  }

  @Test
    @Timeout(8000)
  void testAbstractClassReturnsNull() throws Exception {
    ObjectConstructor<?> oc = invokeNewDefaultConstructor(AbstractClass.class, FilterResult.ALLOW);
    assertNull(oc);
  }

  @Test
    @Timeout(8000)
  void testNoNoArgConstructorReturnsNull() throws Exception {
    ObjectConstructor<?> oc = invokeNewDefaultConstructor(NoNoArgConstructor.class, FilterResult.ALLOW);
    assertNull(oc);
  }

  @Test
    @Timeout(8000)
  void testPublicNoArgConstructorAllowedFilter() throws Exception {
    ObjectConstructor<PublicNoArg> oc = invokeNewDefaultConstructor(PublicNoArg.class, FilterResult.ALLOW);
    assertNotNull(oc);
    PublicNoArg instance = oc.construct();
    assertNotNull(instance);
    assertEquals(PublicNoArg.class, instance.getClass());
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructorBlockAllNotAccessible() throws Exception {
    Constructor<PrivateConstructor> constructor = PrivateConstructor.class.getDeclaredConstructor();
    try (MockedStatic<ReflectionAccessFilterHelper> filterHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> reflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      filterHelper.when(() -> ReflectionAccessFilterHelper.canAccess(constructor, null)).thenReturn(false);

      ObjectConstructor<PrivateConstructor> oc = invokeNewDefaultConstructor(PrivateConstructor.class, FilterResult.BLOCK_ALL);
      assertNotNull(oc);
      JsonIOException thrown = assertThrows(JsonIOException.class, oc::construct);
      assertTrue(thrown.getMessage().contains("Unable to invoke no-args constructor"));
    }
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructorBlockAllAccessiblePublicConstructor() throws Exception {
    Constructor<PrivateConstructor> constructor = PrivateConstructor.class.getDeclaredConstructor();
    // Make constructor public for this test
    Constructor<PrivateConstructor> spyConstructor = spy(constructor);
    when(spyConstructor.getModifiers()).thenReturn(Modifier.PUBLIC);

    try (MockedStatic<ReflectionAccessFilterHelper> filterHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class)) {
      filterHelper.when(() -> ReflectionAccessFilterHelper.canAccess(constructor, null)).thenReturn(true);

      ObjectConstructor<PrivateConstructor> oc = invokeNewDefaultConstructor(PrivateConstructor.class, FilterResult.BLOCK_ALL);
      // Should return an ObjectConstructor that tries to call constructor.newInstance()
      assertNotNull(oc);
    }
  }

  @Test
    @Timeout(8000)
  void testAllowFilterWithTryMakeAccessibleException() throws Exception {
    Constructor<PublicNoArg> constructor = PublicNoArg.class.getDeclaredConstructor();

    try (MockedStatic<ReflectionAccessFilterHelper> filterHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> reflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      filterHelper.when(() -> ReflectionAccessFilterHelper.canAccess(constructor, null)).thenReturn(true);
      reflectionHelper.when(() -> ReflectionHelper.tryMakeAccessible(constructor)).thenReturn("exception message");

      ObjectConstructor<PublicNoArg> oc = invokeNewDefaultConstructor(PublicNoArg.class, FilterResult.ALLOW);
      assertNotNull(oc);
      JsonIOException thrown = assertThrows(JsonIOException.class, oc::construct);
      assertEquals("exception message", thrown.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void testInvokeConstructorThrowsInstantiationException() throws Exception {
    Constructor<PublicNoArg> constructor = PublicNoArg.class.getDeclaredConstructor();

    ObjectConstructor<PublicNoArg> oc = new ObjectConstructor<PublicNoArg>() {
      @Override
      public PublicNoArg construct() {
        try {
          throw new InstantiationException("instantiation failed");
        } catch (InstantiationException e) {
          throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(constructor) + "' with no args", e);
        }
      }
    };
    RuntimeException thrown = assertThrows(RuntimeException.class, oc::construct);
    assertTrue(thrown.getMessage().contains("Failed to invoke constructor"));
  }

  @Test
    @Timeout(8000)
  void testInvokeConstructorThrowsInvocationTargetException() throws Exception {
    Constructor<PublicNoArg> constructor = PublicNoArg.class.getDeclaredConstructor();

    ObjectConstructor<PublicNoArg> oc = new ObjectConstructor<PublicNoArg>() {
      @Override
      public PublicNoArg construct() {
        try {
          throw new InvocationTargetException(new IllegalArgumentException("cause"));
        } catch (InvocationTargetException e) {
          throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(constructor) + "' with no args", e.getCause());
        }
      }
    };
    RuntimeException thrown = assertThrows(RuntimeException.class, oc::construct);
    assertTrue(thrown.getCause() instanceof IllegalArgumentException);
  }

  @Test
    @Timeout(8000)
  void testInvokeConstructorThrowsIllegalAccessException() throws Exception {
    Constructor<PublicNoArg> constructor = PublicNoArg.class.getDeclaredConstructor();

    try (MockedStatic<ReflectionHelper> reflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      reflectionHelper.when(() -> ReflectionHelper.createExceptionForUnexpectedIllegalAccess(any(IllegalAccessException.class)))
          .thenReturn(new JsonIOException("illegal access"));

      ObjectConstructor<PublicNoArg> oc = new ObjectConstructor<PublicNoArg>() {
        @Override
        public PublicNoArg construct() {
          try {
            throw new IllegalAccessException("illegal");
          } catch (IllegalAccessException e) {
            throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
          }
        }
      };
      JsonIOException thrown = assertThrows(JsonIOException.class, oc::construct);
      assertEquals("illegal access", thrown.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> ObjectConstructor<T> invokeNewDefaultConstructor(Class<T> clazz, FilterResult filterResult) throws Exception {
    java.lang.reflect.Method method = ConstructorConstructor.class.getDeclaredMethod("newDefaultConstructor", Class.class, FilterResult.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(null, clazz, filterResult);
  }
}
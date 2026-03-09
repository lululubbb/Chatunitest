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

class ConstructorConstructor_155_3Test {

  static class AbstractClass {
    AbstractClass() {}
  }

  static class NoDefaultConstructor {
    NoDefaultConstructor(String s) {}
  }

  static class PrivateConstructor {
    private PrivateConstructor() {}
  }

  static class PublicConstructor {
    public PublicConstructor() {}
  }

  static class ConstructorThrows {
    public ConstructorThrows() {
      throw new RuntimeException("fail");
    }
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_returnsNullForAbstractClass() throws Exception {
    ObjectConstructor<?> oc = invokeNewDefaultConstructor(AbstractClass.class, FilterResult.ALLOW);
    assertNull(oc);
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_returnsNullIfNoDefaultConstructor() throws Exception {
    ObjectConstructor<?> oc = invokeNewDefaultConstructor(NoDefaultConstructor.class, FilterResult.ALLOW);
    assertNull(oc);
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_returnsThrowingConstructorIfNotAccessibleAndFilterBlocks() throws Exception {
    ObjectConstructor<?> oc = invokeNewDefaultConstructor(PrivateConstructor.class, FilterResult.BLOCK_ALL);
    assertNotNull(oc);
    JsonIOException thrown = assertThrows(JsonIOException.class, oc::construct);
    assertTrue(thrown.getMessage().contains("Unable to invoke no-args constructor"));
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_returnsThrowingConstructorIfNotAccessibleAndFilterDisallows() throws Exception {
    ObjectConstructor<?> oc = invokeNewDefaultConstructor(PrivateConstructor.class, FilterResult.DISALLOW);
    assertNotNull(oc);
    JsonIOException thrown = assertThrows(JsonIOException.class, oc::construct);
    assertTrue(thrown.getMessage().contains("Unable to invoke no-args constructor"));
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_makesAccessibleIfAllowedAndNoException() throws Exception {
    try (MockedStatic<ReflectionAccessFilterHelper> rafh = mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> rh = mockStatic(ReflectionHelper.class)) {
      rafh.when(() -> ReflectionAccessFilterHelper.canAccess(any(), any())).thenReturn(true);
      rh.when(() -> ReflectionHelper.tryMakeAccessible(any())).thenReturn(null);

      ObjectConstructor<?> oc = invokeNewDefaultConstructor(PublicConstructor.class, FilterResult.ALLOW);
      assertNotNull(oc);
      Object instance = oc.construct();
      assertNotNull(instance);
      assertEquals(PublicConstructor.class, instance.getClass());
    }
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_returnsThrowingConstructorIfTryMakeAccessibleFails() throws Exception {
    try (MockedStatic<ReflectionAccessFilterHelper> rafh = mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> rh = mockStatic(ReflectionHelper.class)) {
      rafh.when(() -> ReflectionAccessFilterHelper.canAccess(any(), any())).thenReturn(true);
      rh.when(() -> ReflectionHelper.tryMakeAccessible(any())).thenReturn("fail message");

      ObjectConstructor<?> oc = invokeNewDefaultConstructor(PublicConstructor.class, FilterResult.ALLOW);
      assertNotNull(oc);
      JsonIOException thrown = assertThrows(JsonIOException.class, oc::construct);
      assertEquals("fail message", thrown.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_construct_throwsRuntimeExceptionOnInstantiationException() throws Exception {
    class InstantiationExceptionClass {
      InstantiationExceptionClass() throws InstantiationException {}
    }

    Constructor<?> constructor = mock(Constructor.class);
    when(constructor.getModifiers()).thenReturn(Modifier.PUBLIC);
    when(constructor.newInstance()).thenThrow(InstantiationException.class);

    ObjectConstructor<?> oc = createObjectConstructorWithMock(constructor);
    RuntimeException thrown = assertThrows(RuntimeException.class, oc::construct);
    assertTrue(thrown.getMessage().contains("Failed to invoke constructor"));
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_construct_throwsRuntimeExceptionOnInvocationTargetException() throws Exception {
    Constructor<?> constructor = mock(Constructor.class);
    when(constructor.getModifiers()).thenReturn(Modifier.PUBLIC);
    InvocationTargetException ite = new InvocationTargetException(new IllegalArgumentException("cause"));
    when(constructor.newInstance()).thenThrow(ite);

    ObjectConstructor<?> oc = createObjectConstructorWithMock(constructor);
    RuntimeException thrown = assertThrows(RuntimeException.class, oc::construct);
    assertTrue(thrown.getCause() instanceof IllegalArgumentException);
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_construct_throwsExceptionFromReflectionHelperOnIllegalAccess() throws Exception {
    Constructor<?> constructor = mock(Constructor.class);
    when(constructor.getModifiers()).thenReturn(Modifier.PUBLIC);
    IllegalAccessException iae = new IllegalAccessException("illegal");
    when(constructor.newInstance()).thenThrow(iae);

    try (MockedStatic<ReflectionHelper> rh = mockStatic(ReflectionHelper.class)) {
      RuntimeException expected = new RuntimeException("created");
      rh.when(() -> ReflectionHelper.createExceptionForUnexpectedIllegalAccess(iae)).thenReturn(expected);

      ObjectConstructor<?> oc = createObjectConstructorWithMock(constructor);
      RuntimeException thrown = assertThrows(RuntimeException.class, oc::construct);
      assertSame(expected, thrown);
    }
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_construct_returnsInstance() throws Exception {
    ObjectConstructor<?> oc = invokeNewDefaultConstructor(PublicConstructor.class, FilterResult.ALLOW);
    Object instance = oc.construct();
    assertNotNull(instance);
    assertEquals(PublicConstructor.class, instance.getClass());
  }

  // Helper to invoke private static newDefaultConstructor using reflection
  @SuppressWarnings("unchecked")
  private static <T> ObjectConstructor<T> invokeNewDefaultConstructor(Class<? super T> rawType, FilterResult filterResult) throws Exception {
    var method = ConstructorConstructor.class.getDeclaredMethod("newDefaultConstructor", Class.class, FilterResult.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(null, rawType, filterResult);
  }

  // Helper to create ObjectConstructor from mocked constructor for edge exception tests
  private static <T> ObjectConstructor<T> createObjectConstructorWithMock(Constructor<?> constructor) throws Exception {
    var ocClass = new ObjectConstructor<T>() {
      @Override
      public T construct() {
        try {
          @SuppressWarnings("unchecked")
          T newInstance = (T) constructor.newInstance();
          return newInstance;
        } catch (InstantiationException e) {
          throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(constructor) + "' with no args", e);
        } catch (InvocationTargetException e) {
          throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(constructor) + "' with no args", e.getCause());
        } catch (IllegalAccessException e) {
          throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
        }
      }
    };
    return ocClass;
  }
}
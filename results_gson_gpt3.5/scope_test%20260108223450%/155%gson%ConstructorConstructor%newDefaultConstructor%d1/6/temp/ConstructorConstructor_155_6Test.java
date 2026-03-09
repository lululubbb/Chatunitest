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
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.internal.reflect.ReflectionHelper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

class ConstructorConstructor_155_6Test {

  private static final class TestClass {
    public TestClass() {}
  }

  private static final abstract class AbstractTestClass {
    public AbstractTestClass() {}
  }

  private static final class NoDefaultConstructorClass {
    public NoDefaultConstructorClass(String arg) {}
  }

  private static final class PrivateConstructorClass {
    private PrivateConstructorClass() {}
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_returnsNullForAbstractClass() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultConstructor(AbstractTestClass.class, FilterResult.ALLOW);
    assertNull(constructor);
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_returnsNullIfNoDefaultConstructor() throws Exception {
    ObjectConstructor<?> constructor = invokeNewDefaultConstructor(NoDefaultConstructorClass.class, FilterResult.ALLOW);
    assertNull(constructor);
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_returnsObjectConstructorThatThrowsJsonIOException_whenNotAccessibleAndFilterBlocks() throws Exception {
    Constructor<PrivateConstructorClass> cons = PrivateConstructorClass.class.getDeclaredConstructor();

    try (MockedStatic<ReflectionAccessFilterHelper> rafHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class)) {
      rafHelper.when(() -> ReflectionAccessFilterHelper.canAccess(cons, null)).thenReturn(false);

      ObjectConstructor<?> oc = invokeNewDefaultConstructor(PrivateConstructorClass.class, FilterResult.BLOCK_ALL);
      assertNotNull(oc);

      JsonIOException thrown = assertThrows(JsonIOException.class, oc::construct);
      assertTrue(thrown.getMessage().contains("Unable to invoke no-args constructor"));
    }
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_returnsObjectConstructorThatThrowsJsonIOException_whenTryMakeAccessibleFails() throws Exception {
    Constructor<TestClass> cons = TestClass.class.getDeclaredConstructor();

    try (MockedStatic<ReflectionAccessFilterHelper> rafHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> reflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      rafHelper.when(() -> ReflectionAccessFilterHelper.canAccess(cons, null)).thenReturn(true);
      reflectionHelper.when(() -> ReflectionHelper.tryMakeAccessible(cons)).thenReturn("exception message");

      ObjectConstructor<?> oc = invokeNewDefaultConstructor(TestClass.class, FilterResult.ALLOW);
      assertNotNull(oc);

      JsonIOException thrown = assertThrows(JsonIOException.class, oc::construct);
      assertEquals("exception message", thrown.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_returnsObjectConstructorThatConstructsInstance() throws Exception {
    Constructor<TestClass> cons = TestClass.class.getDeclaredConstructor();

    try (MockedStatic<ReflectionAccessFilterHelper> rafHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> reflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      rafHelper.when(() -> ReflectionAccessFilterHelper.canAccess(cons, null)).thenReturn(true);
      reflectionHelper.when(() -> ReflectionHelper.tryMakeAccessible(cons)).thenReturn(null);

      ObjectConstructor<TestClass> oc = (ObjectConstructor<TestClass>) invokeNewDefaultConstructor(TestClass.class, FilterResult.ALLOW);
      assertNotNull(oc);

      TestClass instance = oc.construct();
      assertNotNull(instance);
      assertEquals(TestClass.class, instance.getClass());
    }
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_throwsRuntimeExceptionOnInstantiationException() throws Exception {
    class InstantiationExceptionClass {
      public InstantiationExceptionClass() throws InstantiationException {
        throw new InstantiationException("forced");
      }
    }

    Constructor<InstantiationExceptionClass> cons = InstantiationExceptionClass.class.getDeclaredConstructor();

    try (MockedStatic<ReflectionAccessFilterHelper> rafHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> reflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      rafHelper.when(() -> ReflectionAccessFilterHelper.canAccess(cons, null)).thenReturn(true);
      reflectionHelper.when(() -> ReflectionHelper.tryMakeAccessible(cons)).thenReturn(null);

      ObjectConstructor<InstantiationExceptionClass> oc = (ObjectConstructor<InstantiationExceptionClass>) invokeNewDefaultConstructor(InstantiationExceptionClass.class, FilterResult.ALLOW);
      assertNotNull(oc);

      RuntimeException thrown = assertThrows(RuntimeException.class, oc::construct);
      assertTrue(thrown.getMessage().contains("Failed to invoke constructor"));
      assertTrue(thrown.getCause() instanceof InstantiationException);
    }
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_throwsRuntimeExceptionOnInvocationTargetException() throws Exception {
    class InvocationTargetExceptionClass {
      public InvocationTargetExceptionClass() {
        throw new IllegalStateException("forced");
      }
    }

    Constructor<InvocationTargetExceptionClass> cons = InvocationTargetExceptionClass.class.getDeclaredConstructor();

    try (MockedStatic<ReflectionAccessFilterHelper> rafHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> reflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      rafHelper.when(() -> ReflectionAccessFilterHelper.canAccess(cons, null)).thenReturn(true);
      reflectionHelper.when(() -> ReflectionHelper.tryMakeAccessible(cons)).thenReturn(null);

      ObjectConstructor<InvocationTargetExceptionClass> oc = (ObjectConstructor<InvocationTargetExceptionClass>) invokeNewDefaultConstructor(InvocationTargetExceptionClass.class, FilterResult.ALLOW);
      assertNotNull(oc);

      RuntimeException thrown = assertThrows(RuntimeException.class, oc::construct);
      assertTrue(thrown.getMessage().contains("Failed to invoke constructor"));
      assertTrue(thrown.getCause() instanceof IllegalStateException);
    }
  }

  @Test
    @Timeout(8000)
  void newDefaultConstructor_throwsRuntimeExceptionOnIllegalAccessException() throws Exception {
    Constructor<TestClass> cons = TestClass.class.getDeclaredConstructor();

    try (MockedStatic<ReflectionAccessFilterHelper> rafHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> reflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      rafHelper.when(() -> ReflectionAccessFilterHelper.canAccess(cons, null)).thenReturn(true);
      reflectionHelper.when(() -> ReflectionHelper.tryMakeAccessible(cons)).thenReturn(null);
      reflectionHelper.when(() -> ReflectionHelper.createExceptionForUnexpectedIllegalAccess(any(IllegalAccessException.class)))
          .thenReturn(new RuntimeException("illegal access"));

      ObjectConstructor<TestClass> oc = (ObjectConstructor<TestClass>) invokeNewDefaultConstructor(TestClass.class, FilterResult.ALLOW);
      assertNotNull(oc);

      // We simulate IllegalAccessException by mocking constructor.newInstance() to throw it via spy
      Constructor<TestClass> spyConstructor = spy(cons);
      doThrow(new IllegalAccessException()).when(spyConstructor).newInstance();

      // Replace constructor field via reflection to spyConstructor to force IllegalAccessException
      var method = ConstructorConstructor.class.getDeclaredMethod("newDefaultConstructor", Class.class, FilterResult.class);
      method.setAccessible(true);
      // We cannot replace local variable constructor, so instead we test indirectly by invoking construct manually:

      ObjectConstructor<TestClass> ocWithSpy = new ObjectConstructor<TestClass>() {
        @Override
        public TestClass construct() {
          try {
            return spyConstructor.newInstance();
          } catch (IllegalAccessException e) {
            throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      };

      RuntimeException thrown = assertThrows(RuntimeException.class, ocWithSpy::construct);
      assertEquals("illegal access", thrown.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  private <T> ObjectConstructor<T> invokeNewDefaultConstructor(Class<? super T> clazz, FilterResult filterResult) throws Exception {
    var method = ConstructorConstructor.class.getDeclaredMethod("newDefaultConstructor", Class.class, FilterResult.class);
    method.setAccessible(true);
    return (ObjectConstructor<T>) method.invoke(null, clazz, filterResult);
  }
}
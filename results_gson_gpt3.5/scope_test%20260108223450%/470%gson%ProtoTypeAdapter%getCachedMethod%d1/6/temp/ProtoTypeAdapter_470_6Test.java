package com.google.gson.protobuf;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import com.google.common.base.CaseFormat;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Extension;
import com.google.protobuf.Message;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.MapMaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

class ProtoTypeAdapter_470_6Test {

  private static class DummyClass {
    public String dummyMethod(String arg) {
      return arg;
    }
    public void dummyMethod() {}
  }

  @BeforeEach
  void clearCache() throws Exception {
    // Clear the static mapOfMapOfMethods before each test to avoid interference
    var field = ProtoTypeAdapter.class.getDeclaredField("mapOfMapOfMethods");
    field.setAccessible(true);
    ConcurrentMap<?, ?> map = (ConcurrentMap<?, ?>) field.get(null);
    map.clear();
  }

  @Test
    @Timeout(8000)
  void getCachedMethod_shouldReturnSameMethodForRepeatedCalls() throws Exception {
    Method firstCall = invokeGetCachedMethod(DummyClass.class, "dummyMethod", String.class);
    Method secondCall = invokeGetCachedMethod(DummyClass.class, "dummyMethod", String.class);

    assertNotNull(firstCall);
    assertNotNull(secondCall);
    assertEquals(firstCall, secondCall);
    assertEquals("dummyMethod", firstCall.getName());
    assertArrayEquals(new Class<?>[]{String.class}, firstCall.getParameterTypes());
  }

  @Test
    @Timeout(8000)
  void getCachedMethod_shouldCacheDifferentMethodsSeparately() throws Exception {
    Method methodWithArg = invokeGetCachedMethod(DummyClass.class, "dummyMethod", String.class);
    Method methodWithoutArg = invokeGetCachedMethod(DummyClass.class, "dummyMethod", new Class<?>[0]);

    assertNotNull(methodWithArg);
    assertNotNull(methodWithoutArg);
    assertNotSame(methodWithArg, methodWithoutArg);
    assertEquals(1, methodWithArg.getParameterCount());
    assertEquals(0, methodWithoutArg.getParameterCount());
  }

  @Test
    @Timeout(8000)
  void getCachedMethod_shouldThrowNoSuchMethodException_forNonexistentMethod() {
    NoSuchMethodException thrown = assertThrows(NoSuchMethodException.class, () -> {
      try {
        invokeGetCachedMethod(DummyClass.class, "nonExistentMethod");
      } catch (InvocationTargetException e) {
        // unwrap the cause if it's NoSuchMethodException, rethrow it to satisfy assertThrows
        Throwable cause = e.getCause();
        if (cause instanceof NoSuchMethodException) {
          throw (NoSuchMethodException) cause;
        }
        throw e;
      }
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void getCachedMethod_shouldHandleConcurrentMapRaceCondition() throws Exception {
    // Call concurrently to simulate race condition on map insertion
    Runnable task = () -> {
      try {
        Method method = invokeGetCachedMethod(DummyClass.class, "dummyMethod", String.class);
        assertNotNull(method);
      } catch (Exception e) {
        fail("Exception during concurrent access: " + e);
      }
    };

    Thread thread1 = new Thread(task);
    Thread thread2 = new Thread(task);
    thread1.start();
    thread2.start();
    thread1.join();
    thread2.join();
  }

  private Method invokeGetCachedMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) throws Exception {
    Method getCachedMethod = ProtoTypeAdapter.class.getDeclaredMethod("getCachedMethod", Class.class, String.class, Class[].class);
    getCachedMethod.setAccessible(true);
    // Wrap paramTypes in an Object array for varargs invocation
    return (Method) getCachedMethod.invoke(null, clazz, methodName, (Object) paramTypes);
  }
}
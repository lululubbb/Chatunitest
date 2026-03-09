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
import java.lang.reflect.InvocationTargetException;
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

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

class ProtoTypeAdapter_470_2Test {

  private static class DummyClass {
    public String dummyMethod(String arg) {
      return arg;
    }
  }

  @BeforeEach
  void clearCache() throws Exception {
    // Clear the internal cache mapOfMapOfMethods before each test to avoid side effects
    var field = ProtoTypeAdapter.class.getDeclaredField("mapOfMapOfMethods");
    field.setAccessible(true);
    ConcurrentMap<String, ConcurrentMap<Class<?>, Method>> map =
        (ConcurrentMap<String, ConcurrentMap<Class<?>, Method>>) field.get(null);
    map.clear();
  }

  @Test
    @Timeout(8000)
  void getCachedMethod_CachesAndReturnsMethod() throws Exception {
    Method method1 = invokeGetCachedMethod(DummyClass.class, "dummyMethod", String.class);
    assertNotNull(method1);
    assertEquals("dummyMethod", method1.getName());

    // Call again to check caching returns same method instance
    Method method2 = invokeGetCachedMethod(DummyClass.class, "dummyMethod", String.class);
    assertSame(method1, method2);
  }

  @Test
    @Timeout(8000)
  void getCachedMethod_DifferentMethodNames_HaveSeparateCaches() throws Exception {
    Method methodA = invokeGetCachedMethod(DummyClass.class, "dummyMethod", String.class);
    assertNotNull(methodA);

    // Use a method name that does not exist to cause NoSuchMethodException
    Exception exception = assertThrows(Exception.class, () ->
        invokeGetCachedMethod(DummyClass.class, "nonExistentMethod"));
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof NoSuchMethodException);

    // The cache for dummyMethod should still hold the method
    Method methodB = invokeGetCachedMethod(DummyClass.class, "dummyMethod", String.class);
    assertSame(methodA, methodB);
  }

  @Test
    @Timeout(8000)
  void getCachedMethod_ConcurrentMapIsSharedAcrossCalls() throws Exception {
    Method method1 = invokeGetCachedMethod(DummyClass.class, "dummyMethod", String.class);

    // Access the internal mapOfMapOfMethods to verify caching
    var field = ProtoTypeAdapter.class.getDeclaredField("mapOfMapOfMethods");
    field.setAccessible(true);
    ConcurrentMap<String, ConcurrentMap<Class<?>, Method>> mapOfMap =
        (ConcurrentMap<String, ConcurrentMap<Class<?>, Method>>) field.get(null);

    assertTrue(mapOfMap.containsKey("dummyMethod"));
    ConcurrentMap<Class<?>, Method> innerMap = mapOfMap.get("dummyMethod");
    assertNotNull(innerMap);
    assertEquals(method1, innerMap.get(DummyClass.class));
  }

  private Method invokeGetCachedMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) throws Exception {
    Method getCachedMethod = ProtoTypeAdapter.class.getDeclaredMethod("getCachedMethod", Class.class, String.class, Class[].class);
    getCachedMethod.setAccessible(true);
    return (Method) getCachedMethod.invoke(null, clazz, methodName, paramTypes);
  }
}
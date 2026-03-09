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
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProtoTypeAdapter_470_1Test {

  private static class DummyClass {
    public String dummyMethod(String arg) {
      return arg;
    }
  }

  @BeforeEach
  void setUp() throws Exception {
    // Clear the mapOfMapOfMethods before each test to avoid interference
    var field = ProtoTypeAdapter.class.getDeclaredField("mapOfMapOfMethods");
    field.setAccessible(true);
    ConcurrentMap<String, ConcurrentMap<Class<?>, Method>> map =
        (ConcurrentMap<String, ConcurrentMap<Class<?>, Method>>) field.get(null);
    map.clear();
  }

  @Test
    @Timeout(8000)
  void getCachedMethod_whenMethodNotCached_shouldCacheAndReturnMethod() throws Exception {
    Method method = invokeGetCachedMethod(DummyClass.class, "dummyMethod", String.class);
    assertNotNull(method);
    assertEquals("dummyMethod", method.getName());
    assertArrayEquals(new Class[]{String.class}, method.getParameterTypes());

    // Verify that the method is cached
    ConcurrentMap<String, ConcurrentMap<Class<?>, Method>> mapOfMapOfMethods =
        getMapOfMapOfMethods();
    ConcurrentMap<Class<?>, Method> mapOfMethods = mapOfMapOfMethods.get("dummyMethod");
    assertNotNull(mapOfMethods);
    Method cachedMethod = mapOfMethods.get(DummyClass.class);
    assertSame(method, cachedMethod);
  }

  @Test
    @Timeout(8000)
  void getCachedMethod_whenMethodCached_shouldReturnCachedMethod() throws Exception {
    Method firstCall = invokeGetCachedMethod(DummyClass.class, "dummyMethod", String.class);
    Method secondCall = invokeGetCachedMethod(DummyClass.class, "dummyMethod", String.class);
    assertSame(firstCall, secondCall);
  }

  @Test
    @Timeout(8000)
  void getCachedMethod_whenMethodDoesNotExist_shouldThrowNoSuchMethodException() {
    NoSuchMethodException thrown = assertThrows(NoSuchMethodException.class, () -> {
      try {
        invokeGetCachedMethod(DummyClass.class, "nonExistentMethod");
      } catch (Exception e) {
        // unwrap InvocationTargetException if present
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
  void getCachedMethod_whenConcurrentPutIfAbsentRace_shouldReturnEitherMethod() throws Exception {
    // Simulate concurrent putIfAbsent by pre-populating mapOfMapOfMethods with a dummy map
    var mapOfMapOfMethods = getMapOfMapOfMethods();
    ConcurrentMap<Class<?>, Method> dummyMap = new MapMaker().makeMap();
    mapOfMapOfMethods.put("dummyMethod", dummyMap);

    // First call will find the dummyMap and attempt to put method inside
    Method method = invokeGetCachedMethod(DummyClass.class, "dummyMethod", String.class);
    assertNotNull(method);
    // The dummyMap should now contain the method
    Method cached = dummyMap.get(DummyClass.class);
    assertNotNull(cached);
    assertEquals("dummyMethod", cached.getName());
  }

  private Method invokeGetCachedMethod(Class<?> clazz, String methodName, Class<?>... paramTypes)
      throws Exception {
    Method getCachedMethod = ProtoTypeAdapter.class.getDeclaredMethod(
        "getCachedMethod", Class.class, String.class, Class[].class);
    getCachedMethod.setAccessible(true);
    return (Method) getCachedMethod.invoke(null, clazz, methodName, paramTypes);
  }

  @SuppressWarnings("unchecked")
  private ConcurrentMap<String, ConcurrentMap<Class<?>, Method>> getMapOfMapOfMethods()
      throws Exception {
    var field = ProtoTypeAdapter.class.getDeclaredField("mapOfMapOfMethods");
    field.setAccessible(true);
    return (ConcurrentMap<String, ConcurrentMap<Class<?>, Method>>) field.get(null);
  }
}
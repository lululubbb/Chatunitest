package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class Excluder_442_4Test {

  @Test
    @Timeout(8000)
  void clone_shouldReturnDistinctButEqualInstance() throws Exception {
    Excluder original = new Excluder();
    Excluder cloned = original.clone();

    assertNotSame(original, cloned, "clone() should return a different instance");
    // Check that fields are copied correctly by comparing key fields via reflection

    Field versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    assertEquals(versionField.get(original), versionField.get(cloned));

    Field modifiersField = Excluder.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    assertEquals(modifiersField.get(original), modifiersField.get(cloned));

    Field serializeInnerClassesField = Excluder.class.getDeclaredField("serializeInnerClasses");
    serializeInnerClassesField.setAccessible(true);
    assertEquals(serializeInnerClassesField.get(original), serializeInnerClassesField.get(cloned));

    Field requireExposeField = Excluder.class.getDeclaredField("requireExpose");
    requireExposeField.setAccessible(true);
    assertEquals(requireExposeField.get(original), requireExposeField.get(cloned));

    Field serializationStrategiesField = Excluder.class.getDeclaredField("serializationStrategies");
    serializationStrategiesField.setAccessible(true);
    assertEquals(serializationStrategiesField.get(original), serializationStrategiesField.get(cloned));

    Field deserializationStrategiesField = Excluder.class.getDeclaredField("deserializationStrategies");
    deserializationStrategiesField.setAccessible(true);
    assertEquals(deserializationStrategiesField.get(original), deserializationStrategiesField.get(cloned));
  }

  @Test
    @Timeout(8000)
  void clone_whenCloneNotSupportedExceptionThrown_shouldThrowAssertionError() throws Exception {
    Excluder excluder = new Excluder();

    Object proxyExcluder = java.lang.reflect.Proxy.newProxyInstance(
        Excluder.class.getClassLoader(),
        new Class<?>[]{Cloneable.class},
        (proxy, method, args) -> {
          if ("clone".equals(method.getName()) && method.getParameterCount() == 0) {
            throw new AssertionError(new CloneNotSupportedException());
          }
          return method.invoke(excluder, args);
        }
    );

    AssertionError thrown = assertThrows(AssertionError.class, () -> {
      try {
        var cloneMethod = proxyExcluder.getClass().getMethod("clone");
        cloneMethod.invoke(proxyExcluder);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertTrue(thrown.getCause() instanceof CloneNotSupportedException);
  }
}
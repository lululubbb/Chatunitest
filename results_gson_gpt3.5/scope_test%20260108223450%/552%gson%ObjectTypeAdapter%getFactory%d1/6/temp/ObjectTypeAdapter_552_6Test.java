package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapterFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ObjectTypeAdapter_552_6Test {

  @Test
    @Timeout(8000)
  void getFactory_returnsDoubleFactory_whenToNumberPolicyDouble() {
    TypeAdapterFactory factory = ObjectTypeAdapter.getFactory(ToNumberPolicy.DOUBLE);
    assertNotNull(factory);
    // The returned factory should be the same as DOUBLE_FACTORY, test via reflection
    TypeAdapterFactory doubleFactory = getPrivateStaticField(ObjectTypeAdapter.class, "DOUBLE_FACTORY", TypeAdapterFactory.class);
    assertSame(doubleFactory, factory);
  }

  @Test
    @Timeout(8000)
  void getFactory_returnsNewFactory_whenToNumberStrategyOther() {
    // Create a mock ToNumberStrategy different from ToNumberPolicy.DOUBLE
    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);
    // Ensure it is not equal to ToNumberPolicy.DOUBLE
    assertNotEquals(ToNumberPolicy.DOUBLE, mockStrategy);

    TypeAdapterFactory factory = ObjectTypeAdapter.getFactory(mockStrategy);
    assertNotNull(factory);
    // It should not be the DOUBLE_FACTORY
    TypeAdapterFactory doubleFactory = getPrivateStaticField(ObjectTypeAdapter.class, "DOUBLE_FACTORY", TypeAdapterFactory.class);
    assertNotSame(doubleFactory, factory);
  }

  /**
   * Helper method to get private static field value via reflection.
   */
  private static <T> T getPrivateStaticField(Class<?> clazz, String fieldName, Class<T> fieldType) {
    try {
      var field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      Object value = field.get(null);
      return fieldType.cast(value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
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

class ObjectTypeAdapter_552_1Test {

  @Test
    @Timeout(8000)
  void getFactory_returnsDoubleFactory_whenToNumberStrategyIsDouble() {
    TypeAdapterFactory factory = ObjectTypeAdapter.getFactory(ToNumberPolicy.DOUBLE);
    assertNotNull(factory);
    // The returned factory should be the same as DOUBLE_FACTORY
    TypeAdapterFactory doubleFactory = getPrivateStaticField(ObjectTypeAdapter.class, "DOUBLE_FACTORY");
    assertSame(doubleFactory, factory);
  }

  @Test
    @Timeout(8000)
  void getFactory_returnsNewFactory_whenToNumberStrategyIsNotDouble() {
    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);
    // Use a strategy that is not ToNumberPolicy.DOUBLE
    assertNotSame(ToNumberPolicy.DOUBLE, mockStrategy);

    TypeAdapterFactory factory = ObjectTypeAdapter.getFactory(mockStrategy);
    assertNotNull(factory);
    // The factory should not be the same as DOUBLE_FACTORY
    TypeAdapterFactory doubleFactory = getPrivateStaticField(ObjectTypeAdapter.class, "DOUBLE_FACTORY");
    assertNotSame(doubleFactory, factory);
  }

  @Test
    @Timeout(8000)
  void newFactory_invocation_returnsFactory() throws Exception {
    ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
    Method newFactoryMethod = ObjectTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);

    Object factory = newFactoryMethod.invoke(null, strategy);
    assertNotNull(factory);
    assertTrue(factory instanceof TypeAdapterFactory);
  }

  @SuppressWarnings("unchecked")
  private <T> T getPrivateStaticField(Class<?> clazz, String fieldName) {
    try {
      java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T) field.get(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Failed to get private static field " + fieldName + ": " + e);
      return null;
    }
  }
}
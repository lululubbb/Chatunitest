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

class ObjectTypeAdapter_552_5Test {

  @Test
    @Timeout(8000)
  void getFactory_returnsDoubleFactory_whenToNumberPolicyDouble() {
    TypeAdapterFactory factory = ObjectTypeAdapter.getFactory(ToNumberPolicy.DOUBLE);
    assertNotNull(factory);
    // DOUBLE_FACTORY is private static final, test identity by invoking getFactory twice with DOUBLE
    TypeAdapterFactory secondCall = ObjectTypeAdapter.getFactory(ToNumberPolicy.DOUBLE);
    assertSame(factory, secondCall);
  }

  @Test
    @Timeout(8000)
  void getFactory_returnsNewFactory_whenToNumberStrategyNotDouble() {
    // Create a mock ToNumberStrategy different from ToNumberPolicy.DOUBLE
    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);
    // Ensure mockStrategy != ToNumberPolicy.DOUBLE
    assertNotSame(ToNumberPolicy.DOUBLE, mockStrategy);

    TypeAdapterFactory factory = ObjectTypeAdapter.getFactory(mockStrategy);
    assertNotNull(factory);
    // The returned factory should not be the DOUBLE_FACTORY singleton
    TypeAdapterFactory doubleFactory = ObjectTypeAdapter.getFactory(ToNumberPolicy.DOUBLE);
    assertNotSame(doubleFactory, factory);
  }

  @Test
    @Timeout(8000)
  void newFactory_invocation_viaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method newFactoryMethod = ObjectTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);

    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);
    Object result = newFactoryMethod.invoke(null, mockStrategy);

    assertNotNull(result);
    assertTrue(result instanceof TypeAdapterFactory);
  }
}
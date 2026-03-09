package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
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

import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ObjectTypeAdapter_551_4Test {

  @Test
    @Timeout(8000)
  void testNewFactoryCreatesFactoryThatReturnsObjectTypeAdapterForObject() {
    ToNumberStrategy toNumberStrategy = ToNumberPolicy.DOUBLE;
    TypeAdapterFactory factory = invokeNewFactory(toNumberStrategy);
    assertNotNull(factory);

    Gson mockGson = mock(Gson.class);
    TypeToken<Object> objectTypeToken = TypeToken.get(Object.class);
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);

    // Should create ObjectTypeAdapter for Object.class
    TypeAdapter<Object> adapter = factory.create(mockGson, objectTypeToken);
    assertNotNull(adapter);
    assertEquals(ObjectTypeAdapter.class, adapter.getClass());

    // Should return null for other types
    TypeAdapter<String> stringAdapter = factory.create(mockGson, stringTypeToken);
    assertNull(stringAdapter);
  }

  private TypeAdapterFactory invokeNewFactory(ToNumberStrategy toNumberStrategy) {
    try {
      Method newFactoryMethod = ObjectTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
      newFactoryMethod.setAccessible(true);
      return (TypeAdapterFactory) newFactoryMethod.invoke(null, toNumberStrategy);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      fail("Failed to invoke newFactory method via reflection", e);
      return null;
    }
  }
}
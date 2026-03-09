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
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectTypeAdapter_551_2Test {

  @Test
    @Timeout(8000)
  void testNewFactoryCreatesFactoryThatReturnsObjectTypeAdapterForObjectClass() throws Exception {
    ToNumberStrategy toNumberStrategy = ToNumberPolicy.DOUBLE;
    // Use reflection to access private static newFactory method
    Method newFactoryMethod = ObjectTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);

    TypeAdapterFactory factory = (TypeAdapterFactory) newFactoryMethod.invoke(null, toNumberStrategy);
    assertNotNull(factory);

    Gson gson = mock(Gson.class);
    TypeToken<Object> objectTypeToken = TypeToken.get(Object.class);

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> adapter = (TypeAdapter<Object>) factory.create(gson, objectTypeToken);
    assertNotNull(adapter);
    assertEquals(ObjectTypeAdapter.class, adapter.getClass());

    // Confirm factory returns null for other types
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapter<String> stringAdapter = factory.create(gson, stringTypeToken);
    assertNull(stringAdapter);
  }

  @Test
    @Timeout(8000)
  void testDoubleFactoryFieldHasExpectedBehavior() throws Exception {
    // Access private static field DOUBLE_FACTORY
    java.lang.reflect.Field doubleFactoryField = ObjectTypeAdapter.class.getDeclaredField("DOUBLE_FACTORY");
    doubleFactoryField.setAccessible(true);
    TypeAdapterFactory doubleFactory = (TypeAdapterFactory) doubleFactoryField.get(null);

    assertNotNull(doubleFactory);

    Gson gson = mock(Gson.class);
    TypeToken<Object> objectTypeToken = TypeToken.get(Object.class);

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> adapter = (TypeAdapter<Object>) doubleFactory.create(gson, objectTypeToken);
    assertNotNull(adapter);
    assertEquals(ObjectTypeAdapter.class, adapter.getClass());

    TypeToken<Integer> intTypeToken = TypeToken.get(Integer.class);
    TypeAdapter<Integer> intAdapter = doubleFactory.create(gson, intTypeToken);
    assertNull(intAdapter);
  }
}
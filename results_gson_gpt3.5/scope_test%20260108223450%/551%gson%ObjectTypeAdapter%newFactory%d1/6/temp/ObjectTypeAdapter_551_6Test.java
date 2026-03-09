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

public class ObjectTypeAdapter_551_6Test {

  @Test
    @Timeout(8000)
  public void testNewFactoryCreatesFactoryThatReturnsObjectTypeAdapterForObjectClass() throws Exception {
    ToNumberStrategy toNumberStrategy = ToNumberPolicy.DOUBLE;
    TypeAdapterFactory factory = invokeNewFactory(toNumberStrategy);

    assertNotNull(factory);

    Gson gson = mock(Gson.class);
    TypeToken<Object> objectTypeToken = TypeToken.get(Object.class);
    TypeAdapter<Object> adapter = factory.create(gson, objectTypeToken);

    assertNotNull(adapter);
    assertEquals(ObjectTypeAdapter.class, adapter.getClass());

    // For a different type, should return null
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapter<String> stringAdapter = factory.create(gson, stringTypeToken);
    assertNull(stringAdapter);
  }

  @Test
    @Timeout(8000)
  public void testNewFactoryCreatesFactoryWithDifferentToNumberStrategy() throws Exception {
    // Use a custom ToNumberStrategy mock to verify it is passed correctly
    ToNumberStrategy customStrategy = mock(ToNumberStrategy.class);
    TypeAdapterFactory factory = invokeNewFactory(customStrategy);

    Gson gson = mock(Gson.class);
    TypeToken<Object> objectTypeToken = TypeToken.get(Object.class);
    TypeAdapter<Object> adapter = factory.create(gson, objectTypeToken);

    assertNotNull(adapter);
    assertEquals(ObjectTypeAdapter.class, adapter.getClass());

    // We use reflection to access the private field 'toNumberStrategy'
    ToNumberStrategy actualStrategy = (ToNumberStrategy) getPrivateField(adapter, "toNumberStrategy");
    assertSame(customStrategy, actualStrategy);
  }

  private static TypeAdapterFactory invokeNewFactory(ToNumberStrategy toNumberStrategy)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method newFactoryMethod = ObjectTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);
    return (TypeAdapterFactory) newFactoryMethod.invoke(null, toNumberStrategy);
  }

  private static Object getPrivateField(Object instance, String fieldName) {
    try {
      java.lang.reflect.Field field = instance.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(instance);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ToNumberStrategy;
import com.google.gson.ToNumberPolicy;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NumberTypeAdapter_301_1Test {

  @Test
    @Timeout(8000)
  void newFactory_returnsFactoryThatCreatesNumberTypeAdapter() throws Exception {
    // Use reflection to access the private static method newFactory
    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", com.google.gson.ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);

    // Prepare a mock ToNumberStrategy
    com.google.gson.ToNumberStrategy toNumberStrategy = mock(com.google.gson.ToNumberStrategy.class);

    // Invoke newFactory
    Object factoryObj = newFactoryMethod.invoke(null, toNumberStrategy);
    assertNotNull(factoryObj);
    assertTrue(factoryObj instanceof TypeAdapterFactory);

    TypeAdapterFactory factory = (TypeAdapterFactory) factoryObj;

    // Prepare a mock Gson and TypeToken for Number.class
    Gson gson = mock(Gson.class);
    TypeToken<Number> numberTypeToken = TypeToken.get(Number.class);

    // The factory should create a TypeAdapter for Number.class
    TypeAdapter<Number> adapter = factory.create(gson, numberTypeToken);
    assertNotNull(adapter);
    assertEquals(NumberTypeAdapter.class, adapter.getClass());

    // The factory should return null for other types
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapter<String> stringAdapter = factory.create(gson, stringTypeToken);
    assertNull(stringAdapter);
  }
}
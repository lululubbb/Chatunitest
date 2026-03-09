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

class NumberTypeAdapter_301_2Test {

  @Test
    @Timeout(8000)
  void newFactory_createsFactoryThatCreatesAdapterForNumber() throws Exception {
    // Use reflection to access private static method newFactory
    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);

    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);

    Object factoryObj = newFactoryMethod.invoke(null, mockStrategy);
    assertNotNull(factoryObj);
    assertTrue(factoryObj instanceof TypeAdapterFactory);

    TypeAdapterFactory factory = (TypeAdapterFactory) factoryObj;

    Gson mockGson = mock(Gson.class);
    TypeToken<Number> numberTypeToken = TypeToken.get(Number.class);
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);

    // Should return a TypeAdapter for Number.class
    TypeAdapter<?> adapterForNumber = factory.create(mockGson, numberTypeToken);
    assertNotNull(adapterForNumber);
    assertTrue(adapterForNumber instanceof NumberTypeAdapter);

    // Should return null for other types
    TypeAdapter<?> adapterForString = factory.create(mockGson, stringTypeToken);
    assertNull(adapterForString);
  }
}
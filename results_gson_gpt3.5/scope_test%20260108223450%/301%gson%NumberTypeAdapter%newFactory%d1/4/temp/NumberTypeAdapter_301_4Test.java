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

class NumberTypeAdapter_301_4Test {

  @Test
    @Timeout(8000)
  void newFactory_createsFactoryThatReturnsAdapterForNumber() throws Exception {
    // Use reflection to access the private static method newFactory
    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);

    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);
    Object factoryObj = newFactoryMethod.invoke(null, mockStrategy);
    assertNotNull(factoryObj);
    assertTrue(factoryObj instanceof TypeAdapterFactory);
    TypeAdapterFactory factory = (TypeAdapterFactory) factoryObj;

    Gson mockGson = mock(Gson.class);
    TypeToken<Number> numberType = TypeToken.get(Number.class);
    TypeToken<String> stringType = TypeToken.get(String.class);

    // When type is Number.class, create should return a non-null adapter of type NumberTypeAdapter
    TypeAdapter<?> adapterForNumber = factory.create(mockGson, numberType);
    assertNotNull(adapterForNumber);
    // The adapter should be a NumberTypeAdapter
    assertEquals(NumberTypeAdapter.class, adapterForNumber.getClass());

    // When type is not Number.class, create should return null
    TypeAdapter<?> adapterForString = factory.create(mockGson, stringType);
    assertNull(adapterForString);
  }
}
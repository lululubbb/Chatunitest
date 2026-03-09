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

class NumberTypeAdapter_301_5Test {

  @Test
    @Timeout(8000)
  void testNewFactoryCreatesFactoryThatReturnsAdapterForNumber() throws Exception {
    // Use reflection to access private static method newFactory
    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);

    // Use ToNumberPolicy.LAZILY_PARSED_NUMBER as argument (a known static final ToNumberStrategy)
    Object factoryObj = newFactoryMethod.invoke(null, ToNumberPolicy.LAZILY_PARSED_NUMBER);
    assertNotNull(factoryObj);
    assertTrue(factoryObj instanceof TypeAdapterFactory);

    TypeAdapterFactory factory = (TypeAdapterFactory) factoryObj;

    // Mock Gson instance (not used in create method, so can be null or mock)
    Gson gson = mock(Gson.class);

    // Create TypeToken for Number.class
    TypeToken<Number> numberTypeToken = TypeToken.get(Number.class);
    TypeAdapter<Number> adapter = factory.create(gson, numberTypeToken);
    assertNotNull(adapter, "Factory should create adapter for Number.class");

    // Create TypeToken for a different class, e.g. String.class
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapter<String> stringAdapter = factory.create(gson, stringTypeToken);
    assertNull(stringAdapter, "Factory should return null for non-Number types");
  }
}
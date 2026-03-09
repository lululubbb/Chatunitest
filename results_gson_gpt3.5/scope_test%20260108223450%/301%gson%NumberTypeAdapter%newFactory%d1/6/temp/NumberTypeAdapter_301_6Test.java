package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
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
import com.google.gson.ToNumberStrategy;
import com.google.gson.ToNumberPolicy;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NumberTypeAdapter_301_6Test {

  @Test
    @Timeout(8000)
  void newFactory_createsFactory_thatReturnsAdapterForNumber() throws Exception {
    // Arrange
    ToNumberStrategy strategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;

    // Use reflection to access private static method newFactory
    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);

    // Act
    Object factoryObj = newFactoryMethod.invoke(null, strategy);

    // Assert factory is TypeAdapterFactory
    assertNotNull(factoryObj);
    assertTrue(factoryObj instanceof TypeAdapterFactory);
    TypeAdapterFactory factory = (TypeAdapterFactory) factoryObj;

    // Create mocks for Gson and TypeToken
    Gson gson = mock(Gson.class);
    @SuppressWarnings("unchecked")
    TypeToken<Number> numberTypeToken = (TypeToken<Number>) (TypeToken<?>) TypeToken.get(Number.class);
    @SuppressWarnings("unchecked")
    TypeToken<String> stringTypeToken = (TypeToken<String>) (TypeToken<?>) TypeToken.get(String.class);

    // When type is Number.class, should return non-null adapter
    TypeAdapter<?> adapterForNumber = factory.create(gson, numberTypeToken);
    assertNotNull(adapterForNumber);
    assertEquals(Number.class, adapterForNumber.getClass().getDeclaredMethod("read", com.google.gson.stream.JsonReader.class).getReturnType());

    // When type is not Number.class, should return null
    TypeAdapter<?> adapterForString = factory.create(gson, stringTypeToken);
    assertNull(adapterForString);
  }
}
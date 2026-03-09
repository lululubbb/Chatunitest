package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapterFactory;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class NumberTypeAdapter_302_3Test {

  @Test
    @Timeout(8000)
  void getFactory_returnsLazilyParsedNumberFactory_whenStrategyIsLazilyParsedNumber() {
    TypeAdapterFactory factory = NumberTypeAdapter.getFactory(ToNumberPolicy.LAZILY_PARSED_NUMBER);
    assertNotNull(factory);
    assertSame(getLazilyParsedNumberFactoryViaReflection(), factory);
  }

  @Test
    @Timeout(8000)
  void getFactory_returnsNewFactory_whenStrategyIsNotLazilyParsedNumber() {
    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);
    when(mockStrategy.toString()).thenReturn("customStrategy");

    TypeAdapterFactory factory = NumberTypeAdapter.getFactory(mockStrategy);
    assertNotNull(factory);
    assertNotSame(getLazilyParsedNumberFactoryViaReflection(), factory);
  }

  // Helper method to get private static field LAZILY_PARSED_NUMBER_FACTORY via reflection
  private TypeAdapterFactory getLazilyParsedNumberFactoryViaReflection() {
    try {
      var field = NumberTypeAdapter.class.getDeclaredField("LAZILY_PARSED_NUMBER_FACTORY");
      field.setAccessible(true);
      return (TypeAdapterFactory) field.get(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void newFactory_privateStaticMethod_invocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);
    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);

    Object factory = newFactoryMethod.invoke(null, mockStrategy);
    assertNotNull(factory);
    assertTrue(factory instanceof TypeAdapterFactory);
  }
}
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

import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapterFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class NumberTypeAdapter_302_6Test {

  @Test
    @Timeout(8000)
  void getFactory_returnsLazilyParsedNumberFactory_whenPolicyIsLazilyParsedNumber() {
    TypeAdapterFactory factory = NumberTypeAdapter.getFactory(ToNumberPolicy.LAZILY_PARSED_NUMBER);
    assertNotNull(factory);
    // The returned factory should be the LAZILY_PARSED_NUMBER_FACTORY singleton
    TypeAdapterFactory expectedFactory = getStaticField("LAZILY_PARSED_NUMBER_FACTORY", NumberTypeAdapter.class);
    assertSame(expectedFactory, factory);
  }

  @Test
    @Timeout(8000)
  void getFactory_returnsNewFactory_whenPolicyIsNotLazilyParsedNumber() {
    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);
    when(mockStrategy.toString()).thenReturn("mockStrategy");

    TypeAdapterFactory factory = NumberTypeAdapter.getFactory(mockStrategy);
    assertNotNull(factory);

    // The returned factory should NOT be the LAZILY_PARSED_NUMBER_FACTORY
    TypeAdapterFactory lazilyParsedFactory = getStaticField("LAZILY_PARSED_NUMBER_FACTORY", NumberTypeAdapter.class);
    assertNotSame(lazilyParsedFactory, factory);
  }

  @Test
    @Timeout(8000)
  void newFactory_invokesPrivateStaticMethod_correctly() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);

    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);
    Object result = newFactoryMethod.invoke(null, mockStrategy);

    assertNotNull(result);
    assertTrue(result instanceof TypeAdapterFactory);
  }

  private static <T> T getStaticField(String fieldName, Class<?> clazz) {
    try {
      var field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      @SuppressWarnings("unchecked")
      T value = (T) field.get(null);
      return value;
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
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

class NumberTypeAdapter_302_5Test {

  @Test
    @Timeout(8000)
  void getFactory_returnsLazilyParsedNumberFactory_whenPolicyIsLazilyParsedNumber() {
    TypeAdapterFactory factory = NumberTypeAdapter.getFactory(ToNumberPolicy.LAZILY_PARSED_NUMBER);
    assertNotNull(factory);
    // The returned factory should be the same as the private static final LAZILY_PARSED_NUMBER_FACTORY
    // We use reflection to verify this:
    try {
      var field = NumberTypeAdapter.class.getDeclaredField("LAZILY_PARSED_NUMBER_FACTORY");
      field.setAccessible(true);
      Object lazilyParsedNumberFactory = field.get(null);
      assertSame(lazilyParsedNumberFactory, factory);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed to access LAZILY_PARSED_NUMBER_FACTORY field");
    }
  }

  @Test
    @Timeout(8000)
  void getFactory_returnsNewFactory_whenPolicyIsNotLazilyParsedNumber() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Create a mock ToNumberStrategy different from LAZILY_PARSED_NUMBER
    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);
    // Ensure it's not equal to LAZILY_PARSED_NUMBER
    assertNotEquals(ToNumberPolicy.LAZILY_PARSED_NUMBER, mockStrategy);

    TypeAdapterFactory factory = NumberTypeAdapter.getFactory(mockStrategy);
    assertNotNull(factory);

    // Use reflection to invoke private static newFactory method with mockStrategy
    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);
    Object expectedFactory = newFactoryMethod.invoke(null, mockStrategy);

    // Use assertEquals instead of assertSame because newFactory returns a new instance each time
    assertEquals(expectedFactory, factory);
  }
}
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

public class NumberTypeAdapter_302_1Test {

  @Test
    @Timeout(8000)
  public void testGetFactory_withLazilyParsedNumber_returnsLazilyParsedNumberFactory() {
    TypeAdapterFactory factory = NumberTypeAdapter.getFactory(ToNumberPolicy.LAZILY_PARSED_NUMBER);
    assertNotNull(factory);
    // LAZILY_PARSED_NUMBER_FACTORY is private static final, check equality by reference
    TypeAdapterFactory expectedFactory = getPrivateStaticField(NumberTypeAdapter.class, "LAZILY_PARSED_NUMBER_FACTORY", TypeAdapterFactory.class);
    assertSame(expectedFactory, factory);
  }

  @Test
    @Timeout(8000)
  public void testGetFactory_withOtherStrategy_returnsNewFactory() {
    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);
    // Ensure mockStrategy != ToNumberPolicy.LAZILY_PARSED_NUMBER
    assertNotSame(ToNumberPolicy.LAZILY_PARSED_NUMBER, mockStrategy);

    TypeAdapterFactory factory = NumberTypeAdapter.getFactory(mockStrategy);
    assertNotNull(factory);

    // The returned factory should not be the LAZILY_PARSED_NUMBER_FACTORY
    TypeAdapterFactory lazilyParsed = getPrivateStaticField(NumberTypeAdapter.class, "LAZILY_PARSED_NUMBER_FACTORY", TypeAdapterFactory.class);
    assertNotSame(lazilyParsed, factory);
  }

  @Test
    @Timeout(8000)
  public void testNewFactory_returnsTypeAdapterFactory() throws Throwable {
    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);
    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);
    Object factory = newFactoryMethod.invoke(null, mockStrategy);
    assertNotNull(factory);
    assertTrue(factory instanceof TypeAdapterFactory);
  }

  @Test
    @Timeout(8000)
  public void testPrivateConstructor_createsInstance() throws Throwable {
    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);
    var constructor = NumberTypeAdapter.class.getDeclaredConstructor(ToNumberStrategy.class);
    constructor.setAccessible(true);
    Object instance = constructor.newInstance(mockStrategy);
    assertNotNull(instance);
    assertTrue(instance instanceof NumberTypeAdapter);
  }

  @SuppressWarnings("unchecked")
  private static <T> T getPrivateStaticField(Class<?> clazz, String fieldName, Class<T> fieldType) {
    try {
      var field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      Object value = field.get(null);
      return (T) value;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
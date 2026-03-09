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

class NumberTypeAdapter_302_2Test {

  @Test
    @Timeout(8000)
  void getFactory_withLazilyParsedNumber_returnsLazilyParsedNumberFactory() {
    TypeAdapterFactory factory = NumberTypeAdapter.getFactory(ToNumberPolicy.LAZILY_PARSED_NUMBER);
    assertNotNull(factory);
    assertSame(getPrivateStaticField("LAZILY_PARSED_NUMBER_FACTORY"), factory);
  }

  @Test
    @Timeout(8000)
  void getFactory_withCustomStrategy_returnsNewFactory() {
    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);
    TypeAdapterFactory factory = NumberTypeAdapter.getFactory(mockStrategy);
    assertNotNull(factory);
    assertNotSame(getPrivateStaticField("LAZILY_PARSED_NUMBER_FACTORY"), factory);
  }

  @Test
    @Timeout(8000)
  void newFactory_reflection_invocation_returnsFactory() throws Exception {
    ToNumberStrategy mockStrategy = mock(ToNumberStrategy.class);
    Method newFactoryMethod = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);
    Object factory = newFactoryMethod.invoke(null, mockStrategy);
    assertNotNull(factory);
    assertTrue(factory instanceof TypeAdapterFactory);
  }

  private Object getPrivateStaticField(String fieldName) {
    try {
      var field = NumberTypeAdapter.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Failed to access private static field: " + fieldName);
      return null;
    }
  }
}
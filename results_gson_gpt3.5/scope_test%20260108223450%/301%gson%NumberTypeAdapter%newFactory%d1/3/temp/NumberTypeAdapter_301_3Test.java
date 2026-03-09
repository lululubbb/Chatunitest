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

class NumberTypeAdapter_301_3Test {

  @Test
    @Timeout(8000)
  void newFactory_create_returnsAdapterForNumber() throws Exception {
    // Use reflection to access private static method newFactory
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

    // When type is Number.class, should return non-null adapter
    TypeAdapter<Number> adapter = factory.create(mockGson, numberType);
    assertNotNull(adapter);
    assertEquals(Number.class, adapter.getClass().getSuperclass().getTypeParameters().length == 1 ? Number.class : null);

    // When type is not Number.class, should return null
    TypeAdapter<?> adapterNull = factory.create(mockGson, stringType);
    assertNull(adapterNull);
  }
}
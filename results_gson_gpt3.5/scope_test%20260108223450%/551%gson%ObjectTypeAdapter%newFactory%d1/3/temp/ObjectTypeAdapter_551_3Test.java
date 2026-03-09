package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ObjectTypeAdapter_551_3Test {

  @Test
    @Timeout(8000)
  void newFactory_returnsFactoryThatCreatesObjectTypeAdapterForObjectClass() throws Exception {
    // Arrange
    ToNumberStrategy toNumberStrategy = ToNumberPolicy.DOUBLE;
    Method newFactoryMethod = ObjectTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactoryMethod.setAccessible(true);

    // Act
    TypeAdapterFactory factory = (TypeAdapterFactory) newFactoryMethod.invoke(null, toNumberStrategy);

    // Assert factory is not null
    assertNotNull(factory);

    // Prepare mocks for Gson and TypeToken<Object>
    Gson gson = mock(Gson.class);
    TypeToken<Object> objectTypeToken = TypeToken.get(Object.class);
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);

    // Act - create adapter for Object.class
    TypeAdapter<Object> adapter = factory.create(gson, objectTypeToken);
    // Act - create adapter for non-Object class
    TypeAdapter<String> nullAdapter = factory.create(gson, stringTypeToken);

    // Assert the adapter for Object.class is an ObjectTypeAdapter instance
    assertNotNull(adapter);
    assertEquals(ObjectTypeAdapter.class, adapter.getClass());

    // Assert the adapter for non-Object class is null
    assertNull(nullAdapter);
  }
}
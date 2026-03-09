package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapterFactory;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TreeTypeAdapter_497_6Test {

  @Test
    @Timeout(8000)
  public void testNewTypeHierarchyFactory_returnsSingleTypeFactoryInstance() {
    Class<?> hierarchyType = Number.class;

    // Create a mock of JsonSerializer or JsonDeserializer (both are accepted)
    JsonSerializer<?> serializer = mock(JsonSerializer.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(hierarchyType, serializer);

    assertNotNull(factory);
    assertEquals("com.google.gson.internal.bind.TreeTypeAdapter$SingleTypeFactory", factory.getClass().getName());
  }

  @Test
    @Timeout(8000)
  public void testNewTypeHierarchyFactory_reflectionInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class<?> hierarchyType = String.class;

    JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);

    Method method = TreeTypeAdapter.class.getDeclaredMethod("newTypeHierarchyFactory", Class.class, Object.class);
    method.setAccessible(true);
    Object result = method.invoke(null, hierarchyType, deserializer);

    assertNotNull(result);
    assertTrue(result instanceof TypeAdapterFactory);
    assertEquals("com.google.gson.internal.bind.TreeTypeAdapter$SingleTypeFactory", result.getClass().getName());
  }
}
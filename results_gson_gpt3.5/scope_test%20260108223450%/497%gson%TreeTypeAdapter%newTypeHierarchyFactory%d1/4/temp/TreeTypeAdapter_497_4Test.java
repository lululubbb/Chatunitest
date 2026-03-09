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

public class TreeTypeAdapter_497_4Test {

  @Test
    @Timeout(8000)
  public void testNewTypeHierarchyFactory() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class<?> hierarchyType = Number.class;

    // Provide a valid typeAdapter: either JsonSerializer or JsonDeserializer mock
    JsonSerializer<?> serializer = mock(JsonSerializer.class);

    // Call the static method directly
    TypeAdapterFactory factoryDirect = TreeTypeAdapter.newTypeHierarchyFactory(hierarchyType, serializer);
    assertNotNull(factoryDirect);

    // Use reflection to invoke the private constructor of SingleTypeFactory indirectly via newTypeHierarchyFactory
    Method method = TreeTypeAdapter.class.getDeclaredMethod("newTypeHierarchyFactory", Class.class, Object.class);
    method.setAccessible(true);
    TypeAdapterFactory factoryReflect = (TypeAdapterFactory) method.invoke(null, hierarchyType, serializer);
    assertNotNull(factoryReflect);
    assertEquals(factoryDirect.getClass(), factoryReflect.getClass());

    // Validate the returned factory is of expected type by checking class name
    assertEquals("com.google.gson.internal.bind.TreeTypeAdapter$SingleTypeFactory", factoryDirect.getClass().getName());
  }
}
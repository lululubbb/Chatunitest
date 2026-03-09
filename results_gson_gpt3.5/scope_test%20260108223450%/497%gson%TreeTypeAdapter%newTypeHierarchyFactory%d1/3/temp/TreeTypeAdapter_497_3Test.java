package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TreeTypeAdapter_497_3Test {

  @Test
    @Timeout(8000)
  public void testNewTypeHierarchyFactory_returnsSingleTypeFactoryWithCorrectParams() throws Exception {
    Class<?> hierarchyType = Number.class;

    // Use a mock JsonSerializer as the typeAdapter parameter to satisfy Preconditions.checkArgument
    JsonSerializer<?> typeAdapter = mock(JsonSerializer.class);

    // Invoke the static method TreeTypeAdapter.newTypeHierarchyFactory via reflection
    Method method = TreeTypeAdapter.class.getDeclaredMethod("newTypeHierarchyFactory", Class.class, Object.class);
    method.setAccessible(true);
    Object result = method.invoke(null, hierarchyType, typeAdapter);

    assertNotNull(result);
    assertTrue(result instanceof TypeAdapterFactory);

    // The returned object is an instance of SingleTypeFactory (package-private)
    // We verify its internal fields via reflection to ensure correct construction

    Class<?> singleTypeFactoryClass = result.getClass();

    // Verify field 'hierarchyType'
    Field hierarchyTypeField = singleTypeFactoryClass.getDeclaredField("hierarchyType");
    hierarchyTypeField.setAccessible(true);
    Object actualHierarchyType = hierarchyTypeField.get(result);
    assertEquals(hierarchyType, actualHierarchyType);

    // Verify field 'typeAdapter' (not 'typeAdapterFactory')
    Field typeAdapterField = singleTypeFactoryClass.getDeclaredField("typeAdapter");
    typeAdapterField.setAccessible(true);
    Object actualTypeAdapter = typeAdapterField.get(result);
    assertSame(typeAdapter, actualTypeAdapter);

    // Verify field 'matchRawType' is true
    Field matchRawTypeField = singleTypeFactoryClass.getDeclaredField("matchRawType");
    matchRawTypeField.setAccessible(true);
    boolean actualMatchRawType = matchRawTypeField.getBoolean(result);
    assertTrue(actualMatchRawType);

    // Verify field 'exactType' is null
    Field exactTypeField = singleTypeFactoryClass.getDeclaredField("exactType");
    exactTypeField.setAccessible(true);
    Object actualExactType = exactTypeField.get(result);
    assertNull(actualExactType);
  }
}
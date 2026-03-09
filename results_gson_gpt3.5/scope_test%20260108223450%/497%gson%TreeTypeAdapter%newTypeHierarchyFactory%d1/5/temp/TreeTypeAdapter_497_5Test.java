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

public class TreeTypeAdapter_497_5Test {

  @Test
    @Timeout(8000)
  public void testNewTypeHierarchyFactory() throws NoSuchFieldException, IllegalAccessException {
    Class<?> hierarchyType = String.class;

    // Pass a mock JsonSerializer instead of TypeAdapter to satisfy Preconditions.checkArgument
    JsonSerializer<?> serializer = mock(JsonSerializer.class);

    // Call the static method directly
    TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(hierarchyType, serializer);

    assertNotNull(factory);

    // Use reflection to verify that the returned instance is of type SingleTypeFactory
    Class<?> factoryClass = factory.getClass();
    assertEquals("com.google.gson.internal.bind.TreeTypeAdapter$SingleTypeFactory", factoryClass.getName());

    // Verify fields of SingleTypeFactory via reflection

    // The field holding the typeAdapter is named "serializer" when the passed object is a JsonSerializer
    Field serializerField = factoryClass.getDeclaredField("serializer");
    serializerField.setAccessible(true);
    Object actualSerializer = serializerField.get(factory);
    assertSame(serializer, actualSerializer);

    Field hierarchyTypeField = factoryClass.getDeclaredField("hierarchyType");
    hierarchyTypeField.setAccessible(true);
    Object actualHierarchyType = hierarchyTypeField.get(factory);
    assertSame(hierarchyType, actualHierarchyType);

    Field matchRawTypeField = factoryClass.getDeclaredField("matchRawType");
    matchRawTypeField.setAccessible(true);
    boolean matchRawType = matchRawTypeField.getBoolean(factory);
    assertFalse(matchRawType);
  }
}
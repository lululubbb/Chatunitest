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

class TreeTypeAdapter_497_1Test {

  @Test
    @Timeout(8000)
  void newTypeHierarchyFactory_shouldReturnSingleTypeFactoryWithCorrectParams() throws Exception {
    Class<?> hierarchyType = String.class;

    Object serializerMock = mock(JsonSerializer.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(hierarchyType, serializerMock);

    assertNotNull(factory);

    Class<?> singleTypeFactoryClass = factory.getClass();

    Field typeAdapterField = singleTypeFactoryClass.getDeclaredField("typeAdapter");
    typeAdapterField.setAccessible(true);

    Field hierarchyTypeField = singleTypeFactoryClass.getDeclaredField("hierarchyType");
    hierarchyTypeField.setAccessible(true);

    Field matchRawTypeField = singleTypeFactoryClass.getDeclaredField("matchRawType");
    matchRawTypeField.setAccessible(true);

    Field exactTypeField = singleTypeFactoryClass.getDeclaredField("exactType");
    exactTypeField.setAccessible(true);

    assertSame(serializerMock, typeAdapterField.get(factory));
    assertNull(exactTypeField.get(factory));
    assertFalse((Boolean) matchRawTypeField.get(factory));
    assertSame(hierarchyType, hierarchyTypeField.get(factory));
  }
}
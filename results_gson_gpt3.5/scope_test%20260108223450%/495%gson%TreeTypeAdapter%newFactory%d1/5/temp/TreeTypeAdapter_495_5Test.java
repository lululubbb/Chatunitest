package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class TreeTypeAdapter_495_5Test {

  @Test
    @Timeout(8000)
  void newFactory_returnsSingleTypeFactoryWithExpectedProperties() throws Exception {
    TypeToken<String> exactType = TypeToken.get(String.class);
    // Pass a JsonSerializer or JsonDeserializer mock instead of TypeAdapter to satisfy Preconditions
    Object typeAdapter = mock(com.google.gson.JsonSerializer.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(exactType, typeAdapter);

    assertNotNull(factory);
    assertEquals("com.google.gson.internal.bind.TreeTypeAdapter$SingleTypeFactory", factory.getClass().getName());

    // Use reflection to verify private fields of SingleTypeFactory
    Class<?> cls = factory.getClass();

    Field typeAdapterField = cls.getDeclaredField("adapter");
    typeAdapterField.setAccessible(true);
    assertSame(typeAdapter, typeAdapterField.get(factory));

    Field exactTypeField = cls.getDeclaredField("exactType");
    exactTypeField.setAccessible(true);
    assertEquals(exactType, exactTypeField.get(factory));

    Field matchRawTypeField = cls.getDeclaredField("matchRawType");
    matchRawTypeField.setAccessible(true);
    assertFalse(matchRawTypeField.getBoolean(factory));

    Field hierarchyTypeField = cls.getDeclaredField("hierarchyType");
    hierarchyTypeField.setAccessible(true);
    assertNull(hierarchyTypeField.get(factory));
  }
}
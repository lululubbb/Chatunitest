package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

class TreeTypeAdapter_495_3Test {

  @Test
    @Timeout(8000)
  void newFactory_returnsSingleTypeFactoryWithCorrectFields() throws Exception {
    TypeToken<String> exactType = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> typeAdapter = (TypeAdapter<String>) mock(TypeAdapter.class);

    // Pass the actual typeAdapter instance, not a TreeTypeAdapter, to satisfy Preconditions.checkArgument
    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(exactType, typeAdapter);

    assertNotNull(factory);
    assertEquals("com.google.gson.internal.bind.TreeTypeAdapter$SingleTypeFactory", factory.getClass().getName());

    // Use reflection to verify private fields of SingleTypeFactory
    var clazz = factory.getClass();

    var typeAdapterField = clazz.getDeclaredField("typeAdapter");
    typeAdapterField.setAccessible(true);
    assertSame(typeAdapter, typeAdapterField.get(factory));

    var exactTypeField = clazz.getDeclaredField("exactType");
    exactTypeField.setAccessible(true);
    assertEquals(exactType, exactTypeField.get(factory));

    var matchRawTypeField = clazz.getDeclaredField("matchRawType");
    matchRawTypeField.setAccessible(true);
    assertFalse(matchRawTypeField.getBoolean(factory));

    var hierarchyTypeField = clazz.getDeclaredField("hierarchyType");
    hierarchyTypeField.setAccessible(true);
    assertNull(hierarchyTypeField.get(factory));
  }
}
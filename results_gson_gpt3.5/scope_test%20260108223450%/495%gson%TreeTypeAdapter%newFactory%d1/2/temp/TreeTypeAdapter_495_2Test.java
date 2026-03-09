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

import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapter;

import org.junit.jupiter.api.Test;

class TreeTypeAdapter_495_2Test {

  @Test
    @Timeout(8000)
  void newFactory_shouldReturnSingleTypeFactoryWithCorrectProperties() throws Exception {
    TypeToken<String> exactType = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(exactType, typeAdapter);

    assertNotNull(factory);
    // Using reflection to verify internal state of SingleTypeFactory
    Class<?> singleTypeFactoryClass = factory.getClass();
    var typeAdapterField = singleTypeFactoryClass.getDeclaredField("typeAdapter");
    typeAdapterField.setAccessible(true);
    var exactTypeField = singleTypeFactoryClass.getDeclaredField("exactType");
    exactTypeField.setAccessible(true);
    var matchRawTypeField = singleTypeFactoryClass.getDeclaredField("matchRawType");
    matchRawTypeField.setAccessible(true);
    var hierarchyTypeField = singleTypeFactoryClass.getDeclaredField("hierarchyType");
    hierarchyTypeField.setAccessible(true);

    assertSame(typeAdapter, typeAdapterField.get(factory));
    assertEquals(exactType, exactTypeField.get(factory));
    assertEquals(false, matchRawTypeField.get(factory));
    assertNull(hierarchyTypeField.get(factory));
  }
}
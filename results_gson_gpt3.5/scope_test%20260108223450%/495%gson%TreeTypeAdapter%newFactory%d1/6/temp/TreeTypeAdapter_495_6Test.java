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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonSerializer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class TreeTypeAdapter_495_6Test {

  @Test
    @Timeout(8000)
  void newFactory_shouldReturnSingleTypeFactoryWithCorrectProperties() throws Exception {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    // Use a mock JsonSerializer to satisfy the Preconditions check in SingleTypeFactory constructor
    JsonSerializer<String> typeAdapter = mock(JsonSerializer.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(typeToken, typeAdapter);

    assertNotNull(factory);
    assertEquals("com.google.gson.internal.bind.TreeTypeAdapter$SingleTypeFactory", factory.getClass().getName());

    // Use reflection to verify SingleTypeFactory fields
    Class<?> clazz = factory.getClass();

    // The field holding the typeAdapter is named 'typeAdapter$delegate' (or similar) in some versions,
    // but to be sure, we can iterate fields and find the one holding the passed typeAdapter instance.
    Field typeAdapterField = null;
    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      Object value = field.get(factory);
      if (value == typeAdapter) {
        typeAdapterField = field;
        break;
      }
    }
    assertNotNull(typeAdapterField, "typeAdapter field not found or does not hold the expected instance");

    Field exactTypeField = clazz.getDeclaredField("exactType");
    exactTypeField.setAccessible(true);

    Field matchRawField = clazz.getDeclaredField("matchRawType");
    matchRawField.setAccessible(true);

    Field hierarchyTypeField = clazz.getDeclaredField("hierarchyType");
    hierarchyTypeField.setAccessible(true);

    assertSame(typeAdapter, typeAdapterField.get(factory));
    assertEquals(typeToken, exactTypeField.get(factory));
    assertFalse(matchRawField.getBoolean(factory));
    assertNull(hierarchyTypeField.get(factory));
  }
}
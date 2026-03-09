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

import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

class TreeTypeAdapter_496_6Test {

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_exactTypeIsRawType_matchRawTypeTrue() throws Exception {
    // Arrange
    // Use raw type to satisfy the precondition in SingleTypeFactory constructor
    TypeToken<String> exactType = TypeToken.get(String.class);

    TypeAdapter<String> typeAdapter = new TypeAdapter<String>() {
      @Override
      public String read(com.google.gson.stream.JsonReader in) {
        return null;
      }

      @Override
      public void write(com.google.gson.stream.JsonWriter out, String value) {
      }

      @Override
      public String toString() {
        return "dummyTypeAdapter";
      }
    };

    // Act
    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(TypeToken.get(String.class), typeAdapter);

    // Assert
    assertNotNull(factory);

    // Use reflection to verify SingleTypeFactory fields
    Class<?> singleTypeFactoryClass = Class.forName("com.google.gson.internal.bind.TreeTypeAdapter$SingleTypeFactory");
    assertTrue(singleTypeFactoryClass.isInstance(factory));

    var matchRawTypeField = singleTypeFactoryClass.getDeclaredField("matchRawType");
    matchRawTypeField.setAccessible(true);
    boolean matchRawTypeValue = (boolean) matchRawTypeField.get(factory);
    assertTrue(matchRawTypeValue);

    var exactTypeField = singleTypeFactoryClass.getDeclaredField("exactType");
    exactTypeField.setAccessible(true);
    TypeToken<?> exactTypeValue = (TypeToken<?>) exactTypeField.get(factory);
    assertEquals(TypeToken.get(String.class), exactTypeValue);

    var typeAdapterField = singleTypeFactoryClass.getDeclaredField("typeAdapter");
    typeAdapterField.setAccessible(true);
    Object typeAdapterValue = typeAdapterField.get(factory);
    assertEquals(typeAdapter, typeAdapterValue);
  }

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_exactTypeIsNotRawType_matchRawTypeFalse() throws Exception {
    // Arrange
    TypeToken<java.util.List<String>> exactType = new TypeToken<java.util.List<String>>() {};
    TypeAdapter<java.util.List<String>> typeAdapter = new TypeAdapter<java.util.List<String>>() {
      @Override
      public java.util.List<String> read(com.google.gson.stream.JsonReader in) {
        return null;
      }

      @Override
      public void write(com.google.gson.stream.JsonWriter out, java.util.List<String> value) {
      }

      @Override
      public String toString() {
        return "dummyTypeAdapter";
      }
    };

    // Act
    // Pass exactType (not raw type) to get matchRawType = false
    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(exactType, typeAdapter);

    // Assert
    assertNotNull(factory);

    // Use reflection to verify SingleTypeFactory fields
    Class<?> singleTypeFactoryClass = Class.forName("com.google.gson.internal.bind.TreeTypeAdapter$SingleTypeFactory");
    assertTrue(singleTypeFactoryClass.isInstance(factory));

    var matchRawTypeField = singleTypeFactoryClass.getDeclaredField("matchRawType");
    matchRawTypeField.setAccessible(true);
    boolean matchRawTypeValue = (boolean) matchRawTypeField.get(factory);
    assertFalse(matchRawTypeValue);

    var exactTypeField = singleTypeFactoryClass.getDeclaredField("exactType");
    exactTypeField.setAccessible(true);
    TypeToken<?> exactTypeValue = (TypeToken<?>) exactTypeField.get(factory);
    assertEquals(exactType, exactTypeValue);

    var typeAdapterField = singleTypeFactoryClass.getDeclaredField("typeAdapter");
    typeAdapterField.setAccessible(true);
    Object typeAdapterValue = typeAdapterField.get(factory);
    assertEquals(typeAdapter, typeAdapterValue);
  }
}
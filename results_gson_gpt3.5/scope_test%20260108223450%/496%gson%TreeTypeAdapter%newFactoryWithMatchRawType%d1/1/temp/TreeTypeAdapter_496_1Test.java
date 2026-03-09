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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import org.junit.jupiter.api.Test;

class TreeTypeAdapter_496_1Test {

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_whenExactTypeIsRawType_shouldMatchRawTypeTrue() {
    TypeToken<String> exactType = TypeToken.get(String.class);
    // Use mocks for JsonSerializer and JsonDeserializer to satisfy constructor requirements
    JsonSerializer<String> serializer = (src, typeOfSrc, context) -> null;
    JsonDeserializer<String> deserializer = (json, typeOfT, context) -> null;

    Object typeAdapter = new Object() {
      // The actual typeAdapter parameter is expected to be JsonSerializer or JsonDeserializer or TypeAdapter
      // But the method accepts Object, so we can pass either serializer or deserializer
      @Override public String toString() {
        return "mockTypeAdapter";
      }
    };

    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(exactType, serializer);

    assertNotNull(factory);

    // Using reflection to access SingleTypeFactory.matchRawType field
    boolean matchRawType = getMatchRawType(factory);
    assertTrue(matchRawType);
  }

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_whenExactTypeIsParameterized_shouldMatchRawTypeFalse() {
    TypeToken<java.util.List<String>> exactType = new TypeToken<java.util.List<String>>() {};
    JsonSerializer<java.util.List<String>> serializer = (src, typeOfSrc, context) -> null;

    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(exactType, serializer);

    assertNotNull(factory);

    // Using reflection to access SingleTypeFactory.matchRawType field
    boolean matchRawType = getMatchRawType(factory);
    assertFalse(matchRawType);
  }

  private boolean getMatchRawType(TypeAdapterFactory factory) {
    try {
      // SingleTypeFactory is package-private static class inside TreeTypeAdapter
      Class<?> singleTypeFactoryClass = Class.forName("com.google.gson.internal.bind.TreeTypeAdapter$SingleTypeFactory");
      assertTrue(singleTypeFactoryClass.isInstance(factory));
      java.lang.reflect.Field matchRawTypeField = singleTypeFactoryClass.getDeclaredField("matchRawType");
      matchRawTypeField.setAccessible(true);
      return matchRawTypeField.getBoolean(factory);
    } catch (Exception e) {
      fail("Reflection failed: " + e.getMessage());
      return false;
    }
  }
}
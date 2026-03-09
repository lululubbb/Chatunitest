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

class TreeTypeAdapter_496_3Test {

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_matchRawTypeTrue_createsSingleTypeFactory() {
    TypeToken<String> exactType = TypeToken.get(String.class);
    // Use a mock TypeAdapter instead of Object to satisfy Preconditions
    TypeAdapter<?> typeAdapter = mock(TypeAdapter.class);

    // Pass the actual TypeAdapter instance, not just Object
    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(exactType, typeAdapter);

    assertNotNull(factory);

    // Use reflection to verify SingleTypeFactory internal fields
    try {
      var clazz = factory.getClass();
      var typeAdapterField = clazz.getDeclaredField("typeAdapter");
      typeAdapterField.setAccessible(true);
      Object actualTypeAdapter = typeAdapterField.get(factory);
      assertSame(typeAdapter, actualTypeAdapter);

      var exactTypeField = clazz.getDeclaredField("exactType");
      exactTypeField.setAccessible(true);
      Object actualExactType = exactTypeField.get(factory);
      assertEquals(exactType, actualExactType);

      var matchRawTypeField = clazz.getDeclaredField("matchRawType");
      matchRawTypeField.setAccessible(true);
      boolean actualMatchRawType = matchRawTypeField.getBoolean(factory);
      assertTrue(actualMatchRawType);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_matchRawTypeFalse_createsSingleTypeFactory() {
    // Create a TypeToken with a parameterized type so getType() != getRawType()
    TypeToken<java.util.List<String>> exactType = new TypeToken<java.util.List<String>>() {};
    // Use a mock TypeAdapter instead of Object to satisfy Preconditions
    TypeAdapter<?> typeAdapter = mock(TypeAdapter.class);

    // Pass the actual TypeAdapter instance, not just Object
    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(exactType, typeAdapter);

    assertNotNull(factory);

    // Verify SingleTypeFactory internal fields via reflection
    try {
      var clazz = factory.getClass();
      var typeAdapterField = clazz.getDeclaredField("typeAdapter");
      typeAdapterField.setAccessible(true);
      Object actualTypeAdapter = typeAdapterField.get(factory);
      assertSame(typeAdapter, actualTypeAdapter);

      var exactTypeField = clazz.getDeclaredField("exactType");
      exactTypeField.setAccessible(true);
      Object actualExactType = exactTypeField.get(factory);
      assertEquals(exactType, actualExactType);

      var matchRawTypeField = clazz.getDeclaredField("matchRawType");
      matchRawTypeField.setAccessible(true);
      boolean actualMatchRawType = matchRawTypeField.getBoolean(factory);
      assertFalse(actualMatchRawType);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}
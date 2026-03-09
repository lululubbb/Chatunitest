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
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class TreeTypeAdapter_496_4Test {

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_exactTypeEqualsRawType_matchRawTypeTrue() {
    TypeToken<String> exactType = TypeToken.get(String.class);
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(exactType, serializer);

    assertNotNull(factory);

    // Use reflection to check SingleTypeFactory fields
    try {
      Class<?> clazz = factory.getClass();

      Field typeAdapterField = null;
      Field exactTypeField = null;
      Field matchRawTypeField = null;

      // The SingleTypeFactory is a private static inner class, fields might be in superclass or declared in class
      // Try to find fields by name in the class and its superclasses
      for (Field field : clazz.getDeclaredFields()) {
        if (field.getName().equals("typeAdapter")) {
          typeAdapterField = field;
        } else if (field.getName().equals("exactType")) {
          exactTypeField = field;
        } else if (field.getName().equals("matchRawType")) {
          matchRawTypeField = field;
        }
      }

      if (typeAdapterField == null || exactTypeField == null || matchRawTypeField == null) {
        // Try superclass if any
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
          for (Field field : superClazz.getDeclaredFields()) {
            if (field.getName().equals("typeAdapter")) {
              typeAdapterField = field;
            } else if (field.getName().equals("exactType")) {
              exactTypeField = field;
            } else if (field.getName().equals("matchRawType")) {
              matchRawTypeField = field;
            }
          }
        }
      }

      // If still not found, try enclosing class fields (for inner class synthetic fields)
      if (typeAdapterField == null) {
        for (Field field : clazz.getDeclaredFields()) {
          if (field.getType().isInstance(serializer)) {
            typeAdapterField = field;
            break;
          }
        }
      }

      assertNotNull(typeAdapterField, "Field 'typeAdapter' not found");
      assertNotNull(exactTypeField, "Field 'exactType' not found");
      assertNotNull(matchRawTypeField, "Field 'matchRawType' not found");

      typeAdapterField.setAccessible(true);
      exactTypeField.setAccessible(true);
      matchRawTypeField.setAccessible(true);

      assertSame(serializer, typeAdapterField.get(factory));
      assertEquals(exactType, exactTypeField.get(factory));
      assertTrue((Boolean) matchRawTypeField.get(factory));
    } catch (IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_exactTypeNotEqualsRawType_matchRawTypeFalse() {
    // Create a TypeToken with a parameterized type so getType()!=getRawType()
    TypeToken<java.util.List<String>> exactType = new TypeToken<java.util.List<String>>() {};
    JsonSerializer<java.util.List<String>> serializer = mock(JsonSerializer.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(exactType, serializer);

    assertNotNull(factory);

    try {
      Class<?> clazz = factory.getClass();

      Field matchRawTypeField = null;

      for (Field field : clazz.getDeclaredFields()) {
        if (field.getName().equals("matchRawType")) {
          matchRawTypeField = field;
          break;
        }
      }

      if (matchRawTypeField == null) {
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
          for (Field field : superClazz.getDeclaredFields()) {
            if (field.getName().equals("matchRawType")) {
              matchRawTypeField = field;
              break;
            }
          }
        }
      }

      assertNotNull(matchRawTypeField, "Field 'matchRawType' not found");

      matchRawTypeField.setAccessible(true);
      assertFalse((Boolean) matchRawTypeField.get(factory));
    } catch (IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}
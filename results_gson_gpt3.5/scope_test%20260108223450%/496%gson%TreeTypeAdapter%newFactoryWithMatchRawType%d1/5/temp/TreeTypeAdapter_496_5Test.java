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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;

class TreeTypeAdapter_496_5Test {

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_exactTypeEqualsRawType_shouldMatchRawTypeTrue() throws Exception {
    // Arrange
    @SuppressWarnings("unchecked")
    TypeToken<?> exactType = mock(TypeToken.class);
    Object typeAdapter = new Object();

    when(exactType.getType()).thenReturn((Type) String.class);
    // Fix: cast to Class<?> to satisfy Mockito's type expectations
    when(exactType.getRawType()).thenReturn((Class<?>) String.class);

    // Act
    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(exactType, typeAdapter);

    // Assert
    assertNotNull(factory);
    // Using reflection to verify SingleTypeFactory.matchRawType field is true
    Class<?> singleTypeFactoryClass = Class.forName("com.google.gson.internal.bind.TreeTypeAdapter$SingleTypeFactory");
    assertTrue(singleTypeFactoryClass.isInstance(factory));

    java.lang.reflect.Field matchRawTypeField = singleTypeFactoryClass.getDeclaredField("matchRawType");
    matchRawTypeField.setAccessible(true);
    boolean matchRawTypeValue = (boolean) matchRawTypeField.get(factory);
    assertTrue(matchRawTypeValue);

    java.lang.reflect.Field typeAdapterField = singleTypeFactoryClass.getDeclaredField("typeAdapter");
    typeAdapterField.setAccessible(true);
    assertSame(typeAdapter, typeAdapterField.get(factory));

    java.lang.reflect.Field exactTypeField = singleTypeFactoryClass.getDeclaredField("exactType");
    exactTypeField.setAccessible(true);
    assertSame(exactType, exactTypeField.get(factory));
  }

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_exactTypeNotEqualsRawType_shouldMatchRawTypeFalse() throws Exception {
    // Arrange
    @SuppressWarnings("unchecked")
    TypeToken<?> exactType = mock(TypeToken.class);
    Object typeAdapter = new Object();

    when(exactType.getType()).thenReturn((Type) String.class);
    // Fix: cast to Class<?> to satisfy Mockito's type expectations
    when(exactType.getRawType()).thenReturn((Class<?>) Object.class);

    // Act
    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(exactType, typeAdapter);

    // Assert
    assertNotNull(factory);
    // Using reflection to verify SingleTypeFactory.matchRawType field is false
    Class<?> singleTypeFactoryClass = Class.forName("com.google.gson.internal.bind.TreeTypeAdapter$SingleTypeFactory");
    assertTrue(singleTypeFactoryClass.isInstance(factory));

    java.lang.reflect.Field matchRawTypeField = singleTypeFactoryClass.getDeclaredField("matchRawType");
    matchRawTypeField.setAccessible(true);
    boolean matchRawTypeValue = (boolean) matchRawTypeField.get(factory);
    assertFalse(matchRawTypeValue);

    java.lang.reflect.Field typeAdapterField = singleTypeFactoryClass.getDeclaredField("typeAdapter");
    typeAdapterField.setAccessible(true);
    assertSame(typeAdapter, typeAdapterField.get(factory));

    java.lang.reflect.Field exactTypeField = singleTypeFactoryClass.getDeclaredField("exactType");
    exactTypeField.setAccessible(true);
    assertSame(exactType, exactTypeField.get(factory));
  }
}
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
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class TreeTypeAdapter_496_2Test {

  @Test
    @Timeout(8000)
  public void testNewFactoryWithMatchRawType_exactTypeIsRawType() throws Exception {
    // Arrange
    TypeToken<?> rawTypeToken = TypeToken.get(String.class);

    // Create a mock JsonSerializer to pass as typeAdapter to satisfy Preconditions.checkArgument
    JsonSerializer<?> mockSerializer = mock(JsonSerializer.class);

    // Act
    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(rawTypeToken, mockSerializer);

    // Assert
    assertNotNull(factory);

    // Use reflection to check the private boolean matchRawType field inside SingleTypeFactory
    Class<?> singleTypeFactoryClass = factory.getClass();
    Field matchRawTypeField = singleTypeFactoryClass.getDeclaredField("matchRawType");
    matchRawTypeField.setAccessible(true);
    boolean matchRawTypeValue = matchRawTypeField.getBoolean(factory);

    // Since exactType.getType() == exactType.getRawType() for rawTypeToken, matchRawType should be true
    assertTrue(matchRawTypeValue);
  }

  @Test
    @Timeout(8000)
  public void testNewFactoryWithMatchRawType_exactTypeIsNotRawType() throws Exception {
    // Arrange
    TypeToken<?> genericTypeToken = new TypeToken<java.util.List<String>>() {};

    // Create a mock JsonSerializer to pass as typeAdapter to satisfy Preconditions.checkArgument
    JsonSerializer<?> mockSerializer = mock(JsonSerializer.class);

    // Act
    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(genericTypeToken, mockSerializer);

    // Assert
    assertNotNull(factory);

    // Use reflection to check the private boolean matchRawType field inside SingleTypeFactory
    Class<?> singleTypeFactoryClass = factory.getClass();
    Field matchRawTypeField = singleTypeFactoryClass.getDeclaredField("matchRawType");
    matchRawTypeField.setAccessible(true);
    boolean matchRawTypeValue = matchRawTypeField.getBoolean(factory);

    // exactType.getType() != exactType.getRawType() for genericTypeToken, so matchRawType should be false
    assertFalse(matchRawTypeValue);
  }
}
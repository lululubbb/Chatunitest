package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ExclusionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

class Excluder_447_1Test {

  private Excluder excluder;
  private ExclusionStrategy mockStrategy;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
    mockStrategy = mock(ExclusionStrategy.class);
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationTrue_deserializationFalse_addsToSerializationStrategies() {
    Excluder result = excluder.withExclusionStrategy(mockStrategy, true, false);

    assertNotSame(excluder, result);
    List<ExclusionStrategy> serializationStrategies = getPrivateList(result, "serializationStrategies");
    List<ExclusionStrategy> deserializationStrategies = getPrivateList(result, "deserializationStrategies");

    assertTrue(serializationStrategies.contains(mockStrategy));
    assertEquals(1, serializationStrategies.size());
    assertTrue(deserializationStrategies.isEmpty());
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationFalse_deserializationTrue_addsToDeserializationStrategies() {
    Excluder result = excluder.withExclusionStrategy(mockStrategy, false, true);

    assertNotSame(excluder, result);
    List<ExclusionStrategy> serializationStrategies = getPrivateList(result, "serializationStrategies");
    List<ExclusionStrategy> deserializationStrategies = getPrivateList(result, "deserializationStrategies");

    assertTrue(deserializationStrategies.contains(mockStrategy));
    assertEquals(1, deserializationStrategies.size());
    assertTrue(serializationStrategies.isEmpty());
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationTrue_deserializationTrue_addsToBothStrategies() {
    Excluder result = excluder.withExclusionStrategy(mockStrategy, true, true);

    assertNotSame(excluder, result);
    List<ExclusionStrategy> serializationStrategies = getPrivateList(result, "serializationStrategies");
    List<ExclusionStrategy> deserializationStrategies = getPrivateList(result, "deserializationStrategies");

    assertTrue(serializationStrategies.contains(mockStrategy));
    assertTrue(deserializationStrategies.contains(mockStrategy));
    assertEquals(1, serializationStrategies.size());
    assertEquals(1, deserializationStrategies.size());
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationFalse_deserializationFalse_noStrategiesAdded() {
    Excluder result = excluder.withExclusionStrategy(mockStrategy, false, false);

    assertNotSame(excluder, result);
    List<ExclusionStrategy> serializationStrategies = getPrivateList(result, "serializationStrategies");
    List<ExclusionStrategy> deserializationStrategies = getPrivateList(result, "deserializationStrategies");

    assertTrue(serializationStrategies.isEmpty());
    assertTrue(deserializationStrategies.isEmpty());
  }

  @SuppressWarnings("unchecked")
  private List<ExclusionStrategy> getPrivateList(Excluder excluder, String fieldName) {
    try {
      Field field = Excluder.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (List<ExclusionStrategy>) field.get(excluder);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
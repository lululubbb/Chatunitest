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

class Excluder_447_6Test {

  private Excluder excluder;
  private ExclusionStrategy exclusionStrategy;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
    exclusionStrategy = mock(ExclusionStrategy.class);
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationTrue_deserializationFalse() throws Exception {
    Excluder result = excluder.withExclusionStrategy(exclusionStrategy, true, false);

    assertNotSame(excluder, result);

    // serializationStrategies should contain the new exclusionStrategy
    List<ExclusionStrategy> serializationStrategies = getPrivateField(result, "serializationStrategies");
    assertNotNull(serializationStrategies);
    assertTrue(serializationStrategies.contains(exclusionStrategy));

    // deserializationStrategies should be empty list (original)
    List<ExclusionStrategy> deserializationStrategies = getPrivateField(result, "deserializationStrategies");
    assertNotNull(deserializationStrategies);
    assertTrue(deserializationStrategies.isEmpty());
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationFalse_deserializationTrue() throws Exception {
    Excluder result = excluder.withExclusionStrategy(exclusionStrategy, false, true);

    assertNotSame(excluder, result);

    // serializationStrategies should be empty list (original)
    List<ExclusionStrategy> serializationStrategies = getPrivateField(result, "serializationStrategies");
    assertNotNull(serializationStrategies);
    assertTrue(serializationStrategies.isEmpty());

    // deserializationStrategies should contain the new exclusionStrategy
    List<ExclusionStrategy> deserializationStrategies = getPrivateField(result, "deserializationStrategies");
    assertNotNull(deserializationStrategies);
    assertTrue(deserializationStrategies.contains(exclusionStrategy));
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationTrue_deserializationTrue() throws Exception {
    Excluder result = excluder.withExclusionStrategy(exclusionStrategy, true, true);

    assertNotSame(excluder, result);

    List<ExclusionStrategy> serializationStrategies = getPrivateField(result, "serializationStrategies");
    assertNotNull(serializationStrategies);
    assertTrue(serializationStrategies.contains(exclusionStrategy));

    List<ExclusionStrategy> deserializationStrategies = getPrivateField(result, "deserializationStrategies");
    assertNotNull(deserializationStrategies);
    assertTrue(deserializationStrategies.contains(exclusionStrategy));
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationFalse_deserializationFalse() throws Exception {
    Excluder result = excluder.withExclusionStrategy(exclusionStrategy, false, false);

    assertNotSame(excluder, result);

    List<ExclusionStrategy> serializationStrategies = getPrivateField(result, "serializationStrategies");
    assertNotNull(serializationStrategies);
    assertTrue(serializationStrategies.isEmpty());

    List<ExclusionStrategy> deserializationStrategies = getPrivateField(result, "deserializationStrategies");
    assertNotNull(deserializationStrategies);
    assertTrue(deserializationStrategies.isEmpty());
  }

  // Helper method to access private fields via reflection
  @SuppressWarnings("unchecked")
  private <T> T getPrivateField(Object target, String fieldName) throws Exception {
    Field field = Excluder.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }
}
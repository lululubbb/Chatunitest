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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ExclusionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

class Excluder_447_2Test {

  private Excluder excluder;
  private ExclusionStrategy mockStrategy;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
    mockStrategy = mock(ExclusionStrategy.class);
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_neitherSerializationNorDeserialization_returnsCloneWithoutStrategies() {
    Excluder result = excluder.withExclusionStrategy(mockStrategy, false, false);

    assertNotSame(excluder, result);
    // Original Excluder has empty lists
    assertTrue(((List<?>) getField(excluder, "serializationStrategies")).isEmpty());
    assertTrue(((List<?>) getField(excluder, "deserializationStrategies")).isEmpty());
    // Result should also have empty lists since no serialization or deserialization requested
    assertNotNull(getField(result, "serializationStrategies"));
    assertNotNull(getField(result, "deserializationStrategies"));
    assertTrue(((List<?>) getField(result, "serializationStrategies")).isEmpty());
    assertTrue(((List<?>) getField(result, "deserializationStrategies")).isEmpty());
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_onlySerialization_addsStrategyToSerializationStrategies() {
    Excluder result = excluder.withExclusionStrategy(mockStrategy, true, false);

    assertNotSame(excluder, result);
    // Original Excluder empty lists
    assertTrue(((List<?>) getField(excluder, "serializationStrategies")).isEmpty());
    assertTrue(((List<?>) getField(excluder, "deserializationStrategies")).isEmpty());

    List<ExclusionStrategy> serializationStrategies = getField(result, "serializationStrategies");
    List<ExclusionStrategy> deserializationStrategies = getField(result, "deserializationStrategies");

    assertNotNull(serializationStrategies);
    assertNotNull(deserializationStrategies);

    assertEquals(1, serializationStrategies.size());
    assertSame(mockStrategy, serializationStrategies.get(0));
    // Deserialization list remains empty
    assertTrue(deserializationStrategies.isEmpty());
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_onlyDeserialization_addsStrategyToDeserializationStrategies() {
    Excluder result = excluder.withExclusionStrategy(mockStrategy, false, true);

    assertNotSame(excluder, result);
    // Original Excluder empty lists
    assertTrue(((List<?>) getField(excluder, "serializationStrategies")).isEmpty());
    assertTrue(((List<?>) getField(excluder, "deserializationStrategies")).isEmpty());

    List<ExclusionStrategy> serializationStrategies = getField(result, "serializationStrategies");
    List<ExclusionStrategy> deserializationStrategies = getField(result, "deserializationStrategies");

    assertNotNull(serializationStrategies);
    assertNotNull(deserializationStrategies);

    assertTrue(serializationStrategies.isEmpty());
    assertEquals(1, deserializationStrategies.size());
    assertSame(mockStrategy, deserializationStrategies.get(0));
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_bothSerializationAndDeserialization_addsStrategyToBoth() {
    Excluder result = excluder.withExclusionStrategy(mockStrategy, true, true);

    assertNotSame(excluder, result);
    // Original Excluder empty lists
    assertTrue(((List<?>) getField(excluder, "serializationStrategies")).isEmpty());
    assertTrue(((List<?>) getField(excluder, "deserializationStrategies")).isEmpty());

    List<ExclusionStrategy> serializationStrategies = getField(result, "serializationStrategies");
    List<ExclusionStrategy> deserializationStrategies = getField(result, "deserializationStrategies");

    assertNotNull(serializationStrategies);
    assertNotNull(deserializationStrategies);

    assertEquals(1, serializationStrategies.size());
    assertEquals(1, deserializationStrategies.size());
    assertSame(mockStrategy, serializationStrategies.get(0));
    assertSame(mockStrategy, deserializationStrategies.get(0));
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_originalStrategiesCopiedAndNotModified() {
    // Prepare excluder with existing strategies
    ExclusionStrategy existingSerializationStrategy = mock(ExclusionStrategy.class);
    ExclusionStrategy existingDeserializationStrategy = mock(ExclusionStrategy.class);

    // Use reflection to set private fields serializationStrategies and deserializationStrategies
    setField(excluder, "serializationStrategies", List.of(existingSerializationStrategy));
    setField(excluder, "deserializationStrategies", List.of(existingDeserializationStrategy));

    Excluder result = excluder.withExclusionStrategy(mockStrategy, true, true);

    // Original lists remain unchanged and size 1
    List<?> origSerialization = getField(excluder, "serializationStrategies");
    List<?> origDeserialization = getField(excluder, "deserializationStrategies");
    assertEquals(1, origSerialization.size());
    assertEquals(existingSerializationStrategy, origSerialization.get(0));
    assertEquals(1, origDeserialization.size());
    assertEquals(existingDeserializationStrategy, origDeserialization.get(0));

    // Result lists contain previous + new strategy (size 2)
    List<?> resultSerialization = getField(result, "serializationStrategies");
    List<?> resultDeserialization = getField(result, "deserializationStrategies");
    assertEquals(2, resultSerialization.size());
    assertTrue(resultSerialization.contains(existingSerializationStrategy));
    assertTrue(resultSerialization.contains(mockStrategy));
    assertEquals(2, resultDeserialization.size());
    assertTrue(resultDeserialization.contains(existingDeserializationStrategy));
    assertTrue(resultDeserialization.contains(mockStrategy));
  }

  // Helper methods to get/set private fields by reflection
  @SuppressWarnings("unchecked")
  private <T> T getField(Object target, String fieldName) {
    try {
      Field field = Excluder.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T) field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = Excluder.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
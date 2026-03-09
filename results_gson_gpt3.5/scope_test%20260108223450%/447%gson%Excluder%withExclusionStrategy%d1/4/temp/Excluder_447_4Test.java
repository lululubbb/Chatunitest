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

class Excluder_447_4Test {

  private Excluder excluder;
  private ExclusionStrategy serializationStrategyMock;
  private ExclusionStrategy deserializationStrategyMock;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
    serializationStrategyMock = mock(ExclusionStrategy.class);
    deserializationStrategyMock = mock(ExclusionStrategy.class);
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationTrue_deserializationFalse_addsToSerializationStrategies() throws Exception {
    Excluder result = excluder.withExclusionStrategy(serializationStrategyMock, true, false);

    assertNotSame(excluder, result);
    List<ExclusionStrategy> serializationStrategies = getFieldValue(result, "serializationStrategies");
    List<ExclusionStrategy> deserializationStrategies = getFieldValue(result, "deserializationStrategies");
    assertNotNull(serializationStrategies);
    assertTrue(serializationStrategies.contains(serializationStrategyMock));
    assertNotNull(deserializationStrategies);
    assertEquals(0, deserializationStrategies.size());
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationFalse_deserializationTrue_addsToDeserializationStrategies() throws Exception {
    Excluder result = excluder.withExclusionStrategy(deserializationStrategyMock, false, true);

    assertNotSame(excluder, result);
    List<ExclusionStrategy> serializationStrategies = getFieldValue(result, "serializationStrategies");
    List<ExclusionStrategy> deserializationStrategies = getFieldValue(result, "deserializationStrategies");
    assertNotNull(deserializationStrategies);
    assertTrue(deserializationStrategies.contains(deserializationStrategyMock));
    assertNotNull(serializationStrategies);
    assertEquals(0, serializationStrategies.size());
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_bothTrue_addsToBothStrategies() throws Exception {
    Excluder result = excluder.withExclusionStrategy(serializationStrategyMock, true, true);

    assertNotSame(excluder, result);
    List<ExclusionStrategy> serializationStrategies = getFieldValue(result, "serializationStrategies");
    List<ExclusionStrategy> deserializationStrategies = getFieldValue(result, "deserializationStrategies");
    assertNotNull(serializationStrategies);
    assertTrue(serializationStrategies.contains(serializationStrategyMock));
    assertNotNull(deserializationStrategies);
    assertTrue(deserializationStrategies.contains(serializationStrategyMock));
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_bothFalse_returnsCloneWithoutAdding() throws Exception {
    Excluder result = excluder.withExclusionStrategy(serializationStrategyMock, false, false);

    assertNotSame(excluder, result);
    List<ExclusionStrategy> serializationStrategies = getFieldValue(result, "serializationStrategies");
    List<ExclusionStrategy> deserializationStrategies = getFieldValue(result, "deserializationStrategies");
    assertNotNull(serializationStrategies);
    assertEquals(0, serializationStrategies.size());
    assertNotNull(deserializationStrategies);
    assertEquals(0, deserializationStrategies.size());
  }

  @SuppressWarnings("unchecked")
  private <T> T getFieldValue(Object instance, String fieldName) throws Exception {
    Field field = Excluder.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(instance);
  }
}
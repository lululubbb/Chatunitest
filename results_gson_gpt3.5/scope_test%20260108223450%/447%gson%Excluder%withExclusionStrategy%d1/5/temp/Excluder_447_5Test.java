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

class ExcluderWithExclusionStrategyTest {

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

    // Original excluder strategies remain empty
    assertTrue(getField(excluder, "serializationStrategies").isEmpty());
    assertTrue(getField(excluder, "deserializationStrategies").isEmpty());

    // Result strategies contain the added strategy only in serializationStrategies
    List<ExclusionStrategy> serializationStrategies = getField(result, "serializationStrategies");
    List<ExclusionStrategy> deserializationStrategies = getField(result, "deserializationStrategies");

    assertNotNull(serializationStrategies);
    assertNotNull(deserializationStrategies);

    assertEquals(1, serializationStrategies.size());
    assertSame(mockStrategy, serializationStrategies.get(0));

    assertTrue(deserializationStrategies.isEmpty());

    // The result is a different instance
    assertNotSame(excluder, result);
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationFalse_deserializationTrue_addsToDeserializationStrategies() {
    Excluder result = excluder.withExclusionStrategy(mockStrategy, false, true);

    // Original excluder strategies remain empty
    assertTrue(getField(excluder, "serializationStrategies").isEmpty());
    assertTrue(getField(excluder, "deserializationStrategies").isEmpty());

    List<ExclusionStrategy> serializationStrategies = getField(result, "serializationStrategies");
    List<ExclusionStrategy> deserializationStrategies = getField(result, "deserializationStrategies");

    assertNotNull(serializationStrategies);
    assertNotNull(deserializationStrategies);

    assertTrue(serializationStrategies.isEmpty());

    assertEquals(1, deserializationStrategies.size());
    assertSame(mockStrategy, deserializationStrategies.get(0));

    assertNotSame(excluder, result);
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationTrue_deserializationTrue_addsToBothStrategies() {
    Excluder result = excluder.withExclusionStrategy(mockStrategy, true, true);

    assertTrue(getField(excluder, "serializationStrategies").isEmpty());
    assertTrue(getField(excluder, "deserializationStrategies").isEmpty());

    List<ExclusionStrategy> serializationStrategies = getField(result, "serializationStrategies");
    List<ExclusionStrategy> deserializationStrategies = getField(result, "deserializationStrategies");

    assertNotNull(serializationStrategies);
    assertNotNull(deserializationStrategies);

    assertEquals(1, serializationStrategies.size());
    assertSame(mockStrategy, serializationStrategies.get(0));

    assertEquals(1, deserializationStrategies.size());
    assertSame(mockStrategy, deserializationStrategies.get(0));

    assertNotSame(excluder, result);
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationFalse_deserializationFalse_returnsCloneWithoutChanges() {
    Excluder result = excluder.withExclusionStrategy(mockStrategy, false, false);

    // Strategies remain empty
    assertTrue(getField(result, "serializationStrategies").isEmpty());
    assertTrue(getField(result, "deserializationStrategies").isEmpty());

    // The result is a different instance
    assertNotSame(excluder, result);
  }

  @SuppressWarnings("unchecked")
  private List<ExclusionStrategy> getField(Excluder excluderInstance, String fieldName) {
    try {
      Field field = Excluder.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (List<ExclusionStrategy>) field.get(excluderInstance);
    } catch (Exception e) {
      fail("Reflection failed to get field: " + fieldName);
      return null;
    }
  }
}
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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ExclusionStrategy;
import java.util.List;
import org.junit.jupiter.api.Test;

class Excluder_447_3Test {

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationTrue_deserializationFalse_addsToSerializationStrategies() {
    Excluder original = new Excluder();
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);

    Excluder result = original.withExclusionStrategy(strategy, true, false);

    // Original remains unchanged
    assertNotSame(original, result);
    assertTrue(isEmptyList(getField(original, "serializationStrategies")));
    assertTrue(isEmptyList(getField(original, "deserializationStrategies")));

    // Result has new strategy in serializationStrategies
    List<ExclusionStrategy> serializationStrategies = getField(result, "serializationStrategies");
    assertEquals(1, serializationStrategies.size());
    assertSame(strategy, serializationStrategies.get(0));

    // deserializationStrategies remains empty
    List<ExclusionStrategy> deserializationStrategies = getField(result, "deserializationStrategies");
    assertTrue(isEmptyList(deserializationStrategies));
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationFalse_deserializationTrue_addsToDeserializationStrategies() {
    Excluder original = new Excluder();
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);

    Excluder result = original.withExclusionStrategy(strategy, false, true);

    // Original remains unchanged
    assertNotSame(original, result);
    assertTrue(isEmptyList(getField(original, "serializationStrategies")));
    assertTrue(isEmptyList(getField(original, "deserializationStrategies")));

    // serializationStrategies remains empty
    List<ExclusionStrategy> serializationStrategies = getField(result, "serializationStrategies");
    assertTrue(isEmptyList(serializationStrategies));

    // Result has new strategy in deserializationStrategies
    List<ExclusionStrategy> deserializationStrategies = getField(result, "deserializationStrategies");
    assertEquals(1, deserializationStrategies.size());
    assertSame(strategy, deserializationStrategies.get(0));
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationTrue_deserializationTrue_addsToBothStrategies() {
    Excluder original = new Excluder();
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);

    Excluder result = original.withExclusionStrategy(strategy, true, true);

    // Original remains unchanged
    assertNotSame(original, result);
    assertTrue(isEmptyList(getField(original, "serializationStrategies")));
    assertTrue(isEmptyList(getField(original, "deserializationStrategies")));

    // Result has new strategy in serializationStrategies
    List<ExclusionStrategy> serializationStrategies = getField(result, "serializationStrategies");
    assertEquals(1, serializationStrategies.size());
    assertSame(strategy, serializationStrategies.get(0));

    // Result has new strategy in deserializationStrategies
    List<ExclusionStrategy> deserializationStrategies = getField(result, "deserializationStrategies");
    assertEquals(1, deserializationStrategies.size());
    assertSame(strategy, deserializationStrategies.get(0));
  }

  @Test
    @Timeout(8000)
  void withExclusionStrategy_serializationFalse_deserializationFalse_returnsCloneWithEmptyLists() {
    Excluder original = new Excluder();
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);

    Excluder result = original.withExclusionStrategy(strategy, false, false);

    // Original remains unchanged
    assertNotSame(original, result);
    assertTrue(isEmptyList(getField(original, "serializationStrategies")));
    assertTrue(isEmptyList(getField(original, "deserializationStrategies")));

    // Result's serializationStrategies unchanged (empty)
    List<ExclusionStrategy> serializationStrategies = getField(result, "serializationStrategies");
    assertTrue(isEmptyList(serializationStrategies));

    // Result's deserializationStrategies unchanged (empty)
    List<ExclusionStrategy> deserializationStrategies = getField(result, "deserializationStrategies");
    assertTrue(isEmptyList(deserializationStrategies));
  }

  @SuppressWarnings("unchecked")
  private <T> T getField(Object instance, String fieldName) {
    try {
      java.lang.reflect.Field field = Excluder.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T) field.get(instance);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private boolean isEmptyList(Object obj) {
    if (obj instanceof List) {
      return ((List<?>) obj).isEmpty();
    }
    return false;
  }
}
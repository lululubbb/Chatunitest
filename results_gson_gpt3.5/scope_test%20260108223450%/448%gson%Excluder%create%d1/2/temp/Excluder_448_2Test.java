package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

class Excluder_448_2Test {

  private Excluder excluder;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnNull_whenClassNotExcluded() {
    Gson gson = mock(Gson.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    TypeAdapter<String> adapter = excluder.create(gson, typeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnTypeAdapter_whenClassExcludedByExcludeClassChecks() throws Exception {
    Gson gson = mock(Gson.class);

    // Use a local class to trigger excludeClassChecks returning true
    class LocalClass {}
    TypeToken<LocalClass> localType = TypeToken.get(LocalClass.class);

    Excluder spyExcluder = Mockito.spy(excluder);

    // Directly invoke create, the local class should trigger excludeClassChecks true naturally
    TypeAdapter<?> adapter = spyExcluder.create(gson, localType);
    assertNotNull(adapter);

    JsonReader jsonReader = mock(JsonReader.class);
    JsonWriter jsonWriter = mock(JsonWriter.class);

    // Because skipDeserialize is true, read should skip value and return null
    Object readResult = adapter.read(jsonReader);
    verify(jsonReader).skipValue();
    assertNull(readResult);

    // Because skipSerialize is true, write should write nullValue
    // Fix the incompatible type error by casting adapter to raw TypeAdapter and suppressing unchecked warning
    @SuppressWarnings({"unchecked", "rawtypes"})
    TypeAdapter rawAdapter = (TypeAdapter) adapter;
    rawAdapter.write(jsonWriter, new LocalClass());
    verify(jsonWriter).nullValue();
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnTypeAdapter_whenClassExcludedByExcludeClassInStrategy() throws Exception {
    Gson gson = mock(Gson.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Use reflection to create a spy Excluder and override private methods via reflection
    Excluder spyExcluder = Mockito.spy(excluder);

    // Use reflection to override excludeClassChecks to always return false
    Method excludeClassChecksMethod = Excluder.class.getDeclaredMethod("excludeClassChecks", Class.class);
    excludeClassChecksMethod.setAccessible(true);
    Method excludeClassInStrategyMethod = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
    excludeClassInStrategyMethod.setAccessible(true);

    // Use Mockito to stub excludeClassChecks and excludeClassInStrategy via spy and reflection
    // Since private methods cannot be stubbed directly, we use doReturn on spyExcluder with doAnswer on create

    // Instead, we create a proxy TypeAdapter by calling create and intercepting the calls to private methods via spy
    // Since mocking private methods is not possible, we simulate behavior by creating a new Excluder subclass with public methods

    // To avoid final class subclassing error, use reflection to create a proxy Excluder instance with overridden behavior
    // But since Excluder is final, we cannot subclass it, so use a helper class that wraps Excluder and delegates calls

    // Instead, we create a custom ExcluderDelegate that implements TypeAdapterFactory and uses Excluder internally,
    // but overrides excludeClassChecks and excludeClassInStrategy via reflection on the Excluder instance.

    // For this test, we simulate excludeClassChecks returning false and excludeClassInStrategy returning true for serialize, false for deserialize
    // by creating a spyExcluder and mocking create method to call a helper method that uses reflection to override private methods

    // Since the above is complicated, the simplest fix is to use reflection to set fields to simulate the behavior.

    // Use reflection to set serializationStrategies and deserializationStrategies to simulate excludeClassInStrategy returning true for serialize and false for deserialize

    // Create dummy ExclusionStrategy that returns true for serialize and false for deserialize
    spyExcluder.serializationStrategies = java.util.Collections.singletonList(new com.google.gson.ExclusionStrategy() {
      @Override
      public boolean shouldSkipField(com.google.gson.FieldAttributes f) {
        return false;
      }
      @Override
      public boolean shouldSkipClass(Class<?> clazz) {
        return true;
      }
    });
    spyExcluder.deserializationStrategies = java.util.Collections.emptyList();

    TypeAdapter<String> adapter = spyExcluder.create(gson, typeToken);
    assertNotNull(adapter);

    JsonWriter jsonWriter = mock(JsonWriter.class);
    JsonReader jsonReader = mock(JsonReader.class);

    // skipSerialize is true, so write should write nullValue
    adapter.write(jsonWriter, "value");
    verify(jsonWriter).nullValue();

    // skipDeserialize is false, so read should delegate to delegate adapter
    @SuppressWarnings("unchecked")
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(spyExcluder, typeToken)).thenReturn(delegateAdapter);
    when(delegateAdapter.read(jsonReader)).thenReturn("readValue");

    String readValue = adapter.read(jsonReader);
    assertEquals("readValue", readValue);
  }

  @Test
    @Timeout(8000)
  void create_delegateIsLazilyCreatedAndCached() throws Exception {
    Gson gson = mock(Gson.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Use reflection to set serializationStrategies and deserializationStrategies to simulate excludeClassInStrategy returning false for serialize and true for deserialize
    Excluder spyExcluder = Mockito.spy(excluder);

    spyExcluder.serializationStrategies = java.util.Collections.emptyList();
    spyExcluder.deserializationStrategies = java.util.Collections.singletonList(new com.google.gson.ExclusionStrategy() {
      @Override
      public boolean shouldSkipField(com.google.gson.FieldAttributes f) {
        return false;
      }
      @Override
      public boolean shouldSkipClass(Class<?> clazz) {
        return true;
      }
    });

    TypeAdapter<String> adapter = spyExcluder.create(gson, typeToken);
    assertNotNull(adapter);

    JsonReader jsonReader = mock(JsonReader.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(spyExcluder, typeToken)).thenReturn(delegateAdapter);
    when(delegateAdapter.read(jsonReader)).thenReturn("delegateRead");

    // First call to read invokes delegate()
    String result1 = adapter.read(jsonReader);
    assertEquals("delegateRead", result1);

    // Second call should reuse cached delegate, so getDelegateAdapter called only once
    String result2 = adapter.read(jsonReader);
    assertEquals("delegateRead", result2);

    verify(gson, times(1)).getDelegateAdapter(spyExcluder, typeToken);
    verify(delegateAdapter, times(2)).read(jsonReader);
  }
}
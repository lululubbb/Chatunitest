package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.FieldAttributes;
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

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class Excluder_448_5Test {

  private Excluder excluder;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
  }

  // Since Excluder is final, we cannot extend it.
  // Instead, we create a wrapper class that delegates to an Excluder instance,
  // and overrides the methods excludeClassChecks and excludeClassInStrategy by delegation.

  static class TestExcluder implements TypeAdapterFactory {
    final Excluder delegate;
    private final boolean excludeClassChecksValue;
    private final boolean excludeClassInStrategySerializeValue;
    private final boolean excludeClassInStrategyDeserializeValue;

    TestExcluder(boolean excludeClassChecksValue,
                 boolean excludeClassInStrategySerializeValue,
                 boolean excludeClassInStrategyDeserializeValue) {
      this.delegate = new Excluder();
      this.excludeClassChecksValue = excludeClassChecksValue;
      this.excludeClassInStrategySerializeValue = excludeClassInStrategySerializeValue;
      this.excludeClassInStrategyDeserializeValue = excludeClassInStrategyDeserializeValue;
    }

    private boolean excludeClassChecks(Class<?> clazz) {
      return excludeClassChecksValue;
    }

    private boolean excludeClassInStrategy(Class<?> clazz, boolean serialize) {
      return serialize ? excludeClassInStrategySerializeValue : excludeClassInStrategyDeserializeValue;
    }

    // We need to replicate the create method logic with our overrides for excludeClassChecks and excludeClassInStrategy
    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
      Class<?> rawType = type.getRawType();

      // Use overridden excludeClassChecks
      boolean excludeClass = excludeClassChecks(rawType);

      // Use overridden excludeClassInStrategy
      final boolean skipSerialize = excludeClass || excludeClassInStrategy(rawType, true);
      final boolean skipDeserialize = excludeClass || excludeClassInStrategy(rawType, false);

      if (!skipSerialize && !skipDeserialize) {
        return null;
      }

      return new TypeAdapter<T>() {
        private TypeAdapter<T> delegateAdapter;

        @Override
        public T read(JsonReader in) throws IOException {
          if (skipDeserialize) {
            in.skipValue();
            return null;
          }
          return delegate().read(in);
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
          if (skipSerialize) {
            out.nullValue();
            return;
          }
          delegate().write(out, value);
        }

        private TypeAdapter<T> delegate() {
          TypeAdapter<T> d = delegateAdapter;
          return d != null ? d : (delegateAdapter = gson.getDelegateAdapter(delegate, type));
        }
      };
    }
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnNullIfNotExcluded() {
    Gson gson = mock(Gson.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    TypeAdapter<String> adapter = excluder.create(gson, typeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnTypeAdapterThatSkipsDeserialize() throws IOException {
    Gson gson = mock(Gson.class);
    TypeToken<Object> typeToken = TypeToken.get(Object.class);

    TestExcluder testExcluder = new TestExcluder(true, false, false);

    TypeAdapter<Object> adapter = testExcluder.create(gson, typeToken);

    assertNotNull(adapter);

    JsonReader jsonReader = mock(JsonReader.class);
    Object result = adapter.read(jsonReader);

    verify(jsonReader).skipValue();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnTypeAdapterThatSkipsSerialize() throws IOException {
    Gson gson = mock(Gson.class);
    TypeToken<Object> typeToken = TypeToken.get(Object.class);

    TestExcluder testExcluder = new TestExcluder(false, true, false);

    TypeAdapter<Object> adapter = testExcluder.create(gson, typeToken);

    assertNotNull(adapter);

    JsonWriter jsonWriter = mock(JsonWriter.class);
    Object value = new Object();
    adapter.write(jsonWriter, value);

    verify(jsonWriter).nullValue();
  }

  @Test
    @Timeout(8000)
  void create_shouldDelegateReadAndWriteWhenNotSkipped() throws IOException {
    Gson gson = mock(Gson.class);
    TypeToken<Object> typeToken = TypeToken.get(Object.class);

    TestExcluder testExcluder = new TestExcluder(false, false, false);

    TypeAdapter<Object> adapter = testExcluder.create(gson, typeToken);

    assertNull(adapter);

    // Now test with exclusion to get non-null adapter and delegate calls
    testExcluder = new TestExcluder(true, false, false);

    TypeAdapter<Object> delegateAdapter = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(testExcluder.delegate, typeToken)).thenReturn(delegateAdapter);

    adapter = testExcluder.create(gson, typeToken);
    assertNotNull(adapter);

    JsonReader jsonReader = mock(JsonReader.class);
    Object expectedRead = new Object();
    when(delegateAdapter.read(jsonReader)).thenReturn(expectedRead);
    Object actualRead = adapter.read(jsonReader);
    assertSame(expectedRead, actualRead);

    JsonWriter jsonWriter = mock(JsonWriter.class);
    Object value = new Object();
    adapter.write(jsonWriter, value);
    verify(delegateAdapter).write(jsonWriter, value);
  }
}
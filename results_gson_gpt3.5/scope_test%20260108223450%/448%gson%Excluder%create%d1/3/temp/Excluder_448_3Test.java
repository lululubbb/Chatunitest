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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

class Excluder_448_3Test {

  private Excluder excluder;
  private Gson mockGson;

  @BeforeEach
  void setUp() {
    excluder = Excluder.DEFAULT;
    mockGson = mock(Gson.class);
  }

  // Helper class to simulate Excluder behavior by composition and reflection
  private static class ExcluderWithOverrides {
    private final Boolean excludeClassChecksResult;
    private final Boolean excludeClassInStrategySerializeResult;
    private final Boolean excludeClassInStrategyDeserializeResult;

    private final Excluder delegate = Excluder.DEFAULT;

    ExcluderWithOverrides(Boolean excludeClassChecksResult,
                          Boolean excludeClassInStrategySerializeResult,
                          Boolean excludeClassInStrategyDeserializeResult) {
      this.excludeClassChecksResult = excludeClassChecksResult;
      this.excludeClassInStrategySerializeResult = excludeClassInStrategySerializeResult;
      this.excludeClassInStrategyDeserializeResult = excludeClassInStrategyDeserializeResult;
    }

    private boolean invokeExcludeClassChecks(Class<?> clazz) {
      if (excludeClassChecksResult != null) {
        return excludeClassChecksResult;
      }
      try {
        Method method = Excluder.class.getDeclaredMethod("excludeClassChecks", Class.class);
        method.setAccessible(true);
        return (boolean) method.invoke(delegate, clazz);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }

    private boolean invokeExcludeClassInStrategy(Class<?> clazz, boolean serialization) {
      if (excludeClassInStrategySerializeResult != null && excludeClassInStrategyDeserializeResult != null) {
        return serialization ? excludeClassInStrategySerializeResult : excludeClassInStrategyDeserializeResult;
      }
      try {
        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);
        return (boolean) method.invoke(delegate, clazz, serialization);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }

    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
      Class<?> rawType = type.getRawType();
      boolean excludeClass = invokeExcludeClassChecks(rawType);

      final boolean skipSerialize = excludeClass || invokeExcludeClassInStrategy(rawType, true);
      final boolean skipDeserialize = excludeClass || invokeExcludeClassInStrategy(rawType, false);

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
          return d != null
              ? d
              : (delegateAdapter = gson.getDelegateAdapter(delegate, type));
        }
      };
    }
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnNull_whenClassNotExcluded() {
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // By default Excluder.DEFAULT does not exclude String class
    TypeAdapter<String> adapter = excluder.create(mockGson, typeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnAdapterThatSkipsSerialization_whenClassExcludedByExcludeClassChecks() throws IOException {
    ExcluderWithOverrides excluderSpy = new ExcluderWithOverrides(true, null, null);

    TypeToken<String> typeToken = TypeToken.get(String.class);

    TypeAdapter<String> adapter = excluderSpy.create(mockGson, typeToken);
    assertNotNull(adapter);

    JsonWriter mockWriter = mock(JsonWriter.class);
    adapter.write(mockWriter, "value");

    // Because skipSerialize is true, write should call out.nullValue()
    verify(mockWriter).nullValue();
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnAdapterThatSkipsDeserialization_whenClassExcludedByExcludeClassInStrategy() throws IOException {
    ExcluderWithOverrides excluderSpy = new ExcluderWithOverrides(false, false, true);

    TypeToken<String> typeToken = TypeToken.get(String.class);

    TypeAdapter<String> adapter = excluderSpy.create(mockGson, typeToken);
    assertNotNull(adapter);

    JsonReader mockReader = mock(JsonReader.class);
    String result = adapter.read(mockReader);

    // Because skipDeserialize is true, read should call in.skipValue() and return null
    verify(mockReader).skipValue();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void create_shouldDelegateReadAndWrite_whenNotSkipped() throws IOException {
    ExcluderWithOverrides excluderSpy = new ExcluderWithOverrides(false, true, false);

    TypeToken<String> typeToken = TypeToken.get(String.class);

    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(mockGson.getDelegateAdapter(ArgumentMatchers.any(), ArgumentMatchers.eq(typeToken))).thenReturn(delegateAdapter);

    TypeAdapter<String> adapter = excluderSpy.create(mockGson, typeToken);
    assertNotNull(adapter);

    JsonReader mockReader = mock(JsonReader.class);
    String readValue = "readValue";
    when(delegateAdapter.read(mockReader)).thenReturn(readValue);

    // read should delegate because skipDeserialize is false
    String result = adapter.read(mockReader);
    assertEquals(readValue, result);
    verify(delegateAdapter).read(mockReader);

    JsonWriter mockWriter = mock(JsonWriter.class);

    // write should call out.nullValue() because skipSerialize is true
    adapter.write(mockWriter, "value");
    verify(mockWriter).nullValue();
  }

  @Test
    @Timeout(8000)
  void create_delegateMethod_shouldLazilyInitializeDelegate() throws Exception {
    ExcluderWithOverrides excluderSpy = new ExcluderWithOverrides(false, true, false);

    TypeToken<String> typeToken = TypeToken.get(String.class);

    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(mockGson.getDelegateAdapter(ArgumentMatchers.any(), ArgumentMatchers.eq(typeToken))).thenReturn(delegateAdapter);

    TypeAdapter<String> adapter = excluderSpy.create(mockGson, typeToken);
    assertNotNull(adapter);

    // Use reflection to access private method delegate()
    Method delegateMethod = adapter.getClass().getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    // Initially delegate field is null, so calling delegate() should call gson.getDelegateAdapter
    Object firstDelegate = delegateMethod.invoke(adapter);
    assertSame(delegateAdapter, firstDelegate);

    // Calling delegate() second time should return cached delegate without calling getDelegateAdapter again
    Object secondDelegate = delegateMethod.invoke(adapter);
    assertSame(delegateAdapter, secondDelegate);

    // Verify getDelegateAdapter was called only once
    verify(mockGson, times(1)).getDelegateAdapter(ArgumentMatchers.any(), ArgumentMatchers.eq(typeToken));
  }
}
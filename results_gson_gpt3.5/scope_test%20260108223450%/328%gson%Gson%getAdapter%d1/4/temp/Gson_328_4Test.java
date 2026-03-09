package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;

import java.lang.reflect.Field;
import java.util.*;

class GsonGetAdapterTest {

  Gson gson;

  @BeforeEach
  void setUp() throws Exception {
    // We create a Gson instance with controlled factories list and empty caches to isolate getAdapter
    gson = new Gson();

    // Use reflection to inject mocks into private final fields
    // factories list
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factoriesField.set(gson, factories);

    // typeTokenCache map
    Field typeTokenCacheField = Gson.class.getDeclaredField("typeTokenCache");
    typeTokenCacheField.setAccessible(true);
    ConcurrentMap<TypeToken<?>, TypeAdapter<?>> typeTokenCache = new java.util.concurrent.ConcurrentHashMap<>();
    typeTokenCacheField.set(gson, typeTokenCache);

    // threadLocalAdapterResults ThreadLocal
    Field threadLocalAdapterResultsField = Gson.class.getDeclaredField("threadLocalAdapterResults");
    threadLocalAdapterResultsField.setAccessible(true);
    ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>> threadLocalAdapterResults = new ThreadLocal<>();
    threadLocalAdapterResultsField.set(gson, threadLocalAdapterResults);
  }

  @Test
    @Timeout(8000)
  void getAdapter_nullType_throwsNPE() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> gson.getAdapter(null));
    assertEquals("type must not be null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void getAdapter_returnsCachedAdapterIfPresent() throws Exception {
    // Prepare a TypeToken and a cached adapter
    TypeToken<String> typeToken = TypeToken.get(String.class);
    TypeAdapter<String> cachedAdapter = mock(TypeAdapter.class);

    // Put cached adapter into typeTokenCache
    Field typeTokenCacheField = Gson.class.getDeclaredField("typeTokenCache");
    typeTokenCacheField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ConcurrentMap<TypeToken<?>, TypeAdapter<?>> typeTokenCache = (ConcurrentMap<TypeToken<?>, TypeAdapter<?>>) typeTokenCacheField.get(gson);
    typeTokenCache.put(typeToken, cachedAdapter);

    // Call getAdapter and verify cached adapter is returned
    TypeAdapter<String> adapter = gson.getAdapter(typeToken);
    assertSame(cachedAdapter, adapter);
  }

  @Test
    @Timeout(8000)
  void getAdapter_returnsOngoingCallAdapterIfPresent() throws Exception {
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Create a FutureTypeAdapter for ongoing call
    FutureTypeAdapter<String> futureAdapter = new FutureTypeAdapter<>();

    // Put into threadLocalAdapterResults map
    Field threadLocalAdapterResultsField = Gson.class.getDeclaredField("threadLocalAdapterResults");
    threadLocalAdapterResultsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>> threadLocalAdapterResults =
        (ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>>) threadLocalAdapterResultsField.get(gson);

    Map<TypeToken<?>, TypeAdapter<?>> threadCalls = new HashMap<>();
    threadCalls.put(typeToken, futureAdapter);
    threadLocalAdapterResults.set(threadCalls);

    // Call getAdapter and verify the ongoing call adapter is returned
    TypeAdapter<String> adapter = gson.getAdapter(typeToken);
    assertSame(futureAdapter, adapter);

    // Clean up thread local
    threadLocalAdapterResults.remove();
  }

  @Test
    @Timeout(8000)
  void getAdapter_findsAdapterFromFactories_andCaches() throws Exception {
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Create a mock factory that returns a mock adapter for the typeToken
    TypeAdapterFactory factory = mock(TypeAdapterFactory.class);
    TypeAdapter<String> mockAdapter = mock(TypeAdapter.class);
    when(factory.create(eq(gson), eq(typeToken))).thenReturn(mockAdapter);

    // Inject this factory into factories list
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gson);
    factories.clear();
    factories.add(factory);

    // Call getAdapter and verify returned adapter is mockAdapter
    TypeAdapter<String> adapter = gson.getAdapter(typeToken);
    assertSame(mockAdapter, adapter);

    // Verify cache is populated with the adapter for the typeToken
    Field typeTokenCacheField = Gson.class.getDeclaredField("typeTokenCache");
    typeTokenCacheField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ConcurrentMap<TypeToken<?>, TypeAdapter<?>> typeTokenCache = (ConcurrentMap<TypeToken<?>, TypeAdapter<?>>) typeTokenCacheField.get(gson);
    assertTrue(typeTokenCache.containsKey(typeToken));
    assertSame(mockAdapter, typeTokenCache.get(typeToken));
  }

  @Test
    @Timeout(8000)
  void getAdapter_factoryReturnsNull_throwsIllegalArgumentException() throws Exception {
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Create a factory that returns null for the typeToken
    TypeAdapterFactory factory = mock(TypeAdapterFactory.class);
    when(factory.create(eq(gson), eq(typeToken))).thenReturn(null);

    // Inject this factory into factories list
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gson);
    factories.clear();
    factories.add(factory);

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> gson.getAdapter(typeToken));
    assertTrue(ex.getMessage().contains("cannot handle"));
    assertTrue(ex.getMessage().contains(typeToken.toString()));
  }

  @Test
    @Timeout(8000)
  void getAdapter_multipleFactories_firstNonNullAdapterUsed() throws Exception {
    TypeToken<String> typeToken = TypeToken.get(String.class);

    TypeAdapterFactory factory1 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory factory2 = mock(TypeAdapterFactory.class);
    TypeAdapter<String> adapter2 = mock(TypeAdapter.class);

    when(factory1.create(eq(gson), eq(typeToken))).thenReturn(null);
    when(factory2.create(eq(gson), eq(typeToken))).thenReturn(adapter2);

    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gson);
    factories.clear();
    factories.add(factory1);
    factories.add(factory2);

    TypeAdapter<String> adapter = gson.getAdapter(typeToken);
    assertSame(adapter2, adapter);

    // Verify cache has adapter2
    Field typeTokenCacheField = Gson.class.getDeclaredField("typeTokenCache");
    typeTokenCacheField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ConcurrentMap<TypeToken<?>, TypeAdapter<?>> typeTokenCache = (ConcurrentMap<TypeToken<?>, TypeAdapter<?>>) typeTokenCacheField.get(gson);
    assertSame(adapter2, typeTokenCache.get(typeToken));
  }

  @Test
    @Timeout(8000)
  void getAdapter_cyclicDependency_doesNotPublishPartialAdapters() throws Exception {
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Create a factory that returns a FutureTypeAdapter initially to simulate cyclic dependency
    TypeAdapterFactory factory = new TypeAdapterFactory() {
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.equals(typeToken)) {
          // Return a FutureTypeAdapter with delegate set to a real adapter
          FutureTypeAdapter<T> future = new FutureTypeAdapter<>();
          TypeAdapter<T> realAdapter = new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) {
            }
            @Override
            public T read(JsonReader in) {
              return null;
            }
          };
          future.setDelegate(realAdapter);
          return future;
        }
        return null;
      }
    };

    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gson);
    factories.clear();
    factories.add(factory);

    // Call getAdapter to trigger code path
    TypeAdapter<String> adapter = gson.getAdapter(typeToken);
    assertNotNull(adapter);

    // The typeTokenCache should contain the adapter now
    Field typeTokenCacheField = Gson.class.getDeclaredField("typeTokenCache");
    typeTokenCacheField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ConcurrentMap<TypeToken<?>, TypeAdapter<?>> typeTokenCache = (ConcurrentMap<TypeToken<?>, TypeAdapter<?>>) typeTokenCacheField.get(gson);

    // Because this is initial request, cache should contain adapter
    assertTrue(typeTokenCache.containsKey(typeToken));
    assertSame(adapter, typeTokenCache.get(typeToken));
  }
}
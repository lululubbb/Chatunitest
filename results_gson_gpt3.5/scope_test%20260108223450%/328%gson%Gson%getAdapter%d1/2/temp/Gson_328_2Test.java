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
import com.google.gson.internal.bind.TypeAdapters;
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
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class Gson_GetAdapterTest {

  private Gson gson;
  private TypeToken<String> stringTypeToken;
  private TypeToken<Integer> intTypeToken;

  @BeforeEach
  public void setUp() throws Exception {
    // Create Gson instance with minimal required setup
    gson = new Gson();

    // Inject mock factories list with controlled behavior using reflection
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);

    // Create mock TypeAdapterFactory
    TypeAdapterFactory mockFactory1 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory mockFactory2 = mock(TypeAdapterFactory.class);

    // Setup mockFactory1 to return null for any create call
    when(mockFactory1.create(any(Gson.class), any(TypeToken.class))).thenReturn(null);

    // Setup mockFactory2 to return a dummy TypeAdapter for String type only
    TypeAdapter<String> stringAdapter = mock(TypeAdapter.class);
    when(mockFactory2.create(any(Gson.class), eq(TypeToken.get(String.class)))).thenReturn(stringAdapter);
    when(mockFactory2.create(any(Gson.class), argThat(t -> !t.equals(TypeToken.get(String.class))))).thenReturn(null);

    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(mockFactory1);
    factories.add(mockFactory2);

    // Use unmodifiableList for 'factories' as Gson expects immutable list
    factoriesField.set(gson, Collections.unmodifiableList(factories));

    // Also initialize the caches (typeTokenCache and threadLocalAdapterResults) if needed
    Field cacheField = Gson.class.getDeclaredField("typeTokenCache");
    cacheField.setAccessible(true);
    cacheField.set(gson, new ConcurrentHashMap<>());

    Field threadLocalField = Gson.class.getDeclaredField("threadLocalAdapterResults");
    threadLocalField.setAccessible(true);
    threadLocalField.set(gson, new ThreadLocal<>());

    // Initialize TypeTokens
    stringTypeToken = TypeToken.get(String.class);
    intTypeToken = TypeToken.get(Integer.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAdapter_NullType_ThrowsNPE() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> gson.getAdapter((TypeToken<?>) null));
    assertEquals("type must not be null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAdapter_CachedAdapterReturned() throws Exception {
    // Prepare a mock TypeAdapter cached in typeTokenCache
    TypeAdapter<String> cachedAdapter = mock(TypeAdapter.class);

    Field cacheField = Gson.class.getDeclaredField("typeTokenCache");
    cacheField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<TypeToken<?>, TypeAdapter<?>> cache = (Map<TypeToken<?>, TypeAdapter<?>>) cacheField.get(gson);
    cache.put(stringTypeToken, cachedAdapter);

    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertSame(cachedAdapter, adapter);
  }

  @Test
    @Timeout(8000)
  public void testGetAdapter_OngoingCallReturned() throws Exception {
    // Simulate ongoing call in threadLocalAdapterResults
    Field threadLocalField = Gson.class.getDeclaredField("threadLocalAdapterResults");
    threadLocalField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>> threadLocal = (ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>>) threadLocalField.get(gson);

    Map<TypeToken<?>, TypeAdapter<?>> ongoingMap = new HashMap<>();
    TypeAdapter<String> ongoingAdapter = mock(TypeAdapter.class);
    ongoingMap.put(stringTypeToken, ongoingAdapter);
    threadLocal.set(ongoingMap);

    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertSame(ongoingAdapter, adapter);

    threadLocal.remove();
  }

  @Test
    @Timeout(8000)
  public void testGetAdapter_FactoryCreatesAdapter() {
    // stringTypeToken is handled by mockFactory2 which returns a mock adapter
    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testGetAdapter_FactoryReturnsNull_ThrowsIllegalArgumentException() {
    // intTypeToken is not handled by any factory (both return null)
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> gson.getAdapter(intTypeToken));
    assertTrue(ex.getMessage().contains("cannot handle"));
  }

  @Test
    @Timeout(8000)
  public void testGetAdapter_InitialRequestPublishesToCache() throws Exception {
    // Clear caches first
    Field cacheField = Gson.class.getDeclaredField("typeTokenCache");
    cacheField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<TypeToken<?>, TypeAdapter<?>> cache = (Map<TypeToken<?>, TypeAdapter<?>>) cacheField.get(gson);
    cache.clear();

    // Call getAdapter for stringTypeToken (handled by mockFactory2)
    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertNotNull(adapter);

    // The cache should be populated with stringTypeToken key
    assertTrue(cache.containsKey(stringTypeToken));
    assertSame(adapter, cache.get(stringTypeToken));
  }
}
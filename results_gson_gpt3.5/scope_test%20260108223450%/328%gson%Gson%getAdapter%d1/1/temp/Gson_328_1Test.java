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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.FutureTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.lang.reflect.Field;
import java.util.*;

class GsonGetAdapterTest {

  private Gson gson;
  private TypeAdapterFactory factory1;
  private TypeAdapterFactory factory2;
  private TypeToken<String> stringTypeToken;
  private TypeAdapter<String> stringAdapter;

  @BeforeEach
  void setUp() throws Exception {
    gson = new Gson();

    // Create mocks for factories and adapters
    factory1 = mock(TypeAdapterFactory.class);
    factory2 = mock(TypeAdapterFactory.class);
    stringAdapter = mock(TypeAdapter.class);
    stringTypeToken = TypeToken.get(String.class);

    // Use reflection to set the private final factories field to a list of mocks
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    factoriesField.set(gson, Arrays.asList(factory1, factory2));

    // Clear caches by reflection
    Field cacheField = Gson.class.getDeclaredField("typeTokenCache");
    cacheField.setAccessible(true);
    ((Map<?, ?>) cacheField.get(gson)).clear();

    Field threadLocalField = Gson.class.getDeclaredField("threadLocalAdapterResults");
    threadLocalField.setAccessible(true);
    ThreadLocal<?> threadLocal = (ThreadLocal<?>) threadLocalField.get(gson);
    threadLocal.remove();
  }

  @Test
    @Timeout(8000)
  void getAdapter_nullType_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> gson.getAdapter(null));
    assertEquals("type must not be null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void getAdapter_returnsCachedAdapter() throws Exception {
    // Put an adapter in cache manually
    Field cacheField = Gson.class.getDeclaredField("typeTokenCache");
    cacheField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<TypeToken<?>, TypeAdapter<?>> cache = (Map<TypeToken<?>, TypeAdapter<?>>) cacheField.get(gson);
    cache.put(stringTypeToken, stringAdapter);

    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertSame(stringAdapter, adapter);

    // Verify factories are never called
    verifyNoInteractions(factory1, factory2);
  }

  @Test
    @Timeout(8000)
  void getAdapter_reusesOngoingCallFromThreadLocal() throws Exception {
    // Prepare a FutureTypeAdapter to simulate ongoing call
    FutureTypeAdapter<String> futureAdapter = new FutureTypeAdapter<>();
    futureAdapter.setDelegate(stringAdapter);

    // Put ongoing call in thread local map
    Field threadLocalField = Gson.class.getDeclaredField("threadLocalAdapterResults");
    threadLocalField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>> threadLocal =
        (ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>>) threadLocalField.get(gson);
    Map<TypeToken<?>, TypeAdapter<?>> threadMap = new HashMap<>();
    threadMap.put(stringTypeToken, futureAdapter);
    threadLocal.set(threadMap);

    // Call getAdapter should return ongoing call without invoking factories
    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertSame(futureAdapter, adapter);

    verifyNoInteractions(factory1, factory2);
  }

  @Test
    @Timeout(8000)
  void getAdapter_findsAdapterFromFactories_andCachesIt() throws Exception {
    // Setup factory1 to return null, factory2 to return stringAdapter
    when(factory1.create(gson, stringTypeToken)).thenReturn(null);
    when(factory2.create(gson, stringTypeToken)).thenReturn(stringAdapter);

    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertSame(stringAdapter, adapter);

    // Verify factories called in order
    verify(factory1).create(gson, stringTypeToken);
    verify(factory2).create(gson, stringTypeToken);

    // Verify adapter cached in typeTokenCache
    Field cacheField = Gson.class.getDeclaredField("typeTokenCache");
    cacheField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<TypeToken<?>, TypeAdapter<?>> cache = (Map<TypeToken<?>, TypeAdapter<?>>) cacheField.get(gson);
    assertTrue(cache.containsKey(stringTypeToken));
    assertSame(stringAdapter, cache.get(stringTypeToken));
  }

  @Test
    @Timeout(8000)
  void getAdapter_noFactoryHandlesType_throwsIllegalArgumentException() {
    // Setup factories to return null for the type
    when(factory1.create(gson, stringTypeToken)).thenReturn(null);
    when(factory2.create(gson, stringTypeToken)).thenReturn(null);

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> gson.getAdapter(stringTypeToken));
    assertTrue(ex.getMessage().contains("cannot handle"));
  }

  @Test
    @Timeout(8000)
  void getAdapter_initialRequest_removesThreadLocalAfter() throws Exception {
    // Setup factory1 to return stringAdapter
    when(factory1.create(gson, stringTypeToken)).thenReturn(stringAdapter);

    // Spy on threadLocalAdapterResults to verify remove is called
    Field threadLocalField = Gson.class.getDeclaredField("threadLocalAdapterResults");
    threadLocalField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>> threadLocal =
        (ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>>) threadLocalField.get(gson);

    // Initially null
    assertNull(threadLocal.get());

    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertSame(stringAdapter, adapter);

    // After call, thread local should be removed (null)
    assertNull(threadLocal.get());
  }
}
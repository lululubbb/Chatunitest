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

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GsonGetAdapterTest {

  private Gson gson;
  private TypeToken<String> stringTypeToken;
  private TypeToken<Integer> integerTypeToken;

  @BeforeEach
  void setUp() throws Exception {
    // Create Gson instance with minimal required factories list
    gson = new Gson();

    // Inject empty factories list for control
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    factoriesField.set(gson, new ArrayList<TypeAdapterFactory>());

    // Clear caches and thread local for clean state
    Field cacheField = Gson.class.getDeclaredField("typeTokenCache");
    cacheField.setAccessible(true);
    ((Map<?, ?>) cacheField.get(gson)).clear();

    Field threadLocalField = Gson.class.getDeclaredField("threadLocalAdapterResults");
    threadLocalField.setAccessible(true);
    ThreadLocal<?> threadLocal = (ThreadLocal<?>) threadLocalField.get(gson);
    threadLocal.remove();

    stringTypeToken = TypeToken.get(String.class);
    integerTypeToken = TypeToken.get(Integer.class);
  }

  @Test
    @Timeout(8000)
  void getAdapter_nullType_throws() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> gson.getAdapter(null));
    assertEquals("type must not be null", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void getAdapter_returnsCachedAdapter() throws Exception {
    TypeAdapter<String> mockedAdapter = mock(TypeAdapter.class);

    // Put adapter into cache
    Field cacheField = Gson.class.getDeclaredField("typeTokenCache");
    cacheField.setAccessible(true);
    Map<TypeToken<?>, TypeAdapter<?>> cache = (Map<TypeToken<?>, TypeAdapter<?>>) cacheField.get(gson);
    cache.put(stringTypeToken, mockedAdapter);

    // Should return cached adapter without calling factories
    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertSame(mockedAdapter, adapter);
  }

  @Test
    @Timeout(8000)
  void getAdapter_reusesOngoingCall() throws Exception {
    TypeAdapterFactory factory = mock(TypeAdapterFactory.class);
    TypeAdapter<String> adapter = mock(TypeAdapter.class);

    when(factory.create(eq(gson), eq(stringTypeToken))).thenReturn(adapter);

    // Inject factory list with one factory
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    factoriesField.set(gson, Collections.singletonList(factory));

    // Prepare thread local map with ongoing call
    Field threadLocalField = Gson.class.getDeclaredField("threadLocalAdapterResults");
    threadLocalField.setAccessible(true);
    ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>> threadLocal =
        (ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>>) threadLocalField.get(gson);

    Map<TypeToken<?>, TypeAdapter<?>> ongoingCalls = new HashMap<>();
    // Use a dummy FutureTypeAdapter to simulate ongoing call
    TypeAdapter<String> futureAdapter = mock(TypeAdapter.class);
    ongoingCalls.put(stringTypeToken, futureAdapter);
    threadLocal.set(ongoingCalls);

    // Should return ongoing call adapter immediately
    TypeAdapter<String> result = gson.getAdapter(stringTypeToken);
    assertSame(futureAdapter, result);
  }

  @Test
    @Timeout(8000)
  void getAdapter_findsAdapterInFactories_andCaches() throws Exception {
    TypeAdapterFactory factory1 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory factory2 = mock(TypeAdapterFactory.class);
    TypeAdapter<String> adapter = mock(TypeAdapter.class);

    when(factory1.create(eq(gson), eq(stringTypeToken))).thenReturn(null);
    when(factory2.create(eq(gson), eq(stringTypeToken))).thenReturn(adapter);

    // Inject factories list with two factories
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    factoriesField.set(gson, Arrays.asList(factory1, factory2));

    // Call getAdapter first time: should find adapter from factory2
    TypeAdapter<String> result = gson.getAdapter(stringTypeToken);
    assertSame(adapter, result);

    // After call, cache should contain adapter
    Field cacheField = Gson.class.getDeclaredField("typeTokenCache");
    cacheField.setAccessible(true);
    Map<TypeToken<?>, TypeAdapter<?>> cache = (Map<TypeToken<?>, TypeAdapter<?>>) cacheField.get(gson);
    assertTrue(cache.containsKey(stringTypeToken));
    assertSame(adapter, cache.get(stringTypeToken));
  }

  @Test
    @Timeout(8000)
  void getAdapter_noAdapterFound_throws() throws Exception {
    TypeAdapterFactory factory1 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory factory2 = mock(TypeAdapterFactory.class);

    when(factory1.create(eq(gson), eq(integerTypeToken))).thenReturn(null);
    when(factory2.create(eq(gson), eq(integerTypeToken))).thenReturn(null);

    // Inject factories list with two factories returning null
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    factoriesField.set(gson, Arrays.asList(factory1, factory2));

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> gson.getAdapter(integerTypeToken));
    assertTrue(thrown.getMessage().contains("cannot handle"));
  }
}
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

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.lang.reflect.Field;
import java.util.*;

public class GsonGetAdapterTest {

  private Gson gson;
  private TypeToken<String> stringTypeToken;
  private TypeToken<Integer> integerTypeToken;
  private TypeAdapterFactory mockFactory1;
  private TypeAdapterFactory mockFactory2;
  private TypeAdapter<String> stringAdapter;
  private TypeAdapter<Integer> integerAdapter;

  @BeforeEach
  public void setUp() throws Exception {
    gson = new Gson();

    // Prepare TypeTokens
    stringTypeToken = TypeToken.get(String.class);
    integerTypeToken = TypeToken.get(Integer.class);

    // Prepare mock TypeAdapters
    stringAdapter = mock(TypeAdapter.class);
    integerAdapter = mock(TypeAdapter.class);

    // Prepare mock factories
    mockFactory1 = mock(TypeAdapterFactory.class);
    mockFactory2 = mock(TypeAdapterFactory.class);

    // Use reflection to set the factories list in Gson instance
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(mockFactory1);
    factories.add(mockFactory2);
    factoriesField.set(gson, Collections.unmodifiableList(factories));

    // Clear caches and thread local before each test to avoid cross-test pollution
    Field cacheField = Gson.class.getDeclaredField("typeTokenCache");
    cacheField.setAccessible(true);
    Map<TypeToken<?>, TypeAdapter<?>> cache = (Map<TypeToken<?>, TypeAdapter<?>>) cacheField.get(gson);
    cache.clear();

    Field threadLocalField = Gson.class.getDeclaredField("threadLocalAdapterResults");
    threadLocalField.setAccessible(true);
    ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>> threadLocal = (ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>>) threadLocalField.get(gson);
    threadLocal.remove();
  }

  @Test
    @Timeout(8000)
  public void getAdapter_nullType_throwsNPE() {
    NullPointerException npe = assertThrows(NullPointerException.class, () -> {
      gson.getAdapter(null);
    });
    assertEquals("type must not be null", npe.getMessage());
  }

  @Test
    @Timeout(8000)
  public void getAdapter_returnsCachedAdapter() throws Exception {
    // Put adapter in cache
    Field cacheField = Gson.class.getDeclaredField("typeTokenCache");
    cacheField.setAccessible(true);
    Map<TypeToken<?>, TypeAdapter<?>> cache = (Map<TypeToken<?>, TypeAdapter<?>>) cacheField.get(gson);
    cache.put(stringTypeToken, stringAdapter);

    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertSame(stringAdapter, adapter);

    // Verify no factory create calls
    verifyNoInteractions(mockFactory1, mockFactory2);
  }

  @Test
    @Timeout(8000)
  public void getAdapter_returnsOngoingCallAdapter() throws Exception {
    // Setup threadLocalAdapterResults with an ongoing call for stringTypeToken
    Field threadLocalField = Gson.class.getDeclaredField("threadLocalAdapterResults");
    threadLocalField.setAccessible(true);
    ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>> threadLocal = (ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>>) threadLocalField.get(gson);

    // Create a FutureTypeAdapter manually via reflection
    Class<?> futureTypeAdapterClass = Class.forName("com.google.gson.Gson$FutureTypeAdapter");
    Object futureTypeAdapterInstance = futureTypeAdapterClass.getDeclaredConstructor().newInstance();

    Map<TypeToken<?>, TypeAdapter<?>> threadCalls = new HashMap<>();
    threadCalls.put(stringTypeToken, (TypeAdapter<?>) futureTypeAdapterInstance);
    threadLocal.set(threadCalls);

    // Call getAdapter, should return the ongoing call adapter
    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertSame(futureTypeAdapterInstance, adapter);
  }

  @Test
    @Timeout(8000)
  public void getAdapter_factoryCreatesAdapter_candidateNotNull() throws Exception {
    // Setup mockFactory1 to return null, mockFactory2 to return adapter
    when(mockFactory1.create(eq(gson), eq(stringTypeToken))).thenReturn(null);
    when(mockFactory2.create(eq(gson), eq(stringTypeToken))).thenReturn(stringAdapter);

    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);

    assertSame(stringAdapter, adapter);

    // Verify factories called in order
    InOrder inOrder = inOrder(mockFactory1, mockFactory2);
    inOrder.verify(mockFactory1).create(gson, stringTypeToken);
    inOrder.verify(mockFactory2).create(gson, stringTypeToken);

    // Verify adapter cached in typeTokenCache
    Field cacheField = Gson.class.getDeclaredField("typeTokenCache");
    cacheField.setAccessible(true);
    Map<TypeToken<?>, TypeAdapter<?>> cache = (Map<TypeToken<?>, TypeAdapter<?>>) cacheField.get(gson);
    assertTrue(cache.containsKey(stringTypeToken));
    assertSame(stringAdapter, cache.get(stringTypeToken));
  }

  @Test
    @Timeout(8000)
  public void getAdapter_factoryCreatesAdapter_candidateNull_throwsIllegalArgumentException() throws Exception {
    // Setup all factories to return null
    when(mockFactory1.create(eq(gson), eq(stringTypeToken))).thenReturn(null);
    when(mockFactory2.create(eq(gson), eq(stringTypeToken))).thenReturn(null);

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
      gson.getAdapter(stringTypeToken);
    });
    assertTrue(ex.getMessage().contains("cannot handle"));
    assertTrue(ex.getMessage().contains(stringTypeToken.toString()));
  }

  @Test
    @Timeout(8000)
  public void getAdapter_initialRequest_publishesAdaptersToCache() throws Exception {
    // Setup mockFactory1 returns adapter, mockFactory2 not called
    when(mockFactory1.create(eq(gson), eq(stringTypeToken))).thenReturn(stringAdapter);

    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertSame(stringAdapter, adapter);

    // The cache should contain the stringTypeToken with stringAdapter
    Field cacheField = Gson.class.getDeclaredField("typeTokenCache");
    cacheField.setAccessible(true);
    Map<TypeToken<?>, TypeAdapter<?>> cache = (Map<TypeToken<?>, TypeAdapter<?>>) cacheField.get(gson);
    assertTrue(cache.containsKey(stringTypeToken));
    assertSame(stringAdapter, cache.get(stringTypeToken));
  }

  @Test
    @Timeout(8000)
  public void getAdapter_nestedCall_returnsOngoingAdapter() throws Exception {
    // Setup mockFactory1 to create an adapter that calls getAdapter recursively for same type
    when(mockFactory1.create(eq(gson), eq(stringTypeToken))).thenAnswer(invocation -> {
      // During creation, call getAdapter again for same type, should return ongoing call adapter
      TypeAdapter<String> nestedCall = gson.getAdapter(stringTypeToken);
      return nestedCall;
    });
    when(mockFactory2.create(eq(gson), eq(stringTypeToken))).thenReturn(null);

    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);

    // The returned adapter is the nested call adapter, which is the FutureTypeAdapter proxy
    assertNotNull(adapter);
  }

}
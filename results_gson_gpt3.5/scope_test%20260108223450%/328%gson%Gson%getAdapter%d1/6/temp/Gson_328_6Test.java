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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Gson_GetAdapterTest {

  private Gson gson;
  private TypeToken<String> stringTypeToken;
  private TypeAdapterFactory mockFactory1;
  private TypeAdapterFactory mockFactory2;
  private TypeAdapter<String> mockAdapter;

  @BeforeEach
  public void setUp() throws Exception {
    gson = new Gson();

    // Set up factories list with mocks
    mockFactory1 = mock(TypeAdapterFactory.class);
    mockFactory2 = mock(TypeAdapterFactory.class);

    // Mock adapter to be returned by factory
    mockAdapter = mock(TypeAdapter.class);

    // Inject mocked factories list into gson instance via reflection
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(mockFactory1);
    factories.add(mockFactory2);
    factoriesField.set(gson, factories);

    // Clear caches and threadLocal to ensure clean state
    Field typeTokenCacheField = Gson.class.getDeclaredField("typeTokenCache");
    typeTokenCacheField.setAccessible(true);
    ((Map<?, ?>) typeTokenCacheField.get(gson)).clear();

    Field threadLocalField = Gson.class.getDeclaredField("threadLocalAdapterResults");
    threadLocalField.setAccessible(true);
    ThreadLocal<?> threadLocal = (ThreadLocal<?>) threadLocalField.get(gson);
    threadLocal.remove();

    stringTypeToken = TypeToken.get(String.class);
  }

  @Test
    @Timeout(8000)
  public void getAdapter_whenTypeIsNull_throwsNullPointerException() {
    NullPointerException npe = assertThrows(NullPointerException.class, () -> gson.getAdapter(null));
    assertEquals("type must not be null", npe.getMessage());
  }

  @Test
    @Timeout(8000)
  public void getAdapter_returnsCachedAdapterIfPresent() throws Exception {
    Field typeTokenCacheField = Gson.class.getDeclaredField("typeTokenCache");
    typeTokenCacheField.setAccessible(true);
    Map<TypeToken<?>, TypeAdapter<?>> cache = (Map<TypeToken<?>, TypeAdapter<?>>) typeTokenCacheField.get(gson);
    cache.put(stringTypeToken, mockAdapter);

    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);

    assertSame(mockAdapter, adapter);
    // Verify no factory create calls
    verifyNoInteractions(mockFactory1, mockFactory2);
  }

  @Test
    @Timeout(8000)
  public void getAdapter_returnsOngoingCallAdapterIfPresentInThreadLocal() throws Exception {
    // Prepare a FutureTypeAdapter to simulate ongoing call
    // We cannot instantiate FutureTypeAdapter directly (package private),
    // so simulate by putting mockAdapter in thread local map before call

    Field threadLocalField = Gson.class.getDeclaredField("threadLocalAdapterResults");
    threadLocalField.setAccessible(true);
    ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>> threadLocal =
        (ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>>) threadLocalField.get(gson);

    Map<TypeToken<?>, TypeAdapter<?>> ongoingCalls = new HashMap<>();
    ongoingCalls.put(stringTypeToken, mockAdapter);
    threadLocal.set(ongoingCalls);

    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);

    assertSame(mockAdapter, adapter);
    // Verify no factory create calls
    verifyNoInteractions(mockFactory1, mockFactory2);
  }

  @Test
    @Timeout(8000)
  public void getAdapter_returnsAdapterFromFactoryAndCachesAdapters() {
    // Factory1 returns null, Factory2 returns mockAdapter
    when(mockFactory1.create(eq(gson), eq(stringTypeToken))).thenReturn(null);
    when(mockFactory2.create(eq(gson), eq(stringTypeToken))).thenReturn(mockAdapter);

    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);

    assertSame(mockAdapter, adapter);

    // Verify factories called in order
    verify(mockFactory1).create(eq(gson), eq(stringTypeToken));
    verify(mockFactory2).create(eq(gson), eq(stringTypeToken));

    // The adapter should be cached in typeTokenCache
    Map<TypeToken<?>, TypeAdapter<?>> cache = getTypeTokenCache(gson);
    assertTrue(cache.containsKey(stringTypeToken));
    assertSame(mockAdapter, cache.get(stringTypeToken));
  }

  @Test
    @Timeout(8000)
  public void getAdapter_whenNoFactoryCreatesAdapter_throwsIllegalArgumentException() {
    when(mockFactory1.create(eq(gson), eq(stringTypeToken))).thenReturn(null);
    when(mockFactory2.create(eq(gson), eq(stringTypeToken))).thenReturn(null);

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> gson.getAdapter(stringTypeToken));
    assertTrue(ex.getMessage().contains("cannot handle"));
    assertTrue(ex.getMessage().contains(stringTypeToken.toString()));

    // Verify factories called
    verify(mockFactory1).create(eq(gson), eq(stringTypeToken));
    verify(mockFactory2).create(eq(gson), eq(stringTypeToken));
  }

  @Test
    @Timeout(8000)
  public void getAdapter_cachesAllAdaptersFromThreadLocalOnInitialRequest() {
    // Factory1 returns a new adapter, Factory2 not called
    TypeAdapter<String> adapter1 = mock(TypeAdapter.class);
    when(mockFactory1.create(eq(gson), eq(stringTypeToken))).thenReturn(adapter1);

    TypeToken<Integer> intTypeToken = TypeToken.get(Integer.class);
    TypeAdapter<Integer> adapter2 = mock(TypeAdapter.class);
    when(mockFactory2.create(eq(gson), eq(intTypeToken))).thenReturn(adapter2);

    // We simulate a call to getAdapter for intTypeToken inside a getAdapter call for stringTypeToken
    // To do this, we mock factory1 to call gson.getAdapter(intTypeToken) during create.

    when(mockFactory1.create(eq(gson), eq(stringTypeToken))).thenAnswer(invocation -> {
      // Nested call to getAdapter for intTypeToken
      TypeAdapter<Integer> nestedAdapter = gson.getAdapter(intTypeToken);
      assertSame(adapter2, nestedAdapter);
      return adapter1;
    });

    // Call getAdapter for stringTypeToken, which triggers nested call for intTypeToken
    TypeAdapter<String> result = gson.getAdapter(stringTypeToken);

    assertSame(adapter1, result);

    Map<TypeToken<?>, TypeAdapter<?>> cache = getTypeTokenCache(gson);
    // Both adapters should be cached
    assertEquals(adapter1, cache.get(stringTypeToken));
    assertEquals(adapter2, cache.get(intTypeToken));
  }

  private Map<TypeToken<?>, TypeAdapter<?>> getTypeTokenCache(Gson gson) {
    try {
      Field typeTokenCacheField = Gson.class.getDeclaredField("typeTokenCache");
      typeTokenCacheField.setAccessible(true);
      return (Map<TypeToken<?>, TypeAdapter<?>>) typeTokenCacheField.get(gson);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
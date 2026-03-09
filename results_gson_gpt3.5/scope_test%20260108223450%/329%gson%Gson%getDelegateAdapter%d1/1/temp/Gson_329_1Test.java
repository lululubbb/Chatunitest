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
import java.util.Collections;
import java.util.HashMap;
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
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class Gson_getDelegateAdapter_Test {

  private Gson gson;
  private TypeAdapterFactory skipPastFactory;
  private TypeAdapterFactory factory1;
  private TypeAdapterFactory factory2;
  private TypeAdapterFactory jsonAdapterFactory;
  private TypeToken<String> typeToken;

  @BeforeEach
  void setUp() throws Exception {
    // Create gson instance with empty factories list
    gson = Mockito.spy(Gson.class);
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);

    // Prepare factories list
    List<TypeAdapterFactory> factories = new ArrayList<>();
    skipPastFactory = mock(TypeAdapterFactory.class);
    factory1 = mock(TypeAdapterFactory.class);
    factory2 = mock(TypeAdapterFactory.class);
    jsonAdapterFactory = mock(TypeAdapterFactory.class);

    // By default, factories list contains skipPastFactory and others
    factories.add(skipPastFactory);
    factories.add(factory1);
    factories.add(factory2);

    factoriesField.set(gson, factories);

    // Set jsonAdapterFactory field
    Field jsonAdapterFactoryField = Gson.class.getDeclaredField("jsonAdapterFactory");
    jsonAdapterFactoryField.setAccessible(true);
    jsonAdapterFactoryField.set(gson, jsonAdapterFactory);

    typeToken = TypeToken.get(String.class);
  }

  @Test
    @Timeout(8000)
  void whenSkipPastFactoryInFactories_thenReturnsFirstNonNullAdapterAfterSkipPast() {
    @SuppressWarnings("unchecked")
    TypeAdapter<String> adapter1 = mock(TypeAdapter.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> adapter2 = mock(TypeAdapter.class);

    // factory1.create returns null, factory2.create returns adapter2
    when(factory1.create(gson, typeToken)).thenReturn(null);
    when(factory2.create(gson, typeToken)).thenReturn(adapter2);

    TypeAdapter<String> result = gson.getDelegateAdapter(skipPastFactory, typeToken);

    assertSame(adapter2, result);

    // Verify create called only on factory1 and factory2, not on skipPastFactory
    verify(factory1).create(gson, typeToken);
    verify(factory2).create(gson, typeToken);
    verify(skipPastFactory, never()).create(any(), any());
  }

  @Test
    @Timeout(8000)
  void whenSkipPastFactoryNotInFactories_thenUsesJsonAdapterFactoryAsSkipPast() {
    @SuppressWarnings("unchecked")
    TypeAdapter<String> adapter = mock(TypeAdapter.class);

    // Remove skipPastFactory from factories to trigger fallback
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(factory1);
    factories.add(factory2);
    try {
      Field factoriesField = Gson.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      factoriesField.set(gson, factories);
    } catch (Exception e) {
      fail("Setup failed: " + e);
    }

    // jsonAdapterFactory is used as skipPast, so skipPastFound triggers after it
    factories.add(0, jsonAdapterFactory);

    when(factory1.create(gson, typeToken)).thenReturn(null);
    when(factory2.create(gson, typeToken)).thenReturn(adapter);

    TypeAdapter<String> result = gson.getDelegateAdapter(skipPastFactory, typeToken);

    assertSame(adapter, result);

    verify(jsonAdapterFactory, never()).create(any(), any());
    verify(factory1).create(gson, typeToken);
    verify(factory2).create(gson, typeToken);
  }

  @Test
    @Timeout(8000)
  void whenNoAdapterFound_thenThrowsIllegalArgumentException() {
    // All factories return null adapter
    when(skipPastFactory.create(gson, typeToken)).thenReturn(null);
    when(factory1.create(gson, typeToken)).thenReturn(null);
    when(factory2.create(gson, typeToken)).thenReturn(null);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
      () -> gson.getDelegateAdapter(skipPastFactory, typeToken));

    assertTrue(thrown.getMessage().contains("GSON cannot serialize"));
  }

  @Test
    @Timeout(8000)
  void whenSkipPastFactoryIsLastFactory_thenThrowsIllegalArgumentException() {
    // Setup factories so skipPastFactory is last
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(factory1);
    factories.add(factory2);
    factories.add(skipPastFactory);
    try {
      Field factoriesField = Gson.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      factoriesField.set(gson, factories);
    } catch (Exception e) {
      fail("Setup failed: " + e);
    }

    when(factory1.create(gson, typeToken)).thenReturn(null);
    when(factory2.create(gson, typeToken)).thenReturn(null);
    when(skipPastFactory.create(gson, typeToken)).thenReturn(null);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
      () -> gson.getDelegateAdapter(skipPastFactory, typeToken));

    assertTrue(thrown.getMessage().contains("GSON cannot serialize"));
  }
}
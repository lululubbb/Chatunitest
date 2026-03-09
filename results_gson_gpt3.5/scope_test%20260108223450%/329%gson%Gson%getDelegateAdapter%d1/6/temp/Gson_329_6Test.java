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

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class Gson_GetDelegateAdapterTest {

  private Gson gson;
  private TypeAdapterFactory factory1;
  private TypeAdapterFactory factory2;
  private TypeAdapterFactory skipPastFactory;
  private TypeToken<String> typeToken;

  @BeforeEach
  public void setUp() throws Exception {
    gson = new Gson();

    // Using reflection to set the final 'factories' field with a mock list
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);

    // Prepare mock factories
    factory1 = mock(TypeAdapterFactory.class);
    factory2 = mock(TypeAdapterFactory.class);
    skipPastFactory = mock(TypeAdapterFactory.class);
    typeToken = TypeToken.get(String.class);

    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(factory1);
    factories.add(skipPastFactory);
    factories.add(factory2);

    // Remove final modifier from factories field to set it
    factoriesField.set(gson, factories);

    // Set jsonAdapterFactory field (required if skipPast not in factories)
    Field jsonAdapterFactoryField = Gson.class.getDeclaredField("jsonAdapterFactory");
    jsonAdapterFactoryField.setAccessible(true);
    TypeAdapterFactory jsonAdapterFactory = mock(TypeAdapterFactory.class);
    jsonAdapterFactoryField.set(gson, jsonAdapterFactory);
  }

  @Test
    @Timeout(8000)
  public void testGetDelegateAdapter_skipPastNotInFactories_usesJsonAdapterFactory() throws Exception {
    // Arrange: skipPast not in factories list
    TypeAdapterFactory notInFactories = mock(TypeAdapterFactory.class);

    // Setup jsonAdapterFactory to return a TypeAdapter
    Field jsonAdapterFactoryField = Gson.class.getDeclaredField("jsonAdapterFactory");
    jsonAdapterFactoryField.setAccessible(true);
    TypeAdapterFactory jsonAdapterFactory = (TypeAdapterFactory) jsonAdapterFactoryField.get(gson);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> expectedAdapter = mock(TypeAdapter.class);

    // Make jsonAdapterFactory.create return null so that loop continues,
    // then factory1, skipPastFactory return null, and factory2 returns expectedAdapter
    when(jsonAdapterFactory.create(eq(gson), eq(typeToken))).thenReturn(null);
    when(factory1.create(eq(gson), eq(typeToken))).thenReturn(null);
    when(skipPastFactory.create(eq(gson), eq(typeToken))).thenReturn(null);
    when(factory2.create(eq(gson), eq(typeToken))).thenReturn(expectedAdapter);

    // Act
    TypeAdapter<String> actualAdapter = gson.getDelegateAdapter(notInFactories, typeToken);

    // Assert
    assertSame(expectedAdapter, actualAdapter);
  }

  @Test
    @Timeout(8000)
  public void testGetDelegateAdapter_skipPastInFactories_returnsNextFactoryAdapter() {
    // Arrange
    @SuppressWarnings("unchecked")
    TypeAdapter<String> expectedAdapter = mock(TypeAdapter.class);
    when(factory1.create(eq(gson), eq(typeToken))).thenReturn(null);
    when(factory2.create(eq(gson), eq(typeToken))).thenReturn(expectedAdapter);

    // Act
    TypeAdapter<String> actualAdapter = gson.getDelegateAdapter(skipPastFactory, typeToken);

    // Assert
    assertSame(expectedAdapter, actualAdapter);
  }

  @Test
    @Timeout(8000)
  public void testGetDelegateAdapter_noFactoryReturnsAdapter_throws() {
    // Arrange
    when(factory1.create(eq(gson), eq(typeToken))).thenReturn(null);
    when(skipPastFactory.create(eq(gson), eq(typeToken))).thenReturn(null);
    when(factory2.create(eq(gson), eq(typeToken))).thenReturn(null);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> gson.getDelegateAdapter(skipPastFactory, typeToken));
    assertTrue(exception.getMessage().contains("GSON cannot serialize"));
    assertTrue(exception.getMessage().contains(typeToken.toString()));
  }

  @Test
    @Timeout(8000)
  public void testGetDelegateAdapter_skipPastIsLastFactory_throws() throws Exception {
    // Arrange
    // Make skipPastFactory last in list
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(factory1);
    factories.add(factory2);
    factories.add(skipPastFactory);

    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    factoriesField.set(gson, factories);

    when(factory1.create(eq(gson), eq(typeToken))).thenReturn(null);
    when(factory2.create(eq(gson), eq(typeToken))).thenReturn(null);
    when(skipPastFactory.create(eq(gson), eq(typeToken))).thenReturn(null);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> gson.getDelegateAdapter(skipPastFactory, typeToken));
    assertTrue(exception.getMessage().contains("GSON cannot serialize"));
  }
}
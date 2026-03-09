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

class GsonGetDelegateAdapterTest {

  private Gson gson;
  private TypeAdapterFactory skipPastFactory;
  private TypeAdapterFactory otherFactory1;
  private TypeAdapterFactory otherFactory2;
  private TypeToken<String> typeToken;

  @BeforeEach
  void setUp() throws Exception {
    gson = new Gson();

    // Prepare mocks
    skipPastFactory = mock(TypeAdapterFactory.class);
    otherFactory1 = mock(TypeAdapterFactory.class);
    otherFactory2 = mock(TypeAdapterFactory.class);

    typeToken = TypeToken.get(String.class);

    // Use reflection to set the private final field 'factories'
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);

    // Create a list of factories including skipPastFactory and others
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(otherFactory1);
    factories.add(skipPastFactory);
    factories.add(otherFactory2);

    factoriesField.set(gson, factories);

    // Also set jsonAdapterFactory to a mock for the test where skipPast not contained
    Field jsonAdapterFactoryField = Gson.class.getDeclaredField("jsonAdapterFactory");
    jsonAdapterFactoryField.setAccessible(true);
    TypeAdapterFactory jsonAdapterFactory = mock(TypeAdapterFactory.class);
    jsonAdapterFactoryField.set(gson, jsonAdapterFactory);
  }

  @Test
    @Timeout(8000)
  void getDelegateAdapter_skipPastNotInFactories_usesJsonAdapterFactory() throws Exception {
    // Arrange
    TypeAdapterFactory notInFactories = mock(TypeAdapterFactory.class);
    TypeAdapter<String> expectedAdapter = mock(TypeAdapter.class);

    // jsonAdapterFactory is mock set in setUp
    Field jsonAdapterFactoryField = Gson.class.getDeclaredField("jsonAdapterFactory");
    jsonAdapterFactoryField.setAccessible(true);
    TypeAdapterFactory jsonAdapterFactory = (TypeAdapterFactory) jsonAdapterFactoryField.get(gson);
    when(jsonAdapterFactory.create(eq(gson), eq(typeToken))).thenReturn(expectedAdapter);

    // Act
    TypeAdapter<String> adapter = gson.getDelegateAdapter(notInFactories, typeToken);

    // Assert
    assertSame(expectedAdapter, adapter);

    // Verify that create was called on jsonAdapterFactory and not on others after skipPast
    verify(jsonAdapterFactory).create(eq(gson), eq(typeToken));
    verifyNoMoreInteractions(otherFactory1, skipPastFactory, otherFactory2);
  }

  @Test
    @Timeout(8000)
  void getDelegateAdapter_skipPastInFactories_returnsFirstNonNullAdapterAfterSkipPast() {
    // Arrange
    TypeAdapter<String> nullAdapter = null;
    TypeAdapter<String> expectedAdapter = mock(TypeAdapter.class);

    // Configure factories:
    // otherFactory1.create returns null
    when(otherFactory1.create(eq(gson), eq(typeToken))).thenReturn(nullAdapter);
    // skipPastFactory.create returns null (should not be called)
    // otherFactory2.create returns expectedAdapter
    when(otherFactory2.create(eq(gson), eq(typeToken))).thenReturn(expectedAdapter);

    // Act
    TypeAdapter<String> adapter = gson.getDelegateAdapter(skipPastFactory, typeToken);

    // Assert
    assertSame(expectedAdapter, adapter);

    // Verify call order and calls
    verify(otherFactory1).create(eq(gson), eq(typeToken));
    verify(otherFactory2).create(eq(gson), eq(typeToken));
    verify(skipPastFactory, never()).create(any(), any());
  }

  @Test
    @Timeout(8000)
  void getDelegateAdapter_skipPastInFactories_noNonNullAdapter_throws() {
    // Arrange
    // All factories after skipPast return null
    when(otherFactory2.create(eq(gson), eq(typeToken))).thenReturn(null);
    when(otherFactory1.create(eq(gson), eq(typeToken))).thenReturn(null);

    // Act & Assert
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
      gson.getDelegateAdapter(skipPastFactory, typeToken);
    });
    assertTrue(ex.getMessage().contains("GSON cannot serialize"));
    assertTrue(ex.getMessage().contains(typeToken.toString()));
  }

  @Test
    @Timeout(8000)
  void getDelegateAdapter_skipPastIsLastFactory_throws() {
    // Arrange
    // Set factories list so skipPast is last element
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(otherFactory1);
    factories.add(otherFactory2);
    factories.add(skipPastFactory);
    try {
      Field factoriesField = Gson.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      factoriesField.set(gson, factories);
    } catch (Exception e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    // Act & Assert
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
      gson.getDelegateAdapter(skipPastFactory, typeToken);
    });
    assertTrue(ex.getMessage().contains("GSON cannot serialize"));
  }

  @Test
    @Timeout(8000)
  void getDelegateAdapter_skipPastFactoryNullInList_ignoresNullsAndContinues() {
    // Arrange
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(otherFactory1);
    factories.add(null); // null factory
    factories.add(skipPastFactory);
    factories.add(otherFactory2);

    try {
      Field factoriesField = Gson.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      factoriesField.set(gson, factories);
    } catch (Exception e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    TypeAdapter<String> expectedAdapter = mock(TypeAdapter.class);
    when(otherFactory2.create(eq(gson), eq(typeToken))).thenReturn(expectedAdapter);
    when(otherFactory1.create(eq(gson), eq(typeToken))).thenReturn(null);

    // Act
    TypeAdapter<String> adapter = gson.getDelegateAdapter(skipPastFactory, typeToken);

    // Assert
    assertSame(expectedAdapter, adapter);
  }
}
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

class Gson_329_5Test {

  private Gson gson;
  private TypeAdapterFactory skipPastFactory;
  private TypeAdapterFactory factory1;
  private TypeAdapterFactory factory2;
  private TypeToken<Object> typeToken;

  @BeforeEach
  void setUp() throws Exception {
    gson = new Gson();

    // Create mock factories
    skipPastFactory = mock(TypeAdapterFactory.class);
    factory1 = mock(TypeAdapterFactory.class);
    factory2 = mock(TypeAdapterFactory.class);

    // Prepare factories list: skipPastFactory, factory1, factory2
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(skipPastFactory);
    factories.add(factory1);
    factories.add(factory2);

    // Use reflection to set the private final field 'factories'
    setFinalField(gson, "factories", factories);

    // Set jsonAdapterFactory field to an actual instance of JsonAdapterAnnotationTypeAdapterFactory
    // to avoid IllegalArgumentException when setting a Mockito mock to a final field of that type
    Field jsonAdapterFactoryField = Gson.class.getDeclaredField("jsonAdapterFactory");
    jsonAdapterFactoryField.setAccessible(true);
    Object originalJsonAdapterFactory = jsonAdapterFactoryField.get(gson);

    // We keep original jsonAdapterFactory to avoid IllegalArgumentException on final field assignment
    // So do not replace it with mock, but keep original instance

    typeToken = TypeToken.get(Object.class);
  }

  @Test
    @Timeout(8000)
  void getDelegateAdapter_skipPastNotInFactories_usesJsonAdapterFactory() throws Exception {
    // Arrange: skipPastFactory is NOT in factories list
    // So getDelegateAdapter should use jsonAdapterFactory as skipPast

    // Retrieve the original jsonAdapterFactory instance
    Object jsonAdapterFactory = getFinalField(gson, "jsonAdapterFactory");

    // Setup a new factories list: jsonAdapterFactory, nextFactory
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add((TypeAdapterFactory) jsonAdapterFactory);
    TypeAdapterFactory nextFactory = mock(TypeAdapterFactory.class);
    factories.add(nextFactory);
    setFinalField(gson, "factories", factories);

    // Setup nextFactory to return a non-null adapter
    TypeAdapter<Object> adapter = mock(TypeAdapter.class);
    when(nextFactory.create(eq(gson), eq(typeToken))).thenReturn(adapter);

    // Act
    TypeAdapter<Object> result = gson.getDelegateAdapter(skipPastFactory, typeToken);

    // Assert
    assertSame(adapter, result);
    verify(nextFactory).create(gson, typeToken);
  }

  @Test
    @Timeout(8000)
  void getDelegateAdapter_skipPastInFactories_returnsAdapterFromNextFactory() throws Exception {
    // Arrange: skipPastFactory is in factories list, followed by factory1 and factory2
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(factory1);
    factories.add(skipPastFactory);
    factories.add(factory2);
    setFinalField(gson, "factories", factories);

    // Setup factory2 to return a non-null adapter
    TypeAdapter<Object> adapter = mock(TypeAdapter.class);
    when(factory2.create(eq(gson), eq(typeToken))).thenReturn(adapter);

    // Act
    TypeAdapter<Object> result = gson.getDelegateAdapter(skipPastFactory, typeToken);

    // Assert
    assertSame(adapter, result);
    verify(factory2).create(gson, typeToken);
    verify(factory1, never()).create(any(), any());
    verify(skipPastFactory, never()).create(any(), any());
  }

  @Test
    @Timeout(8000)
  void getDelegateAdapter_noFactoryReturnsAdapter_throwsIllegalArgumentException() throws Exception {
    // Arrange: skipPastFactory is in factories list, but no factory returns non-null adapter
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(skipPastFactory);
    factories.add(factory1);
    factories.add(factory2);
    setFinalField(gson, "factories", factories);

    when(factory1.create(eq(gson), eq(typeToken))).thenReturn(null);
    when(factory2.create(eq(gson), eq(typeToken))).thenReturn(null);

    // Act & Assert
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
        gson.getDelegateAdapter(skipPastFactory, typeToken));
    assertTrue(thrown.getMessage().contains("GSON cannot serialize"));
  }

  // Helper methods to set and get private final fields via reflection

  private void setFinalField(Object target, String fieldName, Object value) throws Exception {
    Field field = Gson.class.getDeclaredField(fieldName);
    field.setAccessible(true);

    // Remove final modifier using reflection
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    field.set(target, value);
  }

  private Object getFinalField(Object target, String fieldName) throws Exception {
    Field field = Gson.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }
}
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class GsonGetDelegateAdapterTest {

  private Gson gson;
  private TypeAdapterFactory skipPastFactory;
  private TypeAdapterFactory factory1;
  private TypeAdapterFactory factory2;
  private TypeAdapterFactory jsonAdapterFactory;
  private TypeToken<String> typeToken;
  private TypeAdapter<String> adapter1;
  private TypeAdapter<String> adapter2;

  @BeforeEach
  void setUp() throws Exception {
    gson = new Gson();

    skipPastFactory = mock(TypeAdapterFactory.class);
    factory1 = mock(TypeAdapterFactory.class);
    factory2 = mock(TypeAdapterFactory.class);
    jsonAdapterFactory = mock(TypeAdapterFactory.class);

    adapter1 = mock(TypeAdapter.class);
    adapter2 = mock(TypeAdapter.class);

    typeToken = TypeToken.get(String.class);

    // Set factories field via reflection
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(factory1);
    factories.add(factory2);
    factories.add(jsonAdapterFactory); // Add jsonAdapterFactory to factories list
    try {
      setFinalField(gson, "factories", factories);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    // Set jsonAdapterFactory field via reflection
    try {
      setFinalField(gson, "jsonAdapterFactory", jsonAdapterFactory);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void testGetDelegateAdapter_skipPastFactoryInFactories_returnsNextAdapter() {
    // factories = [factory1, factory2, jsonAdapterFactory]
    // skipPast = factory1
    // factory2.create() returns adapter2

    when(factory2.create(gson, typeToken)).thenReturn(adapter2);

    TypeAdapter<String> result = gson.getDelegateAdapter(factory1, typeToken);

    assertSame(adapter2, result);

    // Verify create() called only on factory2
    verify(factory1, never()).create(any(), any());
    verify(factory2).create(gson, typeToken);
  }

  @Test
    @Timeout(8000)
  void testGetDelegateAdapter_skipPastFactoryNotInFactories_usesJsonAdapterFactory() throws Exception {
    // skipPastFactory not in factories -> replaced by jsonAdapterFactory
    // factories = [factory1, factory2, jsonAdapterFactory]
    // jsonAdapterFactory.create returns null, factory2.create returns adapter2

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> {
          // Temporarily remove jsonAdapterFactory from factories to trigger exception
          List<TypeAdapterFactory> factoriesWithoutJsonAdapter = new ArrayList<>();
          factoriesWithoutJsonAdapter.add(factory1);
          factoriesWithoutJsonAdapter.add(factory2);
          setFinalField(gson, "factories", factoriesWithoutJsonAdapter);

          try {
            gson.getDelegateAdapter(skipPastFactory, typeToken);
          } finally {
            // Restore factories with jsonAdapterFactory after test
            List<TypeAdapterFactory> factoriesWithJsonAdapter = new ArrayList<>();
            factoriesWithJsonAdapter.add(factory1);
            factoriesWithJsonAdapter.add(factory2);
            factoriesWithJsonAdapter.add(jsonAdapterFactory);
            setFinalField(gson, "factories", factoriesWithJsonAdapter);
          }
        });
    assertTrue(ex.getMessage().contains("GSON cannot serialize"));

    // Now factories contain jsonAdapterFactory
    when(jsonAdapterFactory.create(gson, typeToken)).thenReturn(null);
    when(factory2.create(gson, typeToken)).thenReturn(adapter2);

    TypeAdapter<String> result = gson.getDelegateAdapter(skipPastFactory, typeToken);
    assertSame(adapter2, result);

    InOrder inOrder = inOrder(factory1, jsonAdapterFactory, factory2);
    inOrder.verify(factory1, never()).create(any(), any());
    inOrder.verify(jsonAdapterFactory).create(gson, typeToken);
    inOrder.verify(factory2).create(gson, typeToken);
  }

  @Test
    @Timeout(8000)
  void testGetDelegateAdapter_candidateNullContinuesLoop() {
    // factories = [skipPastFactory, factory1, factory2, jsonAdapterFactory]
    // skipPastFactory.create returns null
    // factory1.create returns null
    // factory2.create returns adapter2

    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(skipPastFactory);
    factories.add(factory1);
    factories.add(factory2);
    factories.add(jsonAdapterFactory);
    try {
      setFinalField(gson, "factories", factories);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    when(skipPastFactory.create(gson, typeToken)).thenReturn(null);
    when(factory1.create(gson, typeToken)).thenReturn(null);
    when(factory2.create(gson, typeToken)).thenReturn(adapter2);

    TypeAdapter<String> result = gson.getDelegateAdapter(skipPastFactory, typeToken);
    assertSame(adapter2, result);

    verify(skipPastFactory).create(gson, typeToken);
    verify(factory1).create(gson, typeToken);
    verify(factory2).create(gson, typeToken);
  }

  @Test
    @Timeout(8000)
  void testGetDelegateAdapter_noFactoriesAfterSkipPast_throws() {
    // factories = [factory1, jsonAdapterFactory]
    // skipPast = factory1
    // no factories after skipPast that return adapter, expect exception

    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(factory1);
    factories.add(jsonAdapterFactory);
    try {
      setFinalField(gson, "factories", factories);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    when(jsonAdapterFactory.create(gson, typeToken)).thenReturn(null);

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> gson.getDelegateAdapter(factory1, typeToken));

    assertTrue(ex.getMessage().contains("GSON cannot serialize"));
  }

  private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
    Field field = Gson.class.getDeclaredField(fieldName);
    field.setAccessible(true);

    // Remove final modifier if present
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    field.set(target, value);
  }
}
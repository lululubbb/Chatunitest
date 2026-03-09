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
  private TypeAdapterFactory skipPastFactory;
  private TypeAdapterFactory factory1;
  private TypeAdapterFactory factory2;
  private TypeAdapterFactory jsonAdapterFactory;
  private TypeToken<String> typeToken;

  @BeforeEach
  void setUp() throws Exception {
    // Create Gson instance with reflection to set final fields correctly
    gson = createGsonWithFactories();

    skipPastFactory = mock(TypeAdapterFactory.class);
    factory1 = mock(TypeAdapterFactory.class);
    factory2 = mock(TypeAdapterFactory.class);
    jsonAdapterFactory = mock(TypeAdapterFactory.class);

    // Set factories list with factory1 and factory2
    List<TypeAdapterFactory> factories = new ArrayList<>();
    factories.add(factory1);
    factories.add(factory2);

    setField(gson, "factories", factories);
    setField(gson, "jsonAdapterFactory", jsonAdapterFactory);

    typeToken = TypeToken.get(String.class);
  }

  @Test
    @Timeout(8000)
  void testGetDelegateAdapter_skipPastInFactories_returnsNextFactoryAdapter() throws Exception {
    // Arrange
    List<TypeAdapterFactory> factories = getFactoriesList(gson);
    factories.add(0, skipPastFactory); // add skipPast at start

    TypeAdapter<String> adapterFromFactory2 = mock(TypeAdapter.class);
    when(factory2.create(gson, typeToken)).thenReturn(adapterFromFactory2);

    // Act
    TypeAdapter<String> result = gson.getDelegateAdapter(skipPastFactory, typeToken);

    // Assert
    assertSame(adapterFromFactory2, result);
    verify(factory1, never()).create(any(), any());
    verify(factory2).create(gson, typeToken);
  }

  @Test
    @Timeout(8000)
  void testGetDelegateAdapter_skipPastNotInFactories_usesJsonAdapterFactory() throws Exception {
    // Arrange
    // skipPastFactory not in factories list
    TypeAdapter<String> adapterFromFactory1 = mock(TypeAdapter.class);
    when(factory1.create(gson, typeToken)).thenReturn(adapterFromFactory1);
    when(factory2.create(gson, typeToken)).thenReturn(null);
    when(jsonAdapterFactory.create(gson, typeToken)).thenReturn(null);

    // Act
    // Since skipPastFactory not in factories, skipPast becomes jsonAdapterFactory
    // Add jsonAdapterFactory to factories to allow iteration after it
    List<TypeAdapterFactory> factories = getFactoriesList(gson);
    factories.clear();
    factories.add(jsonAdapterFactory);
    factories.add(factory1);
    factories.add(factory2);

    TypeAdapter<String> result = gson.getDelegateAdapter(skipPastFactory, typeToken);

    // Assert
    assertSame(adapterFromFactory1, result);
  }

  @Test
    @Timeout(8000)
  void testGetDelegateAdapter_noFactoryCreatesAdapter_throws() throws Exception {
    // Arrange
    List<TypeAdapterFactory> factories = getFactoriesList(gson);
    factories.add(0, skipPastFactory);

    when(factory1.create(gson, typeToken)).thenReturn(null);
    when(factory2.create(gson, typeToken)).thenReturn(null);

    // Act & Assert
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> gson.getDelegateAdapter(skipPastFactory, typeToken));
    assertTrue(ex.getMessage().contains("GSON cannot serialize"));
    assertTrue(ex.getMessage().contains(typeToken.toString()));
  }

  @SuppressWarnings("unchecked")
  private List<TypeAdapterFactory> getFactoriesList(Gson gsonInstance) throws Exception {
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonInstance);
    if (factories == null) {
      factories = new ArrayList<>();
      factoriesField.set(gsonInstance, factories);
    }
    return factories;
  }

  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = Gson.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private static Gson createGsonWithFactories() throws Exception {
    // Create Gson instance with default constructor
    Gson gson = new Gson();

    // Use reflection to set final fields factories and jsonAdapterFactory to avoid NPEs
    setField(gson, "factories", new ArrayList<TypeAdapterFactory>());
    setField(gson, "jsonAdapterFactory", mock(TypeAdapterFactory.class));

    return gson;
  }
}
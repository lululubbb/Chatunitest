package com.google.gson;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.Gson.DEFAULT_COMPLEX_MAP_KEYS;
import static com.google.gson.Gson.DEFAULT_DATE_PATTERN;
import static com.google.gson.Gson.DEFAULT_ESCAPE_HTML;
import static com.google.gson.Gson.DEFAULT_JSON_NON_EXECUTABLE;
import static com.google.gson.Gson.DEFAULT_LENIENT;
import static com.google.gson.Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY;
import static com.google.gson.Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY;
import static com.google.gson.Gson.DEFAULT_PRETTY_PRINT;
import static com.google.gson.Gson.DEFAULT_SERIALIZE_NULLS;
import static com.google.gson.Gson.DEFAULT_SPECIALIZE_FLOAT_VALUES;
import static com.google.gson.Gson.DEFAULT_USE_JDK_UNSAFE;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

class GsonBuilder_24_5Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withNullType_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> gsonBuilder.registerTypeAdapter(null, new Object()));
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withInvalidTypeAdapter_throwsIllegalArgumentException() {
    Object invalidAdapter = new Object();
    assertThrows(IllegalArgumentException.class, () -> gsonBuilder.registerTypeAdapter(String.class, invalidAdapter));
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withInstanceCreator_registersInInstanceCreators() throws Exception {
    InstanceCreator<String> instanceCreator = mock(InstanceCreator.class);
    gsonBuilder.registerTypeAdapter(String.class, instanceCreator);

    // Access private field instanceCreators to verify
    Field instanceCreatorsField = GsonBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(gsonBuilder);

    assertTrue(instanceCreators.containsKey(String.class));
    assertSame(instanceCreator, instanceCreators.get(String.class));
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withJsonSerializer_addsFactory() throws Exception {
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    Type type = String.class;

    try (MockedStatic<TreeTypeAdapter> treeTypeAdapterStatic = Mockito.mockStatic(TreeTypeAdapter.class)) {
      @SuppressWarnings("unchecked")
      TypeToken<String> typeToken = (TypeToken<String>) TypeToken.get(type);
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
      treeTypeAdapterStatic.when(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, serializer)).thenReturn(factoryMock);

      gsonBuilder.registerTypeAdapter(type, serializer);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      assertTrue(factories.contains(factoryMock));
      treeTypeAdapterStatic.verify(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, serializer));
    }
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withJsonDeserializer_addsFactory() throws Exception {
    JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
    Type type = String.class;

    try (MockedStatic<TreeTypeAdapter> treeTypeAdapterStatic = Mockito.mockStatic(TreeTypeAdapter.class)) {
      @SuppressWarnings("unchecked")
      TypeToken<String> typeToken = (TypeToken<String>) TypeToken.get(type);
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
      treeTypeAdapterStatic.when(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, deserializer)).thenReturn(factoryMock);

      gsonBuilder.registerTypeAdapter(type, deserializer);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      assertTrue(factories.contains(factoryMock));
      treeTypeAdapterStatic.verify(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, deserializer));
    }
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withTypeAdapter_addsFactory() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);
    Type type = String.class;

    try (MockedStatic<TypeAdapters> typeAdaptersStatic = Mockito.mockStatic(TypeAdapters.class)) {
      @SuppressWarnings("unchecked")
      TypeToken<String> typeToken = (TypeToken<String>) TypeToken.get(type);
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
      typeAdaptersStatic.when(() -> TypeAdapters.newFactory(typeToken, (TypeAdapter<String>) typeAdapter)).thenReturn(factoryMock);

      gsonBuilder.registerTypeAdapter(type, typeAdapter);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      assertTrue(factories.contains(factoryMock));
      typeAdaptersStatic.verify(() -> TypeAdapters.newFactory(typeToken, (TypeAdapter<String>) typeAdapter));
    }
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withMultipleInterfaces_addsAllRelevantFactoriesAndInstanceCreator() throws Exception {
    // Create a mock that implements multiple interfaces, only interfaces allowed in extraInterfaces
    Object multiAdapter = mock(JsonSerializer.class, withSettings().extraInterfaces(JsonDeserializer.class, InstanceCreator.class));

    Type type = String.class;

    try (MockedStatic<TreeTypeAdapter> treeTypeAdapterStatic = Mockito.mockStatic(TreeTypeAdapter.class);
         MockedStatic<TypeAdapters> typeAdaptersStatic = Mockito.mockStatic(TypeAdapters.class)) {

      @SuppressWarnings("unchecked")
      TypeToken<String> typeToken = (TypeToken<String>) TypeToken.get(type);
      TypeAdapterFactory treeFactoryMock = mock(TypeAdapterFactory.class);
      TypeAdapterFactory typeAdapterFactoryMock = mock(TypeAdapterFactory.class);

      // Setup mocks to return specific factories for each interface
      treeTypeAdapterStatic.when(() -> TreeTypeAdapter.newFactoryWithMatchRawType(eq(typeToken), any(JsonSerializer.class))).thenReturn(treeFactoryMock);
      treeTypeAdapterStatic.when(() -> TreeTypeAdapter.newFactoryWithMatchRawType(eq(typeToken), any(JsonDeserializer.class))).thenReturn(treeFactoryMock);
      typeAdaptersStatic.when(() -> TypeAdapters.newFactory(eq(typeToken), any(TypeAdapter.class))).thenReturn(typeAdapterFactoryMock);

      gsonBuilder.registerTypeAdapter(type, multiAdapter);

      Field instanceCreatorsField = GsonBuilder.class.getDeclaredField("instanceCreators");
      instanceCreatorsField.setAccessible(true);
      Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(gsonBuilder);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      // InstanceCreator registered
      assertTrue(instanceCreators.containsKey(type));
      assertSame(multiAdapter, instanceCreators.get(type));

      // TreeTypeAdapter factories added twice (for JsonSerializer and JsonDeserializer)
      // Because both JsonSerializer and JsonDeserializer are implemented, two factories added
      assertTrue(factories.contains(treeFactoryMock));
      treeTypeAdapterStatic.verify(() -> TreeTypeAdapter.newFactoryWithMatchRawType(eq(typeToken), any(JsonSerializer.class)), times(1));
      treeTypeAdapterStatic.verify(() -> TreeTypeAdapter.newFactoryWithMatchRawType(eq(typeToken), any(JsonDeserializer.class)), times(1));

      // TypeAdapters factory added
      assertTrue(factories.contains(typeAdapterFactoryMock));
      typeAdaptersStatic.verify(() -> TypeAdapters.newFactory(eq(typeToken), any(TypeAdapter.class)), times(1));
    }
  }
}
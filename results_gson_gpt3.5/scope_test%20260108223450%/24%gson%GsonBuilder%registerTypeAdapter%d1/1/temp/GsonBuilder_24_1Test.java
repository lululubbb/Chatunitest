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

class GsonBuilder_24_1Test {

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
  void registerTypeAdapter_withInstanceCreator_addsToInstanceCreators() throws Exception {
    @SuppressWarnings("unchecked")
    InstanceCreator<String> instanceCreator = mock(InstanceCreator.class);

    gsonBuilder.registerTypeAdapter(String.class, instanceCreator);

    Field instanceCreatorsField = GsonBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(gsonBuilder);

    assertTrue(instanceCreators.containsKey(String.class));
    assertSame(instanceCreator, instanceCreators.get(String.class));
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withJsonSerializer_addsFactoryWithTreeTypeAdapter() throws Exception {
    JsonSerializer<String> serializer = mock(JsonSerializer.class);

    try (MockedStatic<TreeTypeAdapter> treeTypeAdapterMockedStatic = Mockito.mockStatic(TreeTypeAdapter.class)) {
      TypeToken<String> typeToken = TypeToken.get(String.class);
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
      treeTypeAdapterMockedStatic.when(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, serializer))
          .thenReturn(factoryMock);

      gsonBuilder.registerTypeAdapter(String.class, serializer);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      assertTrue(factories.contains(factoryMock));
      treeTypeAdapterMockedStatic.verify(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, serializer));
    }
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withJsonDeserializer_addsFactoryWithTreeTypeAdapter() throws Exception {
    JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);

    try (MockedStatic<TreeTypeAdapter> treeTypeAdapterMockedStatic = Mockito.mockStatic(TreeTypeAdapter.class)) {
      TypeToken<String> typeToken = TypeToken.get(String.class);
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
      treeTypeAdapterMockedStatic.when(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, deserializer))
          .thenReturn(factoryMock);

      gsonBuilder.registerTypeAdapter(String.class, deserializer);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      assertTrue(factories.contains(factoryMock));
      treeTypeAdapterMockedStatic.verify(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, deserializer));
    }
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withTypeAdapter_addsFactoryWithTypeAdaptersNewFactory() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);

    try (MockedStatic<TypeAdapters> typeAdaptersMockedStatic = Mockito.mockStatic(TypeAdapters.class)) {
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
      // Cast typeToken and typeAdapter to raw types to avoid generic capture issue
      TypeToken rawTypeToken = TypeToken.get(String.class);
      TypeAdapter rawTypeAdapter = typeAdapter;

      typeAdaptersMockedStatic.when(() -> TypeAdapters.newFactory(rawTypeToken, rawTypeAdapter))
          .thenReturn(factoryMock);

      gsonBuilder.registerTypeAdapter(String.class, typeAdapter);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      assertTrue(factories.contains(factoryMock));
      typeAdaptersMockedStatic.verify(() -> TypeAdapters.newFactory(rawTypeToken, rawTypeAdapter));
    }
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withMultipleInterfaces_addsAllRelevantFactoriesAndInstanceCreators() throws Exception {
    // Create a mock object implementing multiple interfaces
    Object multiAdapter = mock(JsonSerializer.class, withSettings().extraInterfaces(JsonDeserializer.class, InstanceCreator.class, TypeAdapter.class));

    // Because the cast is unsafe, we mock each interface separately
    JsonSerializer<?> serializer = (JsonSerializer<?>) multiAdapter;
    JsonDeserializer<?> deserializer = (JsonDeserializer<?>) multiAdapter;
    InstanceCreator<?> instanceCreator = (InstanceCreator<?>) multiAdapter;
    @SuppressWarnings("unchecked")
    TypeAdapter<?> typeAdapter = (TypeAdapter<?>) multiAdapter;

    try (MockedStatic<TreeTypeAdapter> treeTypeAdapterMockedStatic = Mockito.mockStatic(TreeTypeAdapter.class);
         MockedStatic<TypeAdapters> typeAdaptersMockedStatic = Mockito.mockStatic(TypeAdapters.class)) {

      TypeToken<?> typeToken = TypeToken.get(String.class);
      TypeAdapterFactory treeFactory = mock(TypeAdapterFactory.class);
      TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);

      treeTypeAdapterMockedStatic.when(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, serializer))
          .thenReturn(treeFactory);
      treeTypeAdapterMockedStatic.when(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, deserializer))
          .thenReturn(treeFactory);

      // Cast to raw types to avoid generic capture issue
      TypeToken rawTypeToken = typeToken;
      TypeAdapter rawTypeAdapter = (TypeAdapter) typeAdapter;
      typeAdaptersMockedStatic.when(() -> TypeAdapters.newFactory(rawTypeToken, rawTypeAdapter))
          .thenReturn(typeAdapterFactory);

      gsonBuilder.registerTypeAdapter(String.class, multiAdapter);

      Field instanceCreatorsField = GsonBuilder.class.getDeclaredField("instanceCreators");
      instanceCreatorsField.setAccessible(true);
      Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(gsonBuilder);
      assertTrue(instanceCreators.containsKey(String.class));
      assertSame(instanceCreator, instanceCreators.get(String.class));

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      // Because the same factory is returned for serializer and deserializer mocks, it should be added twice
      assertTrue(factories.contains(treeFactory));
      assertTrue(factories.contains(typeAdapterFactory));

      treeTypeAdapterMockedStatic.verify(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, serializer));
      treeTypeAdapterMockedStatic.verify(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, deserializer));
      typeAdaptersMockedStatic.verify(() -> TypeAdapters.newFactory(rawTypeToken, rawTypeAdapter));
    }
  }
}
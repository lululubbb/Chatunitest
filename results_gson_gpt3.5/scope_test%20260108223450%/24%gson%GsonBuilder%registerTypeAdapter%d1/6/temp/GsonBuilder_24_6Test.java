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
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

class GsonBuilder_24_6Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withNullType_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      gsonBuilder.registerTypeAdapter(null, new Object());
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withInvalidTypeAdapter_throwsIllegalArgumentException() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      gsonBuilder.registerTypeAdapter(String.class, new Object());
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withInstanceCreator_addsToInstanceCreators() throws Exception {
    InstanceCreator<String> instanceCreator = mock(InstanceCreator.class);
    gsonBuilder.registerTypeAdapter(String.class, instanceCreator);

    Field instanceCreatorsField = GsonBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(gsonBuilder);

    assertTrue(instanceCreators.containsKey(String.class));
    assertEquals(instanceCreator, instanceCreators.get(String.class));
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withJsonSerializer_addsFactory() throws Exception {
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    Object typeAdapter = serializer;

    try (MockedStatic<TreeTypeAdapter> treeTypeAdapterStatic = Mockito.mockStatic(TreeTypeAdapter.class)) {
      TypeToken<String> typeToken = TypeToken.get(String.class);
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
      treeTypeAdapterStatic.when(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, typeAdapter)).thenReturn(factoryMock);

      gsonBuilder.registerTypeAdapter(String.class, serializer);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      assertTrue(factories.contains(factoryMock));
    }
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withJsonDeserializer_addsFactory() throws Exception {
    JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
    Object typeAdapter = deserializer;

    try (MockedStatic<TreeTypeAdapter> treeTypeAdapterStatic = Mockito.mockStatic(TreeTypeAdapter.class)) {
      TypeToken<String> typeToken = TypeToken.get(String.class);
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
      treeTypeAdapterStatic.when(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, typeAdapter)).thenReturn(factoryMock);

      gsonBuilder.registerTypeAdapter(String.class, deserializer);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      assertTrue(factories.contains(factoryMock));
    }
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withTypeAdapter_addsFactory() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);

    try (MockedStatic<TypeAdapters> typeAdaptersStatic = Mockito.mockStatic(TypeAdapters.class)) {
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
      // Fix: cast typeAdapter to raw TypeAdapter to avoid generic mismatch
      typeAdaptersStatic.when(() -> TypeAdapters.newFactory(TypeToken.get(String.class), (TypeAdapter) typeAdapter)).thenReturn(factoryMock);

      gsonBuilder.registerTypeAdapter(String.class, typeAdapter);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      assertTrue(factories.contains(factoryMock));
    }
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withMultipleInterfaces_addsAllAppropriate() throws Exception {
    // Create a class implementing JsonSerializer and extending TypeAdapter<Object>
    class MultiAdapter extends TypeAdapter<Object> implements JsonSerializer<String> {
      @Override
      public com.google.gson.JsonElement serialize(String src, Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
        return null;
      }
      @Override
      public Object read(com.google.gson.stream.JsonReader in) {
        return null;
      }
      @Override
      public void write(com.google.gson.stream.JsonWriter out, Object value) {
      }
    }
    MultiAdapter multiAdapter = spy(new MultiAdapter());

    try (MockedStatic<TreeTypeAdapter> treeTypeAdapterStatic = Mockito.mockStatic(TreeTypeAdapter.class);
         MockedStatic<TypeAdapters> typeAdaptersStatic = Mockito.mockStatic(TypeAdapters.class)) {

      TypeToken<String> typeToken = TypeToken.get(String.class);
      TypeAdapterFactory treeFactoryMock = mock(TypeAdapterFactory.class);
      TypeAdapterFactory adapterFactoryMock = mock(TypeAdapterFactory.class);

      treeTypeAdapterStatic.when(() -> TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, multiAdapter)).thenReturn(treeFactoryMock);
      // Fix: cast multiAdapter to raw TypeAdapter to avoid generic mismatch
      typeAdaptersStatic.when(() -> TypeAdapters.newFactory(typeToken, (TypeAdapter) multiAdapter)).thenReturn(adapterFactoryMock);

      gsonBuilder.registerTypeAdapter(String.class, multiAdapter);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      assertTrue(factories.contains(treeFactoryMock));
      assertTrue(factories.contains(adapterFactoryMock));

      Field instanceCreatorsField = GsonBuilder.class.getDeclaredField("instanceCreators");
      instanceCreatorsField.setAccessible(true);
      Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(gsonBuilder);

      assertFalse(instanceCreators.containsKey(String.class));
    }
  }
}
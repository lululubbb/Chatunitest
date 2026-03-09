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

class GsonBuilder_24_4Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withNullType_throwsNullPointerException() {
    NullPointerException exception = assertThrows(NullPointerException.class,
        () -> gsonBuilder.registerTypeAdapter(null, new Object()));
    assertNull(exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withInvalidTypeAdapter_throwsIllegalArgumentException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> gsonBuilder.registerTypeAdapter(String.class, new Object()));
    assertTrue(exception.getMessage().contains("Expected"));
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withInstanceCreator_registersInInstanceCreators() throws Exception {
    InstanceCreator<String> creator = s -> "test";

    GsonBuilder returned = gsonBuilder.registerTypeAdapter(String.class, creator);
    assertSame(gsonBuilder, returned);

    Field instanceCreatorsField = GsonBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(gsonBuilder);

    assertTrue(instanceCreators.containsKey(String.class));
    assertSame(creator, instanceCreators.get(String.class));
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withJsonSerializer_addsFactory() throws Exception {
    JsonSerializer<String> serializer = mock(JsonSerializer.class);

    try (MockedStatic<TypeToken> typeTokenMockedStatic = Mockito.mockStatic(TypeToken.class);
         MockedStatic<TreeTypeAdapter> treeTypeAdapterMockedStatic = Mockito.mockStatic(TreeTypeAdapter.class)) {

      TypeToken<String> mockTypeToken = TypeToken.get(String.class);
      typeTokenMockedStatic.when(() -> TypeToken.get(String.class)).thenReturn(mockTypeToken);

      TypeAdapterFactory factory = mock(TypeAdapterFactory.class);
      treeTypeAdapterMockedStatic.when(() -> TreeTypeAdapter.newFactoryWithMatchRawType(mockTypeToken, serializer))
          .thenReturn(factory);

      GsonBuilder returned = gsonBuilder.registerTypeAdapter(String.class, serializer);
      assertSame(gsonBuilder, returned);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      @SuppressWarnings("unchecked")
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      assertTrue(factories.contains(factory));
    }
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withJsonDeserializer_addsFactory() throws Exception {
    JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);

    try (MockedStatic<TypeToken> typeTokenMockedStatic = Mockito.mockStatic(TypeToken.class);
         MockedStatic<TreeTypeAdapter> treeTypeAdapterMockedStatic = Mockito.mockStatic(TreeTypeAdapter.class)) {

      TypeToken<String> mockTypeToken = TypeToken.get(String.class);
      typeTokenMockedStatic.when(() -> TypeToken.get(String.class)).thenReturn(mockTypeToken);

      TypeAdapterFactory factory = mock(TypeAdapterFactory.class);
      treeTypeAdapterMockedStatic.when(() -> TreeTypeAdapter.newFactoryWithMatchRawType(mockTypeToken, deserializer))
          .thenReturn(factory);

      GsonBuilder returned = gsonBuilder.registerTypeAdapter(String.class, deserializer);
      assertSame(gsonBuilder, returned);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      @SuppressWarnings("unchecked")
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      assertTrue(factories.contains(factory));
    }
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapter_withTypeAdapter_addsFactory() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);

    try (MockedStatic<TypeAdapters> typeAdaptersMockedStatic = Mockito.mockStatic(TypeAdapters.class)) {

      TypeAdapterFactory factory = mock(TypeAdapterFactory.class);
      typeAdaptersMockedStatic.when(() -> TypeAdapters.newFactory(TypeToken.get(String.class), typeAdapter))
          .thenReturn(factory);

      GsonBuilder returned = gsonBuilder.registerTypeAdapter(String.class, typeAdapter);
      assertSame(gsonBuilder, returned);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      @SuppressWarnings("unchecked")
      List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);

      assertTrue(factories.contains(factory));
    }
  }

}
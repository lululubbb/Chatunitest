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
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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
import java.util.List;

class GsonBuilder_26_6Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void registerTypeHierarchyAdapter_nullBaseType_throwsNullPointerException() {
    NullPointerException exception = assertThrows(NullPointerException.class, () -> {
      gsonBuilder.registerTypeHierarchyAdapter(null, new JsonSerializer<Object>() {
        @Override
        public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
          return null;
        }
      });
    });
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  void registerTypeHierarchyAdapter_invalidTypeAdapter_throwsIllegalArgumentException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      gsonBuilder.registerTypeHierarchyAdapter(Object.class, new Object());
    });
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  void registerTypeHierarchyAdapter_withJsonSerializer_addsToHierarchyFactories() throws Exception {
    JsonSerializer<Object> serializer = new JsonSerializer<Object>() {
      @Override
      public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
      }
    };

    try (MockedStatic<TreeTypeAdapter> mockedTreeTypeAdapter = Mockito.mockStatic(TreeTypeAdapter.class)) {
      TreeTypeAdapter.TreeTypeAdapterFactory factoryMock = mock(TreeTypeAdapter.TreeTypeAdapterFactory.class);
      mockedTreeTypeAdapter.when(() -> TreeTypeAdapter.newTypeHierarchyFactory(Object.class, serializer))
          .thenReturn(factoryMock);

      GsonBuilder returned = gsonBuilder.registerTypeHierarchyAdapter(Object.class, serializer);
      assertSame(gsonBuilder, returned);

      Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
      hierarchyFactoriesField.setAccessible(true);
      List<?> hierarchyFactories = (List<?>) hierarchyFactoriesField.get(gsonBuilder);

      assertTrue(hierarchyFactories.contains(factoryMock));
    }
  }

  @Test
    @Timeout(8000)
  void registerTypeHierarchyAdapter_withJsonDeserializer_addsToHierarchyFactories() throws Exception {
    JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {
      @Override
      public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return null;
      }
    };

    try (MockedStatic<TreeTypeAdapter> mockedTreeTypeAdapter = Mockito.mockStatic(TreeTypeAdapter.class)) {
      TreeTypeAdapter.TreeTypeAdapterFactory factoryMock = mock(TreeTypeAdapter.TreeTypeAdapterFactory.class);
      mockedTreeTypeAdapter.when(() -> TreeTypeAdapter.newTypeHierarchyFactory(Object.class, deserializer))
          .thenReturn(factoryMock);

      GsonBuilder returned = gsonBuilder.registerTypeHierarchyAdapter(Object.class, deserializer);
      assertSame(gsonBuilder, returned);

      Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
      hierarchyFactoriesField.setAccessible(true);
      List<?> hierarchyFactories = (List<?>) hierarchyFactoriesField.get(gsonBuilder);

      assertTrue(hierarchyFactories.contains(factoryMock));
    }
  }

  @Test
    @Timeout(8000)
  void registerTypeHierarchyAdapter_withTypeAdapter_addsToFactories() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> typeAdapter = mock(TypeAdapter.class);

    try (MockedStatic<TypeAdapters> mockedTypeAdapters = Mockito.mockStatic(TypeAdapters.class)) {
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
      mockedTypeAdapters.when(() -> TypeAdapters.newTypeHierarchyFactory(Object.class, typeAdapter))
          .thenReturn(factoryMock);

      GsonBuilder returned = gsonBuilder.registerTypeHierarchyAdapter(Object.class, typeAdapter);
      assertSame(gsonBuilder, returned);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<?> factories = (List<?>) factoriesField.get(gsonBuilder);

      assertTrue(factories.contains(factoryMock));
    }
  }

  @Test
    @Timeout(8000)
  void registerTypeHierarchyAdapter_withJsonSerializerAndTypeAdapter_addsToBothLists() throws Exception {
    class MyAdapter extends TypeAdapter<Object> implements JsonSerializer<Object> {
      @Override
      public void write(JsonWriter out, Object value) {
      }

      @Override
      public Object read(JsonReader in) {
        return null;
      }

      @Override
      public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
      }
    }

    MyAdapter adapter = new MyAdapter();

    try (MockedStatic<TreeTypeAdapter> mockedTreeTypeAdapter = Mockito.mockStatic(TreeTypeAdapter.class);
         MockedStatic<TypeAdapters> mockedTypeAdapters = Mockito.mockStatic(TypeAdapters.class)) {

      TreeTypeAdapter.TreeTypeAdapterFactory hierarchyFactoryMock = mock(TreeTypeAdapter.TreeTypeAdapterFactory.class);
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);

      mockedTreeTypeAdapter.when(() -> TreeTypeAdapter.newTypeHierarchyFactory(Object.class, adapter))
          .thenReturn(hierarchyFactoryMock);
      mockedTypeAdapters.when(() -> TypeAdapters.newTypeHierarchyFactory(Object.class, adapter))
          .thenReturn(factoryMock);

      GsonBuilder returned = gsonBuilder.registerTypeHierarchyAdapter(Object.class, adapter);
      assertSame(gsonBuilder, returned);

      Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
      hierarchyFactoriesField.setAccessible(true);
      List<?> hierarchyFactories = (List<?>) hierarchyFactoriesField.get(gsonBuilder);
      assertTrue(hierarchyFactories.contains(hierarchyFactoryMock));

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<?> factories = (List<?>) factoriesField.get(gsonBuilder);
      assertTrue(factories.contains(factoryMock));
    }
  }
}
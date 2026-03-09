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

import java.lang.reflect.Field;
import java.util.List;

public class GsonBuilder_26_4Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  public void registerTypeHierarchyAdapter_nullBaseType_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> gsonBuilder.registerTypeHierarchyAdapter(null, new Object()));
  }

  @Test
    @Timeout(8000)
  public void registerTypeHierarchyAdapter_invalidTypeAdapter_throwsIllegalArgumentException() {
    Object invalidAdapter = new Object();
    assertThrows(IllegalArgumentException.class, () -> gsonBuilder.registerTypeHierarchyAdapter(String.class, invalidAdapter));
  }

  @Test
    @Timeout(8000)
  public void registerTypeHierarchyAdapter_withJsonSerializer_addsToHierarchyFactories() throws Exception {
    JsonSerializer<String> serializer = mock(JsonSerializer.class);

    try (MockedStatic<TreeTypeAdapter> treeTypeAdapterStatic = mockStatic(TreeTypeAdapter.class)) {
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
      treeTypeAdapterStatic.when(() -> TreeTypeAdapter.newTypeHierarchyFactory(String.class, serializer)).thenReturn(factoryMock);

      GsonBuilder returned = gsonBuilder.registerTypeHierarchyAdapter(String.class, serializer);
      assertSame(gsonBuilder, returned);

      Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
      hierarchyFactoriesField.setAccessible(true);
      List<?> hierarchyFactories = (List<?>) hierarchyFactoriesField.get(gsonBuilder);
      assertTrue(hierarchyFactories.contains(factoryMock));
    }
  }

  @Test
    @Timeout(8000)
  public void registerTypeHierarchyAdapter_withJsonDeserializer_addsToHierarchyFactories() throws Exception {
    JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);

    try (MockedStatic<TreeTypeAdapter> treeTypeAdapterStatic = mockStatic(TreeTypeAdapter.class)) {
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
      treeTypeAdapterStatic.when(() -> TreeTypeAdapter.newTypeHierarchyFactory(String.class, deserializer)).thenReturn(factoryMock);

      GsonBuilder returned = gsonBuilder.registerTypeHierarchyAdapter(String.class, deserializer);
      assertSame(gsonBuilder, returned);

      Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
      hierarchyFactoriesField.setAccessible(true);
      List<?> hierarchyFactories = (List<?>) hierarchyFactoriesField.get(gsonBuilder);
      assertTrue(hierarchyFactories.contains(factoryMock));
    }
  }

  @Test
    @Timeout(8000)
  public void registerTypeHierarchyAdapter_withTypeAdapter_addsToFactories() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<String> typeAdapter = mock(TypeAdapter.class);

    try (MockedStatic<TypeAdapters> typeAdaptersStatic = mockStatic(TypeAdapters.class)) {
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
      typeAdaptersStatic.when(() -> TypeAdapters.newTypeHierarchyFactory(String.class, typeAdapter)).thenReturn(factoryMock);

      GsonBuilder returned = gsonBuilder.registerTypeHierarchyAdapter(String.class, typeAdapter);
      assertSame(gsonBuilder, returned);

      Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
      factoriesField.setAccessible(true);
      List<?> factories = (List<?>) factoriesField.get(gsonBuilder);
      assertTrue(factories.contains(factoryMock));
    }
  }

  @Test
    @Timeout(8000)
  public void registerTypeHierarchyAdapter_withJsonSerializerAndTypeAdapter_addsToBothLists() throws Exception {
    class MyAdapter extends TypeAdapter<String> implements JsonSerializer<String> {
      @Override
      public void write(JsonWriter out, String value) {}
      @Override
      public String read(JsonReader in) { return null; }
      @Override
      public JsonElement serialize(String src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) { return null; }
    }
    MyAdapter adapter = spy(new MyAdapter());

    try (MockedStatic<TreeTypeAdapter> treeTypeAdapterStatic = mockStatic(TreeTypeAdapter.class);
         MockedStatic<TypeAdapters> typeAdaptersStatic = mockStatic(TypeAdapters.class)) {
      TypeAdapterFactory hierarchyFactoryMock = mock(TypeAdapterFactory.class);
      TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);

      treeTypeAdapterStatic.when(() -> TreeTypeAdapter.newTypeHierarchyFactory(String.class, adapter)).thenReturn(hierarchyFactoryMock);
      typeAdaptersStatic.when(() -> TypeAdapters.newTypeHierarchyFactory(String.class, adapter)).thenReturn(factoryMock);

      GsonBuilder returned = gsonBuilder.registerTypeHierarchyAdapter(String.class, adapter);
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
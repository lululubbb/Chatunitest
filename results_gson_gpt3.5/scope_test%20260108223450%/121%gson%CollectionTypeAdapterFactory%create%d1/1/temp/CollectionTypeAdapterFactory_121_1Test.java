package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollectionTypeAdapterFactory_121_1Test {

  private ConstructorConstructor constructorConstructor;
  private CollectionTypeAdapterFactory factory;
  private Gson gson;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    factory = new CollectionTypeAdapterFactory(constructorConstructor);
    gson = mock(Gson.class);
  }

  @Test
    @Timeout(8000)
  void create_withNonCollectionType_returnsNull() {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapter<String> adapter = factory.create(gson, stringTypeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_withCollectionType_returnsAdapter() {
    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    Type elementType = String.class;

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> elementTypeAdapter = mock(TypeAdapter.class);
    @SuppressWarnings("unchecked")
    ObjectConstructor<ArrayList<String>> objectConstructor = mock(ObjectConstructor.class);

    when(gson.getAdapter((TypeToken) TypeToken.get(elementType))).thenReturn(elementTypeAdapter);
    when(constructorConstructor.get(typeToken)).thenReturn(objectConstructor);

    TypeAdapter<ArrayList<String>> adapter = factory.create(gson, typeToken);

    assertNotNull(adapter);
    assertNotSame(adapter, null);
    assertEquals("com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter", adapter.getClass().getName());

    try {
      Field fieldElementType = adapter.getClass().getDeclaredField("elementType");
      fieldElementType.setAccessible(true);
      assertEquals(elementType, fieldElementType.get(adapter));

      Field fieldElementTypeAdapter = adapter.getClass().getDeclaredField("elementTypeAdapter");
      fieldElementTypeAdapter.setAccessible(true);
      assertSame(elementTypeAdapter, fieldElementTypeAdapter.get(adapter));

      Field fieldConstructor = adapter.getClass().getDeclaredField("constructor");
      fieldConstructor.setAccessible(true);
      assertSame(objectConstructor, fieldConstructor.get(adapter));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void create_withInterfaceCollectionType_returnsAdapter() {
    TypeToken<Collection<String>> typeToken = new TypeToken<Collection<String>>() {};
    Type elementType = String.class;

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> elementTypeAdapter = mock(TypeAdapter.class);
    @SuppressWarnings("unchecked")
    ObjectConstructor<Collection<String>> objectConstructor = mock(ObjectConstructor.class);

    when(gson.getAdapter((TypeToken) TypeToken.get(elementType))).thenReturn(elementTypeAdapter);
    when(constructorConstructor.get(typeToken)).thenReturn(objectConstructor);

    TypeAdapter<Collection<String>> adapter = factory.create(gson, typeToken);

    assertNotNull(adapter);
    assertEquals("com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter", adapter.getClass().getName());
  }

  @Test
    @Timeout(8000)
  void create_withSubtypeCollection_returnsAdapter() {
    class CustomList extends ArrayList<String> {}
    TypeToken<CustomList> typeToken = TypeToken.get(CustomList.class);
    Type elementType = String.class;

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> elementTypeAdapter = mock(TypeAdapter.class);
    @SuppressWarnings("unchecked")
    ObjectConstructor<CustomList> objectConstructor = mock(ObjectConstructor.class);

    when(gson.getAdapter((TypeToken) TypeToken.get(elementType))).thenReturn(elementTypeAdapter);
    when(constructorConstructor.get(typeToken)).thenReturn(objectConstructor);

    TypeAdapter<CustomList> adapter = factory.create(gson, typeToken);

    assertNotNull(adapter);
    assertEquals("com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter", adapter.getClass().getName());
  }
}
package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionTypeAdapterFactory_121_4Test {

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
  void create_returnsNull_whenRawTypeNotCollection() {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);

    TypeAdapter<String> adapter = factory.create(gson, stringTypeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_returnsAdapter_whenRawTypeIsCollection() {
    // Arrange
    TypeToken<List<String>> listTypeToken = new TypeToken<List<String>>() {};
    Type elementType = $Gson$Types.getCollectionElementType(listTypeToken.getType(), listTypeToken.getRawType());

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> elementAdapter = (TypeAdapter<Object>) mock(TypeAdapter.class);
    when(gson.getAdapter((TypeToken<Object>) (TypeToken<?>) TypeToken.get(elementType))).thenReturn(elementAdapter);

    @SuppressWarnings("unchecked")
    ObjectConstructor<List<String>> objectConstructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(listTypeToken)).thenReturn(objectConstructor);

    // Act
    @SuppressWarnings("unchecked")
    TypeAdapter<List<String>> adapter = factory.create(gson, listTypeToken);

    // Assert
    assertNotNull(adapter);
    // The returned adapter should be an instance of CollectionTypeAdapterFactory.Adapter
    assertEquals("com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter", adapter.getClass().getName());
  }

  @Test
    @Timeout(8000)
  void create_returnsAdapter_forCustomCollection() {
    class CustomCollection extends ArrayList<String> {}

    TypeToken<CustomCollection> customCollectionTypeToken = TypeToken.get(CustomCollection.class);
    Type elementType = $Gson$Types.getCollectionElementType(customCollectionTypeToken.getType(), customCollectionTypeToken.getRawType());

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> elementAdapter = (TypeAdapter<Object>) mock(TypeAdapter.class);
    when(gson.getAdapter((TypeToken<Object>) (TypeToken<?>) TypeToken.get(elementType))).thenReturn(elementAdapter);

    @SuppressWarnings("unchecked")
    ObjectConstructor<CustomCollection> objectConstructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(customCollectionTypeToken)).thenReturn(objectConstructor);

    @SuppressWarnings("unchecked")
    TypeAdapter<CustomCollection> adapter = factory.create(gson, customCollectionTypeToken);

    assertNotNull(adapter);
    assertEquals("com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter", adapter.getClass().getName());
  }
}
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
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionTypeAdapterFactory_121_6Test {

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
  void create_returnsNull_whenRawTypeIsNotCollection() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    TypeAdapter<String> adapter = factory.create(gson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_returnsAdapter_whenRawTypeIsCollection() {
    // Arrange
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
    Type elementType = $Gson$Types.getCollectionElementType(typeToken.getType(), typeToken.getRawType());

    @SuppressWarnings("unchecked")
    TypeAdapter<?> elementTypeAdapter = mock(TypeAdapter.class);

    @SuppressWarnings("unchecked")
    ObjectConstructor<List<String>> objectConstructor = mock(ObjectConstructor.class);

    when(gson.getAdapter((TypeToken) TypeToken.get(elementType))).thenReturn(elementTypeAdapter);
    when(constructorConstructor.get((TypeToken) typeToken)).thenReturn(objectConstructor);

    // Act
    @SuppressWarnings("unchecked")
    TypeAdapter<List<String>> adapter = factory.create(gson, typeToken);

    // Assert
    assertNotNull(adapter);
    // The returned adapter should be an instance of CollectionTypeAdapterFactory.Adapter
    assertEquals("com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter", adapter.getClass().getName());
  }
}
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
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.$Gson$Types;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CollectionTypeAdapterFactory_121_5Test {

  @Mock
  private ConstructorConstructor constructorConstructor;

  @Mock
  private Gson gson;

  @Mock
  private ObjectConstructor<?> objectConstructor;

  @Mock
  private TypeAdapter<?> elementTypeAdapter;

  private CollectionTypeAdapterFactory factory;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new CollectionTypeAdapterFactory(constructorConstructor);
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnNullIfTypeIsNotCollection() {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);

    TypeAdapter<String> adapter = factory.create(gson, stringTypeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnAdapterForCollectionType() {
    TypeToken<List<String>> listTypeToken = new TypeToken<List<String>>() {};
    Type elementType = $Gson$Types.getCollectionElementType(listTypeToken.getType(), listTypeToken.getRawType());

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> castedElementTypeAdapter = (TypeAdapter<Object>) elementTypeAdapter;
    when(gson.getAdapter((TypeToken) TypeToken.get(elementType))).thenReturn(castedElementTypeAdapter);
    when(constructorConstructor.get((TypeToken) listTypeToken)).thenReturn((ObjectConstructor) objectConstructor);

    @SuppressWarnings("unchecked")
    TypeAdapter<List<String>> adapter = (TypeAdapter<List<String>>) factory.create(gson, listTypeToken);

    assertNotNull(adapter);
    assertEquals("com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter", adapter.getClass().getName());
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnAdapterForCustomCollection() {
    class CustomCollection extends ArrayList<String> {}

    TypeToken<CustomCollection> customCollectionTypeToken = TypeToken.get(CustomCollection.class);
    Type elementType = $Gson$Types.getCollectionElementType(customCollectionTypeToken.getType(), customCollectionTypeToken.getRawType());

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> castedElementTypeAdapter = (TypeAdapter<Object>) elementTypeAdapter;
    when(gson.getAdapter((TypeToken) TypeToken.get(elementType))).thenReturn(castedElementTypeAdapter);
    when(constructorConstructor.get((TypeToken) customCollectionTypeToken)).thenReturn((ObjectConstructor) objectConstructor);

    @SuppressWarnings("unchecked")
    TypeAdapter<CustomCollection> adapter = (TypeAdapter<CustomCollection>) factory.create(gson, customCollectionTypeToken);

    assertNotNull(adapter);
    assertEquals("com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter", adapter.getClass().getName());
  }
}
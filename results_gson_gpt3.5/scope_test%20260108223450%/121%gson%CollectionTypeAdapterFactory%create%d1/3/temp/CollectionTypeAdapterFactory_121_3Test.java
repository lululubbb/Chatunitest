package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CollectionTypeAdapterFactory_121_3Test {

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
  public void create_returnsNullWhenRawTypeIsNotCollection() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    TypeAdapter<String> adapter = factory.create(gson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_returnsAdapterWhenRawTypeIsCollection() {
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
    Type elementType = $Gson$Types.getCollectionElementType(typeToken.getType(), typeToken.getRawType());

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> castedElementTypeAdapter = (TypeAdapter<Object>) elementTypeAdapter;

    when(gson.getAdapter((TypeToken<Object>) (TypeToken<?>) TypeToken.get(elementType))).thenReturn(castedElementTypeAdapter);

    @SuppressWarnings("unchecked")
    ObjectConstructor<List<String>> castedObjectConstructor = (ObjectConstructor<List<String>>) objectConstructor;

    when(constructorConstructor.get((TypeToken<List<String>>) (TypeToken<?>) typeToken)).thenReturn(castedObjectConstructor);

    TypeAdapter<List<String>> adapter = factory.create(gson, (TypeToken<List<String>>) (TypeToken<?>) typeToken);

    assertNotNull(adapter);
    assertEquals("com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter", adapter.getClass().getName());
  }

  @Test
    @Timeout(8000)
  public void create_worksWithCustomCollection() {
    class CustomCollection extends ArrayList<String> {}

    TypeToken<CustomCollection> typeToken = new TypeToken<CustomCollection>() {};
    Type elementType = $Gson$Types.getCollectionElementType(typeToken.getType(), typeToken.getRawType());

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> castedElementTypeAdapter = (TypeAdapter<Object>) elementTypeAdapter;

    when(gson.getAdapter((TypeToken<Object>) (TypeToken<?>) TypeToken.get(elementType))).thenReturn(castedElementTypeAdapter);

    @SuppressWarnings("unchecked")
    ObjectConstructor<CustomCollection> castedObjectConstructor = (ObjectConstructor<CustomCollection>) objectConstructor;

    when(constructorConstructor.get((TypeToken<CustomCollection>) (TypeToken<?>) typeToken)).thenReturn(castedObjectConstructor);

    TypeAdapter<CustomCollection> adapter = factory.create(gson, (TypeToken<CustomCollection>) (TypeToken<?>) typeToken);

    assertNotNull(adapter);
    assertEquals("com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter", adapter.getClass().getName());
  }
}
package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionTypeAdapterFactory_120_4Test {

  ConstructorConstructor constructorConstructor;
  CollectionTypeAdapterFactory factory;
  Gson gson;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    factory = new CollectionTypeAdapterFactory(constructorConstructor);
    gson = mock(Gson.class);
  }

  @Test
    @Timeout(8000)
  void create_withCollectionType_returnsTypeAdapter() {
    TypeToken<Collection<String>> typeToken = new TypeToken<Collection<String>>() {};
    @SuppressWarnings("unchecked")
    ObjectConstructor<Collection<String>> objectConstructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(any(TypeToken.class))).thenReturn(objectConstructor);
    when(objectConstructor.construct()).thenReturn(new ArrayList<>());

    @SuppressWarnings("unchecked")
    TypeAdapter<String> elementTypeAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(any(TypeToken.class))).thenReturn(elementTypeAdapter);

    @SuppressWarnings("unchecked")
    TypeAdapter<Collection<String>> adapter =
        (TypeAdapter<Collection<String>>) factory.create(gson, (TypeToken<?>) typeToken);

    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_withNonCollectionType_returnsNull() {
    TypeToken<String> typeToken = TypeToken.get(String.class);

    TypeAdapter<?> adapter = factory.create(gson, typeToken);

    assertNull(adapter);
  }
}
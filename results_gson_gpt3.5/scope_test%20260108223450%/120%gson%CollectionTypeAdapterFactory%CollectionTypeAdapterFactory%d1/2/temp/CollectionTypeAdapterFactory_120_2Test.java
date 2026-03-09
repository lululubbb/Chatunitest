package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionTypeAdapterFactory_120_2Test {

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
  void create_WithCollectionType_ReturnsTypeAdapter() throws Exception {
    Type collectionType = new TypeToken<Collection<String>>() {}.getType();
    // Use raw TypeToken to avoid incompatible type error
    @SuppressWarnings("rawtypes")
    TypeToken rawTypeToken = TypeToken.get(collectionType);

    @SuppressWarnings("unchecked")
    ObjectConstructor<Collection<String>> objectConstructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(rawTypeToken)).thenReturn(objectConstructor);
    Collection<String> collectionInstance = new ArrayList<>();
    when(objectConstructor.construct()).thenReturn(collectionInstance);

    Type elementType = $Gson$Types.getCollectionElementType(collectionType, Collection.class);
    @SuppressWarnings({"unchecked", "rawtypes"})
    TypeAdapter elementAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(TypeToken.get(elementType))).thenReturn(elementAdapter);

    @SuppressWarnings("unchecked")
    TypeAdapter<?> adapter = factory.create(gson, rawTypeToken);

    assertNotNull(adapter);

    // Use reflection to get the enclosing class of the adapter instance
    Class<?> adapterClass = adapter.getClass();
    Class<?> enclosingClass = adapterClass.getEnclosingClass();

    assertEquals(CollectionTypeAdapterFactory.class.getName(), enclosingClass.getName());
    assertEquals("Adapter", adapterClass.getSimpleName());

    // Additional verification: the adapter should be able to write and read (basic smoke test)
    // Using reflection to access private inner Adapter class methods is complex and unnecessary here.
  }

  @Test
    @Timeout(8000)
  void create_WithNonCollectionType_ReturnsNull() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    TypeAdapter<?> adapter = factory.create(gson, typeToken);
    assertNull(adapter);
  }
}
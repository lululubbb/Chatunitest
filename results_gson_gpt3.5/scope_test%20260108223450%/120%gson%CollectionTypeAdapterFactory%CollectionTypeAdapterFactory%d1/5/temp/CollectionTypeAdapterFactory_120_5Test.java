package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
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
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionTypeAdapterFactory_120_5Test {

  ConstructorConstructor constructorConstructor;
  CollectionTypeAdapterFactory factory;
  Gson gson;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    factory = new CollectionTypeAdapterFactory(constructorConstructor);
  }

  @Test
    @Timeout(8000)
  void create_withNonCollectionType_returnsNull() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    TypeAdapter<?> adapter = factory.create(gson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_withCollectionType_returnsCollectionTypeAdapter() throws Exception {
    // Use reflection to create an instance of the private ParameterizedTypeImpl class
    Class<?> parameterizedTypeImplClass = Class.forName("com.google.gson.internal.$Gson$Types$ParameterizedTypeImpl");
    Constructor<?> constructor = parameterizedTypeImplClass.getDeclaredConstructor(Type.class, Type.class, Type[].class);
    constructor.setAccessible(true);
    Type collectionType = (Type) constructor.newInstance(null, Collection.class, new Type[] {String.class});
    TypeToken<?> typeToken = TypeToken.get(collectionType);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> elementAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(TypeToken.get(String.class))).thenReturn(elementAdapter);

    @SuppressWarnings("unchecked")
    ObjectConstructor<?> objectConstructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(any(TypeToken.class))).thenReturn(objectConstructor);

    @SuppressWarnings("unchecked")
    TypeAdapter<?> adapter = factory.create(gson, (TypeToken) typeToken);
    assertNotNull(adapter);

    // We can also verify that the returned adapter is a CollectionTypeAdapterFactory.Adapter instance
    assertEquals("com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter",
        adapter.getClass().getName());
  }
}
package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionTypeAdapterFactory_120_6Test {

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
  void create_shouldReturnNullForNonCollectionType() {
    TypeToken<String> stringType = TypeToken.get(String.class);

    TypeAdapter<?> adapter = factory.create(gson, stringType);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnCollectionTypeAdapter_forCollectionType() {
    TypeToken<List<String>> listType = new TypeToken<List<String>>() {};
    Type elementType = com.google.gson.internal.$Gson$Types.getCollectionElementType(listType.getType(), List.class);

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> elementAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(any(TypeToken.class))).thenReturn(elementAdapter);

    @SuppressWarnings("unchecked")
    ObjectConstructor<Object> objectConstructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(any(TypeToken.class))).thenReturn(objectConstructor);

    TypeAdapter<?> adapter = factory.create(gson, listType);

    assertNotNull(adapter);
    assertEquals("com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter", adapter.getClass().getName());

    try {
      Collection<String> sampleCollection = new ArrayList<>();
      sampleCollection.add("test");

      var adapterClass = adapter.getClass();
      var writeMethod = adapterClass.getDeclaredMethod("write", com.google.gson.stream.JsonWriter.class, Object.class);
      var readMethod = adapterClass.getDeclaredMethod("read", com.google.gson.stream.JsonReader.class);

      assertNotNull(writeMethod);
      assertNotNull(readMethod);

    } catch (NoSuchMethodException e) {
      fail("Expected methods not found in Adapter class");
    }
  }
}
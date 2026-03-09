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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

class CollectionTypeAdapterFactory_121_2Test {

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
  void create_returnsAdapter_whenRawTypeIsCollection() throws Exception {
    // Prepare a Collection type token using an anonymous subclass to avoid capture issues
    TypeToken<List<String>> listTypeToken = new TypeToken<List<String>>() {};
    Type elementType = String.class;
    Class<?> rawType = listTypeToken.getRawType();
    assertTrue(Collection.class.isAssignableFrom(rawType));

    // Mock $Gson$Types.getCollectionElementType to return elementType
    try (MockedStatic<com.google.gson.internal.$Gson$Types> mockedTypes = Mockito.mockStatic(com.google.gson.internal.$Gson$Types.class)) {
      mockedTypes.when(() -> com.google.gson.internal.$Gson$Types.getCollectionElementType(listTypeToken.getType(), rawType))
          .thenReturn(elementType);

      // Mock gson.getAdapter to return a dummy TypeAdapter for elementType
      @SuppressWarnings({"unchecked", "rawtypes"})
      TypeAdapter elementAdapter = mock(TypeAdapter.class);
      when(gson.getAdapter(TypeToken.get(elementType))).thenReturn(elementAdapter);

      // Mock constructorConstructor.get to return a dummy ObjectConstructor
      @SuppressWarnings("unchecked")
      ObjectConstructor<List<String>> objectConstructor = mock(ObjectConstructor.class);
      when(constructorConstructor.get(listTypeToken)).thenReturn(objectConstructor);

      // Call create
      @SuppressWarnings("unchecked")
      TypeAdapter<List<String>> adapter = factory.create(gson, listTypeToken);

      assertNotNull(adapter);
      // The returned adapter class name should be CollectionTypeAdapterFactory$Adapter
      assertEquals("com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter", adapter.getClass().getName());

      // Verify that the adapter's element type is correctly set via reflection
      Class<?> adapterClass = adapter.getClass();

      java.lang.reflect.Field elementTypeField = adapterClass.getDeclaredField("elementType");
      elementTypeField.setAccessible(true);
      assertEquals(elementType, elementTypeField.get(adapter));

      java.lang.reflect.Field elementTypeAdapterField = adapterClass.getDeclaredField("elementTypeAdapter");
      elementTypeAdapterField.setAccessible(true);
      assertSame(elementAdapter, elementTypeAdapterField.get(adapter));

      java.lang.reflect.Field constructorField = adapterClass.getDeclaredField("constructor");
      constructorField.setAccessible(true);
      assertSame(objectConstructor, constructorField.get(adapter));
    }
  }
}
package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Type;
import java.util.Map;

class MapTypeAdapterFactory_594_2Test {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;
  private MapTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
    @Timeout(8000)
  void create_WithMapType_ShouldReturnTypeAdapter() {
    factory = new MapTypeAdapterFactory(constructorConstructor, true);

    TypeToken<Map<String, String>> mapTypeToken = new TypeToken<Map<String, String>>() {};
    Type mapType = mapTypeToken.getType();

    // Mock Gson behavior for key and value TypeAdapters
    TypeToken<String> keyTypeToken = TypeToken.get(String.class);
    TypeToken<String> valueTypeToken = TypeToken.get(String.class);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> keyAdapter = mock(TypeAdapter.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> valueAdapter = mock(TypeAdapter.class);

    when(gson.getAdapter(keyTypeToken)).thenReturn(keyAdapter);
    when(gson.getAdapter(valueTypeToken)).thenReturn(valueAdapter);

    // Spy on factory to call real getKeyAdapter (private method)
    MapTypeAdapterFactory spyFactory = Mockito.spy(factory);

    // Use reflection to invoke private getKeyAdapter method
    Type keyType = $Gson$Types.getMapKeyAndValueTypes(mapType, Map.class)[0];
    try {
      var method = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
      method.setAccessible(true);
      TypeAdapter<?> returnedKeyAdapter = (TypeAdapter<?>) method.invoke(spyFactory, gson, keyType);
      assertNotNull(returnedKeyAdapter);
    } catch (Exception e) {
      fail("Reflection invocation of getKeyAdapter failed: " + e.getMessage());
    }

    TypeAdapter<?> adapter = factory.create(gson, mapTypeToken);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_WithNonMapType_ShouldReturnNull() {
    factory = new MapTypeAdapterFactory(constructorConstructor, false);

    TypeToken<String> stringTypeToken = TypeToken.get(String.class);

    TypeAdapter<?> adapter = factory.create(gson, stringTypeToken);
    assertNull(adapter);
  }
}
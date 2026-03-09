package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.JsonReaderInternalAccess;
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
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

class MapTypeAdapterFactory_594_3Test {

  ConstructorConstructor constructorConstructor;
  MapTypeAdapterFactory factory;
  Gson gson;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    factory = new MapTypeAdapterFactory(constructorConstructor, true);
    gson = mock(Gson.class);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnNullForNonMapType() {
    TypeToken<String> stringType = TypeToken.get(String.class);
    TypeAdapter<?> adapter = factory.create(gson, stringType);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnTypeAdapterForMap() throws Exception {
    TypeToken<Map<String, Integer>> mapType = new TypeToken<Map<String, Integer>>() {};
    Type keyType = mapType.getType();
    Type valueType = ((java.lang.reflect.ParameterizedType) keyType).getActualTypeArguments()[1];

    // Mock constructorConstructor.get(TypeToken) to return an ObjectConstructor returning a TypeAdapter for valueType
    @SuppressWarnings("unchecked")
    TypeAdapter<Integer> valueAdapter = mock(TypeAdapter.class);
    @SuppressWarnings("unchecked")
    ObjectConstructor<TypeAdapter<Integer>> objectConstructor = mock(ObjectConstructor.class);
    when(objectConstructor.construct()).thenReturn(valueAdapter);
    when(constructorConstructor.get(any(TypeToken.class))).thenReturn(objectConstructor);

    // Mock gson.getAdapter for key adapter
    @SuppressWarnings("unchecked")
    TypeAdapter<String> keyAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(any(TypeToken.class))).thenReturn(keyAdapter);

    TypeAdapter<?> adapter = factory.create(gson, mapType);

    assertNotNull(adapter);
    // The returned adapter should be a MapTypeAdapterFactory.Adapter instance
    assertEquals("com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter", adapter.getClass().getName());
  }

  @Test
    @Timeout(8000)
  void getKeyAdapter_shouldReturnAdapterFromGson() throws Exception {
    Method getKeyAdapterMethod = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
    getKeyAdapterMethod.setAccessible(true);

    Type keyType = String.class;
    @SuppressWarnings("unchecked")
    TypeAdapter<?> keyAdapter = mock(TypeAdapter.class);
    // Fix: avoid eq() matcher, use argument matcher with a custom matcher to match TypeToken.get(keyType)
    when(gson.getAdapter(argThat(argument -> argument != null && argument.getType().equals(keyType)))).thenReturn(keyAdapter);

    TypeAdapter<?> result = (TypeAdapter<?>) getKeyAdapterMethod.invoke(factory, gson, keyType);

    assertSame(keyAdapter, result);
  }
}
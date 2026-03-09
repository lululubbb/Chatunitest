package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
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
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

public class MapTypeAdapterFactory_594_1Test {

  private ConstructorConstructor constructorConstructor;
  private MapTypeAdapterFactory factory;
  private Gson gson;

  @BeforeEach
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
  }

  @Test
    @Timeout(8000)
  public void testCreate_withMapType_shouldReturnTypeAdapter() {
    factory = new MapTypeAdapterFactory(constructorConstructor, false);
    TypeToken<Map<String, String>> typeToken = new TypeToken<Map<String, String>>() {};
    TypeAdapter<?> adapter = factory.create(gson, typeToken);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testCreate_withNonMapType_shouldReturnNull() {
    factory = new MapTypeAdapterFactory(constructorConstructor, false);
    TypeToken<String> typeToken = TypeToken.get(String.class);
    TypeAdapter<?> adapter = factory.create(gson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testGetKeyAdapter_withPrimitiveKeyType() throws Exception {
    factory = new MapTypeAdapterFactory(constructorConstructor, false);

    // Mock gson.getAdapter to return a dummy TypeAdapter for String.class
    @SuppressWarnings("unchecked")
    TypeAdapter<String> dummyAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(TypeToken.get(String.class))).thenReturn(dummyAdapter);

    Method method = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
    method.setAccessible(true);

    Type keyType = String.class;
    TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory, gson, keyType);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testGetKeyAdapter_withComplexKeyType() throws Exception {
    factory = new MapTypeAdapterFactory(constructorConstructor, false);

    Type complexKeyType = new TypeToken<Map<String, String>>() {}.getType();

    // Mock gson.getAdapter to return a dummy TypeAdapter for the complexKeyType
    @SuppressWarnings("unchecked")
    TypeAdapter<?> dummyAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter((TypeToken<?>) any())).thenAnswer(invocation -> {
      TypeToken<?> token = invocation.getArgument(0);
      if (token.getType().equals(complexKeyType)) {
        return dummyAdapter;
      }
      return null;
    });

    Method method = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
    method.setAccessible(true);

    TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory, gson, complexKeyType);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testCreate_withComplexMapKeySerializationTrue() {
    factory = new MapTypeAdapterFactory(constructorConstructor, true);
    TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};
    TypeAdapter<?> adapter = factory.create(gson, typeToken);
    assertNotNull(adapter);
  }
}
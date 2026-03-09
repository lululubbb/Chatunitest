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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

public class MapTypeAdapterFactory_594_6Test {

  @Mock
  private ConstructorConstructor constructorConstructor;

  @Mock
  private Gson gson;

  @Mock
  private TypeAdapter<?> stringKeyAdapter;

  @Mock
  private TypeAdapter<?> customKeyAdapter;

  private MapTypeAdapterFactory factory;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    factory = new MapTypeAdapterFactory(constructorConstructor, true);

    // Mock gson.getAdapter(...) behavior for key adapters
    when(gson.getAdapter(TypeToken.get(String.class))).thenReturn((TypeAdapter) stringKeyAdapter);
    when(gson.getAdapter(TypeToken.get(Integer.class))).thenReturn((TypeAdapter) customKeyAdapter);
  }

  @Test
    @Timeout(8000)
  public void testCreate_withMapType_shouldReturnTypeAdapter() {
    TypeToken<Map<String, String>> mapTypeToken = new TypeToken<Map<String, String>>() {};
    TypeAdapter<?> adapter = factory.create(gson, mapTypeToken);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testCreate_withNonMapType_shouldReturnNull() {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapter<?> adapter = factory.create(gson, stringTypeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testGetKeyAdapter_withStringKeyType_returnsStringKeyAdapter() throws Exception {
    Method method = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
    method.setAccessible(true);

    Type keyType = String.class;
    Object adapter = method.invoke(factory, gson, keyType);
    assertNotNull(adapter);
    assertSame(stringKeyAdapter, adapter);
  }

  @Test
    @Timeout(8000)
  public void testGetKeyAdapter_withCustomKeyType_returnsCustomKeyAdapter() throws Exception {
    Method method = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
    method.setAccessible(true);

    Type keyType = Integer.class;
    Object adapter = method.invoke(factory, gson, keyType);
    assertNotNull(adapter);
    assertSame(customKeyAdapter, adapter);
  }

  @Test
    @Timeout(8000)
  public void testConstructorStoresFieldsCorrectly() {
    MapTypeAdapterFactory localFactory = new MapTypeAdapterFactory(constructorConstructor, false);
    assertNotNull(localFactory);
    assertFalse(localFactory.complexMapKeySerialization);
  }
}
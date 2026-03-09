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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.Map;

public class MapTypeAdapterFactory_594_4Test {

  @Mock
  private ConstructorConstructor constructorConstructor;

  @Mock
  private Gson gson;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
    @Timeout(8000)
  public void testCreate_withMapType_returnsTypeAdapter() {
    MapTypeAdapterFactory factoryComplexTrue = new MapTypeAdapterFactory(constructorConstructor, true);
    TypeToken<Map<String, String>> typeToken = new TypeToken<Map<String, String>>() {};
    TypeAdapter<?> adapter = factoryComplexTrue.create(gson, typeToken);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testCreate_withNonMapType_returnsNull() {
    MapTypeAdapterFactory factoryComplexTrue = new MapTypeAdapterFactory(constructorConstructor, true);
    TypeToken<String> typeToken = TypeToken.get(String.class);
    TypeAdapter<?> adapter = factoryComplexTrue.create(gson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testGetKeyAdapter_invokesSuccessfully() throws Exception {
    MapTypeAdapterFactory factoryComplexFalse = new MapTypeAdapterFactory(constructorConstructor, false);
    Method getKeyAdapterMethod = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, java.lang.reflect.Type.class);
    getKeyAdapterMethod.setAccessible(true);

    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Mock gson.getAdapter(TypeToken) to return a dummy TypeAdapter to avoid returning null inside getKeyAdapter
    when(gson.getAdapter(TypeToken.get(String.class))).thenReturn(mock(TypeAdapter.class));

    Object adapter = getKeyAdapterMethod.invoke(factoryComplexFalse, gson, typeToken.getType());
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_setsFieldsCorrectly() {
    MapTypeAdapterFactory factory = new MapTypeAdapterFactory(constructorConstructor, true);
    assertNotNull(factory);
    assertTrue(factory.complexMapKeySerialization);
  }
}
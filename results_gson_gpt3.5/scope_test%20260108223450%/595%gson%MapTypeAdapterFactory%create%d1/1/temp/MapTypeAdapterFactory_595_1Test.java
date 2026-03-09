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
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import java.lang.reflect.Type;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MapTypeAdapterFactory_595_1Test {

  @Mock
  private ConstructorConstructor constructorConstructor;

  @Mock
  private Gson gson;

  @Mock
  private ObjectConstructor<?> objectConstructor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnNullIfRawTypeIsNotMap() {
    MapTypeAdapterFactory factory = new MapTypeAdapterFactory(constructorConstructor, false);
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);

    TypeAdapter<String> adapter = factory.create(gson, stringTypeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnAdapterForMapType() {
    MapTypeAdapterFactory factory = new MapTypeAdapterFactory(constructorConstructor, false);

    TypeToken<Map<String, Integer>> mapTypeToken = new TypeToken<Map<String, Integer>>() {};

    // We need to cast the mocks to raw TypeAdapter<?> and then cast to TypeAdapter of the correct type
    @SuppressWarnings("unchecked")
    TypeAdapter<String> keyAdapter = (TypeAdapter<String>) mock(TypeAdapter.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<Integer> valueAdapter = (TypeAdapter<Integer>) mock(TypeAdapter.class);

    when(gson.getAdapter(TypeToken.get(String.class))).thenReturn(keyAdapter);
    when(gson.getAdapter(TypeToken.get(Integer.class))).thenReturn(valueAdapter);
    when(constructorConstructor.get(mapTypeToken)).thenReturn((ObjectConstructor<Map<String, Integer>>) objectConstructor);

    TypeAdapter<Map<String, Integer>> adapter = factory.create(gson, mapTypeToken);

    assertNotNull(adapter);
    assertEquals("com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter", adapter.getClass().getName());
  }

  @Test
    @Timeout(8000)
  void create_shouldUseComplexMapKeySerializationFlag() {
    MapTypeAdapterFactory factory = new MapTypeAdapterFactory(constructorConstructor, true);

    TypeToken<Map<String, Integer>> mapTypeToken = new TypeToken<Map<String, Integer>>() {};

    @SuppressWarnings("unchecked")
    TypeAdapter<String> keyAdapter = (TypeAdapter<String>) mock(TypeAdapter.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<Integer> valueAdapter = (TypeAdapter<Integer>) mock(TypeAdapter.class);

    when(gson.getAdapter(TypeToken.get(String.class))).thenReturn(keyAdapter);
    when(gson.getAdapter(TypeToken.get(Integer.class))).thenReturn(valueAdapter);
    when(constructorConstructor.get(mapTypeToken)).thenReturn((ObjectConstructor<Map<String, Integer>>) objectConstructor);

    TypeAdapter<Map<String, Integer>> adapter = factory.create(gson, mapTypeToken);

    assertNotNull(adapter);
    assertEquals("com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter", adapter.getClass().getName());
  }
}
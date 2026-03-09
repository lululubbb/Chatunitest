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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Type;
import java.util.HashMap;

public class MapTypeAdapterFactory_595_2Test {

  @Mock
  private ConstructorConstructor constructorConstructor;

  @Mock
  private Gson gson;

  @Mock
  private ObjectConstructor<HashMap<?, ?>> objectConstructor;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnNullIfNotMap() {
    MapTypeAdapterFactory factory = new MapTypeAdapterFactory(constructorConstructor, false);
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);

    TypeAdapter<String> adapter = factory.create(gson, stringTypeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnAdapterForMap() throws Exception {
    MapTypeAdapterFactory factory = new MapTypeAdapterFactory(constructorConstructor, false);
    TypeToken<HashMap<String, Integer>> mapTypeToken = new TypeToken<HashMap<String, Integer>>() {};

    Type keyType = String.class;
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> keyAdapter = (TypeAdapter<Object>) mock(TypeAdapter.class);

    TypeAdapter<Integer> valueAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(ArgumentMatchers.<TypeToken<Integer>>any())).thenReturn(valueAdapter);

    @SuppressWarnings({"unchecked", "rawtypes"})
    TypeToken rawMapTypeToken = mapTypeToken;
    @SuppressWarnings({"unchecked", "rawtypes"})
    ObjectConstructor rawObjectConstructor = objectConstructor;

    when(constructorConstructor.get(rawMapTypeToken)).thenReturn(rawObjectConstructor);

    // Fix: cast TypeToken.get(keyType) to raw TypeToken to avoid generic mismatch in when()
    @SuppressWarnings({"unchecked", "rawtypes"})
    TypeToken keyTypeToken = (TypeToken) TypeToken.get(keyType);
    when(gson.getAdapter(keyTypeToken)).thenReturn(keyAdapter);

    TypeAdapter<HashMap<String, Integer>> adapter = factory.create(gson, mapTypeToken);

    assertNotNull(adapter);
    assertEquals("com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter", adapter.getClass().getName());
  }
}
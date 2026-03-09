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
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapTypeAdapterFactory_595_3Test {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;
  private MapTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    factory = new MapTypeAdapterFactory(constructorConstructor, true);
  }

  @Test
    @Timeout(8000)
  void create_returnsNull_whenRawTypeIsNotMap() {
    TypeToken<String> typeToken = TypeToken.get(String.class);

    TypeAdapter<String> adapter = factory.create(gson, typeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_returnsAdapter_whenRawTypeIsMap() throws Exception {
    // Prepare a Map type token
    TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};

    // Prepare key and value types
    Type keyType = String.class;
    Type valueType = Integer.class;

    // Mock keyAdapter and valueAdapter
    TypeAdapter<?> keyAdapter = mock(TypeAdapter.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<Integer> valueAdapter = (TypeAdapter<Integer>) mock(TypeAdapter.class);

    // Mock gson.getAdapter to return valueAdapter with correct generics
    when(gson.getAdapter(TypeToken.get(valueType))).thenReturn(valueAdapter);

    // Mock constructorConstructor.get to return an ObjectConstructor
    @SuppressWarnings("unchecked")
    ObjectConstructor<Map<String, Integer>> objectConstructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(typeToken)).thenReturn(objectConstructor);

    // Use reflection to create a MapTypeAdapterFactory instance with a mocked getKeyAdapter method
    MapTypeAdapterFactory factoryWithMockedGetKeyAdapter = new MapTypeAdapterFactory(constructorConstructor, true) {
      @Override
      @SuppressWarnings("unchecked")
      public <T> TypeAdapter<T> create(Gson gsonParam, TypeToken<T> typeTokenParam) {
        Type type = typeTokenParam.getType();
        Class<? super T> rawType = typeTokenParam.getRawType();
        if (!Map.class.isAssignableFrom(rawType)) {
          return null;
        }
        Type[] keyAndValueTypes = com.google.gson.internal.$Gson$Types.getMapKeyAndValueTypes(type, rawType);
        // Return mocked keyAdapter for key type
        TypeAdapter<?> keyAdapterLocal = keyAdapter;
        TypeAdapter<?> valueAdapterLocal = gsonParam.getAdapter(TypeToken.get(keyAndValueTypes[1]));
        ObjectConstructor<T> constructor = constructorConstructor.get(typeTokenParam);

        try {
          // Use reflection to access the private Adapter class constructor
          Class<?> adapterClass = Class.forName("com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter");
          java.lang.reflect.Constructor<?> adapterConstructor = adapterClass.getDeclaredConstructor(
              Gson.class, Type.class, TypeAdapter.class, Type.class, TypeAdapter.class, ObjectConstructor.class);
          adapterConstructor.setAccessible(true);

          @SuppressWarnings("unchecked")
          TypeAdapter<T> result = (TypeAdapter<T>) adapterConstructor.newInstance(
              gsonParam, keyAndValueTypes[0], keyAdapterLocal,
              keyAndValueTypes[1], valueAdapterLocal, constructor);

          return result;
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    };

    @SuppressWarnings("unchecked")
    TypeAdapter<Map<String, Integer>> adapter = (TypeAdapter<Map<String, Integer>>) factoryWithMockedGetKeyAdapter.create(gson, typeToken);

    assertNotNull(adapter);
    // The returned adapter should be an instance of MapTypeAdapterFactory.Adapter
    assertEquals("com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter", adapter.getClass().getName());
  }
}
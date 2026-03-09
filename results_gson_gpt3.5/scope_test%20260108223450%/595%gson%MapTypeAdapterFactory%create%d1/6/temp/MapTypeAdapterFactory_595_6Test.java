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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

class MapTypeAdapterFactory_595_6Test {

  @Mock Gson gson;
  @Mock ConstructorConstructor constructorConstructor;
  @Mock ObjectConstructor<Object> objectConstructor;

  MapTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new MapTypeAdapterFactory(constructorConstructor, true);
  }

  @Test
    @Timeout(8000)
  void create_whenTypeIsNotMap_returnsNull() {
    TypeToken<String> typeToken = TypeToken.get(String.class);

    TypeAdapter<String> adapter = factory.create(gson, typeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_whenTypeIsMap_returnsAdapter() throws Exception {
    TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};
    Type keyType = String.class;
    Type valueType = Integer.class;

    // Mock gson.getAdapter for value type with correct generic capture
    @SuppressWarnings("unchecked")
    TypeAdapter<Integer> valueAdapter = mock(TypeAdapter.class);
    when(gson.<Integer>getAdapter((TypeToken<Integer>) (TypeToken<?>) TypeToken.get(valueType))).thenReturn(valueAdapter);

    // Mock constructorConstructor.get to return objectConstructor
    when(constructorConstructor.get(typeToken)).thenReturn((ObjectConstructor) objectConstructor);

    // Mock gson.getAdapter for key type as well, because getKeyAdapter internally calls gson.getAdapter
    @SuppressWarnings("unchecked")
    TypeAdapter<String> keyAdapterMock = mock(TypeAdapter.class);
    when(gson.<String>getAdapter((TypeToken<String>) (TypeToken<?>) TypeToken.get(keyType))).thenReturn(keyAdapterMock);

    // Use reflection to get private getKeyAdapter method and invoke it to get the keyAdapter
    java.lang.reflect.Method getKeyAdapterMethod = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
    getKeyAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<?> keyAdapter = (TypeAdapter<?>) getKeyAdapterMethod.invoke(factory, gson, keyType);

    // Call the real create method on factory
    @SuppressWarnings("unchecked")
    TypeAdapter<Map<String, Integer>> adapter = (TypeAdapter<Map<String, Integer>>) factory.create(gson, typeToken);

    assertNotNull(adapter);
    // The returned adapter should be an instance of MapTypeAdapterFactory.Adapter
    assertEquals("com.google.gson.internal.bind.MapTypeAdapterFactory$Adapter", adapter.getClass().getName());

    // Use reflection to verify that the keyAdapter inside Adapter is an instance of the same class as obtained from getKeyAdapter
    Field keyAdapterField = adapter.getClass().getDeclaredField("keyTypeAdapter");
    keyAdapterField.setAccessible(true);
    Object actualKeyAdapter = keyAdapterField.get(adapter);

    assertNotNull(actualKeyAdapter);
    // Because create wraps the keyAdapter in a TypeAdapterRuntimeTypeWrapper, we check the wrapped delegate
    Class<?> actualClass = actualKeyAdapter.getClass();

    // If actualKeyAdapter is a TypeAdapterRuntimeTypeWrapper, unwrap it to get the delegate
    if ("com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper".equals(actualClass.getName())) {
      Field delegateField = actualClass.getDeclaredField("delegate");
      delegateField.setAccessible(true);
      Object delegate = delegateField.get(actualKeyAdapter);
      assertSame(keyAdapter, delegate);
    } else {
      // Otherwise, assert same directly
      assertSame(keyAdapter, actualKeyAdapter);
    }
  }

}
package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.bind.TypeAdapters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

class MapTypeAdapterFactory_596_5Test {

  private MapTypeAdapterFactory mapTypeAdapterFactory;
  private ConstructorConstructor constructorConstructor;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    mapTypeAdapterFactory = new MapTypeAdapterFactory(constructorConstructor, true);
  }

  @Test
    @Timeout(8000)
  void testGetKeyAdapter_withBooleanClass_returnsBooleanAsStringAdapter() throws Exception {
    // Arrange
    Gson gson = mock(Gson.class);
    Class<Boolean> keyType = Boolean.class;

    // Act
    TypeAdapter<?> adapter = invokeGetKeyAdapter(gson, keyType);

    // Assert
    assertSame(TypeAdapters.BOOLEAN_AS_STRING, adapter);
  }

  @Test
    @Timeout(8000)
  void testGetKeyAdapter_withBooleanPrimitive_returnsBooleanAsStringAdapter() throws Exception {
    // Arrange
    Gson gson = mock(Gson.class);
    Class<Boolean> keyType = boolean.class;

    // Act
    TypeAdapter<?> adapter = invokeGetKeyAdapter(gson, keyType);

    // Assert
    assertSame(TypeAdapters.BOOLEAN_AS_STRING, adapter);
  }

  @Test
    @Timeout(8000)
  void testGetKeyAdapter_withOtherType_returnsGsonAdapter() throws Exception {
    // Arrange
    Gson gson = mock(Gson.class);
    Type keyType = String.class;
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> expectedAdapter = mock(TypeAdapter.class);

    when(gson.getAdapter((TypeToken<?>) any())).thenAnswer(invocation -> {
      TypeToken<?> token = invocation.getArgument(0);
      if (token.equals(TypeToken.get(keyType))) {
        return expectedAdapter;
      }
      return null;
    });

    // Act
    TypeAdapter<?> adapter = invokeGetKeyAdapter(gson, keyType);

    // Assert
    assertSame(expectedAdapter, adapter);
    verify(gson).getAdapter(TypeToken.get(keyType));
  }

  private TypeAdapter<?> invokeGetKeyAdapter(Gson context, Type keyType) throws Exception {
    Method method = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
    method.setAccessible(true);
    return (TypeAdapter<?>) method.invoke(mapTypeAdapterFactory, context, keyType);
  }
}
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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapTypeAdapterFactory_596_1Test {

  private MapTypeAdapterFactory mapTypeAdapterFactory;
  private Gson mockGson;

  @BeforeEach
  void setUp() {
    // ConstructorConstructor is required but not used in getKeyAdapter, so can be mocked or null
    mapTypeAdapterFactory = new MapTypeAdapterFactory(null, true);
    mockGson = mock(Gson.class);
  }

  @Test
    @Timeout(8000)
  void testGetKeyAdapter_withBooleanPrimitive_returnsBooleanAsString() throws Exception {
    Method method = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, java.lang.reflect.Type.class);
    method.setAccessible(true);

    TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(mapTypeAdapterFactory, mockGson, boolean.class);

    assertSame(TypeAdapters.BOOLEAN_AS_STRING, adapter);
    verifyNoInteractions(mockGson);
  }

  @Test
    @Timeout(8000)
  void testGetKeyAdapter_withBooleanWrapper_returnsBooleanAsString() throws Exception {
    Method method = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, java.lang.reflect.Type.class);
    method.setAccessible(true);

    TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(mapTypeAdapterFactory, mockGson, Boolean.class);

    assertSame(TypeAdapters.BOOLEAN_AS_STRING, adapter);
    verifyNoInteractions(mockGson);
  }

  @Test
    @Timeout(8000)
  void testGetKeyAdapter_withOtherType_callsGsonGetAdapter() throws Exception {
    Method method = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, java.lang.reflect.Type.class);
    method.setAccessible(true);

    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapter<?> mockAdapter = mock(TypeAdapter.class);
    when(mockGson.getAdapter(any(TypeToken.class))).thenAnswer(invocation -> {
      TypeToken<?> arg = invocation.getArgument(0);
      if (arg.equals(stringTypeToken)) {
        return mockAdapter;
      }
      return null;
    });

    TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(mapTypeAdapterFactory, mockGson, String.class);

    assertSame(mockAdapter, adapter);
    verify(mockGson).getAdapter(stringTypeToken);
  }
}
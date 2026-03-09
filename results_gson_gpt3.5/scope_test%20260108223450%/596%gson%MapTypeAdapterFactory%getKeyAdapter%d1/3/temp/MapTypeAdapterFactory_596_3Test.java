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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class MapTypeAdapterFactory_596_3Test {

  @Mock
  private Gson mockGson;

  private MapTypeAdapterFactory mapTypeAdapterFactory;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    // ConstructorConstructor is not used in getKeyAdapter, so can pass null and false for second param
    mapTypeAdapterFactory = new MapTypeAdapterFactory(null, false);
  }

  @Test
    @Timeout(8000)
  public void testGetKeyAdapter_withBooleanPrimitive_returnsBooleanAsString() throws Exception {
    Method getKeyAdapterMethod = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
    getKeyAdapterMethod.setAccessible(true);

    TypeAdapter<?> adapter = (TypeAdapter<?>) getKeyAdapterMethod.invoke(mapTypeAdapterFactory, mockGson, boolean.class);

    assertSame(TypeAdapters.BOOLEAN_AS_STRING, adapter);
    verifyNoInteractions(mockGson);
  }

  @Test
    @Timeout(8000)
  public void testGetKeyAdapter_withBooleanWrapper_returnsBooleanAsString() throws Exception {
    Method getKeyAdapterMethod = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
    getKeyAdapterMethod.setAccessible(true);

    TypeAdapter<?> adapter = (TypeAdapter<?>) getKeyAdapterMethod.invoke(mapTypeAdapterFactory, mockGson, Boolean.class);

    assertSame(TypeAdapters.BOOLEAN_AS_STRING, adapter);
    verifyNoInteractions(mockGson);
  }

  @Test
    @Timeout(8000)
  public void testGetKeyAdapter_withOtherType_invokesGsonGetAdapter() throws Exception {
    Method getKeyAdapterMethod = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
    getKeyAdapterMethod.setAccessible(true);

    Type someType = String.class;
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> expectedAdapter = mock(TypeAdapter.class);
    when(mockGson.getAdapter(any(TypeToken.class))).thenReturn(expectedAdapter);

    TypeAdapter<?> adapter = (TypeAdapter<?>) getKeyAdapterMethod.invoke(mapTypeAdapterFactory, mockGson, someType);

    assertSame(expectedAdapter, adapter);
    verify(mockGson).getAdapter(TypeToken.get(someType));
  }
}
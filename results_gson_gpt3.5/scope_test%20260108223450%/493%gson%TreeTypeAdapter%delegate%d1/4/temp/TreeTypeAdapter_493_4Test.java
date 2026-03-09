package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TreeTypeAdapterDelegateTest {

  private TreeTypeAdapter<Object> treeTypeAdapter;
  private Gson mockGson;
  private TypeAdapterFactory mockSkipPast;
  private TypeToken<Object> typeToken;
  private TypeAdapter<Object> mockDelegateAdapter;

  @BeforeEach
  void setUp() throws Exception {
    mockGson = mock(Gson.class);
    mockSkipPast = mock(TypeAdapterFactory.class);
    typeToken = TypeToken.get(Object.class);
    mockDelegateAdapter = mock(TypeAdapter.class);

    // Use reflection to find a matching constructor since the public constructor may not exist or be accessible
    treeTypeAdapter = createTreeTypeAdapterInstance(null, null, mockGson, typeToken, mockSkipPast, false);
  }

  private TreeTypeAdapter<Object> createTreeTypeAdapterInstance(
      Object serializer,
      Object deserializer,
      Gson gson,
      TypeToken<Object> typeToken,
      TypeAdapterFactory skipPast,
      boolean nullSafe) throws Exception {
    // Find the constructor with parameters (JsonSerializer, JsonDeserializer, Gson, TypeToken, TypeAdapterFactory, boolean)
    Class<TreeTypeAdapter> clazz = TreeTypeAdapter.class;
    var constructor = clazz.getDeclaredConstructor(
        Object.class, Object.class, Gson.class, TypeToken.class, TypeAdapterFactory.class, boolean.class);
    constructor.setAccessible(true);
    @SuppressWarnings("unchecked")
    TreeTypeAdapter<Object> instance = (TreeTypeAdapter<Object>) constructor.newInstance(
        serializer, deserializer, gson, typeToken, skipPast, nullSafe);
    return instance;
  }

  @Test
    @Timeout(8000)
  void testDelegateReturnsCachedDelegateIfNotNull() throws Exception {
    // Set delegate field via reflection
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapter, mockDelegateAdapter);

    // Invoke private delegate() method via reflection
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> result = (TypeAdapter<Object>) delegateMethod.invoke(treeTypeAdapter);

    // Should return the cached delegate without calling gson.getDelegateAdapter
    assertSame(mockDelegateAdapter, result);
    Mockito.verify(mockGson, Mockito.never()).getDelegateAdapter(any(), any());
  }

  @Test
    @Timeout(8000)
  void testDelegateReturnsAndCachesDelegateWhenNull() throws Exception {
    // Initially delegate field is null
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapter, null);

    // Setup gson.getDelegateAdapter to return mockDelegateAdapter
    when(mockGson.getDelegateAdapter(mockSkipPast, typeToken)).thenReturn(mockDelegateAdapter);

    // Invoke private delegate() method via reflection
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> result = (TypeAdapter<Object>) delegateMethod.invoke(treeTypeAdapter);

    // Should return the delegate from gson and cache it
    assertSame(mockDelegateAdapter, result);

    // Confirm delegate field is updated
    TypeAdapter<Object> cachedDelegate = (TypeAdapter<Object>) delegateField.get(treeTypeAdapter);
    assertSame(mockDelegateAdapter, cachedDelegate);

    Mockito.verify(mockGson).getDelegateAdapter(mockSkipPast, typeToken);
  }
}
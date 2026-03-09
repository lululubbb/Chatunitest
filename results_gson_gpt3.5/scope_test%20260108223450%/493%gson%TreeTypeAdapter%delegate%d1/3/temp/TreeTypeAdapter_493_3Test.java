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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

class TreeTypeAdapterDelegateTest {

  @Mock
  private Gson mockGson;

  @Mock
  private TypeAdapterFactory mockSkipPast;

  @Mock
  private TypeAdapter<Object> mockDelegateAdapter;

  private TypeToken<Object> typeToken;

  private TreeTypeAdapter<Object> treeTypeAdapter;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    typeToken = TypeToken.get(Object.class);
    // Create instance with null serializer and deserializer (allowed)
    treeTypeAdapter = new TreeTypeAdapter<>(null, null, mockGson, typeToken, mockSkipPast, false);
  }

  @Test
    @Timeout(8000)
  public void testDelegateReturnsCachedDelegate() throws Exception {
    // Use reflection to set private volatile delegate field
    var delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapter, mockDelegateAdapter);

    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    // Call delegate() should return cached delegate without calling gson.getDelegateAdapter(...)
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> result = (TypeAdapter<Object>) delegateMethod.invoke(treeTypeAdapter);

    assertSame(mockDelegateAdapter, result);
    verifyNoInteractions(mockGson);
  }

  @Test
    @Timeout(8000)
  public void testDelegateReturnsAndCachesDelegateFromGson() throws Exception {
    // Initially delegate field is null
    var delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapter, null);

    when(mockGson.getDelegateAdapter(eq(mockSkipPast), eq(typeToken))).thenReturn(mockDelegateAdapter);

    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    // Call delegate() should call gson.getDelegateAdapter and cache the result
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> firstCall = (TypeAdapter<Object>) delegateMethod.invoke(treeTypeAdapter);
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> secondCall = (TypeAdapter<Object>) delegateMethod.invoke(treeTypeAdapter);

    assertSame(mockDelegateAdapter, firstCall);
    assertSame(mockDelegateAdapter, secondCall);

    verify(mockGson, times(1)).getDelegateAdapter(eq(mockSkipPast), eq(typeToken));

    // The delegate field should now be set to mockDelegateAdapter
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> cachedDelegate = (TypeAdapter<Object>) delegateField.get(treeTypeAdapter);
    assertSame(mockDelegateAdapter, cachedDelegate);
  }
}
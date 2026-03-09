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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TreeTypeAdapterDelegateMethodTest {

  private TreeTypeAdapter<Object> treeTypeAdapter;
  private Gson gsonMock;
  private TypeAdapterFactory skipPastMock;
  private TypeToken<Object> typeToken;

  @BeforeEach
  void setUp() {
    gsonMock = mock(Gson.class);
    skipPastMock = mock(TypeAdapterFactory.class);
    typeToken = TypeToken.get(Object.class);
  }

  @Test
    @Timeout(8000)
  void delegate_whenDelegateIsNull_invokesGsonGetDelegateAdapterAndCaches() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> delegateAdapterMock = mock(TypeAdapter.class);
    // Create instance with nullSafe = false
    treeTypeAdapter = new TreeTypeAdapter<>(null, null, gsonMock, typeToken, skipPastMock, false);

    // Initially delegate field is null, so getDelegateAdapter should be called
    when(gsonMock.getDelegateAdapter(skipPastMock, typeToken)).thenReturn(delegateAdapterMock);

    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    // First call: should call gson.getDelegateAdapter and assign delegate
    TypeAdapter<Object> firstCallResult = (TypeAdapter<Object>) delegateMethod.invoke(treeTypeAdapter);
    assertSame(delegateAdapterMock, firstCallResult);

    // Second call: delegate field is set, so should return cached delegate without calling gson again
    TypeAdapter<Object> secondCallResult = (TypeAdapter<Object>) delegateMethod.invoke(treeTypeAdapter);
    assertSame(delegateAdapterMock, secondCallResult);
  }

  @Test
    @Timeout(8000)
  void delegate_whenDelegateIsNotNull_returnsCachedDelegate() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> delegateAdapterMock = mock(TypeAdapter.class);
    treeTypeAdapter = new TreeTypeAdapter<>(null, null, gsonMock, typeToken, skipPastMock, false);

    // Use reflection to set the private volatile delegate field to a mock
    java.lang.reflect.Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapter, delegateAdapterMock);

    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    // Since delegate is not null, it should return the cached delegate without calling gson.getDelegateAdapter
    TypeAdapter<Object> result = (TypeAdapter<Object>) delegateMethod.invoke(treeTypeAdapter);
    assertSame(delegateAdapterMock, result);
  }
}
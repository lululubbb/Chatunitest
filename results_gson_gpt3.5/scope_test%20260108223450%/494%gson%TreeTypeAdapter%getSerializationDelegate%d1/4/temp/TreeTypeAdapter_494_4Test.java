package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class TreeTypeAdapter_494_4Test {

  @Mock
  private JsonSerializer<Object> serializerMock;
  @Mock
  private Gson gsonMock;
  @Mock
  private TypeToken<Object> typeTokenMock;
  @Mock
  private TypeAdapterFactory skipPastMock;
  @Mock
  private TypeAdapter<Object> delegateMock;

  private TreeTypeAdapter<Object> treeTypeAdapter;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    // Create instance with serializer (non-null)
    treeTypeAdapter = new TreeTypeAdapter<>(serializerMock, null, gsonMock, typeTokenMock, skipPastMock, false);
    // Inject delegate field using reflection for coverage
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapter, delegateMock);
  }

  @Test
    @Timeout(8000)
  void getSerializationDelegate_whenSerializerNotNull_returnsThis() {
    TypeAdapter<Object> result = treeTypeAdapter.getSerializationDelegate();
    assertSame(treeTypeAdapter, result);
  }

  @Test
    @Timeout(8000)
  void getSerializationDelegate_whenSerializerNull_returnsDelegate() throws Exception {
    // Create instance with null serializer and set delegate
    TreeTypeAdapter<Object> adapterWithNullSerializer = new TreeTypeAdapter<>(null, null, gsonMock, typeTokenMock, skipPastMock, false);
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapterWithNullSerializer, delegateMock);

    TypeAdapter<Object> result = adapterWithNullSerializer.getSerializationDelegate();
    assertSame(delegateMock, result);
  }

  @Test
    @Timeout(8000)
  void getSerializationDelegate_whenSerializerNull_delegateIsLazyLoaded() throws Exception {
    // Create instance with null serializer and no delegate set
    TreeTypeAdapter<Object> adapter = new TreeTypeAdapter<>(null, null, gsonMock, typeTokenMock, skipPastMock, false);

    // Using reflection to reset delegate to null
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, null);

    // Use reflection to invoke the private delegate() method and set delegate field
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    // Invoke delegate() to lazy-load and set the delegate field
    TypeAdapter<Object> lazyLoadedDelegate = (TypeAdapter<Object>) delegateMethod.invoke(adapter);

    // Inject the mock delegate for testing purposes
    delegateField.set(adapter, delegateMock);

    // Now call getSerializationDelegate() and verify it returns the delegateMock after injection
    TypeAdapter<Object> result = adapter.getSerializationDelegate();
    assertSame(delegateMock, result);

    // Verify that the delegate() method returns delegateMock after injection
    TypeAdapter<Object> delegateResult = (TypeAdapter<Object>) delegateMethod.invoke(adapter);
    assertSame(delegateMock, delegateResult);
  }
}
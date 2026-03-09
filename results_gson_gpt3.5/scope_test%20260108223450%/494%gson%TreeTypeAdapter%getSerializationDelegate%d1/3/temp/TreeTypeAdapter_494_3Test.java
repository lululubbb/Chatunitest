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
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class TreeTypeAdapter_494_3Test {

  private TreeTypeAdapter<String> treeTypeAdapterWithSerializer;
  private TreeTypeAdapter<String> treeTypeAdapterWithoutSerializer;
  private JsonSerializer<String> mockSerializer;
  private Gson mockGson;
  private TypeToken<String> typeToken;
  private TypeAdapterFactory mockFactory;
  private TypeAdapter<String> mockDelegate;

  @BeforeEach
  void setUp() throws Exception {
    mockSerializer = mock(JsonSerializer.class);
    mockGson = mock(Gson.class);
    typeToken = TypeToken.get(String.class);
    mockFactory = mock(TypeAdapterFactory.class);

    // Instance with serializer set (should return 'this' in getSerializationDelegate)
    treeTypeAdapterWithSerializer = new TreeTypeAdapter<>(
        mockSerializer, null, mockGson, typeToken, mockFactory, false);

    // Instance without serializer (should call delegate() in getSerializationDelegate)
    treeTypeAdapterWithoutSerializer = new TreeTypeAdapter<>(
        null, null, mockGson, typeToken, mockFactory, false);

    // Prepare delegate TypeAdapter for reflection injection
    mockDelegate = mock(TypeAdapter.class);

    // Inject mock delegate into treeTypeAdapterWithoutSerializer via reflection
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapterWithoutSerializer, mockDelegate);
  }

  @Test
    @Timeout(8000)
  void getSerializationDelegate_returnsThisWhenSerializerNotNull() throws Exception {
    // Act
    TypeAdapter<String> result = treeTypeAdapterWithSerializer.getSerializationDelegate();

    // Assert
    assertSame(treeTypeAdapterWithSerializer, result);
  }

  @Test
    @Timeout(8000)
  void getSerializationDelegate_returnsDelegateWhenSerializerNull() throws Exception {
    // To test delegate() call, we need to override delegate() method or inject delegate field.
    // Since delegate() is private and uses volatile delegate field, we already set delegate field.

    // Act
    TypeAdapter<String> result = treeTypeAdapterWithoutSerializer.getSerializationDelegate();

    // Assert
    assertSame(mockDelegate, result);
  }

  @Test
    @Timeout(8000)
  void getSerializationDelegate_delegateMethodInvokedWhenSerializerNull() throws Exception {
    // Use reflection to set delegate to null and then call getSerializationDelegate to force delegate() call
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapterWithoutSerializer, null);

    // Use reflection to invoke private delegate() method and mock its behavior by temporarily setting delegate field
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    // Setup mockGson to return mockDelegate when getAdapter is called inside delegate()
    when(mockGson.getDelegateAdapter(any(), eq(typeToken))).thenReturn(mockDelegate);

    // Act
    TypeAdapter<String> result = treeTypeAdapterWithoutSerializer.getSerializationDelegate();

    // Assert
    assertSame(mockDelegate, result);

    // Also verify that delegate field was set (cached) after delegate() call
    TypeAdapter<String> cachedDelegate = (TypeAdapter<String>) delegateField.get(treeTypeAdapterWithoutSerializer);
    assertSame(mockDelegate, cachedDelegate);
  }
}
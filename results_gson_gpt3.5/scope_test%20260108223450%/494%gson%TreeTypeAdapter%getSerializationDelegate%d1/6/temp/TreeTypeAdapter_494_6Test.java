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
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TreeTypeAdapter_494_6Test {

  private Gson mockGson;
  private TypeToken<String> typeToken;
  private TypeAdapterFactory mockFactory;

  @BeforeEach
  public void setUp() {
    mockGson = mock(Gson.class);
    typeToken = TypeToken.get(String.class);
    mockFactory = mock(TypeAdapterFactory.class);
  }

  @Test
    @Timeout(8000)
  public void getSerializationDelegate_serializerNotNull_returnsThis() throws Exception {
    // Arrange
    JsonSerializer<String> mockSerializer = mock(JsonSerializer.class);
    TreeTypeAdapter<String> adapter = createTreeTypeAdapter(mockSerializer, null);

    // Act
    TypeAdapter<String> result = invokeGetSerializationDelegate(adapter);

    // Assert
    assertSame(adapter, result);
  }

  @Test
    @Timeout(8000)
  public void getSerializationDelegate_serializerNull_returnsDelegate() throws Exception {
    // Arrange
    TreeTypeAdapter<String> adapter = createTreeTypeAdapter(null, null);
    TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);

    // Set delegate field via reflection to mockDelegate
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, mockDelegate);

    // Act
    TypeAdapter<String> result = invokeGetSerializationDelegate(adapter);

    // Assert
    assertSame(mockDelegate, result);
  }

  private TreeTypeAdapter<String> createTreeTypeAdapter(JsonSerializer<String> serializer, 
                                                        com.google.gson.JsonDeserializer<String> deserializer) {
    return new TreeTypeAdapter<>(serializer, deserializer, mockGson, typeToken, mockFactory, false);
  }

  private TypeAdapter<String> invokeGetSerializationDelegate(TreeTypeAdapter<String> adapter) throws Exception {
    Method method = TreeTypeAdapter.class.getDeclaredMethod("getSerializationDelegate");
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> result = (TypeAdapter<String>) method.invoke(adapter);
    return result;
  }
}
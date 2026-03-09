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
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TreeTypeAdapter_494_2Test {

  private TreeTypeAdapter<String> treeTypeAdapterWithSerializer;
  private TreeTypeAdapter<String> treeTypeAdapterWithoutSerializer;
  private JsonSerializer<String> serializerMock;
  private Gson gsonMock;
  private TypeToken<String> typeToken;
  private TypeAdapterFactory skipPastMock;

  @BeforeEach
  public void setUp() throws Exception {
    serializerMock = mock(JsonSerializer.class);
    gsonMock = mock(Gson.class);
    typeToken = TypeToken.get(String.class);
    skipPastMock = mock(TypeAdapterFactory.class);

    // Instance with non-null serializer
    treeTypeAdapterWithSerializer = new TreeTypeAdapter<>(serializerMock, null, gsonMock, typeToken, skipPastMock, true);

    // Instance with null serializer, so getSerializationDelegate calls delegate()
    treeTypeAdapterWithoutSerializer = new TreeTypeAdapter<>(null, null, gsonMock, typeToken, skipPastMock, true);

    // For treeTypeAdapterWithoutSerializer, we need to set delegate field to a mock TypeAdapter to avoid null pointer
    TypeAdapter<String> delegateMock = mock(TypeAdapter.class);
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapterWithoutSerializer, delegateMock);
  }

  @Test
    @Timeout(8000)
  public void testGetSerializationDelegate_withSerializer_returnsThis() throws Exception {
    Method method = TreeTypeAdapter.class.getDeclaredMethod("getSerializationDelegate");
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> result = (TypeAdapter<String>) method.invoke(treeTypeAdapterWithSerializer);
    assertSame(treeTypeAdapterWithSerializer, result);
  }

  @Test
    @Timeout(8000)
  public void testGetSerializationDelegate_withoutSerializer_returnsDelegate() throws Exception {
    Method method = TreeTypeAdapter.class.getDeclaredMethod("getSerializationDelegate");
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> result = (TypeAdapter<String>) method.invoke(treeTypeAdapterWithoutSerializer);

    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> delegateValue = (TypeAdapter<String>) delegateField.get(treeTypeAdapterWithoutSerializer);

    assertSame(delegateValue, result);
  }
}
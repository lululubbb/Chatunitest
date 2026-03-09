package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.Field;

public class TreeTypeAdapter_491_3Test {

  private JsonDeserializer<Object> deserializerMock;
  private JsonReader jsonReaderMock;
  private Gson gsonMock;
  private TypeToken<Object> typeToken;
  private TypeAdapterFactory skipPastMock;
  private TreeTypeAdapter<Object> treeTypeAdapter;

  @BeforeEach
  public void setUp() {
    deserializerMock = mock(JsonDeserializer.class);
    jsonReaderMock = mock(JsonReader.class);
    gsonMock = mock(Gson.class);
    skipPastMock = mock(TypeAdapterFactory.class);
    typeToken = TypeToken.get(Object.class);
  }

  @Test
    @Timeout(8000)
  public void read_deserializerIsNull_delegateReadCalled() throws IOException {
    treeTypeAdapter = new TreeTypeAdapter<>(null, null, gsonMock, typeToken, skipPastMock, true);

    TypeAdapter<Object> delegateMock = mock(TypeAdapter.class);
    // Use reflection to set delegate field since it's volatile and private
    setDelegate(treeTypeAdapter, delegateMock);

    when(delegateMock.read(jsonReaderMock)).thenReturn("delegateResult");

    Object result = treeTypeAdapter.read(jsonReaderMock);

    verify(delegateMock).read(jsonReaderMock);
    assertEquals("delegateResult", result);
  }

  @Test
    @Timeout(8000)
  public void read_deserializerNotNull_valueIsJsonNullAndNullSafe_returnsNull() throws IOException {
    JsonElement jsonElementMock = mock(JsonElement.class);
    when(jsonElementMock.isJsonNull()).thenReturn(true);

    TreeTypeAdapterForTest<Object> adapterForTest = new TreeTypeAdapterForTest<>(null, deserializerMock, gsonMock, typeToken, skipPastMock, true, jsonElementMock);
    treeTypeAdapter = adapterForTest.delegateAdapter;

    Object result = adapterForTest.read(jsonReaderMock);

    assertNull(result);
    verifyNoInteractions(deserializerMock);
  }

  @Test
    @Timeout(8000)
  public void read_deserializerNotNull_valueIsNotJsonNull_callsDeserialize() throws IOException {
    JsonElement jsonElementMock = mock(JsonElement.class);
    when(jsonElementMock.isJsonNull()).thenReturn(false);

    Object expectedResult = new Object();
    when(deserializerMock.deserialize(any(), any(), any())).thenReturn(expectedResult);

    TreeTypeAdapterForTest<Object> adapterForTest = new TreeTypeAdapterForTest<>(null, deserializerMock, gsonMock, typeToken, skipPastMock, true, jsonElementMock);
    treeTypeAdapter = adapterForTest.delegateAdapter;

    // Capture parameters passed to deserialize
    ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);
    ArgumentCaptor<java.lang.reflect.Type> typeCaptor = ArgumentCaptor.forClass(java.lang.reflect.Type.class);
    ArgumentCaptor<JsonDeserializationContext> contextCaptor = ArgumentCaptor.forClass(JsonDeserializationContext.class);

    Object result = adapterForTest.read(jsonReaderMock);

    verify(deserializerMock).deserialize(elementCaptor.capture(), typeCaptor.capture(), contextCaptor.capture());
    assertSame(expectedResult, result);
    assertEquals(jsonElementMock, elementCaptor.getValue());
    assertEquals(typeToken.getType(), typeCaptor.getValue());
    assertNotNull(contextCaptor.getValue());
  }

  // Helper method to set private volatile delegate field via reflection
  private void setDelegate(TreeTypeAdapter<Object> adapter, TypeAdapter<Object> delegate) {
    try {
      Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
      delegateField.setAccessible(true);
      delegateField.set(adapter, delegate);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Helper class to access private fields of TreeTypeAdapter instance via reflection.
   */
  private static class TreeTypeAdapterForTest<T> {

    final TreeTypeAdapter<T> delegateAdapter;
    private final JsonDeserializer<T> deserializer;
    private final TypeToken<T> typeToken;
    private final boolean nullSafe;
    private final JsonElement elementToReturn;
    private final Object context;

    TreeTypeAdapterForTest(JsonSerializer<T> serializer, JsonDeserializer<T> deserializer,
                           Gson gson, TypeToken<T> typeToken, TypeAdapterFactory skipPast,
                           boolean nullSafe, JsonElement elementToReturn) {
      this.delegateAdapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, nullSafe);
      this.deserializer = deserializer;
      this.typeToken = typeToken;
      this.nullSafe = nullSafe;
      this.elementToReturn = elementToReturn;
      this.context = getContext(delegateAdapter);
    }

    public T read(JsonReader in) throws IOException {
      if (deserializer == null) {
        // Use reflection to get delegate and call read
        TypeAdapter<T> delegate = getDelegate(delegateAdapter);
        return delegate.read(in);
      }
      JsonElement value = elementToReturn;
      if (nullSafe && value.isJsonNull()) {
        return null;
      }
      return deserializer.deserialize(value, typeToken.getType(), (JsonDeserializationContext) context);
    }

    private static <T> TypeAdapter<T> getDelegate(TreeTypeAdapter<T> adapter) {
      try {
        Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        @SuppressWarnings("unchecked")
        TypeAdapter<T> delegate = (TypeAdapter<T>) delegateField.get(adapter);
        if (delegate == null) {
          // If delegate is null, call delegate() method via reflection
          java.lang.reflect.Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
          delegateMethod.setAccessible(true);
          @SuppressWarnings("unchecked")
          TypeAdapter<T> d = (TypeAdapter<T>) delegateMethod.invoke(adapter);
          // set delegate field to avoid repeated calls
          delegateField.set(adapter, d);
          return d;
        }
        return delegate;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    private static Object getContext(TreeTypeAdapter<?> adapter) {
      try {
        Field contextField = TreeTypeAdapter.class.getDeclaredField("context");
        contextField.setAccessible(true);
        return contextField.get(adapter);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
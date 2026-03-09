package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.internal.Streams;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class TreeTypeAdapter_491_6Test {

  private JsonDeserializer<Object> deserializer;
  private JsonSerializer<Object> serializer;
  private Gson gson;
  private TypeToken<Object> typeToken;
  private TypeAdapterFactory skipPast;
  private TreeTypeAdapter<Object> adapter;
  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    deserializer = mock(JsonDeserializer.class);
    serializer = mock(JsonSerializer.class);
    gson = mock(Gson.class);
    typeToken = TypeToken.get(Object.class);
    skipPast = mock(TypeAdapterFactory.class);
    jsonReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  public void read_deserializerNull_delegateReadCalled() throws Exception {
    adapter = new TreeTypeAdapter<>(serializer, null, gson, typeToken, skipPast, true);

    // Prepare delegate TypeAdapter mock and set it via reflection
    TypeAdapter<Object> delegate = mock(TypeAdapter.class);
    when(delegate.read(jsonReader)).thenReturn("delegateResult");

    // Inject delegate into adapter
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, delegate);

    // Use reflection to invoke private delegate() method to verify it returns the injected delegate
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);
    Object delegateResult = delegateMethod.invoke(adapter);
    assertSame(delegate, delegateResult);

    // Call read and verify delegate.read is called and result returned
    Object result = adapter.read(jsonReader);
    verify(delegate).read(jsonReader);
    assertEquals("delegateResult", result);
  }

  @Test
    @Timeout(8000)
  public void read_deserializerNotNull_valueIsJsonNull_nullSafeTrue_returnsNull() throws Exception {
    adapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, true);

    // Mock Streams.parse to return a JsonElement that isJsonNull returns true
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(true);

    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(jsonReader)).thenReturn(jsonElement);

      Object result = adapter.read(jsonReader);
      assertNull(result);
      verify(deserializer, never()).deserialize(any(), any(), any());
    }
  }

  @Test
    @Timeout(8000)
  public void read_deserializerNotNull_valueNotJsonNull_returnsDeserialized() throws Exception {
    adapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, false);

    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(false);

    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(jsonReader)).thenReturn(jsonElement);

      when(deserializer.deserialize(eq(jsonElement), eq(typeToken.getType()), any())).thenReturn("deserializedResult");

      Object result = adapter.read(jsonReader);

      assertEquals("deserializedResult", result);
      verify(deserializer).deserialize(eq(jsonElement), eq(typeToken.getType()), any());
    }
  }
}
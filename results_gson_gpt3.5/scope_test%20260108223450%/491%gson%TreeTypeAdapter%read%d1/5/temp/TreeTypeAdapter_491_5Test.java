package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
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
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.internal.Streams;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;

public class TreeTypeAdapter_491_5Test {

  private TreeTypeAdapter<Object> adapter;
  private JsonDeserializer<Object> deserializer;
  private TypeAdapter<Object> delegateAdapter;
  private Gson gson;
  private TypeToken<Object> typeToken;
  private TypeAdapterFactory skipPast;
  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() throws Exception {
    deserializer = mock(JsonDeserializer.class);
    gson = mock(Gson.class);
    typeToken = TypeToken.get(Object.class);
    skipPast = mock(TypeAdapterFactory.class);
    adapter = new TreeTypeAdapter<>(null, deserializer, gson, typeToken, skipPast, true);

    delegateAdapter = mock(TypeAdapter.class);
    // Inject delegate adapter using reflection
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, delegateAdapter);

    jsonReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  public void read_deserializerNull_callsDelegateRead() throws Exception {
    // Create adapter with null deserializer
    TreeTypeAdapter<Object> adapterWithNullDeserializer = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, true);

    // Inject delegateAdapter into the adapterWithNullDeserializer
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapterWithNullDeserializer, delegateAdapter);

    // Spy on adapterWithNullDeserializer
    TreeTypeAdapter<Object> spyAdapter = Mockito.spy(adapterWithNullDeserializer);

    // Because delegate() is private, we cannot mock it directly.
    // Instead, ensure delegate field is set, so delegate() will return delegateAdapter.

    // Mock delegateAdapter.read to return an object
    Object expected = new Object();
    when(delegateAdapter.read(jsonReader)).thenReturn(expected);

    Object actual = spyAdapter.read(jsonReader);

    assertSame(expected, actual);
    verify(delegateAdapter).read(jsonReader);
  }

  @Test
    @Timeout(8000)
  public void read_nullSafeTrue_valueIsJsonNull_returnsNull() throws IOException {
    // Mock Streams.parse to return JsonElement that isJsonNull() true
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(true);

    try (MockedStatic<Streams> streamsStatic = Mockito.mockStatic(Streams.class)) {
      streamsStatic.when(() -> Streams.parse(jsonReader)).thenReturn(jsonElement);

      Object result = adapter.read(jsonReader);

      assertNull(result);
    }
  }

  @Test
    @Timeout(8000)
  public void read_nullSafeFalse_valueIsJsonNull_callsDeserializer() throws Exception {
    // Setup adapter with nullSafe = false via reflection
    Field nullSafeField = TreeTypeAdapter.class.getDeclaredField("nullSafe");
    nullSafeField.setAccessible(true);
    nullSafeField.set(adapter, false);

    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(true);

    try (MockedStatic<Streams> streamsStatic = Mockito.mockStatic(Streams.class)) {
      streamsStatic.when(() -> Streams.parse(jsonReader)).thenReturn(jsonElement);

      Object deserialized = new Object();
      when(deserializer.deserialize(eq(jsonElement), eq(typeToken.getType()), any())).thenReturn(deserialized);

      Object result = adapter.read(jsonReader);

      assertSame(deserialized, result);
      verify(deserializer).deserialize(eq(jsonElement), eq(typeToken.getType()), any());
    }
  }

  @Test
    @Timeout(8000)
  public void read_valueNotJsonNull_callsDeserializer() throws IOException {
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(false);

    try (MockedStatic<Streams> streamsStatic = Mockito.mockStatic(Streams.class)) {
      streamsStatic.when(() -> Streams.parse(jsonReader)).thenReturn(jsonElement);

      Object deserialized = new Object();
      when(deserializer.deserialize(eq(jsonElement), eq(typeToken.getType()), any())).thenReturn(deserialized);

      Object result = adapter.read(jsonReader);

      assertSame(deserialized, result);
      verify(deserializer).deserialize(eq(jsonElement), eq(typeToken.getType()), any());
    }
  }

  @Test
    @Timeout(8000)
  public void read_delegateThrowsIOException_propagates() throws Exception {
    TreeTypeAdapter<Object> adapterWithNullDeserializer = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, true);

    // Inject delegateAdapter into adapterWithNullDeserializer
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapterWithNullDeserializer, delegateAdapter);

    TreeTypeAdapter<Object> spyAdapter = Mockito.spy(adapterWithNullDeserializer);

    when(delegateAdapter.read(jsonReader)).thenThrow(new IOException("read failed"));

    IOException thrown = assertThrows(IOException.class, () -> spyAdapter.read(jsonReader));
    assertEquals("read failed", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void read_deserializerThrowsException_propagates() throws IOException {
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(false);

    try (MockedStatic<Streams> streamsStatic = Mockito.mockStatic(Streams.class)) {
      streamsStatic.when(() -> Streams.parse(jsonReader)).thenReturn(jsonElement);

      when(deserializer.deserialize(eq(jsonElement), eq(typeToken.getType()), any()))
          .thenThrow(new JsonParseException("deserialize failed"));

      JsonParseException thrown = assertThrows(JsonParseException.class, () -> adapter.read(jsonReader));
      assertEquals("deserialize failed", thrown.getMessage());
    }
  }
}
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
import com.google.gson.JsonNull;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.Streams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class TreeTypeAdapter_491_4Test {

  private JsonDeserializer<String> deserializer;
  private TypeAdapter<String> delegateAdapter;
  private Gson gson;
  private TypeToken<String> typeToken;
  private TypeAdapterFactory skipPast;
  private JsonReader jsonReader;
  private TreeTypeAdapter<String> treeTypeAdapter;

  @BeforeEach
  public void setUp() {
    deserializer = mock(JsonDeserializer.class);
    delegateAdapter = mock(TypeAdapter.class);
    gson = mock(Gson.class);
    typeToken = TypeToken.get(String.class);
    skipPast = mock(TypeAdapterFactory.class);
    jsonReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  public void read_withNullDeserializer_callsDelegateRead() throws Exception {
    treeTypeAdapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, false);

    // Use reflection to set delegate field
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapter, delegateAdapter);

    when(delegateAdapter.read(jsonReader)).thenReturn("delegateResult");

    String result = treeTypeAdapter.read(jsonReader);

    assertEquals("delegateResult", result);
    verify(delegateAdapter).read(jsonReader);
  }

  @Test
    @Timeout(8000)
  public void read_withDeserializerAndNullSafeAndJsonNull_returnsNull() throws Exception {
    treeTypeAdapter = new TreeTypeAdapter<>(null, deserializer, gson, typeToken, skipPast, true);

    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(jsonReader)).thenReturn(JsonNull.INSTANCE);

      String result = treeTypeAdapter.read(jsonReader);

      assertNull(result);
      streamsMock.verify(() -> Streams.parse(jsonReader));
      verifyNoInteractions(deserializer);
    }
  }

  @Test
    @Timeout(8000)
  public void read_withDeserializerAndNonNullJsonElement_callsDeserializer() throws Exception {
    treeTypeAdapter = new TreeTypeAdapter<>(null, deserializer, gson, typeToken, skipPast, true);

    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(false);

    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.parse(jsonReader)).thenReturn(jsonElement);

      // Get the private final context field of type JsonDeserializationContext
      Field contextField = TreeTypeAdapter.class.getDeclaredField("context");
      contextField.setAccessible(true);

      // Remove final modifier
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(contextField, contextField.getModifiers() & ~Modifier.FINAL);

      // Get the current context instance (which is of type GsonContextImpl, implements JsonDeserializationContext)
      Object currentContext = contextField.get(treeTypeAdapter);

      // Spy on the current context instance
      Object spyContext = Mockito.spy(currentContext);

      // Set the spyContext back to the context field
      contextField.set(treeTypeAdapter, spyContext);

      // Cast spyContext to JsonDeserializationContext for correct typing in when/verify
      JsonDeserializationContext spyContextTyped = (JsonDeserializationContext) spyContext;

      when(deserializer.deserialize(jsonElement, typeToken.getType(), spyContextTyped)).thenReturn("deserializedValue");

      String result = treeTypeAdapter.read(jsonReader);

      assertEquals("deserializedValue", result);
      streamsMock.verify(() -> Streams.parse(jsonReader));
      verify(deserializer).deserialize(jsonElement, typeToken.getType(), spyContextTyped);
    }
  }
}
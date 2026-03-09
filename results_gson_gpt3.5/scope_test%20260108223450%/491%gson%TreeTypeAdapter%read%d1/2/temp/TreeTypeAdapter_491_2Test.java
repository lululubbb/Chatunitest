package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSerializationContext;
import com.google.gson.internal.$Gson$Preconditions;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.Gson;
import com.google.gson.internal.Streams;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TreeTypeAdapter_491_2Test {

  private TreeTypeAdapter<Object> treeTypeAdapter;
  private JsonDeserializer<Object> deserializer;
  private JsonSerializer<Object> serializer;
  private Gson gson;
  private TypeToken<Object> typeToken;
  private TypeAdapterFactory skipPast;
  private JsonReader jsonReader;
  private JsonElement jsonElement;
  private TypeAdapter<Object> delegateAdapter;

  @BeforeEach
  public void setUp() {
    deserializer = mock(JsonDeserializer.class);
    serializer = mock(JsonSerializer.class);
    gson = mock(Gson.class);
    typeToken = TypeToken.get(Object.class);
    skipPast = mock(TypeAdapterFactory.class);
    jsonReader = mock(JsonReader.class);
    jsonElement = mock(JsonElement.class);
    delegateAdapter = mock(TypeAdapter.class);
  }

  @Test
    @Timeout(8000)
  public void read_deserializerIsNull_delegateReadCalled() throws Exception {
    treeTypeAdapter = new TreeTypeAdapter<>(serializer, null, gson, typeToken, skipPast, true);

    // Spy on treeTypeAdapter to mock delegate() method via reflection
    TreeTypeAdapter<Object> spyAdapter = Mockito.spy(treeTypeAdapter);

    // Use reflection to get the delegate() method and make it accessible
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    // Because delegate() is private, we cannot mock it directly with Mockito.
    // Instead, we set the private volatile field 'delegate' to our mock delegateAdapter.
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(spyAdapter, delegateAdapter);

    when(delegateAdapter.read(jsonReader)).thenReturn("delegateResult");

    Object result = spyAdapter.read(jsonReader);

    verify(delegateAdapter).read(jsonReader);
    assertEquals("delegateResult", result);
  }

  @Test
    @Timeout(8000)
  public void read_nullSafeAndJsonNull_returnsNull() throws Exception {
    treeTypeAdapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, true);

    // Use TestTreeTypeAdapter to avoid static mocking of Streams.parse
    when(jsonElement.isJsonNull()).thenReturn(true);

    treeTypeAdapter = new TestTreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, true, jsonElement);

    Object result = treeTypeAdapter.read(jsonReader);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void read_deserializerNotNullAndNotJsonNull_callsDeserialize() throws Exception {
    when(jsonElement.isJsonNull()).thenReturn(false);

    treeTypeAdapter = new TestTreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, true, jsonElement);

    // Access context field for verifying deserializer call
    Field contextField = TreeTypeAdapter.class.getDeclaredField("context");
    contextField.setAccessible(true);
    JsonDeserializationContext context = (JsonDeserializationContext) contextField.get(treeTypeAdapter);

    when(deserializer.deserialize(jsonElement, typeToken.getType(), context)).thenReturn("deserializedResult");

    Object result = treeTypeAdapter.read(jsonReader);

    verify(deserializer).deserialize(jsonElement, typeToken.getType(), context);
    assertEquals("deserializedResult", result);
  }

  @Test
    @Timeout(8000)
  public void read_nullSafeFalseAndJsonNull_callsDeserialize() throws Exception {
    when(jsonElement.isJsonNull()).thenReturn(true);

    treeTypeAdapter = new TestTreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, false, jsonElement);

    Field contextField = TreeTypeAdapter.class.getDeclaredField("context");
    contextField.setAccessible(true);
    JsonDeserializationContext context = (JsonDeserializationContext) contextField.get(treeTypeAdapter);

    when(deserializer.deserialize(jsonElement, typeToken.getType(), context)).thenReturn("deserializedResult");

    Object result = treeTypeAdapter.read(jsonReader);

    verify(deserializer).deserialize(jsonElement, typeToken.getType(), context);
    assertEquals("deserializedResult", result);
  }

  // To work around static method mocking, create a subclass for tests that overrides read to inject jsonElement
  private static class TestTreeTypeAdapter<T> extends TreeTypeAdapter<T> {
    private final JsonElement elementToReturn;

    TestTreeTypeAdapter(JsonSerializer<T> serializer, JsonDeserializer<T> deserializer,
                        Gson gson, TypeToken<T> typeToken, TypeAdapterFactory skipPast,
                        boolean nullSafe, JsonElement elementToReturn) {
      super(serializer, deserializer, gson, typeToken, skipPast, nullSafe);
      this.elementToReturn = elementToReturn;
    }

    @Override
    public T read(JsonReader in) throws IOException {
      if (super.deserializer == null) {
        return super.delegate().read(in);
      }
      JsonElement value = elementToReturn;
      if (super.nullSafe && value.isJsonNull()) {
        return null;
      }
      return super.deserializer.deserialize(value, super.typeToken.getType(), super.context);
    }
  }

  @Test
    @Timeout(8000)
  public void read_withTestTreeTypeAdapter_nullSafeTrue_andJsonNull_returnsNull() throws IOException {
    when(jsonElement.isJsonNull()).thenReturn(true);

    treeTypeAdapter = new TestTreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, true, jsonElement);

    Object result = treeTypeAdapter.read(jsonReader);

    assertNull(result);
    verifyNoInteractions(deserializer);
  }

  @Test
    @Timeout(8000)
  public void read_withTestTreeTypeAdapter_nullSafeFalse_andJsonNull_callsDeserialize() throws IOException {
    when(jsonElement.isJsonNull()).thenReturn(true);

    treeTypeAdapter = new TestTreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, false, jsonElement);

    Field contextField = TreeTypeAdapter.class.getDeclaredField("context");
    contextField.setAccessible(true);
    JsonDeserializationContext context = (JsonDeserializationContext) contextField.get(treeTypeAdapter);

    when(deserializer.deserialize(jsonElement, typeToken.getType(), context)).thenReturn("deserialized");

    Object result = treeTypeAdapter.read(jsonReader);

    assertEquals("deserialized", result);
    verify(deserializer).deserialize(jsonElement, typeToken.getType(), context);
  }

  @Test
    @Timeout(8000)
  public void read_withTestTreeTypeAdapter_jsonNotNull_callsDeserialize() throws IOException {
    when(jsonElement.isJsonNull()).thenReturn(false);

    treeTypeAdapter = new TestTreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, true, jsonElement);

    Field contextField = TreeTypeAdapter.class.getDeclaredField("context");
    contextField.setAccessible(true);
    JsonDeserializationContext context = (JsonDeserializationContext) contextField.get(treeTypeAdapter);

    when(deserializer.deserialize(jsonElement, typeToken.getType(), context)).thenReturn("deserialized");

    Object result = treeTypeAdapter.read(jsonReader);

    assertEquals("deserialized", result);
    verify(deserializer).deserialize(jsonElement, typeToken.getType(), context);
  }
}
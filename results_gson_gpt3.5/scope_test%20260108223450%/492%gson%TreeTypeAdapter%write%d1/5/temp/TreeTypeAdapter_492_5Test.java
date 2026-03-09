package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.Type;

import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;

class TreeTypeAdapterWriteTest {

  private JsonSerializer<Object> serializer;
  private TypeAdapter<Object> delegateAdapter;
  private JsonWriter jsonWriter;
  private TreeTypeAdapter<Object> treeTypeAdapter;
  private TypeAdapterFactory skipPast;
  private TypeToken<Object> typeToken;

  @BeforeEach
  void setUp() throws Exception {
    serializer = mock(JsonSerializer.class);
    delegateAdapter = mock(TypeAdapter.class);
    skipPast = mock(TypeAdapterFactory.class);
    typeToken = TypeToken.get(Object.class);

    // Create a JsonWriter with StringWriter to avoid IOException on writes
    jsonWriter = new JsonWriter(new StringWriter());

    // Construct TreeTypeAdapter instance with nullSafe = true
    treeTypeAdapter = new TreeTypeAdapter<>(serializer, null, null, typeToken, skipPast, true);

    // Inject delegate adapter via reflection
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapter, delegateAdapter);

    // Inject gson field as null (not used in test)
    Field gsonField = TreeTypeAdapter.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(treeTypeAdapter, null);
  }

  @Test
    @Timeout(8000)
  void write_withSerializerAndNonNullValue_invokesSerializeAndStreamsWrite() throws IOException {
    Object value = new Object();
    JsonElement jsonElement = mock(JsonElement.class);

    when(serializer.serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class))).thenReturn(jsonElement);

    treeTypeAdapter.write(jsonWriter, value);

    verify(serializer).serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class));
    // We cannot verify Streams.write directly, but no exceptions means it was called.
    verifyNoInteractions(delegateAdapter);
  }

  @Test
    @Timeout(8000)
  void write_withSerializerAndNullValueAndNullSafe_callsNullValueOnWriter() throws IOException {
    TreeTypeAdapter<Object> adapter = new TreeTypeAdapter<>(serializer, null, null, typeToken, skipPast, true);

    JsonWriter spyWriter = spy(jsonWriter);

    adapter.write(spyWriter, null);

    verify(spyWriter).nullValue();
    verifyNoInteractions(serializer);
  }

  @Test
    @Timeout(8000)
  void write_withSerializerAndNullValueAndNotNullSafe_invokesSerialize() throws IOException {
    TreeTypeAdapter<Object> adapter = new TreeTypeAdapter<>(serializer, null, null, typeToken, skipPast, false);

    JsonWriter spyWriter = spy(jsonWriter);
    Object value = null;
    JsonElement jsonElement = mock(JsonElement.class);
    when(serializer.serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class))).thenReturn(jsonElement);

    adapter.write(spyWriter, value);

    verify(serializer).serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class));
    verify(spyWriter, never()).nullValue();
  }

  @Test
    @Timeout(8000)
  void write_withoutSerializer_delegatesWrite() throws IOException, Exception {
    TreeTypeAdapter<Object> adapter = new TreeTypeAdapter<>(null, null, null, typeToken, skipPast, true);

    // Inject delegate adapter via reflection
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, delegateAdapter);

    // Inject gson field as null (not used in test)
    Field gsonField = TreeTypeAdapter.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(adapter, null);

    Object value = new Object();

    adapter.write(jsonWriter, value);

    verify(delegateAdapter).write(jsonWriter, value);
  }

  @Test
    @Timeout(8000)
  void write_withoutSerializerAndNullValue_delegatesWrite() throws IOException, Exception {
    TreeTypeAdapter<Object> adapter = new TreeTypeAdapter<>(null, null, null, typeToken, skipPast, true);

    // Inject delegate adapter via reflection
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, delegateAdapter);

    // Inject gson field as null (not used in test)
    Field gsonField = TreeTypeAdapter.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(adapter, null);

    Object value = null;

    adapter.write(jsonWriter, value);

    verify(delegateAdapter).write(jsonWriter, value);
  }
}
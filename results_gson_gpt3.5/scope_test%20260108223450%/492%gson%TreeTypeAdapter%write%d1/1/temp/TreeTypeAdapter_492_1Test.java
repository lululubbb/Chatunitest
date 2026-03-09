package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.stream.JsonReader;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.internal.Streams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;

class TreeTypeAdapter_492_1Test {

  private JsonSerializer<Object> serializer;
  private JsonWriter jsonWriter;
  private TypeToken<Object> typeToken;
  private TypeAdapterFactory skipPast;
  private TreeTypeAdapter<Object> adapter;

  @BeforeEach
  void setUp() throws Exception {
    serializer = mock(JsonSerializer.class);
    skipPast = mock(TypeAdapterFactory.class);
    typeToken = TypeToken.get(Object.class);

    StringWriter stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);

    // Construct TreeTypeAdapter with nullSafe = true
    adapter = new TreeTypeAdapter<>(serializer, null, null, typeToken, skipPast, true);

    // Inject gson context field (context) if needed or leave as default
  }

  @Test
    @Timeout(8000)
  void write_withNullSerializer_delegatesWrite() throws Exception {
    // Create adapter with null serializer and mock delegate
    TreeTypeAdapter<Object> adapterWithNullSerializer = new TreeTypeAdapter<>(null, null, null, typeToken, skipPast, true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> mockDelegate = mock(TypeAdapter.class);

    // Use reflection to set delegate field
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapterWithNullSerializer, mockDelegate);

    Object value = new Object();
    adapterWithNullSerializer.write(jsonWriter, value);

    verify(mockDelegate).write(jsonWriter, value);
  }

  @Test
    @Timeout(8000)
  void write_nullSafeTrue_valueNull_writesNullValue() throws IOException, NoSuchFieldException, IllegalAccessException {
    // serializer is not null, nullSafe = true, value = null

    // Use reflection to get the underlying StringWriter from jsonWriter
    Field outField = JsonWriter.class.getDeclaredField("out");
    outField.setAccessible(true);
    StringWriter stringWriter = (StringWriter) outField.get(jsonWriter);

    adapter.write(jsonWriter, null);

    // The underlying writer should have written null
    String output = stringWriter.toString();
    assertTrue(output.contains("null"));
  }

  @Test
    @Timeout(8000)
  void write_serializerNotNull_valueNotNull_writesSerializedJsonElement() throws IOException {
    Object value = new Object();
    // Return a real JsonElement instance instead of a mock
    JsonElement jsonElement = JsonNull.INSTANCE;

    when(serializer.serialize(eq(value), any(Type.class), any(JsonSerializationContext.class))).thenReturn(jsonElement);

    // Use a StringWriter to capture output
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);

    // Create adapter with serializer and nullSafe true
    TreeTypeAdapter<Object> localAdapter = new TreeTypeAdapter<>(serializer, null, null, typeToken, skipPast, true);

    localAdapter.write(writer, value);

    // We cannot verify Streams.write directly, but we can check that serializer.serialize was called
    verify(serializer).serialize(eq(value), any(Type.class), any(JsonSerializationContext.class));

    // Since Streams.write writes the JsonElement to JsonWriter, the output should not be empty
    String output = stringWriter.toString();
    assertNotNull(output);
    assertFalse(output.isEmpty());
  }

  @Test
    @Timeout(8000)
  void write_nullSafeFalse_valueNull_serializes() throws IOException {
    // Create adapter with nullSafe = false
    TreeTypeAdapter<Object> localAdapter = new TreeTypeAdapter<>(serializer, null, null, typeToken, skipPast, false);

    Object value = null;
    // Return a real JsonElement instance instead of a mock
    JsonElement jsonElement = JsonNull.INSTANCE;

    when(serializer.serialize(eq(value), any(Type.class), any(JsonSerializationContext.class))).thenReturn(jsonElement);

    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);

    localAdapter.write(writer, value);

    verify(serializer).serialize(eq(value), any(Type.class), any(JsonSerializationContext.class));
    String output = stringWriter.toString();
    assertNotNull(output);
    assertFalse(output.isEmpty());
  }

}
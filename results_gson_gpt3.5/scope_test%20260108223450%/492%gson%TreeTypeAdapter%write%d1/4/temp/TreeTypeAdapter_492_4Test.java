package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.Type;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.internal.Streams;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;

class TreeTypeAdapterWriteTest {

  private TreeTypeAdapter<Object> treeTypeAdapter;
  private JsonSerializer<Object> serializer;
  private JsonWriter jsonWriter;
  private TypeToken<Object> typeToken;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() throws Exception {
    serializer = mock(JsonSerializer.class);
    typeToken = TypeToken.get(Object.class);
    // Construct TreeTypeAdapter with nullSafe = true
    treeTypeAdapter = new TreeTypeAdapter<>(serializer, null, null, typeToken, null, true);

    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);

    // Set delegate to a mock TypeAdapter to verify delegation
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> delegateAdapter = mock(TypeAdapter.class);
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapter, delegateAdapter);
  }

  @Test
    @Timeout(8000)
  void testWrite_withNullValueAndNullSafeTrue_callsNullValue() throws IOException {
    // Act
    treeTypeAdapter.write(jsonWriter, null);
    jsonWriter.flush();

    // Assert
    String output = stringWriter.toString();
    assert output.contains("null");
  }

  @Test
    @Timeout(8000)
  void testWrite_withNullSerializer_delegatesWrite() throws Exception {
    // Arrange
    TreeTypeAdapter<Object> adapterWithNullSerializer = new TreeTypeAdapter<>(null, null, null, typeToken, null, true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> mockDelegate = mock(TypeAdapter.class);

    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapterWithNullSerializer, mockDelegate);

    StringWriter localStringWriter = new StringWriter();
    JsonWriter localJsonWriter = new JsonWriter(localStringWriter);

    Object value = new Object();

    // Act
    adapterWithNullSerializer.write(localJsonWriter, value);

    // Assert
    verify(mockDelegate).write(localJsonWriter, value);
  }

  @Test
    @Timeout(8000)
  void testWrite_withSerializer_serializesAndWrites() throws IOException {
    // Arrange
    Object value = new Object();
    JsonElement element = mock(JsonElement.class);
    when(serializer.serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class))).thenReturn(element);

    // Act
    treeTypeAdapter.write(jsonWriter, value);
    jsonWriter.flush();

    // Assert
    verify(serializer).serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class));
    // No direct verification of Streams.write possible without static mocking
  }

  @Test
    @Timeout(8000)
  void testWrite_withNullValueAndNullSafeFalse_callsSerializeAndWrite() throws IOException {
    // Arrange
    TreeTypeAdapter<Object> adapterWithNullSafeFalse = new TreeTypeAdapter<>(serializer, null, null, typeToken, null, false);
    Object value = null;
    JsonElement element = mock(JsonElement.class);
    when(serializer.serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class))).thenReturn(element);

    StringWriter localStringWriter = new StringWriter();
    JsonWriter localJsonWriter = new JsonWriter(localStringWriter);

    // Act
    adapterWithNullSafeFalse.write(localJsonWriter, value);
    localJsonWriter.flush();

    // Assert
    verify(serializer).serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class));
    // No direct verification of Streams.write possible without static mocking
  }
}
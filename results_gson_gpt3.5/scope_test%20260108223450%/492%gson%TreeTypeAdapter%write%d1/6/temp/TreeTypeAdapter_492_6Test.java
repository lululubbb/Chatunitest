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
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;

class TreeTypeAdapterWriteTest {

  @Mock
  JsonSerializer<String> serializerMock;

  @Mock
  TypeAdapter<String> delegateMock;

  @Mock
  TypeAdapterFactory skipPastMock;

  @Mock
  JsonElement jsonElementMock;

  JsonWriter jsonWriter;

  TreeTypeAdapter<String> treeTypeAdapter;

  StringWriter stringWriter;

  private JsonSerializationContext getContext(TreeTypeAdapter<?> adapter) throws Exception {
    Field contextField = TreeTypeAdapter.class.getDeclaredField("context");
    contextField.setAccessible(true);
    return (JsonSerializationContext) contextField.get(adapter);
  }

  private TypeToken<?> getTypeToken(TreeTypeAdapter<?> adapter) throws Exception {
    Field typeTokenField = TreeTypeAdapter.class.getDeclaredField("typeToken");
    typeTokenField.setAccessible(true);
    return (TypeToken<?>) typeTokenField.get(adapter);
  }

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);

    // Create TreeTypeAdapter instance with nullSafe = true
    treeTypeAdapter = new TreeTypeAdapter<>(serializerMock, null, null, TypeToken.get(String.class), skipPastMock, true);

    // Inject delegate mock via reflection to avoid null delegate
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapter, delegateMock);
  }

  @Test
    @Timeout(8000)
  public void write_withNullSerializer_delegatesWrite() throws IOException, Exception {
    // Create TreeTypeAdapter with serializer = null
    TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(null, null, null, TypeToken.get(String.class), skipPastMock, true);

    // Inject delegate mock
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, delegateMock);

    StringWriter sw = new StringWriter();
    JsonWriter jw = new JsonWriter(sw);

    String value = "testValue";

    adapter.write(jw, value);

    verify(delegateMock).write(jw, value);
  }

  @Test
    @Timeout(8000)
  public void write_withNullSafeTrue_andValueNull_writesNullValue() throws IOException, Exception {
    JsonSerializationContext context = getContext(treeTypeAdapter);
    TypeToken<?> typeToken = getTypeToken(treeTypeAdapter);
    when(serializerMock.serialize(null, typeToken.getType(), context)).thenReturn(jsonElementMock);

    treeTypeAdapter.write(jsonWriter, null);

    // The output should be "null"
    assertEquals("null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void write_withNonNullValue_serializesAndWritesJsonElement() throws IOException, Exception {
    String value = "value";

    JsonSerializationContext context = getContext(treeTypeAdapter);
    TypeToken<?> typeToken = getTypeToken(treeTypeAdapter);
    when(serializerMock.serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class))).thenReturn(jsonElementMock);

    treeTypeAdapter.write(jsonWriter, value);

    String output = stringWriter.toString();
    assertNotNull(output);
    assertFalse(output.isEmpty());

    verify(serializerMock).serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class));
  }

  @Test
    @Timeout(8000)
  public void write_withNullSafeFalse_andValueNull_serializesAndWritesJsonElement() throws IOException, Exception {
    // Create TreeTypeAdapter with nullSafe = false
    TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(serializerMock, null, null, TypeToken.get(String.class), skipPastMock, false);

    StringWriter sw = new StringWriter();
    JsonWriter jw = new JsonWriter(sw);

    // Inject delegate mock to avoid NullPointerException if delegate() is called
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, delegateMock);

    JsonSerializationContext context = getContext(adapter);
    TypeToken<?> typeToken = getTypeToken(adapter);
    when(serializerMock.serialize(null, typeToken.getType(), context)).thenReturn(jsonElementMock);

    adapter.write(jw, null);

    String output = sw.toString();
    assertNotNull(output);
    assertFalse(output.isEmpty());

    verify(serializerMock).serialize(isNull(), eq(typeToken.getType()), any(JsonSerializationContext.class));
  }
}
package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class TreeTypeAdapter_490_6Test {

  private Gson gson;
  private TypeToken<String> typeToken;
  private TypeAdapterFactory skipPast;
  private JsonSerializer<String> serializer;
  private JsonDeserializer<String> deserializer;
  private TreeTypeAdapter<String> adapter;

  @BeforeEach
  void setUp() {
    gson = mock(Gson.class);
    typeToken = TypeToken.get(String.class);
    skipPast = mock(TypeAdapterFactory.class);
    serializer = mock(JsonSerializer.class);
    deserializer = mock(JsonDeserializer.class);
    adapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, true);
  }

  @Test
    @Timeout(8000)
  void constructor_shouldSetFields() throws Exception {
    Field serializerField = TreeTypeAdapter.class.getDeclaredField("serializer");
    serializerField.setAccessible(true);
    assertSame(serializer, serializerField.get(adapter));

    Field deserializerField = TreeTypeAdapter.class.getDeclaredField("deserializer");
    deserializerField.setAccessible(true);
    assertSame(deserializer, deserializerField.get(adapter));

    Field gsonField = TreeTypeAdapter.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    assertSame(gson, gsonField.get(adapter));

    Field typeTokenField = TreeTypeAdapter.class.getDeclaredField("typeToken");
    typeTokenField.setAccessible(true);
    assertSame(typeToken, typeTokenField.get(adapter));

    Field skipPastField = TreeTypeAdapter.class.getDeclaredField("skipPast");
    skipPastField.setAccessible(true);
    assertSame(skipPast, skipPastField.get(adapter));

    Field nullSafeField = TreeTypeAdapter.class.getDeclaredField("nullSafe");
    nullSafeField.setAccessible(true);
    assertTrue((Boolean) nullSafeField.get(adapter));
  }

  @Test
    @Timeout(8000)
  void read_withDeserializer_shouldInvokeDeserializer() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    JsonElement jsonElement = mock(JsonElement.class);
    when(gson.getAdapter(JsonElement.class)).thenReturn(mock(TypeAdapter.class));
    when(gson.fromJsonTree(jsonElement)).thenReturn("deserialized");

    // Mock delegate() to return a TypeAdapter that returns jsonElement on read
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(delegateAdapter.read(reader)).thenReturn("delegateRead");
    setDelegate(adapter, delegateAdapter);

    // Use reflection to call private delegate() and mock it returning delegateAdapter
    // Instead, we test read() behavior when deserializer is present
    // So we mock deserializer.deserialize to return "deserialized"
    when(deserializer.deserialize(any(JsonElement.class), eq(typeToken.getType()), any())).thenReturn("deserialized");

    // Spy gson to return jsonElement from Streams.parse(reader)
    // But Streams.parse is static so we can't mock easily, instead simulate read flow

    // We override delegate to null to force deserializer path
    setDelegate(adapter, null);

    // Mock gson.fromJsonTree to return "deserialized"
    when(gson.fromJsonTree(any())).thenReturn("deserialized");

    // Because read calls Streams.parse(in) which reads JsonElement from JsonReader,
    // we simulate that by mocking Streams.parse if needed, but it's static so can't mock easily.
    // Instead, we can test read by creating a JsonReader from a JSON string.

    // But since deserializer.deserialize is mocked to return "deserialized", 
    // we just test that read returns "deserialized"

    // For coverage, call read()
    // We create a real JsonReader from a string.
    JsonReader realReader = new JsonReader(new java.io.StringReader("\"json\""));
    // We need to mock deserializer.deserialize to return "deserialized"
    when(deserializer.deserialize(any(JsonElement.class), eq(typeToken.getType()), any())).thenReturn("deserialized");

    String result = adapter.read(realReader);
    assertEquals("deserialized", result);
  }

  @Test
    @Timeout(8000)
  void read_withNoDeserializer_shouldUseDelegate() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(delegateAdapter.read(reader)).thenReturn("delegateValue");
    setDelegate(adapter, delegateAdapter);

    // Set deserializer to null to force delegate path
    setField(adapter, "deserializer", null);

    String result = adapter.read(reader);
    assertEquals("delegateValue", result);
    verify(delegateAdapter).read(reader);
  }

  @Test
    @Timeout(8000)
  void write_withSerializer_shouldInvokeSerializer() throws Exception {
    JsonWriter writer = mock(JsonWriter.class);
    String value = "value";
    JsonElement jsonElement = mock(JsonElement.class);

    when(serializer.serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class))).thenReturn(jsonElement);

    // Spy gson to get adapter for JsonElement and write jsonElement
    TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

    adapter.write(writer, value);

    verify(serializer).serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class));
    verify(jsonElementAdapter).write(writer, jsonElement);
  }

  @Test
    @Timeout(8000)
  void write_withNoSerializer_shouldUseDelegate() throws Exception {
    JsonWriter writer = mock(JsonWriter.class);
    String value = "value";

    setField(adapter, "serializer", null);

    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    setDelegate(adapter, delegateAdapter);

    adapter.write(writer, value);

    verify(delegateAdapter).write(writer, value);
  }

  @Test
    @Timeout(8000)
  void delegate_shouldInitializeDelegateOnce() throws Exception {
    // Initially delegate is null
    setField(adapter, "delegate", null);

    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(skipPast.create(eq(gson), eq(typeToken))).thenReturn(delegateAdapter);

    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    // Call delegate() first time, should initialize delegate
    @SuppressWarnings("unchecked")
    TypeAdapter<String> firstDelegate = (TypeAdapter<String>) delegateMethod.invoke(adapter);
    assertNotNull(firstDelegate);
    assertSame(delegateAdapter, firstDelegate);

    // Call delegate() second time, should return cached delegate
    @SuppressWarnings("unchecked")
    TypeAdapter<String> secondDelegate = (TypeAdapter<String>) delegateMethod.invoke(adapter);
    assertSame(firstDelegate, secondDelegate);
  }

  @Test
    @Timeout(8000)
  void getSerializationDelegate_shouldReturnDelegate() {
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    setDelegate(adapter, delegateAdapter);
    assertSame(delegateAdapter, adapter.getSerializationDelegate());
  }

  @Test
    @Timeout(8000)
  void newFactory_shouldCreateFactoryAndProduceAdapter() throws IOException {
    TypeToken<String> stringType = TypeToken.get(String.class);
    JsonSerializer<String> ser = mock(JsonSerializer.class);
    JsonDeserializer<String> deser = mock(JsonDeserializer.class);
    Object typeAdapter = new TreeTypeAdapter<>(ser, deser, gson, stringType, skipPast);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(stringType, typeAdapter);
    assertNotNull(factory);

    TypeAdapter<?> created = factory.create(gson, stringType);
    assertNotNull(created);
  }

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_shouldCreateFactoryAndProduceAdapter() throws IOException {
    TypeToken<String> stringType = TypeToken.get(String.class);
    JsonSerializer<String> ser = mock(JsonSerializer.class);
    JsonDeserializer<String> deser = mock(JsonDeserializer.class);
    Object typeAdapter = new TreeTypeAdapter<>(ser, deser, gson, stringType, skipPast);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(stringType, typeAdapter);
    assertNotNull(factory);

    TypeAdapter<?> created = factory.create(gson, stringType);
    assertNotNull(created);
  }

  @Test
    @Timeout(8000)
  void newTypeHierarchyFactory_shouldCreateFactoryAndProduceAdapter() throws IOException {
    Class<String> hierarchyType = String.class;
    JsonSerializer<String> ser = mock(JsonSerializer.class);
    JsonDeserializer<String> deser = mock(JsonDeserializer.class);
    Object typeAdapter = new TreeTypeAdapter<>(ser, deser, gson, TypeToken.get(hierarchyType), skipPast);

    TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(hierarchyType, typeAdapter);
    assertNotNull(factory);

    TypeAdapter<?> created = factory.create(gson, TypeToken.get(hierarchyType));
    assertNotNull(created);
  }

  // Helper method to set private final delegate field via reflection
  private void setDelegate(TreeTypeAdapter<String> adapter, TypeAdapter<String> delegate) {
    setField(adapter, "delegate", delegate);
  }

  // Helper method to set private fields via reflection
  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = TreeTypeAdapter.class.getDeclaredField(fieldName);
      field.setAccessible(true);

      // Remove final modifier if present
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
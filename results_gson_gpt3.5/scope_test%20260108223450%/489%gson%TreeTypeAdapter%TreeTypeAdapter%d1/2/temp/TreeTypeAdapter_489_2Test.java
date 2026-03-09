package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class TreeTypeAdapter_489_2Test {

  private Gson mockGson;
  private JsonSerializer<String> mockSerializer;
  private JsonDeserializer<String> mockDeserializer;
  private TypeToken<String> typeToken;
  private TypeAdapterFactory mockSkipPast;
  private TreeTypeAdapter<String> adapter;

  @BeforeEach
  void setUp() {
    mockGson = mock(Gson.class);
    mockSerializer = mock(JsonSerializer.class);
    mockDeserializer = mock(JsonDeserializer.class);
    typeToken = TypeToken.get(String.class);
    mockSkipPast = mock(TypeAdapterFactory.class);
    adapter = new TreeTypeAdapter<>(mockSerializer, mockDeserializer, mockGson, typeToken, mockSkipPast, true);
  }

  @Test
    @Timeout(8000)
  void constructor_assignsFieldsCorrectly() throws Exception {
    Field serializerField = TreeTypeAdapter.class.getDeclaredField("serializer");
    serializerField.setAccessible(true);
    assertEquals(mockSerializer, serializerField.get(adapter));

    Field deserializerField = TreeTypeAdapter.class.getDeclaredField("deserializer");
    deserializerField.setAccessible(true);
    assertEquals(mockDeserializer, deserializerField.get(adapter));

    Field gsonField = TreeTypeAdapter.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    assertEquals(mockGson, gsonField.get(adapter));

    Field typeTokenField = TreeTypeAdapter.class.getDeclaredField("typeToken");
    typeTokenField.setAccessible(true);
    assertEquals(typeToken, typeTokenField.get(adapter));

    Field skipPastField = TreeTypeAdapter.class.getDeclaredField("skipPast");
    skipPastField.setAccessible(true);
    assertEquals(mockSkipPast, skipPastField.get(adapter));

    Field nullSafeField = TreeTypeAdapter.class.getDeclaredField("nullSafe");
    nullSafeField.setAccessible(true);
    assertTrue(nullSafeField.getBoolean(adapter));
  }

  @Test
    @Timeout(8000)
  void read_delegatesToDeserializer() throws IOException {
    JsonReader mockReader = mock(JsonReader.class);
    JsonElement jsonElement = JsonParser.parseString("\"value\"");

    // Mock gson.getAdapter(TypeToken) to return a delegate TypeAdapter that reads JsonElement
    TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
    when(mockGson.getAdapter(TypeToken.get(JsonElement.class))).thenReturn(jsonElementAdapter);
    when(jsonElementAdapter.read(mockReader)).thenReturn(jsonElement);

    when(mockDeserializer.deserialize(eq(jsonElement), eq(String.class), any(JsonDeserializationContext.class)))
        .thenReturn("deserialized");

    // Also mock gson.getDelegateAdapter(...) since TreeTypeAdapter.read() calls delegate().read() internally in some code paths
    TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);
    when(mockSkipPast.create(eq(mockGson), eq(typeToken))).thenReturn(mockDelegate);
    when(mockGson.getDelegateAdapter(eq(mockSkipPast), eq(typeToken))).thenReturn(mockDelegate);

    String result = adapter.read(mockReader);
    assertEquals("deserialized", result);
  }

  @Test
    @Timeout(8000)
  void read_throwsJsonParseException_whenDeserializerThrows() throws IOException {
    JsonReader mockReader = mock(JsonReader.class);
    JsonElement jsonElement = JsonParser.parseString("\"value\"");

    TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
    when(mockGson.getAdapter(TypeToken.get(JsonElement.class))).thenReturn(jsonElementAdapter);
    when(jsonElementAdapter.read(mockReader)).thenReturn(jsonElement);

    when(mockDeserializer.deserialize(eq(jsonElement), eq(String.class), any(JsonDeserializationContext.class)))
        .thenThrow(new JsonParseException("error"));

    TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);
    when(mockSkipPast.create(eq(mockGson), eq(typeToken))).thenReturn(mockDelegate);
    when(mockGson.getDelegateAdapter(eq(mockSkipPast), eq(typeToken))).thenReturn(mockDelegate);

    assertThrows(JsonParseException.class, () -> adapter.read(mockReader));
  }

  @Test
    @Timeout(8000)
  void write_delegatesToSerializer() throws IOException {
    JsonWriter mockWriter = mock(JsonWriter.class);
    String value = "value";
    JsonElement jsonElement = JsonParser.parseString("\"value\"");
    when(mockSerializer.serialize(eq(value), eq(String.class), any(JsonSerializationContext.class))).thenReturn(jsonElement);

    adapter.write(mockWriter, value);

    // Verify that Streams.write was called to write the jsonElement to mockWriter
    // Since Streams.write is static, we cannot mock it easily here, so we verify serializer call only
    verify(mockSerializer).serialize(eq(value), eq(String.class), any(JsonSerializationContext.class));
  }

  @Test
    @Timeout(8000)
  void write_writesNull_whenValueIsNull() throws IOException {
    JsonWriter mockWriter = mock(JsonWriter.class);

    adapter.write(mockWriter, null);

    verify(mockWriter).nullValue();
  }

  @Test
    @Timeout(8000)
  void delegate_invokesGsonGetAdapterAndCaches() throws Exception {
    // Initially delegate field is null
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    assertNull(delegateField.get(adapter));

    TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);
    when(mockSkipPast.create(eq(mockGson), eq(typeToken))).thenReturn(mockDelegate);
    when(mockGson.getDelegateAdapter(eq(mockSkipPast), eq(typeToken))).thenReturn(mockDelegate);

    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    TypeAdapter<String> firstCall = (TypeAdapter<String>) delegateMethod.invoke(adapter);
    TypeAdapter<String> secondCall = (TypeAdapter<String>) delegateMethod.invoke(adapter);

    assertSame(mockDelegate, firstCall);
    assertSame(firstCall, secondCall);
    assertSame(mockDelegate, delegateField.get(adapter));
  }

  @Test
    @Timeout(8000)
  void getSerializationDelegate_returnsDelegate() {
    TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);
    // Set delegate field via reflection
    try {
      Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
      delegateField.setAccessible(true);
      delegateField.set(adapter, mockDelegate);
    } catch (Exception e) {
      fail(e);
    }

    assertSame(mockDelegate, adapter.getSerializationDelegate());
  }

  @Test
    @Timeout(8000)
  void newFactory_createsFactoryThatCreatesTreeTypeAdapter() {
    TypeToken<String> exactType = TypeToken.get(String.class);
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
    Object typeAdapter = new Object() {
      @SuppressWarnings("unused")
      JsonSerializer<String> getSerializer() {
        return serializer;
      }

      @SuppressWarnings("unused")
      JsonDeserializer<String> getDeserializer() {
        return deserializer;
      }
    };

    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(exactType, typeAdapter);
    Gson gson = new Gson();

    TypeAdapter<String> adapter = factory.create(gson, exactType);
    assertNotNull(adapter);
    assertTrue(adapter instanceof TreeTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_createsFactory() {
    TypeToken<String> exactType = TypeToken.get(String.class);
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
    Object typeAdapter = new Object() {
      @SuppressWarnings("unused")
      JsonSerializer<String> getSerializer() {
        return serializer;
      }

      @SuppressWarnings("unused")
      JsonDeserializer<String> getDeserializer() {
        return deserializer;
      }
    };

    // Fix: Pass raw Class<String> instead of TypeToken to newFactoryWithMatchRawType
    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(String.class, typeAdapter);
    Gson gson = new Gson();

    TypeAdapter<String> adapter = factory.create(gson, exactType);
    assertNotNull(adapter);
    assertTrue(adapter instanceof TreeTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void newTypeHierarchyFactory_createsFactory() {
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
    Object typeAdapter = new Object() {
      @SuppressWarnings("unused")
      JsonSerializer<String> getSerializer() {
        return serializer;
      }

      @SuppressWarnings("unused")
      JsonDeserializer<String> getDeserializer() {
        return deserializer;
      }
    };

    TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(CharSequence.class, typeAdapter);
    Gson gson = new Gson();

    TypeAdapter<String> adapter = factory.create(gson, TypeToken.get(String.class));
    assertNotNull(adapter);
    assertTrue(adapter instanceof TreeTypeAdapter);
  }
}
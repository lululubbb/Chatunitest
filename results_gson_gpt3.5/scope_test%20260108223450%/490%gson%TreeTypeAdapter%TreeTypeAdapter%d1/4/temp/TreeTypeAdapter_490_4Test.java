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
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class TreeTypeAdapter_490_4Test {

  private Gson gsonMock;
  private TypeToken<String> typeToken;
  private JsonSerializer<String> serializerMock;
  private JsonDeserializer<String> deserializerMock;
  private TypeAdapterFactory skipPastMock;
  private TreeTypeAdapter<String> adapter;

  @BeforeEach
  void setup() {
    gsonMock = mock(Gson.class);
    typeToken = TypeToken.get(String.class);
    serializerMock = mock(JsonSerializer.class);
    deserializerMock = mock(JsonDeserializer.class);
    skipPastMock = mock(TypeAdapterFactory.class);
    adapter = new TreeTypeAdapter<>(serializerMock, deserializerMock, gsonMock, typeToken, skipPastMock);
  }

  @Test
    @Timeout(8000)
  void constructor_shouldSetFieldsCorrectly() throws Exception {
    Field fSerializer = TreeTypeAdapter.class.getDeclaredField("serializer");
    fSerializer.setAccessible(true);
    assertSame(serializerMock, fSerializer.get(adapter));

    Field fDeserializer = TreeTypeAdapter.class.getDeclaredField("deserializer");
    fDeserializer.setAccessible(true);
    assertSame(deserializerMock, fDeserializer.get(adapter));

    Field fGson = TreeTypeAdapter.class.getDeclaredField("gson");
    fGson.setAccessible(true);
    assertSame(gsonMock, fGson.get(adapter));

    Field fTypeToken = TreeTypeAdapter.class.getDeclaredField("typeToken");
    fTypeToken.setAccessible(true);
    assertEquals(typeToken, fTypeToken.get(adapter));

    Field fSkipPast = TreeTypeAdapter.class.getDeclaredField("skipPast");
    fSkipPast.setAccessible(true);
    assertSame(skipPastMock, fSkipPast.get(adapter));

    Field fNullSafe = TreeTypeAdapter.class.getDeclaredField("nullSafe");
    fNullSafe.setAccessible(true);
    assertTrue((Boolean) fNullSafe.get(adapter));
  }

  @Test
    @Timeout(8000)
  void read_withNullJsonElement_shouldReturnNull() throws Exception {
    JsonReader in = mock(JsonReader.class);
    // Mock Streams.parse to return JsonNull.INSTANCE
    JsonElement jsonNull = JsonNull.INSTANCE;
    // Use spy on gson to mock parse method indirectly
    when(gsonMock.fromJson(in, JsonElement.class)).thenReturn(jsonNull);
    // But read method calls Streams.parse, so we cannot mock that directly.
    // Instead, create a JsonReader that returns null JSON element by simulating empty input.
    // Alternative: Use reflection to call private delegate() to set delegate mock returning "delegateValue"
    // But here we test read with deserializer present and JsonNull input

    // We need to spy Streams.parse to return JsonNull, but it's static so not mockable with Mockito 3.
    // So, create a JsonReader over "null"
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("null"));
    String result = adapter.read(jsonReader);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_withNonNullJsonElement_shouldInvokeDeserializer() throws Exception {
    String expected = "deserialized";
    JsonReader in = new JsonReader(new java.io.StringReader("\"value\""));

    // We cannot mock static Streams.parse, so we will spy gson.fromJson instead
    // But read method calls Streams.parse(in), then calls deserializer.deserialize(json, ...)

    // So just test read with real JsonReader and mocked deserializer

    // Prepare a JsonElement representing the JSON string "value"
    JsonElement jsonElement = JsonParser.parseString("\"value\"");

    // Spy gson to return delegate adapter that returns expected value, to test fallback
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(delegateAdapter.read(any())).thenReturn("delegateValue");

    Field fDelegate = TreeTypeAdapter.class.getDeclaredField("delegate");
    fDelegate.setAccessible(true);
    fDelegate.set(adapter, delegateAdapter);

    // Mock deserializer to return expected when called
    when(deserializerMock.deserialize(eq(jsonElement), eq(typeToken.getType()), any()))
        .thenReturn(expected);

    // We cannot inject the jsonElement returned by Streams.parse, so we test fallback delegate read:
    // To force deserializer call, we create a spy on adapter and override delegate() to throw, forcing deserializer path

    TreeTypeAdapter<String> spyAdapter = spy(adapter);
    doReturn(null).when(spyAdapter).delegate();

    // Use reflection to invoke read(JsonReader)
    Method readMethod = TreeTypeAdapter.class.getDeclaredMethod("read", JsonReader.class);
    readMethod.setAccessible(true);
    String actual = (String) readMethod.invoke(spyAdapter, in);

    // deserializerMock.deserialize returns expected, so actual should equal expected
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void write_withNullValue_shouldWriteNull() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    adapter.write(out, null);
    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  void write_withSerializer_shouldInvokeSerializer() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    String value = "value";
    JsonElement jsonElement = JsonParser.parseString("\"json\"");
    when(serializerMock.serialize(eq(value), eq(typeToken.getType()), any())).thenReturn(jsonElement);

    adapter.write(out, value);

    // Verify serializer called
    verify(serializerMock).serialize(eq(value), eq(typeToken.getType()), any());

    // Verify Streams.write called with jsonElement and out
    // Streams.write is static, cannot mock, so verify out calls
    // We can capture calls on out to check write calls
    verify(out, atLeastOnce()).beginArray(); // or other methods called by Streams.write
  }

  @Test
    @Timeout(8000)
  void delegate_shouldInitializeDelegateOnce() throws Exception {
    // Initially delegate is null
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, null);

    // Mock skipPast.create to return a delegate adapter
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(skipPastMock.create(any(), eq(typeToken))).thenReturn(delegateAdapter);

    // Call delegate() via reflection (private)
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    TypeAdapter<String> firstCall = (TypeAdapter<String>) delegateMethod.invoke(adapter);
    TypeAdapter<String> secondCall = (TypeAdapter<String>) delegateMethod.invoke(adapter);

    assertSame(delegateAdapter, firstCall);
    assertSame(firstCall, secondCall);

    // Verify skipPast.create called only once
    verify(skipPastMock, times(1)).create(any(), eq(typeToken));
  }

  @Test
    @Timeout(8000)
  void getSerializationDelegate_shouldReturnDelegate() throws Exception {
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, delegateAdapter);

    TypeAdapter<String> result = adapter.getSerializationDelegate();
    assertSame(delegateAdapter, result);
  }

  @Test
    @Timeout(8000)
  void newFactory_shouldCreateFactoryAndReturnAdapter() {
    TypeToken<String> type = TypeToken.get(String.class);
    JsonSerializer<String> serializer = (src, typeOfSrc, context) -> new JsonPrimitive("ser");
    JsonDeserializer<String> deserializer = (json, typeOfT, context) -> "deser";

    Object typeAdapter = new TreeTypeAdapter<>(serializer, deserializer, gsonMock, type, skipPastMock);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(type, typeAdapter);

    TypeAdapter<?> adapterFromFactory = factory.create(gsonMock, type);
    assertNotNull(adapterFromFactory);
  }

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_shouldMatchRawType() {
    TypeToken<String> type = TypeToken.get(String.class);
    JsonSerializer<String> serializer = (src, typeOfSrc, context) -> new JsonPrimitive("ser");
    JsonDeserializer<String> deserializer = (json, typeOfT, context) -> "deser";

    Object typeAdapter = new TreeTypeAdapter<>(serializer, deserializer, gsonMock, type, skipPastMock);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(type, typeAdapter);

    TypeAdapter<?> adapterFromFactory = factory.create(gsonMock, type);
    assertNotNull(adapterFromFactory);
  }

  @Test
    @Timeout(8000)
  void newTypeHierarchyFactory_shouldMatchHierarchyType() {
    Class<String> hierarchy = String.class;
    JsonSerializer<String> serializer = (src, typeOfSrc, context) -> new JsonPrimitive("ser");
    JsonDeserializer<String> deserializer = (json, typeOfT, context) -> "deser";

    Object typeAdapter = new TreeTypeAdapter<>(serializer, deserializer, gsonMock, TypeToken.get(String.class), skipPastMock);

    TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(hierarchy, typeAdapter);

    TypeAdapter<?> adapterFromFactory = factory.create(gsonMock, TypeToken.get(String.class));
    assertNotNull(adapterFromFactory);
  }
}
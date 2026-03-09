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
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TreeTypeAdapter_490_1Test {

  private Gson mockGson;
  private TypeToken<String> typeToken;
  private TypeAdapterFactory mockFactory;

  private JsonSerializer<String> mockSerializer;
  private JsonDeserializer<String> mockDeserializer;

  private TreeTypeAdapter<String> treeTypeAdapter;

  @BeforeEach
  public void setUp() {
    mockGson = mock(Gson.class);
    typeToken = TypeToken.get(String.class);
    mockFactory = mock(TypeAdapterFactory.class);

    mockSerializer = mock(JsonSerializer.class);
    mockDeserializer = mock(JsonDeserializer.class);

    treeTypeAdapter = new TreeTypeAdapter<>(mockSerializer, mockDeserializer, mockGson, typeToken, mockFactory, true);
  }

  @Test
    @Timeout(8000)
  public void testRead_usesDeserializer_whenDeserializerNotNull() throws Exception {
    JsonReader mockReader = mock(JsonReader.class);
    String expected = "deserializedValue";

    when(mockDeserializer.deserialize(any(JsonElement.class), eq(String.class), any(JsonDeserializationContext.class)))
        .thenReturn(expected);

    // Prepare JsonElement to pass to deserializer
    JsonElement jsonElement = JsonParser.parseString("\"test\"");
    // Use reflection to invoke private delegate() to get delegate adapter (which will be null initially)
    // Instead, we will spy on gson.getDelegateAdapter to return a delegate that throws if called to ensure deserializer is used

    when(mockGson.getDelegateAdapter(any(TypeAdapterFactory.class), eq(typeToken)))
        .thenReturn(mock(TypeAdapter.class));

    // Because Streams.parse(in) is called inside read, we must mock JsonReader behavior minimally
    // but it's complicated, so we use a real JsonReader on a StringReader instead

    JsonReader realReader = new JsonReader(new java.io.StringReader("\"test\""));

    String actual = treeTypeAdapter.read(realReader);

    assertEquals(expected, actual);
    verify(mockDeserializer).deserialize(any(JsonElement.class), eq(String.class), any(JsonDeserializationContext.class));
  }

  @Test
    @Timeout(8000)
  public void testRead_returnsNull_whenJsonNull() throws IOException {
    JsonReader reader = new JsonReader(new java.io.StringReader("null"));
    String result = treeTypeAdapter.read(reader);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testWrite_usesSerializer_whenSerializerNotNull() throws IOException {
    JsonWriter mockWriter = mock(JsonWriter.class);
    String value = "valueToSerialize";

    doAnswer(invocation -> {
      // The serialize method signature is:
      // serialize(T src, Type typeOfSrc, JsonSerializationContext context)
      // The third argument is JsonSerializationContext, NOT JsonWriter.
      // So we just verify serialize was called correctly without casting or accessing JsonWriter here.
      return null;
    }).when(mockSerializer).serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class));

    treeTypeAdapter.write(mockWriter, value);

    verify(mockSerializer).serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class));
  }

  @Test
    @Timeout(8000)
  public void testWrite_writesNull_whenValueIsNull() throws IOException {
    JsonWriter mockWriter = mock(JsonWriter.class);

    treeTypeAdapter.write(mockWriter, null);

    verify(mockWriter).nullValue();
  }

  @Test
    @Timeout(8000)
  public void testDelegate_invokesDelegateAdapter() throws Exception {
    // Create a delegate TypeAdapter mock
    @SuppressWarnings("unchecked")
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);

    // Use reflection to set private volatile delegate field to null to force delegate() to initialize it
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapter, null);

    // Mock gson.getDelegateAdapter to return our delegateAdapter
    when(mockGson.getDelegateAdapter(eq(mockFactory), eq(typeToken))).thenReturn(delegateAdapter);

    // Use reflection to invoke private delegate() method
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> returnedDelegate = (TypeAdapter<String>) delegateMethod.invoke(treeTypeAdapter);

    assertSame(delegateAdapter, returnedDelegate);
    // Second call returns cached delegate without calling getDelegateAdapter again
    TypeAdapter<String> secondCall = (TypeAdapter<String>) delegateMethod.invoke(treeTypeAdapter);
    assertSame(delegateAdapter, secondCall);

    verify(mockGson, times(1)).getDelegateAdapter(mockFactory, typeToken);
  }

  @Test
    @Timeout(8000)
  public void testGetSerializationDelegate_returnsDelegate() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);

    // Set serializer and deserializer to null to force getSerializationDelegate to return delegate
    Field serializerField = TreeTypeAdapter.class.getDeclaredField("serializer");
    serializerField.setAccessible(true);
    serializerField.set(treeTypeAdapter, null);

    Field deserializerField = TreeTypeAdapter.class.getDeclaredField("deserializer");
    deserializerField.setAccessible(true);
    deserializerField.set(treeTypeAdapter, null);

    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapter, delegateAdapter);

    TypeAdapter<String> serializationDelegate = treeTypeAdapter.getSerializationDelegate();

    assertSame(delegateAdapter, serializationDelegate);
  }

  @Test
    @Timeout(8000)
  public void testNewFactory_createsFactoryThatReturnsTreeTypeAdapter() throws IOException {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    JsonSerializer<String> serializer = mock(JsonSerializer.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(stringTypeToken, serializer);

    Gson gson = new Gson();

    TypeAdapter<String> adapter = factory.create(gson, stringTypeToken);

    assertNotNull(adapter);
    assertTrue(adapter instanceof TreeTypeAdapter);

    // The adapter should use the serializer passed
    // We can test write triggers serializer
    JsonWriter writer = new JsonWriter(new java.io.StringWriter());
    adapter.write(writer, "test");
  }

  @Test
    @Timeout(8000)
  public void testNewFactoryWithMatchRawType_createsFactory() {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(stringTypeToken, deserializer);

    Gson gson = new Gson();

    TypeAdapter<String> adapter = factory.create(gson, stringTypeToken);

    assertNotNull(adapter);
    assertTrue(adapter instanceof TreeTypeAdapter);
  }

  @Test
    @Timeout(8000)
  public void testNewTypeHierarchyFactory_createsFactory() {
    JsonSerializer<Object> serializer = mock(JsonSerializer.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(Object.class, serializer);

    Gson gson = new Gson();

    TypeAdapter<String> adapter = factory.create(gson, TypeToken.get(String.class));

    assertNotNull(adapter);
    assertTrue(adapter instanceof TreeTypeAdapter);
  }
}
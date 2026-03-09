package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
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
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

public class TreeTypeAdapter_489_3Test {

  private Gson gson;
  private TypeToken<String> typeToken;
  private TypeAdapterFactory skipPast;
  private JsonSerializer<String> serializer;
  private JsonDeserializer<String> deserializer;
  private TreeTypeAdapter<String> adapter;

  @BeforeEach
  public void setUp() {
    gson = mock(Gson.class);
    typeToken = TypeToken.get(String.class);
    skipPast = mock(TypeAdapterFactory.class);
    serializer = mock(JsonSerializer.class);
    deserializer = mock(JsonDeserializer.class);
    adapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, true);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_fieldsSet() throws Exception {
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
  public void testRead_withDeserializer() throws Exception {
    JsonReader in = new JsonReader(new StringReader("{}"));
    JsonElement jsonElement = mock(JsonElement.class);
    // Use an ArgumentMatcher to accept any JsonElement, since actual is not the mock
    when(deserializer.deserialize(argThat(new ArgumentMatcher<JsonElement>() {
      @Override
      public boolean matches(JsonElement argument) {
        return argument != null;
      }
    }), eq(String.class), any(JsonDeserializationContext.class))).thenReturn("deserialized");

    // Mock gson.fromJson to return a real JsonElement (not the mock)
    when(gson.fromJson(in, JsonElement.class)).thenAnswer(invocation -> {
      // Return a real JsonElement parsed from the input
      return com.google.gson.JsonParser.parseReader(in);
    });

    String result = adapter.read(in);
    assertEquals("deserialized", result);
    verify(deserializer).deserialize(any(JsonElement.class), eq(String.class), any(JsonDeserializationContext.class));
  }

  @Test
    @Timeout(8000)
  public void testRead_withDeserializerThrowsJsonParseException() throws Exception {
    JsonReader in = new JsonReader(new StringReader("{}"));
    JsonElement jsonElement = mock(JsonElement.class);
    when(gson.fromJson(in, JsonElement.class)).thenReturn(jsonElement);
    when(deserializer.deserialize(any(), any(), any()))
        .thenThrow(new JsonParseException("error"));

    assertThrows(JsonParseException.class, () -> adapter.read(in));
  }

  @Test
    @Timeout(8000)
  public void testRead_withoutDeserializer_delegateUsed() throws Exception {
    adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, true);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> delegateMock = mock(TypeAdapter.class);
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, delegateMock);

    JsonReader in = new JsonReader(new StringReader("\"value\""));
    when(delegateMock.read(in)).thenReturn("delegateValue");

    String result = adapter.read(in);
    assertEquals("delegateValue", result);
    verify(delegateMock).read(in);
  }

  @Test
    @Timeout(8000)
  public void testWrite_withSerializer() throws Exception {
    JsonWriter out = new JsonWriter(new StringWriter());
    String value = "value";

    // Instead of mocking JsonElement, use a real JsonElement instance to avoid serialization issues
    com.google.gson.JsonPrimitive jsonPrimitive = new com.google.gson.JsonPrimitive("serializedValue");

    when(serializer.serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class)))
        .thenReturn(jsonPrimitive);

    adapter.write(out, value);
    verify(serializer).serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class));
  }

  @Test
    @Timeout(8000)
  public void testWrite_withSerializerNullValue() throws Exception {
    // Create a new adapter with serializer but no deserializer to ensure serializer is used
    adapter = new TreeTypeAdapter<>(serializer, null, gson, typeToken, skipPast, true);

    JsonWriter out = new JsonWriter(new StringWriter());

    com.google.gson.JsonPrimitive jsonPrimitive = new com.google.gson.JsonPrimitive("serializedNullValue");

    when(serializer.serialize(isNull(), eq(typeToken.getType()), any(JsonSerializationContext.class)))
        .thenReturn(jsonPrimitive);

    adapter.write(out, null);
    verify(serializer).serialize(isNull(), eq(typeToken.getType()), any(JsonSerializationContext.class));
  }

  @Test
    @Timeout(8000)
  public void testWrite_withoutSerializer_delegateUsed() throws Exception {
    adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, true);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> delegateMock = mock(TypeAdapter.class);
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, delegateMock);

    JsonWriter out = new JsonWriter(new StringWriter());
    String value = "value";

    adapter.write(out, value);
    verify(delegateMock).write(out, value);
  }

  @Test
    @Timeout(8000)
  public void testDelegate_invokesGsonGetDelegate() throws Exception {
    adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, true);

    // Clear delegate field to force delegate() call
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, null);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> expectedDelegate = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(skipPast, typeToken)).thenReturn(expectedDelegate);

    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> actualDelegate = (TypeAdapter<String>) delegateMethod.invoke(adapter);

    assertSame(expectedDelegate, actualDelegate);
    // Subsequent call returns same delegate without calling gson.getDelegateAdapter again
    TypeAdapter<String> secondCall = (TypeAdapter<String>) delegateMethod.invoke(adapter);
    assertSame(expectedDelegate, secondCall);
    verify(gson, times(1)).getDelegateAdapter(skipPast, typeToken);
  }

  @Test
    @Timeout(8000)
  public void testGetSerializationDelegate_returnsDelegate() {
    adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, true);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> delegateMock = mock(TypeAdapter.class);
    try {
      Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
      delegateField.setAccessible(true);
      delegateField.set(adapter, delegateMock);
    } catch (Exception e) {
      fail(e);
    }
    assertSame(delegateMock, adapter.getSerializationDelegate());
  }

  @Test
    @Timeout(8000)
  public void testNewFactory_exactType_returnsFactory() {
    TypeToken<String> exactType = TypeToken.get(String.class);
    JsonSerializer<String> typeAdapter = mock(JsonSerializer.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(exactType, typeAdapter);
    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  public void testNewFactoryWithMatchRawType_returnsFactory() {
    TypeToken<String> exactType = TypeToken.get(String.class);
    JsonSerializer<String> typeAdapter = mock(JsonSerializer.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(exactType, typeAdapter);
    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  public void testNewTypeHierarchyFactory_returnsFactory() {
    JsonSerializer<String> typeAdapter = mock(JsonSerializer.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(String.class, typeAdapter);
    assertNotNull(factory);
  }
}
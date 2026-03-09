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
import java.lang.reflect.Type;

public class TreeTypeAdapter_489_5Test {

  private JsonSerializer<String> serializer;
  private JsonDeserializer<String> deserializer;
  private Gson gson;
  private TypeToken<String> typeToken;
  private TypeAdapterFactory skipPast;
  private TreeTypeAdapter<String> adapter;

  @BeforeEach
  public void setUp() {
    serializer = mock(JsonSerializer.class);
    deserializer = mock(JsonDeserializer.class);
    gson = mock(Gson.class);
    typeToken = TypeToken.get(String.class);
    skipPast = mock(TypeAdapterFactory.class);
    adapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, true);
  }

  @Test
    @Timeout(8000)
  public void testRead_withNonNullJsonElement_callsDeserializer() throws Exception {
    JsonReader in = mock(JsonReader.class);
    JsonElement element = JsonParser.parseString("\"test\"");
    when(gson.getAdapter(JsonElement.class)).thenReturn(new TypeAdapter<JsonElement>() {
      @Override
      public void write(JsonWriter out, JsonElement value) throws IOException {
      }

      @Override
      public JsonElement read(JsonReader in) throws IOException {
        return element;
      }
    });
    when(deserializer.deserialize(eq(element), eq(typeToken.getType()), any(JsonDeserializationContext.class)))
        .thenReturn("deserialized");

    String result = adapter.read(in);

    assertEquals("deserialized", result);
    verify(deserializer).deserialize(eq(element), eq(typeToken.getType()), any(JsonDeserializationContext.class));
  }

  @Test
    @Timeout(8000)
  public void testRead_withNullJsonElement_returnsNull() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(gson.getAdapter(JsonElement.class)).thenReturn(new TypeAdapter<JsonElement>() {
      @Override
      public void write(JsonWriter out, JsonElement value) throws IOException {
      }

      @Override
      public JsonElement read(JsonReader in) throws IOException {
        return JsonNull.INSTANCE;
      }
    });

    String result = adapter.read(in);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testRead_deserializerThrowsJsonParseException_wrapped() throws IOException {
    JsonReader in = mock(JsonReader.class);
    JsonElement element = JsonParser.parseString("\"test\"");
    when(gson.getAdapter(JsonElement.class)).thenReturn(new TypeAdapter<JsonElement>() {
      @Override
      public void write(JsonWriter out, JsonElement value) throws IOException {
      }

      @Override
      public JsonElement read(JsonReader in) throws IOException {
        return element;
      }
    });
    when(deserializer.deserialize(eq(element), eq(typeToken.getType()), any(JsonDeserializationContext.class)))
        .thenThrow(new JsonParseException("parse error"));

    assertThrows(JsonParseException.class, () -> adapter.read(in));
  }

  @Test
    @Timeout(8000)
  public void testWrite_withSerializer_serializesJsonElement() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    String value = "value";
    JsonElement element = JsonParser.parseString("\"json\"");
    when(serializer.serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class)))
        .thenReturn(element);

    // Spy adapter to verify serializer is called
    TreeTypeAdapter<String> spyAdapter = Mockito.spy(adapter);
    spyAdapter.write(out, value);

    verify(serializer).serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class));
  }

  @Test
    @Timeout(8000)
  public void testWrite_withoutSerializer_delegates() throws IOException {
    TreeTypeAdapter<String> adapterNoSerializer = new TreeTypeAdapter<>(null, deserializer, gson, typeToken, skipPast, true);
    JsonWriter out = mock(JsonWriter.class);
    String value = "value";

    // Setup delegate TypeAdapter to verify delegation
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    // Inject delegate via reflection
    setPrivateField(adapterNoSerializer, "delegate", delegateAdapter);

    adapterNoSerializer.write(out, value);

    verify(delegateAdapter).write(out, value);
  }

  @Test
    @Timeout(8000)
  public void testDelegate_invokesGsonGetDelegate() throws Exception {
    // Prepare a delegate TypeAdapter to be returned by gson.getDelegateAdapter
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(skipPast, typeToken)).thenReturn(delegateAdapter);

    // Clear delegate to force delegate() call
    setPrivateField(adapter, "delegate", null);

    TypeAdapter<String> returnedDelegate = invokeDelegate(adapter);

    assertSame(delegateAdapter, returnedDelegate);
    // The delegate field is cached
    TypeAdapter<String> cachedDelegate = (TypeAdapter<String>) getPrivateField(adapter, "delegate");
    assertSame(delegateAdapter, cachedDelegate);
  }

  @Test
    @Timeout(8000)
  public void testGetSerializationDelegate_returnsDelegate() throws Exception {
    // Setup delegate adapter to a mock
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    setPrivateField(adapter, "delegate", delegateAdapter);

    // Because getSerializationDelegate() returns delegate if serializer != null,
    // but if serializer == null, it returns 'this' (TreeTypeAdapter).
    // So ensure serializer is null to test correct behavior.
    setPrivateField(adapter, "serializer", null);

    TypeAdapter<String> result = adapter.getSerializationDelegate();

    assertSame(delegateAdapter, result);
  }

  @Test
    @Timeout(8000)
  public void testGetSerializationDelegate_withSerializer_returnsThis() throws Exception {
    // serializer is non-null (default)
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    setPrivateField(adapter, "delegate", delegateAdapter);

    TypeAdapter<String> result = adapter.getSerializationDelegate();

    assertSame(adapter, result);
  }

  @Test
    @Timeout(8000)
  public void testNewFactory_returnsFactory_forExactType() {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    JsonSerializer<String> ser = mock(JsonSerializer.class);
    JsonDeserializer<String> deser = mock(JsonDeserializer.class);
    TreeTypeAdapter<String> typeAdapter = new TreeTypeAdapter<>(ser, deser, gson, stringTypeToken, skipPast, true);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(stringTypeToken, typeAdapter);
    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  public void testNewFactoryWithMatchRawType_returnsFactory() {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    JsonSerializer<String> ser = mock(JsonSerializer.class);
    JsonDeserializer<String> deser = mock(JsonDeserializer.class);
    TreeTypeAdapter<String> typeAdapter = new TreeTypeAdapter<>(ser, deser, gson, stringTypeToken, skipPast, true);

    // Fix: Use a raw Class type directly instead of TypeToken.getRawType()
    Class<String> rawClass = String.class;
    TypeToken<?> rawTypeToken = TypeToken.get(rawClass);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(rawTypeToken, typeAdapter);
    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  public void testNewTypeHierarchyFactory_returnsFactory() {
    JsonSerializer<String> ser = mock(JsonSerializer.class);
    JsonDeserializer<String> deser = mock(JsonDeserializer.class);
    TreeTypeAdapter<String> typeAdapter = new TreeTypeAdapter<>(ser, deser, gson, typeToken, skipPast, true);

    // Fix: Pass a class that is assignable from String.class (String.class itself is fine)
    Class<String> clazz = String.class;
    TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(clazz, typeAdapter);
    assertNotNull(factory);
  }

  // Helper to set private field via reflection
  private void setPrivateField(Object target, String fieldName, Object value) {
    try {
      Field field = TreeTypeAdapter.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to get private field via reflection
  private Object getPrivateField(Object target, String fieldName) {
    try {
      Field field = TreeTypeAdapter.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to invoke private delegate() method via reflection
  @SuppressWarnings("unchecked")
  private TypeAdapter<String> invokeDelegate(TreeTypeAdapter<String> adapter) throws Exception {
    Method method = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    method.setAccessible(true);
    return (TypeAdapter<String>) method.invoke(adapter);
  }
}
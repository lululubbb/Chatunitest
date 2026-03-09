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
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.BooleanSupplier;

class TreeTypeAdapter_490_2Test {

  private Gson gson;
  private TypeToken<String> typeToken;
  private JsonSerializer<String> serializer;
  private JsonDeserializer<String> deserializer;
  private TypeAdapterFactory skipPast;
  private TreeTypeAdapter<String> adapter;

  @BeforeEach
  void setUp() {
    gson = mock(Gson.class);
    serializer = mock(JsonSerializer.class);
    deserializer = mock(JsonDeserializer.class);
    skipPast = mock(TypeAdapterFactory.class);
    typeToken = TypeToken.get(String.class);
  }

  @Test
    @Timeout(8000)
  void constructor_and_nullSafe_true() {
    adapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast);
    assertNotNull(adapter);
    // nullSafe should be true by default constructor
    Object nullSafeField = getPrivateField(adapter, "nullSafe");
    assertTrue(resolveNullSafeValue(nullSafeField));
  }

  @Test
    @Timeout(8000)
  void constructor_and_nullSafe_false() {
    adapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, false);
    assertNotNull(adapter);
    Object nullSafeField = getPrivateField(adapter, "nullSafe");
    assertFalse(resolveNullSafeValue(nullSafeField));
  }

  @Test
    @Timeout(8000)
  void read_withDeserializer_returnsDeserializedValue() throws Exception {
    adapter = new TreeTypeAdapter<>(null, deserializer, gson, typeToken, skipPast, true);

    JsonReader in = new JsonReader(new StringReader("\"test\""));
    JsonElement jsonElement = JsonParser.parseString("\"test\"");
    when(deserializer.deserialize(eq(jsonElement), eq(String.class), any(JsonDeserializationContext.class)))
        .thenReturn("mocked");

    // Mock gson.fromJson to return JsonElement for read delegate
    @SuppressWarnings("unchecked")
    TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);
    when(jsonElementAdapter.read(in)).thenReturn(jsonElement);

    String result = adapter.read(in);
    assertEquals("mocked", result);
  }

  @Test
    @Timeout(8000)
  void read_withDeserializer_nullJsonToken_returnsNull() throws IOException {
    adapter = new TreeTypeAdapter<>(null, deserializer, gson, typeToken, skipPast, true);

    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    // Because read(JsonReader) calls delegate() which calls gson.getDelegateAdapter,
    // mock delegate adapter to avoid errors
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(skipPast, typeToken)).thenReturn(delegateAdapter);

    // Since peek returns NULL, read should consume null and return null
    when(delegateAdapter.read(in)).thenReturn(null);

    String result = adapter.read(in);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void write_withSerializer_writesSerializedJson() throws IOException {
    adapter = new TreeTypeAdapter<>(serializer, null, gson, typeToken, skipPast, true);

    StringWriter stringWriter = new StringWriter();
    JsonWriter out = new JsonWriter(stringWriter);
    String value = "value";

    JsonElement jsonElement = JsonParser.parseString("\"serialized\"");
    when(serializer.serialize(eq(value), eq(String.class), any(JsonSerializationContext.class)))
        .thenReturn(jsonElement);

    // Mock gson.getAdapter(JsonElement.class) to return a TypeAdapter that writes the JsonElement
    @SuppressWarnings("unchecked")
    TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
    doAnswer(invocation -> {
      JsonWriter writer = invocation.getArgument(1);
      JsonElement element = invocation.getArgument(0);
      // Write the JsonElement using Streams.write to properly serialize the element
      Streams.write(element, writer);
      return null;
    }).when(jsonElementAdapter).write(any(), any());
    when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

    adapter.write(out, value);
    out.flush();
    assertEquals("\"serialized\"", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void write_withSerializer_nullValue_writesNull() throws IOException {
    adapter = new TreeTypeAdapter<>(serializer, null, gson, typeToken, skipPast, true);

    StringWriter stringWriter = new StringWriter();
    JsonWriter out = new JsonWriter(stringWriter);

    adapter.write(out, null);
    out.flush();
    assertEquals("null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void write_withoutSerializer_delegatesWrite() throws IOException {
    adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, true);

    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(skipPast, typeToken)).thenReturn(delegateAdapter);

    StringWriter stringWriter = new StringWriter();
    JsonWriter out = new JsonWriter(stringWriter);
    String value = "val";

    adapter.write(out, value);

    verify(delegateAdapter).write(out, value);
  }

  @Test
    @Timeout(8000)
  void delegate_invokesAndCachesDelegateAdapter() throws Exception {
    adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, true);

    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(skipPast, typeToken)).thenReturn(delegateAdapter);

    // delegate() first call - should call gson.getDelegateAdapter
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);
    TypeAdapter<String> firstDelegate = (TypeAdapter<String>) delegateMethod.invoke(adapter);
    assertSame(delegateAdapter, firstDelegate);

    // delegate() second call - should return cached delegate without calling gson again
    TypeAdapter<String> secondDelegate = (TypeAdapter<String>) delegateMethod.invoke(adapter);
    assertSame(delegateAdapter, secondDelegate);

    verify(gson, times(1)).getDelegateAdapter(skipPast, typeToken);
  }

  @Test
    @Timeout(8000)
  void getSerializationDelegate_returnsDelegate() {
    adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, true);
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    setPrivateField(adapter, "delegate", delegateAdapter);

    TypeAdapter<String> result = adapter.getSerializationDelegate();
    assertSame(delegateAdapter, result);
  }

  @Test
    @Timeout(8000)
  void newFactory_returnsFactory() {
    TypeToken<String> exactType = TypeToken.get(String.class);
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(exactType, serializer);
    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_returnsFactory() {
    TypeToken<String> exactType = TypeToken.get(String.class);
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(exactType, serializer);
    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  void newTypeHierarchyFactory_returnsFactory() {
    Class<?> hierarchyType = String.class;
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(hierarchyType, serializer);
    assertNotNull(factory);
  }

  // Helper to access private fields via reflection
  private <V> V getPrivateField(Object instance, String fieldName) {
    try {
      Field field = instance.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return (V) field.get(instance);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setPrivateField(Object instance, String fieldName, Object value) {
    try {
      Field field = instance.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(instance, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private boolean resolveNullSafeValue(Object nullSafeField) {
    if (nullSafeField instanceof Boolean) {
      return (Boolean) nullSafeField;
    }
    if (nullSafeField instanceof BooleanSupplier) {
      return ((BooleanSupplier) nullSafeField).getAsBoolean();
    }
    throw new IllegalStateException("Unexpected type for nullSafe field: " + nullSafeField.getClass());
  }
}
package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TreeTypeAdapter_490_5Test {

  private Gson mockGson;
  private TypeToken<String> typeToken;
  private TypeAdapterFactory mockFactory;

  @BeforeEach
  void setUp() {
    mockGson = mock(Gson.class);
    typeToken = TypeToken.get(String.class);
    mockFactory = mock(TypeAdapterFactory.class);
  }

  @Test
    @Timeout(8000)
  void constructor_defaultNullSafe_true() {
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);

    TreeTypeAdapter<String> adapter =
        new TreeTypeAdapter<>(serializer, deserializer, mockGson, typeToken, mockFactory);

    assertNotNull(adapter);
    // Check nullSafe field true via reflection
    boolean nullSafe = false;
    try {
      Field nullSafeField = TreeTypeAdapter.class.getDeclaredField("nullSafe");
      nullSafeField.setAccessible(true);
      nullSafe = nullSafeField.getBoolean(adapter);
    } catch (Exception e) {
      fail(e);
    }
    assertTrue(nullSafe);
  }

  @Test
    @Timeout(8000)
  void constructor_withNullSafe_false() {
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);

    TreeTypeAdapter<String> adapter =
        new TreeTypeAdapter<>(serializer, deserializer, mockGson, typeToken, mockFactory, false);

    assertNotNull(adapter);
    // Check nullSafe field false via reflection
    boolean nullSafe = true;
    try {
      Field nullSafeField = TreeTypeAdapter.class.getDeclaredField("nullSafe");
      nullSafeField.setAccessible(true);
      nullSafe = nullSafeField.getBoolean(adapter);
    } catch (Exception e) {
      fail(e);
    }
    assertFalse(nullSafe);
  }

  @Test
    @Timeout(8000)
  void read_withDeserializer_invokesDeserializer() throws Exception {
    @SuppressWarnings("unchecked")
    JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
    JsonSerializer<String> serializer = null;

    TreeTypeAdapter<String> adapter =
        new TreeTypeAdapter<>(serializer, deserializer, mockGson, typeToken, mockFactory);

    // Mock gson.getDelegateAdapter to return a mock TypeAdapter to avoid NPE inside delegate()
    @SuppressWarnings("unchecked")
    TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);
    when(mockGson.getDelegateAdapter(mockFactory, typeToken)).thenReturn(mockDelegate);

    // Set delegate field to mockDelegate to avoid null delegate usage inside read()
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, mockDelegate);

    JsonReader jsonReader = mock(JsonReader.class);
    // Mock jsonReader behavior to avoid NPE in delegate read
    when(jsonReader.peek()).thenReturn(com.google.gson.stream.JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn("dummy");

    String expected = "deserialized";
    when(deserializer.deserialize(any(), any(), any())).thenReturn(expected);

    String result = adapter.read(jsonReader);

    assertEquals(expected, result);
    verify(deserializer).deserialize(any(), any(), any());
  }

  @Test
    @Timeout(8000)
  void read_withoutDeserializer_delegates() throws Exception {
    JsonSerializer<String> serializer = null;
    JsonDeserializer<String> deserializer = null;
    TreeTypeAdapter<String> adapter =
        new TreeTypeAdapter<>(serializer, deserializer, mockGson, typeToken, mockFactory);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, mockDelegate);

    JsonReader jsonReader = mock(JsonReader.class);
    String expected = "delegateRead";
    when(mockDelegate.read(jsonReader)).thenReturn(expected);

    String result = adapter.read(jsonReader);

    assertEquals(expected, result);
    verify(mockDelegate).read(jsonReader);
  }

  @Test
    @Timeout(8000)
  void write_withSerializer_invokesSerializer() throws Exception {
    @SuppressWarnings("unchecked")
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    JsonDeserializer<String> deserializer = null;
    TreeTypeAdapter<String> adapter =
        new TreeTypeAdapter<>(serializer, deserializer, mockGson, typeToken, mockFactory);

    JsonWriter jsonWriter = mock(JsonWriter.class);
    String value = "value";

    adapter.write(jsonWriter, value);

    verify(serializer).serialize(eq(value), any(), any());
  }

  @Test
    @Timeout(8000)
  void write_withoutSerializer_delegates() throws Exception {
    JsonSerializer<String> serializer = null;
    JsonDeserializer<String> deserializer = null;
    TreeTypeAdapter<String> adapter =
        new TreeTypeAdapter<>(serializer, deserializer, mockGson, typeToken, mockFactory);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, mockDelegate);

    JsonWriter jsonWriter = mock(JsonWriter.class);
    String value = "value";

    adapter.write(jsonWriter, value);

    verify(mockDelegate).write(jsonWriter, value);
  }

  @Test
    @Timeout(8000)
  void delegate_invokesGetDelegateAndCaches() throws Exception {
    JsonSerializer<String> serializer = null;
    JsonDeserializer<String> deserializer = null;
    TreeTypeAdapter<String> adapter =
        new TreeTypeAdapter<>(serializer, deserializer, mockGson, typeToken, mockFactory);

    // Initially delegate field is null
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, null);

    // Use reflection to invoke private delegate() method
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    // Mock gson.getDelegateAdapter to return a mock TypeAdapter
    @SuppressWarnings("unchecked")
    TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);
    when(mockGson.getDelegateAdapter(mockFactory, typeToken)).thenReturn(mockDelegate);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> delegateResult = (TypeAdapter<String>) delegateMethod.invoke(adapter);

    assertNotNull(delegateResult);
    assertSame(mockDelegate, delegateResult);

    // Confirm delegate field is cached
    TypeAdapter<String> cachedDelegate = (TypeAdapter<String>) delegateField.get(adapter);
    assertSame(mockDelegate, cachedDelegate);
  }

  @Test
    @Timeout(8000)
  void getSerializationDelegate_returnsDelegate() {
    JsonSerializer<String> serializer = null;
    JsonDeserializer<String> deserializer = null;
    TreeTypeAdapter<String> adapter =
        new TreeTypeAdapter<>(serializer, deserializer, mockGson, typeToken, mockFactory);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);
    try {
      Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
      delegateField.setAccessible(true);
      delegateField.set(adapter, mockDelegate);
    } catch (Exception e) {
      fail(e);
    }

    TypeAdapter<String> serializationDelegate = adapter.getSerializationDelegate();
    assertSame(mockDelegate, serializationDelegate);
  }

  @Test
    @Timeout(8000)
  void newFactory_returnsFactory() {
    TypeToken<String> token = TypeToken.get(String.class);
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(token, serializer);
    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_returnsFactory() {
    TypeToken<String> token = TypeToken.get(String.class);
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(token, serializer);
    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  void newTypeHierarchyFactory_returnsFactory() {
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    TypeAdapterFactory factory =
        TreeTypeAdapter.newTypeHierarchyFactory(String.class, serializer);
    assertNotNull(factory);
  }

  private Object getContext(TreeTypeAdapter<?> adapter) throws Exception {
    Field contextField = TreeTypeAdapter.class.getDeclaredField("context");
    contextField.setAccessible(true);
    return contextField.get(adapter);
  }
}
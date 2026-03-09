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
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TreeTypeAdapter_489_6Test {

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
  public void testRead_withDeserializer_returnsDeserializedValue() throws Exception {
    JsonReader in = mock(JsonReader.class);
    JsonElement jsonElement = mock(JsonElement.class);
    when(gson.getAdapter(JsonElement.class)).thenReturn(mock(TypeAdapter.class));
    when(gson.fromJson(in, JsonElement.class)).thenReturn(jsonElement);
    when(deserializer.deserialize(eq(jsonElement), eq(typeToken.getType()), any()))
        .thenReturn("deserialized");

    String result = adapter.read(in);

    assertEquals("deserialized", result);
  }

  @Test
    @Timeout(8000)
  public void testRead_withNullJsonElement_returnsNull() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(gson.fromJson(in, JsonElement.class)).thenReturn(null);

    String result = adapter.read(in);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testRead_withDeserializerThrows_throwsJsonParseException() throws Exception {
    JsonReader in = mock(JsonReader.class);
    JsonElement jsonElement = mock(JsonElement.class);
    when(gson.fromJson(in, JsonElement.class)).thenReturn(jsonElement);
    when(deserializer.deserialize(eq(jsonElement), eq(typeToken.getType()), any()))
        .thenThrow(new JsonParseException("error"));

    assertThrows(JsonParseException.class, () -> adapter.read(in));
  }

  @Test
    @Timeout(8000)
  public void testWrite_withSerializer_writesJson() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    String value = "value";
    JsonElement jsonElement = mock(JsonElement.class);
    when(serializer.serialize(eq(value), eq(typeToken.getType()), any())).thenReturn(jsonElement);
    TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

    adapter.write(out, value);

    verify(jsonElementAdapter).write(out, jsonElement);
  }

  @Test
    @Timeout(8000)
  public void testWrite_withNullValue_writesNull() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

    adapter.write(out, null);

    verify(jsonElementAdapter).write(out, null);
  }

  @Test
    @Timeout(8000)
  public void testWrite_withoutSerializer_usesDelegate() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    TreeTypeAdapter<String> adapterNoSerializer = new TreeTypeAdapter<>(null, deserializer, gson, typeToken, skipPast, true);

    TypeAdapter<String> delegate = mock(TypeAdapter.class);
    setDelegate(adapterNoSerializer, delegate);

    adapterNoSerializer.write(out, "value");

    verify(delegate).write(out, "value");
  }

  @Test
    @Timeout(8000)
  public void testRead_withoutDeserializer_usesDelegate() throws Exception {
    JsonReader in = mock(JsonReader.class);
    TreeTypeAdapter<String> adapterNoDeserializer = new TreeTypeAdapter<>(serializer, null, gson, typeToken, skipPast, true);

    TypeAdapter<String> delegate = mock(TypeAdapter.class);
    setDelegate(adapterNoDeserializer, delegate);
    when(delegate.read(in)).thenReturn("delegateValue");

    String result = adapterNoDeserializer.read(in);

    assertEquals("delegateValue", result);
  }

  @Test
    @Timeout(8000)
  public void testDelegate_method_initializesDelegateOnce() throws Exception {
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    TypeAdapter<String> first = (TypeAdapter<String>) delegateMethod.invoke(adapter);
    TypeAdapter<String> second = (TypeAdapter<String>) delegateMethod.invoke(adapter);

    assertSame(first, second);
  }

  @Test
    @Timeout(8000)
  public void testGetSerializationDelegate_returnsDelegate() {
    TypeAdapter<String> delegate = mock(TypeAdapter.class);
    setDelegate(adapter, delegate);

    TypeAdapter<String> result = adapter.getSerializationDelegate();

    assertSame(delegate, result);
  }

  @Test
    @Timeout(8000)
  public void testNewFactory_returnsFactory() {
    Object typeAdapter = new Object();
    TypeToken<String> exactType = TypeToken.get(String.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(exactType, typeAdapter);

    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  public void testNewFactoryWithMatchRawType_returnsFactory() {
    Object typeAdapter = new Object();
    TypeToken<String> exactType = TypeToken.get(String.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(exactType, typeAdapter);

    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  public void testNewTypeHierarchyFactory_returnsFactory() {
    Object typeAdapter = new Object();

    TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(String.class, typeAdapter);

    assertNotNull(factory);
  }

  private void setDelegate(TreeTypeAdapter<String> adapter, TypeAdapter<String> delegate) {
    try {
      Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
      delegateField.setAccessible(true);
      delegateField.set(adapter, delegate);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
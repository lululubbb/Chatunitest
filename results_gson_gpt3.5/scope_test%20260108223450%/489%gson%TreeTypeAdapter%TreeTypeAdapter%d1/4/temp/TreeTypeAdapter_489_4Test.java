package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
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

class TreeTypeAdapter_489_4Test {

  private JsonSerializer<String> serializer;
  private JsonDeserializer<String> deserializer;
  private Gson gson;
  private TypeToken<String> typeToken;
  private TypeAdapterFactory skipPast;
  private JsonReader jsonReader;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    serializer = mock(JsonSerializer.class);
    deserializer = mock(JsonDeserializer.class);
    gson = mock(Gson.class);
    typeToken = TypeToken.get(String.class);
    skipPast = mock(TypeAdapterFactory.class);
    jsonReader = mock(JsonReader.class);
    jsonWriter = mock(JsonWriter.class);
  }

  @Test
    @Timeout(8000)
  void constructor_and_fields() throws Exception {
    TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, true);

    // Using reflection to check fields
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
  void read_withDeserializer_returnsDeserializedValue() throws Exception {
    TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(null, deserializer, gson, typeToken, skipPast, false);

    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonReader.peek()).thenReturn(com.google.gson.stream.JsonToken.STRING);
    when(Streams.parse(jsonReader)).thenReturn(jsonElement);
    when(deserializer.deserialize(eq(jsonElement), eq(String.class), any())).thenReturn("deserializedValue");

    String result = adapter.read(jsonReader);
    assertEquals("deserializedValue", result);
  }

  @Test
    @Timeout(8000)
  void read_withDeserializer_throwsJsonParseException_onException() throws Exception {
    TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(null, deserializer, gson, typeToken, skipPast, false);

    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonReader.peek()).thenReturn(com.google.gson.stream.JsonToken.STRING);
    when(Streams.parse(jsonReader)).thenReturn(jsonElement);
    when(deserializer.deserialize(eq(jsonElement), eq(String.class), any())).thenThrow(new RuntimeException("fail"));

    JsonParseException ex = assertThrows(JsonParseException.class, () -> adapter.read(jsonReader));
    assertTrue(ex.getMessage().contains("fail"));
  }

  @Test
    @Timeout(8000)
  void read_withNullDeserializer_delegates() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, false);

    // Inject delegate via reflection
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, delegateAdapter);

    when(delegateAdapter.read(jsonReader)).thenReturn("delegateValue");

    String result = adapter.read(jsonReader);
    assertEquals("delegateValue", result);
  }

  @Test
    @Timeout(8000)
  void write_withSerializer_writesSerializedValue() throws Exception {
    TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(serializer, null, gson, typeToken, skipPast, false);

    JsonElement jsonElement = mock(JsonElement.class);
    when(serializer.serialize(eq("value"), eq(String.class), any())).thenReturn(jsonElement);

    // Mock JsonSerializationContext inside adapter.context
    // The context is private final GsonContextImpl; we rely on gson context passed to serialize
    doAnswer(invocation -> {
      JsonWriter writer = invocation.getArgument(1);
      // simulate writing JsonElement
      return null;
    }).when(jsonElement).writeTo(any());

    adapter.write(jsonWriter, "value");

    // verify serializer.serialize called
    verify(serializer).serialize(eq("value"), eq(String.class), any(JsonSerializationContext.class));
  }

  @Test
    @Timeout(8000)
  void write_withNullSerializer_delegates() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, false);

    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, delegateAdapter);

    adapter.write(jsonWriter, "value");

    verify(delegateAdapter).write(jsonWriter, "value");
  }

  @Test
    @Timeout(8000)
  void delegate_invokesDelegateFactory() throws Exception {
    TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, false);

    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    assertNull(delegateField.get(adapter));

    @SuppressWarnings("unchecked")
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(skipPast.create(eq(gson), eq(typeToken))).thenReturn(delegateAdapter);

    // invoke private delegate() method via reflection
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);
    TypeAdapter<String> returnedDelegate = (TypeAdapter<String>) delegateMethod.invoke(adapter);

    assertSame(delegateAdapter, returnedDelegate);
    assertSame(delegateAdapter, delegateField.get(adapter));
  }

  @Test
    @Timeout(8000)
  void getSerializationDelegate_returnsDelegate() throws Exception {
    TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, false);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, delegateAdapter);

    TypeAdapter<String> result = adapter.getSerializationDelegate();
    assertSame(delegateAdapter, result);
  }

  @Test
    @Timeout(8000)
  void newFactory_createsFactory_andCreatesAdapter() throws Exception {
    TypeToken<String> stringType = TypeToken.get(String.class);
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(stringType, serializer);

    Gson gson = mock(Gson.class);
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(any(), eq(stringType))).thenReturn(delegateAdapter);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> adapter = (TypeAdapter<String>) factory.create(gson, stringType);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void newFactoryWithMatchRawType_createsFactory_andCreatesAdapter() throws Exception {
    TypeToken<String> stringType = TypeToken.get(String.class);
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(stringType, serializer);

    Gson gson = mock(Gson.class);
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(any(), eq(stringType))).thenReturn(delegateAdapter);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> adapter = (TypeAdapter<String>) factory.create(gson, stringType);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void newTypeHierarchyFactory_createsFactory_andCreatesAdapter() throws Exception {
    JsonSerializer<String> serializer = mock(JsonSerializer.class);
    TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(String.class, serializer);

    Gson gson = mock(Gson.class);
    TypeToken<String> stringType = TypeToken.get(String.class);
    TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(any(), eq(stringType))).thenReturn(delegateAdapter);

    @SuppressWarnings("unchecked")
    TypeAdapter<String> adapter = (TypeAdapter<String>) factory.create(gson, stringType);
    assertNotNull(adapter);
  }
}
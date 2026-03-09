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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TreeTypeAdapter_490_3Test {

  private Gson gsonMock;
  private JsonSerializer<String> serializerMock;
  private JsonDeserializer<String> deserializerMock;
  private TypeToken<String> typeToken;
  private TypeAdapterFactory skipPastMock;
  private TreeTypeAdapter<String> treeTypeAdapter;

  @BeforeEach
  public void setUp() {
    gsonMock = mock(Gson.class);
    serializerMock = mock(JsonSerializer.class);
    deserializerMock = mock(JsonDeserializer.class);
    skipPastMock = mock(TypeAdapterFactory.class);
    typeToken = TypeToken.get(String.class);
  }

  @Test
    @Timeout(8000)
  public void testConstructorAndNullSafeTrue_DefaultConstructor() {
    treeTypeAdapter = new TreeTypeAdapter<>(serializerMock, deserializerMock, gsonMock, typeToken, skipPastMock);

    assertNotNull(treeTypeAdapter);
    // Use reflection to check nullSafe field is true
    try {
      Field nullSafeField = TreeTypeAdapter.class.getDeclaredField("nullSafe");
      nullSafeField.setAccessible(true);
      assertTrue(nullSafeField.getBoolean(treeTypeAdapter));
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
    @Timeout(8000)
  public void testConstructorAndNullSafeFalse() {
    treeTypeAdapter = new TreeTypeAdapter<>(serializerMock, deserializerMock, gsonMock, typeToken, skipPastMock, false);

    assertNotNull(treeTypeAdapter);
    try {
      Field nullSafeField = TreeTypeAdapter.class.getDeclaredField("nullSafe");
      nullSafeField.setAccessible(true);
      assertFalse(nullSafeField.getBoolean(treeTypeAdapter));
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
    @Timeout(8000)
  public void testRead_UsesDeserializer() throws Exception {
    treeTypeAdapter = new TreeTypeAdapter<>(null, deserializerMock, gsonMock, typeToken, skipPastMock);

    // Mock delegate TypeAdapter to avoid real parsing and static calls
    TypeAdapter<String> delegateMock = mock(TypeAdapter.class);
    setDelegate(treeTypeAdapter, delegateMock);

    // Create JsonReader from valid JSON string
    JsonReader jsonReader = new JsonReader(new java.io.StringReader("\"jsonString\""));

    // Mock deserializer to return expected value
    String expected = "deserializedValue";
    when(deserializerMock.deserialize(any(JsonElement.class), eq(typeToken.getType()), any(JsonDeserializationContext.class)))
      .thenReturn(expected);

    String actual = treeTypeAdapter.read(jsonReader);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  public void testRead_NoDeserializer_UsesDelegate() throws Exception {
    treeTypeAdapter = new TreeTypeAdapter<>(serializerMock, null, gsonMock, typeToken, skipPastMock);
    TypeAdapter<String> delegateMock = mock(TypeAdapter.class);
    setDelegate(treeTypeAdapter, delegateMock);

    JsonReader jsonReader = new JsonReader(new java.io.StringReader("\"value\""));
    String expected = "delegateValue";
    when(delegateMock.read(any(JsonReader.class))).thenReturn(expected);

    String actual = treeTypeAdapter.read(jsonReader);
    assertEquals(expected, actual);
    verify(delegateMock).read(any(JsonReader.class));
  }

  @Test
    @Timeout(8000)
  public void testWrite_UsesSerializer() throws Exception {
    treeTypeAdapter = new TreeTypeAdapter<>(serializerMock, null, gsonMock, typeToken, skipPastMock);
    JsonWriter jsonWriter = new JsonWriter(new java.io.StringWriter());
    String value = "value";

    JsonSerializationContext context = getContext(treeTypeAdapter);
    JsonElement jsonElement = JsonParser.parseString("\"serializedValue\"");
    when(serializerMock.serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class)))
      .thenReturn(jsonElement);

    treeTypeAdapter.write(jsonWriter, value);
  }

  @Test
    @Timeout(8000)
  public void testWrite_NoSerializer_UsesDelegate() throws Exception {
    treeTypeAdapter = new TreeTypeAdapter<>(null, deserializerMock, gsonMock, typeToken, skipPastMock);
    TypeAdapter<String> delegateMock = mock(TypeAdapter.class);
    setDelegate(treeTypeAdapter, delegateMock);

    JsonWriter jsonWriter = new JsonWriter(new java.io.StringWriter());
    String value = "value";

    treeTypeAdapter.write(jsonWriter, value);
    verify(delegateMock).write(any(JsonWriter.class), eq(value));
  }

  @Test
    @Timeout(8000)
  public void testDelegateMethod_InitializesDelegate() throws Exception {
    treeTypeAdapter = new TreeTypeAdapter<>(null, null, gsonMock, typeToken, skipPastMock);
    TypeAdapter<String> delegateMock = mock(TypeAdapter.class);

    when(skipPastMock.create(eq(gsonMock), eq(typeToken))).thenReturn(delegateMock);

    // Clear delegate field forcibly to null
    setDelegate(treeTypeAdapter, null);

    // Call the public getSerializationDelegate() to trigger delegate initialization
    TypeAdapter<String> delegate1 = treeTypeAdapter.getSerializationDelegate();
    assertNotNull(delegate1);
    assertSame(delegateMock, delegate1);

    // Subsequent calls return cached delegate
    TypeAdapter<String> delegate2 = treeTypeAdapter.getSerializationDelegate();
    assertSame(delegate1, delegate2);
  }

  @Test
    @Timeout(8000)
  public void testGetSerializationDelegate_UsesDelegate() throws Exception {
    treeTypeAdapter = new TreeTypeAdapter<>(null, null, gsonMock, typeToken, skipPastMock);
    TypeAdapter<String> delegateMock = mock(TypeAdapter.class);
    setDelegate(treeTypeAdapter, delegateMock);

    TypeAdapter<String> result = treeTypeAdapter.getSerializationDelegate();
    assertSame(delegateMock, result);
  }

  @Test
    @Timeout(8000)
  public void testStaticNewFactory_ExactType() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    JsonSerializer<String> serializer = (src, typeOfSrc, context) -> JsonNull.INSTANCE;
    Object typeAdapter = serializer;

    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(typeToken, typeAdapter);
    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  public void testStaticNewFactoryWithMatchRawType() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    JsonSerializer<String> serializer = (src, typeOfSrc, context) -> JsonNull.INSTANCE;
    Object typeAdapter = serializer;

    TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, typeAdapter);
    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  public void testStaticNewTypeHierarchyFactory() {
    JsonSerializer<String> serializer = (src, typeOfSrc, context) -> JsonNull.INSTANCE;
    Object typeAdapter = serializer;

    TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(String.class, typeAdapter);
    assertNotNull(factory);
  }

  private void setDelegate(TreeTypeAdapter<String> adapter, TypeAdapter<String> delegate) throws Exception {
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(adapter, delegate);
  }

  private TypeAdapter<String> invokeDelegate(TreeTypeAdapter<String> adapter) throws Exception {
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> delegate = (TypeAdapter<String>) delegateMethod.invoke(adapter);
    return delegate;
  }

  private JsonSerializationContext getContext(TreeTypeAdapter<String> adapter) throws Exception {
    Field contextField = TreeTypeAdapter.class.getDeclaredField("context");
    contextField.setAccessible(true);
    return (JsonSerializationContext) contextField.get(adapter);
  }
}
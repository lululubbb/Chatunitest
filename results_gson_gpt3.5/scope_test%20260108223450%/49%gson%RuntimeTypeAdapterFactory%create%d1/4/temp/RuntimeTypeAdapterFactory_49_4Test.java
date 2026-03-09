package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class RuntimeTypeAdapterFactory_49_4Test {

  private RuntimeTypeAdapterFactory<Object> factory;
  private Gson mockGson;
  private TypeToken<Object> baseTypeToken;
  private final String typeFieldName = "type";
  private final Class<Object> baseType = Object.class;

  // Dummy subtype classes for testing
  static class SubtypeA {}
  static class SubtypeB {}

  @BeforeEach
  public void setUp() throws Exception {
    // Create instance of RuntimeTypeAdapterFactory using reflection (private constructor)
    Class<RuntimeTypeAdapterFactory> clazz = RuntimeTypeAdapterFactory.class;
    var constructor = clazz.getDeclaredConstructor(Class.class, String.class, boolean.class);
    constructor.setAccessible(true);
    factory = (RuntimeTypeAdapterFactory<Object>) constructor.newInstance(baseType, typeFieldName, false);

    // Register subtypes by reflection to populate maps labelToSubtype and subtypeToLabel
    Field labelToSubtypeField = clazz.getDeclaredField("labelToSubtype");
    labelToSubtypeField.setAccessible(true);
    Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(factory);
    labelToSubtype.put("subtypeA", SubtypeA.class);
    labelToSubtype.put("subtypeB", SubtypeB.class);

    Field subtypeToLabelField = clazz.getDeclaredField("subtypeToLabel");
    subtypeToLabelField.setAccessible(true);
    Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(factory);
    subtypeToLabel.put(SubtypeA.class, "subtypeA");
    subtypeToLabel.put(SubtypeB.class, "subtypeB");

    // Create mock Gson
    mockGson = mock(Gson.class);

    baseTypeToken = TypeToken.get(baseType);
  }

  @Test
    @Timeout(8000)
  public void create_nullType_returnsNull() {
    TypeAdapter<?> adapter = factory.create(mockGson, null);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_typeNotHandled_returnsNull() {
    // Setup typeToken with unrelated class
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);

    // Without recognizeSubtypes, baseType.equals(rawType) must be true to handle
    // String.class != Object.class, so returns null
    TypeAdapter<?> adapter = factory.create(mockGson, stringTypeToken);
    assertNull(adapter);

    // Enable recognizeSubtypes and test with unrelated type
    factory.recognizeSubtypes();
    adapter = factory.create(mockGson, stringTypeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_recognizeSubtypesTrue_handlesAssignableSubtypes() throws Exception {
    // Enable recognizeSubtypes
    factory.recognizeSubtypes();

    // Use subtype class
    TypeToken<SubtypeA> subtypeToken = TypeToken.get(SubtypeA.class);

    // Setup Gson mocks for getAdapter and getDelegateAdapter
    TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
    when(mockGson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

    TypeAdapter<?> delegateAdapterA = mock(TypeAdapter.class);
    when(mockGson.getDelegateAdapter(factory, TypeToken.get(SubtypeA.class))).thenReturn(delegateAdapterA);
    TypeAdapter<?> delegateAdapterB = mock(TypeAdapter.class);
    when(mockGson.getDelegateAdapter(factory, TypeToken.get(SubtypeB.class))).thenReturn(delegateAdapterB);

    TypeAdapter<SubtypeA> adapter = factory.create(mockGson, (TypeToken<SubtypeA>) subtypeToken);
    assertNotNull(adapter);

    // Test read method with maintainType = false (default)
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(typeFieldName, "subtypeA");
    JsonElement jsonElement = jsonObject;

    // Setup jsonElementAdapter.read to return jsonElement
    JsonReader mockReader = mock(JsonReader.class);
    when(jsonElementAdapter.read(mockReader)).thenReturn(jsonElement);

    // Setup delegateAdapterA.fromJsonTree to return an instance
    SubtypeA expectedInstance = new SubtypeA();
    when(delegateAdapterA.fromJsonTree(jsonElement)).thenReturn(expectedInstance);

    SubtypeA result = adapter.read(mockReader);
    assertSame(expectedInstance, result);

    // Test write method
    JsonWriter mockWriter = mock(JsonWriter.class);

    // Setup delegateAdapterA.toJsonTree to return jsonObject
    when(delegateAdapterA.toJsonTree(expectedInstance)).thenReturn(jsonObject);

    // Capture the JsonElement passed to jsonElementAdapter.write
    ArgumentCaptor<JsonElement> jsonElementCaptor = ArgumentCaptor.forClass(JsonElement.class);
    doNothing().when(jsonElementAdapter).write(eq(mockWriter), jsonElementCaptor.capture());

    adapter.write(mockWriter, expectedInstance);

    JsonElement writtenElement = jsonElementCaptor.getValue();
    assertTrue(writtenElement.isJsonObject());
    JsonObject writtenObject = writtenElement.getAsJsonObject();
    assertTrue(writtenObject.has(typeFieldName));
    assertEquals("subtypeA", writtenObject.get(typeFieldName).getAsString());
  }

  @Test
    @Timeout(8000)
  public void create_read_missingTypeField_throwsJsonParseException() throws IOException {
    factory.recognizeSubtypes();

    TypeToken<SubtypeA> subtypeToken = TypeToken.get(SubtypeA.class);

    TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
    when(mockGson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

    TypeAdapter<?> delegateAdapterA = mock(TypeAdapter.class);
    when(mockGson.getDelegateAdapter(factory, TypeToken.get(SubtypeA.class))).thenReturn(delegateAdapterA);
    TypeAdapter<?> delegateAdapterB = mock(TypeAdapter.class);
    when(mockGson.getDelegateAdapter(factory, TypeToken.get(SubtypeB.class))).thenReturn(delegateAdapterB);

    TypeAdapter<SubtypeA> adapter = factory.create(mockGson, (TypeToken<SubtypeA>) subtypeToken);
    assertNotNull(adapter);

    JsonObject jsonObject = new JsonObject(); // no typeFieldName present
    JsonElement jsonElement = jsonObject;

    JsonReader mockReader = mock(JsonReader.class);
    when(jsonElementAdapter.read(mockReader)).thenReturn(jsonElement);

    JsonParseException ex = assertThrows(JsonParseException.class, () -> adapter.read(mockReader));
    assertTrue(ex.getMessage().contains("does not define a field named"));
  }

  @Test
    @Timeout(8000)
  public void create_read_unregisteredSubtype_throwsJsonParseException() throws IOException {
    factory.recognizeSubtypes();

    TypeToken<SubtypeA> subtypeToken = TypeToken.get(SubtypeA.class);

    TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
    when(mockGson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

    TypeAdapter<?> delegateAdapterA = mock(TypeAdapter.class);
    when(mockGson.getDelegateAdapter(factory, TypeToken.get(SubtypeA.class))).thenReturn(delegateAdapterA);
    TypeAdapter<?> delegateAdapterB = mock(TypeAdapter.class);
    when(mockGson.getDelegateAdapter(factory, TypeToken.get(SubtypeB.class))).thenReturn(delegateAdapterB);

    TypeAdapter<SubtypeA> adapter = factory.create(mockGson, (TypeToken<SubtypeA>) subtypeToken);
    assertNotNull(adapter);

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(typeFieldName, "unregisteredSubtype");
    JsonElement jsonElement = jsonObject;

    JsonReader mockReader = mock(JsonReader.class);
    when(jsonElementAdapter.read(mockReader)).thenReturn(jsonElement);

    JsonParseException ex = assertThrows(JsonParseException.class, () -> adapter.read(mockReader));
    assertTrue(ex.getMessage().contains("did you forget to register a subtype?"));
  }

  @Test
    @Timeout(8000)
  public void create_write_unregisteredSubtype_throwsJsonParseException() throws IOException {
    factory.recognizeSubtypes();

    TypeToken<SubtypeA> subtypeToken = TypeToken.get(SubtypeA.class);

    TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
    when(mockGson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

    TypeAdapter<?> delegateAdapterA = mock(TypeAdapter.class);
    when(mockGson.getDelegateAdapter(factory, TypeToken.get(SubtypeA.class))).thenReturn(delegateAdapterA);
    TypeAdapter<?> delegateAdapterB = mock(TypeAdapter.class);
    when(mockGson.getDelegateAdapter(factory, TypeToken.get(SubtypeB.class))).thenReturn(delegateAdapterB);

    TypeAdapter<SubtypeA> adapter = factory.create(mockGson, (TypeToken<SubtypeA>) subtypeToken);
    assertNotNull(adapter);

    // Create an anonymous class not registered as subtype
    Object unregisteredInstance = new Object() {};

    JsonWriter mockWriter = mock(JsonWriter.class);

    JsonParseException ex = assertThrows(JsonParseException.class,
        () -> adapter.write(mockWriter, (SubtypeA) unregisteredInstance));
    assertTrue(ex.getMessage().contains("did you forget to register a subtype?"));
  }

  @Test
    @Timeout(8000)
  public void create_write_maintainTypeTrue_writesDirectly() throws IOException, Exception {
    // Create new factory with maintainType = true
    Class<RuntimeTypeAdapterFactory> clazz = RuntimeTypeAdapterFactory.class;
    var constructor = clazz.getDeclaredConstructor(Class.class, String.class, boolean.class);
    constructor.setAccessible(true);
    RuntimeTypeAdapterFactory<Object> factoryMaintainType = (RuntimeTypeAdapterFactory<Object>) constructor.newInstance(baseType, typeFieldName, true);

    // Register subtypes
    Field labelToSubtypeField = clazz.getDeclaredField("labelToSubtype");
    labelToSubtypeField.setAccessible(true);
    Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(factoryMaintainType);
    labelToSubtype.put("subtypeA", SubtypeA.class);

    Field subtypeToLabelField = clazz.getDeclaredField("subtypeToLabel");
    subtypeToLabelField.setAccessible(true);
    Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(factoryMaintainType);
    subtypeToLabel.put(SubtypeA.class, "subtypeA");

    TypeToken<SubtypeA> subtypeToken = TypeToken.get(SubtypeA.class);

    TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
    when(mockGson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

    TypeAdapter<?> delegateAdapterA = mock(TypeAdapter.class);
    when(mockGson.getDelegateAdapter(factoryMaintainType, TypeToken.get(SubtypeA.class))).thenReturn(delegateAdapterA);

    TypeAdapter<SubtypeA> adapter = factoryMaintainType.create(mockGson, (TypeToken<SubtypeA>) subtypeToken);
    assertNotNull(adapter);

    SubtypeA instance = new SubtypeA();

    JsonObject jsonObject = new JsonObject();
    when(delegateAdapterA.toJsonTree(instance)).thenReturn(jsonObject);

    JsonWriter mockWriter = mock(JsonWriter.class);

    doNothing().when(jsonElementAdapter).write(eq(mockWriter), eq(jsonObject));

    adapter.write(mockWriter, instance);

    verify(jsonElementAdapter).write(eq(mockWriter), eq(jsonObject));
  }

  @Test
    @Timeout(8000)
  public void create_write_existingTypeField_throwsJsonParseException() throws IOException {
    factory.recognizeSubtypes();

    TypeToken<SubtypeA> subtypeToken = TypeToken.get(SubtypeA.class);

    TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
    when(mockGson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

    TypeAdapter<?> delegateAdapterA = mock(TypeAdapter.class);
    when(mockGson.getDelegateAdapter(factory, TypeToken.get(SubtypeA.class))).thenReturn(delegateAdapterA);

    TypeAdapter<SubtypeA> adapter = factory.create(mockGson, (TypeToken<SubtypeA>) subtypeToken);
    assertNotNull(adapter);

    SubtypeA instance = new SubtypeA();

    JsonObject jsonObject = new JsonObject();
    jsonObject.add(typeFieldName, new JsonPrimitive("someValue"));
    when(delegateAdapterA.toJsonTree(instance)).thenReturn(jsonObject);

    JsonWriter mockWriter = mock(JsonWriter.class);

    JsonParseException ex = assertThrows(JsonParseException.class, () -> adapter.write(mockWriter, instance));
    assertTrue(ex.getMessage().contains("already defines a field named"));
  }
}
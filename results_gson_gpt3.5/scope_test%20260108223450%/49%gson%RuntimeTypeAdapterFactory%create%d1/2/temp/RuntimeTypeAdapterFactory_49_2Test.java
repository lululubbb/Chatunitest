package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import java.util.LinkedHashMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class RuntimeTypeAdapterFactory_49_2Test {

  private RuntimeTypeAdapterFactory<Object> factory;
  private Gson gson;

  private static class BaseType {}

  private static class SubTypeA extends BaseType {}

  private static class SubTypeB extends BaseType {}

  private TypeAdapter<JsonElement> jsonElementAdapter;

  @BeforeEach
  public void setUp() throws Exception {
    // Create instance of RuntimeTypeAdapterFactory via reflection because constructor is private
    var constructor = RuntimeTypeAdapterFactory.class.getDeclaredConstructor(
        Class.class, String.class, boolean.class);
    constructor.setAccessible(true);
    factory = (RuntimeTypeAdapterFactory<Object>) constructor.newInstance(BaseType.class, "type", false);

    gson = mock(Gson.class);
    jsonElementAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

    // Add subtypes to factory via reflection to populate labelToSubtype and subtypeToLabel maps
    Field labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
    labelToSubtypeField.setAccessible(true);
    Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(factory);
    labelToSubtype.put("a", SubTypeA.class);
    labelToSubtype.put("b", SubTypeB.class);

    Field subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
    subtypeToLabelField.setAccessible(true);
    Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(factory);
    subtypeToLabel.put(SubTypeA.class, "a");
    subtypeToLabel.put(SubTypeB.class, "b");
  }

  @Test
    @Timeout(8000)
  public void testCreateReturnsNullIfTypeIsNull() {
    TypeAdapter<?> adapter = factory.create(gson, null);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testCreateReturnsNullIfTypeNotHandled() {
    TypeToken<String> stringType = TypeToken.get(String.class);
    TypeAdapter<?> adapter = factory.create(gson, stringType);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testCreateReturnsTypeAdapterWithCorrectBehavior() throws IOException {
    TypeToken<BaseType> baseTypeToken = TypeToken.get(BaseType.class);

    // Setup delegate adapters for subtypes
    TypeAdapter<?> delegateA = mock(TypeAdapter.class);
    TypeAdapter<?> delegateB = mock(TypeAdapter.class);

    when(gson.getDelegateAdapter(eq(factory), eq(TypeToken.get(SubTypeA.class)))).thenReturn(delegateA);
    when(gson.getDelegateAdapter(eq(factory), eq(TypeToken.get(SubTypeB.class)))).thenReturn(delegateB);

    // Setup jsonElementAdapter read to return a JsonObject with type field "a"
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("type", "a");
    JsonPrimitive dummyPrimitive = new JsonPrimitive("dummy");
    jsonObject.add("dummy", dummyPrimitive);

    when(jsonElementAdapter.read(any(JsonReader.class))).thenReturn(jsonObject);

    // Setup delegateA.fromJsonTree to return a SubTypeA instance
    SubTypeA subTypeAInstance = new SubTypeA();
    when(delegateA.fromJsonTree(jsonObject)).thenReturn(subTypeAInstance);

    TypeAdapter<BaseType> adapter = factory.create(gson, baseTypeToken);

    assertNotNull(adapter);

    // Test read method returns the correct subtype instance
    BaseType result = adapter.read(mock(JsonReader.class));
    assertSame(subTypeAInstance, result);

    // Test read throws if type field missing
    JsonObject jsonObjectMissingType = new JsonObject();
    when(jsonElementAdapter.read(any(JsonReader.class))).thenReturn(jsonObjectMissingType);
    JsonParseException ex = assertThrows(JsonParseException.class, () -> adapter.read(mock(JsonReader.class)));
    assertTrue(ex.getMessage().contains("does not define a field named"));

    // Test read throws if label not registered
    JsonObject jsonObjectUnknownLabel = new JsonObject();
    jsonObjectUnknownLabel.addProperty("type", "unknown");
    when(jsonElementAdapter.read(any(JsonReader.class))).thenReturn(jsonObjectUnknownLabel);
    JsonParseException ex2 = assertThrows(JsonParseException.class, () -> adapter.read(mock(JsonReader.class)));
    assertTrue(ex2.getMessage().contains("did you forget to register a subtype?"));

    // Setup delegateA.toJsonTree returns a JsonObject without type field
    JsonObject toJsonTreeObject = new JsonObject();
    toJsonTreeObject.addProperty("field", "value");
    when(delegateA.toJsonTree(subTypeAInstance)).thenReturn(toJsonTreeObject);

    // Prepare JsonWriter mock for write test
    JsonWriter jsonWriter = mock(JsonWriter.class);

    // Test write method writes JsonObject with added type field
    adapter.write(jsonWriter, subTypeAInstance);

    // Capture the JsonObject written by jsonElementAdapter.write
    ArgumentCaptor<JsonElement> jsonElementCaptor = ArgumentCaptor.forClass(JsonElement.class);
    verify(jsonElementAdapter).write(eq(jsonWriter), jsonElementCaptor.capture());
    JsonObject writtenObject = jsonElementCaptor.getValue().getAsJsonObject();

    // The written object should contain the type field and original fields
    assertEquals("a", writtenObject.get("type").getAsString());
    assertEquals("value", writtenObject.get("field").getAsString());

    // Test write throws if delegate missing
    RuntimeTypeAdapterFactory<Object> factoryWithNoDelegate = factory;
    // remove subtypeToDelegate to simulate missing delegate
    // We do this by creating a new factory with no subtypes registered
    try {
      var constructor = RuntimeTypeAdapterFactory.class.getDeclaredConstructor(
          Class.class, String.class, boolean.class);
      constructor.setAccessible(true);
      factoryWithNoDelegate = (RuntimeTypeAdapterFactory<Object>) constructor.newInstance(BaseType.class, "type", false);
    } catch (Exception e) {
      fail(e);
    }
    TypeAdapter<BaseType> adapterNoDelegate = factoryWithNoDelegate.create(gson, baseTypeToken);
    assertNotNull(adapterNoDelegate);

    JsonWriter writer = mock(JsonWriter.class);
    SubTypeA instance = new SubTypeA();
    JsonParseException ex3 = assertThrows(JsonParseException.class, () -> adapterNoDelegate.write(writer, instance));
    assertTrue(ex3.getMessage().contains("did you forget to register a subtype?"));

    // Test write throws if jsonObject already has typeFieldName
    JsonObject jsonObjectWithTypeField = new JsonObject();
    jsonObjectWithTypeField.addProperty("type", "value");
    when(delegateA.toJsonTree(subTypeAInstance)).thenReturn(jsonObjectWithTypeField);

    JsonParseException ex4 = assertThrows(JsonParseException.class, () -> adapter.write(jsonWriter, subTypeAInstance));
    assertTrue(ex4.getMessage().contains("already defines a field named"));
  }

  @Test
    @Timeout(8000)
  public void testCreateWithMaintainTypeTrue() throws IOException {
    // Create factory with maintainType true
    try {
      var constructor = RuntimeTypeAdapterFactory.class.getDeclaredConstructor(
          Class.class, String.class, boolean.class);
      constructor.setAccessible(true);
      factory = (RuntimeTypeAdapterFactory<Object>) constructor.newInstance(BaseType.class, "type", true);

      // Add subtypes again
      Field labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
      labelToSubtypeField.setAccessible(true);
      Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(factory);
      labelToSubtype.put("a", SubTypeA.class);

      Field subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
      subtypeToLabelField.setAccessible(true);
      Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(factory);
      subtypeToLabel.put(SubTypeA.class, "a");

      when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);
    } catch (Exception e) {
      fail(e);
    }

    TypeToken<BaseType> baseTypeToken = TypeToken.get(BaseType.class);

    TypeAdapter<?> delegateA = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(eq(factory), eq(TypeToken.get(SubTypeA.class)))).thenReturn(delegateA);

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("type", "a");
    when(jsonElementAdapter.read(any(JsonReader.class))).thenReturn(jsonObject);

    SubTypeA subTypeAInstance = new SubTypeA();
    when(delegateA.fromJsonTree(jsonObject)).thenReturn(subTypeAInstance);

    TypeAdapter<BaseType> adapter = factory.create(gson, baseTypeToken);

    // read works same as before
    BaseType result = adapter.read(mock(JsonReader.class));
    assertSame(subTypeAInstance, result);

    // write calls jsonElementAdapter.write directly without cloning
    JsonObject toJsonTreeObject = new JsonObject();
    toJsonTreeObject.addProperty("field", "value");
    when(delegateA.toJsonTree(subTypeAInstance)).thenReturn(toJsonTreeObject);
    JsonWriter jsonWriter = mock(JsonWriter.class);

    adapter.write(jsonWriter, subTypeAInstance);

    verify(jsonElementAdapter).write(eq(jsonWriter), eq(toJsonTreeObject));
  }

  @Test
    @Timeout(8000)
  public void testCreateWithRecognizeSubtypesTrue() throws Exception {
    // Set recognizeSubtypes true via reflection
    Field recognizeSubtypesField = RuntimeTypeAdapterFactory.class.getDeclaredField("recognizeSubtypes");
    recognizeSubtypesField.setAccessible(true);
    recognizeSubtypesField.set(factory, true);

    // Create TypeToken of subtype
    TypeToken<SubTypeA> subTypeToken = TypeToken.get(SubTypeA.class);

    // Setup delegate adapter for SubTypeA
    TypeAdapter<?> delegateA = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(eq(factory), eq(TypeToken.get(SubTypeA.class)))).thenReturn(delegateA);

    // Setup jsonElementAdapter read returns JsonObject with type field "a"
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("type", "a");
    when(jsonElementAdapter.read(any(JsonReader.class))).thenReturn(jsonObject);

    SubTypeA subTypeAInstance = new SubTypeA();
    when(delegateA.fromJsonTree(jsonObject)).thenReturn(subTypeAInstance);

    TypeAdapter<SubTypeA> adapter = factory.create(gson, subTypeToken);

    assertNotNull(adapter);

    SubTypeA result = adapter.read(mock(JsonReader.class));
    assertSame(subTypeAInstance, result);
  }
}
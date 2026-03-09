package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
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

public class RuntimeTypeAdapterFactory_49_6Test {

  private RuntimeTypeAdapterFactory<Object> factory;
  private Gson gson;
  private TypeToken<Object> baseTypeToken;

  private static final String TYPE_FIELD = "type";

  static class Base {}
  static class SubTypeA extends Base {}
  static class SubTypeB extends Base {}

  @BeforeEach
  public void setUp() throws Exception {
    // Create instance of RuntimeTypeAdapterFactory using reflection (private constructor)
    Class<?> clazz = RuntimeTypeAdapterFactory.class;
    var constructor = clazz.getDeclaredConstructor(Class.class, String.class, boolean.class);
    constructor.setAccessible(true);
    factory = (RuntimeTypeAdapterFactory<Object>) constructor.newInstance(Base.class, TYPE_FIELD, false);

    // Add subtypes via reflection to labelToSubtype and subtypeToLabel maps
    Field labelToSubtypeField = clazz.getDeclaredField("labelToSubtype");
    labelToSubtypeField.setAccessible(true);
    Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(factory);
    labelToSubtype.put("a", SubTypeA.class);
    labelToSubtype.put("b", SubTypeB.class);

    Field subtypeToLabelField = clazz.getDeclaredField("subtypeToLabel");
    subtypeToLabelField.setAccessible(true);
    Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(factory);
    subtypeToLabel.put(SubTypeA.class, "a");
    subtypeToLabel.put(SubTypeB.class, "b");

    // Create a mock Gson
    gson = mock(Gson.class);
    baseTypeToken = TypeToken.get(Base.class);

    // Mock gson.getAdapter(JsonElement.class)
    TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

    // Mock gson.getDelegateAdapter(factory, TypeToken.get(...)) to return a delegate adapter per subtype
    when(gson.getDelegateAdapter(eq(factory), eq(TypeToken.get(SubTypeA.class))))
        .thenReturn(new TypeAdapter<Object>() {
          @Override
          public void write(JsonWriter out, Object value) throws IOException {}

          @Override
          public Object read(JsonReader in) throws IOException {
            return new SubTypeA();
          }

          @Override
          public Object fromJsonTree(JsonElement jsonTree) {
            return new SubTypeA();
          }

          @Override
          public JsonElement toJsonTree(Object value) {
            JsonObject obj = new JsonObject();
            obj.addProperty("fieldA", "valueA");
            return obj;
          }
        });
    when(gson.getDelegateAdapter(eq(factory), eq(TypeToken.get(SubTypeB.class))))
        .thenReturn(new TypeAdapter<Object>() {
          @Override
          public void write(JsonWriter out, Object value) throws IOException {}

          @Override
          public Object read(JsonReader in) throws IOException {
            return new SubTypeB();
          }

          @Override
          public Object fromJsonTree(JsonElement jsonTree) {
            return new SubTypeB();
          }

          @Override
          public JsonElement toJsonTree(Object value) {
            JsonObject obj = new JsonObject();
            obj.addProperty("fieldB", "valueB");
            return obj;
          }
        });

    // Mock jsonElementAdapter.read(in) to return JsonObject with type field for read tests
    when(jsonElementAdapter.read(any(JsonReader.class))).thenAnswer(invocation -> {
      // Return a JsonObject with type field "a"
      JsonObject obj = new JsonObject();
      obj.addProperty(TYPE_FIELD, "a");
      obj.addProperty("fieldA", "valueA");
      return obj;
    });

    // Mock jsonElementAdapter.write to do nothing for write tests
    doNothing().when(jsonElementAdapter).write(any(JsonWriter.class), any(JsonElement.class));
  }

  @Test
    @Timeout(8000)
  public void testCreate_nullTypeToken_returnsNull() {
    TypeAdapter<?> adapter = factory.create(gson, null);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testCreate_typeNotHandled_returnsNull() {
    TypeToken<String> stringType = TypeToken.get(String.class);
    TypeAdapter<?> adapter = factory.create(gson, stringType);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testCreate_handleType_returnsTypeAdapter() throws IOException {
    TypeAdapter<Base> adapter = factory.create(gson, baseTypeToken);
    assertNotNull(adapter);

    // Prepare mocks for JsonReader and JsonWriter
    JsonReader jsonReader = mock(JsonReader.class);
    JsonWriter jsonWriter = mock(JsonWriter.class);

    // Test read() branch: maintainType false, so label field is removed
    Base readResult = adapter.read(jsonReader);
    assertNotNull(readResult);
    assertTrue(readResult instanceof SubTypeA);

    // Test read() throws on missing label field
    // We simulate jsonElementAdapter.read returning JsonObject without type field
    TypeAdapter<JsonElement> jsonElementAdapter = gson.getAdapter(JsonElement.class);
    when(jsonElementAdapter.read(jsonReader)).thenReturn(new JsonObject()); // no typeFieldName
    JsonParseException e = assertThrows(JsonParseException.class, () -> adapter.read(jsonReader));
    assertTrue(e.getMessage().contains("does not define a field named"));

    // Test read() throws on unregistered label
    JsonObject objWithUnknownLabel = new JsonObject();
    objWithUnknownLabel.addProperty(TYPE_FIELD, "unknown");
    when(jsonElementAdapter.read(jsonReader)).thenReturn(objWithUnknownLabel);
    JsonParseException e2 = assertThrows(JsonParseException.class, () -> adapter.read(jsonReader));
    assertTrue(e2.getMessage().contains("did you forget to register a subtype?"));

    // Reset to valid label for write tests
    when(jsonElementAdapter.read(jsonReader)).thenReturn(new JsonObject() {{
      addProperty(TYPE_FIELD, "a");
    }});

    // Test write() branch: maintainType false, normal write with clone and added type field
    SubTypeA subA = new SubTypeA();
    adapter.write(jsonWriter, (Base) subA);
    // Verify jsonElementAdapter.write called once
    verify(jsonElementAdapter, atLeastOnce()).write(any(JsonWriter.class), any(JsonElement.class));

    // Test write() throws on unregistered subtype
    SubTypeB subB = new SubTypeB();
    // Remove SubTypeB adapter to simulate missing delegate
    try {
      Field subtypeToDelegateField = adapter.getClass().getDeclaredField("subtypeToDelegate");
      subtypeToDelegateField.setAccessible(true);
      Map<Class<?>, TypeAdapter<?>> subtypeToDelegate = (Map<Class<?>, TypeAdapter<?>>) subtypeToDelegateField.get(adapter);
      subtypeToDelegate.remove(SubTypeB.class);
    } catch (NoSuchFieldException ignored) {
      // Field not found in anonymous class, ignore
    }
    JsonParseException e3 = assertThrows(JsonParseException.class, () -> adapter.write(jsonWriter, (Base) subB));
    assertTrue(e3.getMessage().contains("did you forget to register a subtype?"));

    // Test write() throws if jsonObject has typeFieldName
    TypeAdapter<Base> adapterWithMaintainType = createFactoryWithMaintainType(true).create(gson, baseTypeToken);
    assertNotNull(adapterWithMaintainType);
    SubTypeA subA2 = new SubTypeA();
    // Mock delegate.toJsonTree to produce JsonObject with typeFieldName present
    TypeAdapter<Object> delegateAdapter = gson.getDelegateAdapter(factory, TypeToken.get(SubTypeA.class));
    JsonObject jsonObjectWithTypeField = new JsonObject();
    jsonObjectWithTypeField.addProperty(TYPE_FIELD, "a");
    when(delegateAdapter.toJsonTree(subA2)).thenReturn(jsonObjectWithTypeField);

    // Use reflection to get subtypeToDelegate map and put delegateAdapter back
    try {
      Field subtypeToDelegateField = adapterWithMaintainType.getClass().getDeclaredField("subtypeToDelegate");
      subtypeToDelegateField.setAccessible(true);
      Map<Class<?>, TypeAdapter<?>> subtypeToDelegate = (Map<Class<?>, TypeAdapter<?>>) subtypeToDelegateField.get(adapterWithMaintainType);
      subtypeToDelegate.put(SubTypeA.class, delegateAdapter);
    } catch (NoSuchFieldException ignored) {
    }

    // Write should throw because jsonObject already has typeFieldName
    JsonParseException e4 = assertThrows(JsonParseException.class, () -> adapterWithMaintainType.write(mock(JsonWriter.class), subA2));
    assertTrue(e4.getMessage().contains("already defines a field named"));
  }

  @Test
    @Timeout(8000)
  public void testCreate_handleTypeWithRecognizeSubtypesTrue() throws Exception {
    // enable recognizeSubtypes
    Method recognizeSubtypesMethod = RuntimeTypeAdapterFactory.class.getDeclaredMethod("recognizeSubtypes");
    recognizeSubtypesMethod.setAccessible(true);
    recognizeSubtypesMethod.invoke(factory);

    // create typeToken for subtype
    TypeToken<SubTypeA> subtypeToken = TypeToken.get(SubTypeA.class);
    TypeAdapter<SubTypeA> adapter = factory.create(gson, subtypeToken);
    assertNotNull(adapter);

    // create typeToken for unrelated class
    TypeToken<String> stringToken = TypeToken.get(String.class);
    TypeAdapter<String> adapter2 = factory.create(gson, stringToken);
    assertNull(adapter2);
  }

  private RuntimeTypeAdapterFactory<Object> createFactoryWithMaintainType(boolean maintainType) throws Exception {
    Class<?> clazz = RuntimeTypeAdapterFactory.class;
    var constructor = clazz.getDeclaredConstructor(Class.class, String.class, boolean.class);
    constructor.setAccessible(true);
    RuntimeTypeAdapterFactory<Object> f = (RuntimeTypeAdapterFactory<Object>) constructor.newInstance(Base.class, TYPE_FIELD, maintainType);

    Field labelToSubtypeField = clazz.getDeclaredField("labelToSubtype");
    labelToSubtypeField.setAccessible(true);
    Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(f);
    labelToSubtype.put("a", SubTypeA.class);

    Field subtypeToLabelField = clazz.getDeclaredField("subtypeToLabel");
    subtypeToLabelField.setAccessible(true);
    Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(f);
    subtypeToLabel.put(SubTypeA.class, "a");

    return f;
  }
}
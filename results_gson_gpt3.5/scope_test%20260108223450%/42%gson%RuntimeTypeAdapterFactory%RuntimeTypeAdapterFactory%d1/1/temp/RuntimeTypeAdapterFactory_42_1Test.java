package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapterFactory;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RuntimeTypeAdapterFactory_42_1Test {

  private RuntimeTypeAdapterFactory<Base> factory;

  static class Base {}
  static class SubA extends Base {}
  static class SubB extends Base {}

  @BeforeEach
  void setup() {
    factory = RuntimeTypeAdapterFactory.of(Base.class, "type", false);
  }

  @Test
    @Timeout(8000)
  void of_nullBaseType_throws() {
    assertThrows(NullPointerException.class, () -> RuntimeTypeAdapterFactory.of(null, "type", false));
  }

  @Test
    @Timeout(8000)
  void of_nullTypeFieldName_throws() {
    assertThrows(NullPointerException.class, () -> RuntimeTypeAdapterFactory.of(Base.class, null, false));
  }

  @Test
    @Timeout(8000)
  void of_defaultOverloads() {
    var f1 = RuntimeTypeAdapterFactory.of(Base.class);
    assertNotNull(f1);

    var f2 = RuntimeTypeAdapterFactory.of(Base.class, "myType");
    assertNotNull(f2);
  }

  @Test
    @Timeout(8000)
  void recognizeSubtypes_returnsSelf() {
    var returned = factory.recognizeSubtypes();
    assertSame(factory, returned);

    // Check recognizeSubtypes field set true via reflection
    boolean recognizeSubtypesValue = false;
    try {
      Field f = RuntimeTypeAdapterFactory.class.getDeclaredField("recognizeSubtypes");
      f.setAccessible(true);
      recognizeSubtypesValue = f.getBoolean(factory);
    } catch (Exception e) {
      fail(e);
    }
    assertTrue(recognizeSubtypesValue);
  }

  @Test
    @Timeout(8000)
  void registerSubtype_withLabel_and_withoutLabel() {
    var returned1 = factory.registerSubtype(SubA.class, "SubA");
    assertSame(factory, returned1);

    var returned2 = factory.registerSubtype(SubB.class);
    assertSame(factory, returned2);

    try {
      Field labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
      labelToSubtypeField.setAccessible(true);
      Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(factory);
      assertEquals(SubA.class, labelToSubtype.get("SubA"));
      assertEquals(SubB.class, labelToSubtype.get("SubB"));

      Field subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
      subtypeToLabelField.setAccessible(true);
      Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(factory);
      assertEquals("SubA", subtypeToLabel.get(SubA.class));
      assertEquals("SubB", subtypeToLabel.get(SubB.class));
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
    @Timeout(8000)
  void registerSubtype_duplicateLabel_throws() {
    factory.registerSubtype(SubA.class, "SubA");
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> factory.registerSubtype(SubB.class, "SubA"));
    assertTrue(ex.getMessage().toLowerCase().contains("already registered"));
  }

  @Test
    @Timeout(8000)
  void registerSubtype_duplicateType_throws() {
    factory.registerSubtype(SubA.class, "SubA");
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> factory.registerSubtype(SubA.class, "SubA2"));
    assertTrue(ex.getMessage().toLowerCase().contains("already registered"));
  }

  @Test
    @Timeout(8000)
  void create_returnsNullIfNotAssignable() {
    Gson gson = mock(Gson.class);
    TypeToken<String> stringType = TypeToken.get(String.class);
    TypeAdapter<?> adapter = factory.create(gson, stringType);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_returnsTypeAdapter_and_serializesDeserializes() throws Exception {
    factory = RuntimeTypeAdapterFactory.of(Base.class, "type", false)
        .registerSubtype(SubA.class, "suba")
        .registerSubtype(SubB.class, "subb");

    Gson gson = new Gson();

    TypeAdapter<Base> adapter = factory.create(gson, TypeToken.get(Base.class));
    assertNotNull(adapter);

    SubA subA = new SubA();
    // Serialize: ensure "type" field is present with correct label
    java.io.StringWriter sw = new java.io.StringWriter();
    JsonWriter writer = new JsonWriter(sw);
    adapter.write(writer, subA);
    writer.close();
    String json = sw.toString();
    assertTrue(json.contains("\"type\":\"suba\""));

    // Deserialize: create a JsonObject with type field and some data
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("type", "subb");
    // Add some dummy property to simulate actual data
    jsonObject.addProperty("dummy", "value");

    JsonElement jsonElement = jsonObject;
    JsonReader reader = new JsonReader(new java.io.StringReader(jsonElement.toString()));
    Base result = adapter.read(reader);
    assertNotNull(result);
    assertTrue(result instanceof SubB);
  }

  @Test
    @Timeout(8000)
  void create_withMaintainTypeTrue_preservesTypeField() throws Exception {
    factory = RuntimeTypeAdapterFactory.of(Base.class, "type", true)
        .registerSubtype(SubA.class, "suba");

    Gson gson = new Gson();

    TypeAdapter<Base> adapter = factory.create(gson, TypeToken.get(Base.class));
    assertNotNull(adapter);

    SubA subA = new SubA();
    java.io.StringWriter sw = new java.io.StringWriter();
    JsonWriter writer = new JsonWriter(sw);
    adapter.write(writer, subA);
    writer.close();
    String json = sw.toString();
    // The "type" field should be maintained and present at least once
    int firstIndex = json.indexOf("\"type\"");
    assertTrue(firstIndex >= 0);
    // It should not appear more than twice (allowing for one occurrence in the object and one in the subtype)
    int secondIndex = json.indexOf("\"type\"", firstIndex + 1);
    int thirdIndex = json.indexOf("\"type\"", secondIndex + 1);
    assertEquals(-1, thirdIndex);
  }

  @Test
    @Timeout(8000)
  void create_deserializeUnknownLabel_throws() throws Exception {
    factory = RuntimeTypeAdapterFactory.of(Base.class, "type", false)
        .registerSubtype(SubA.class, "suba");

    Gson gson = new Gson();
    TypeAdapter<Base> adapter = factory.create(gson, TypeToken.get(Base.class));

    String json = "{\"type\":\"unknown\"}";
    JsonReader reader = new JsonReader(new java.io.StringReader(json));
    JsonParseException ex = assertThrows(JsonParseException.class, () -> adapter.read(reader));
    assertTrue(ex.getMessage().toLowerCase().contains("cannot deserialize"));
  }

  @Test
    @Timeout(8000)
  void privateConstructor_andFields() throws Exception {
    var ctor = RuntimeTypeAdapterFactory.class.getDeclaredConstructor(Class.class, String.class, boolean.class);
    ctor.setAccessible(true);
    RuntimeTypeAdapterFactory<?> instance = (RuntimeTypeAdapterFactory<?>) ctor.newInstance(Base.class, "typeField", true);
    assertNotNull(instance);

    Field baseTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("baseType");
    baseTypeField.setAccessible(true);
    assertEquals(Base.class, baseTypeField.get(instance));

    Field typeFieldNameField = RuntimeTypeAdapterFactory.class.getDeclaredField("typeFieldName");
    typeFieldNameField.setAccessible(true);
    assertEquals("typeField", typeFieldNameField.get(instance));

    Field maintainTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("maintainType");
    maintainTypeField.setAccessible(true);
    assertEquals(true, maintainTypeField.get(instance));
  }
}
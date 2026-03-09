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
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RuntimeTypeAdapterFactory_42_3Test {

  private RuntimeTypeAdapterFactory<Object> factory;

  @BeforeEach
  public void setup() {
    factory = RuntimeTypeAdapterFactory.of(Object.class, "type", false);
  }

  @Test
    @Timeout(8000)
  public void testOf_withNullBaseType_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> {
      RuntimeTypeAdapterFactory.of(null, "type", false);
    });
  }

  @Test
    @Timeout(8000)
  public void testOf_withNullTypeFieldName_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> {
      RuntimeTypeAdapterFactory.of(Object.class, null, false);
    });
  }

  @Test
    @Timeout(8000)
  public void testOf_defaultTypeFieldNameAndMaintainType() {
    RuntimeTypeAdapterFactory<Object> f1 = RuntimeTypeAdapterFactory.of(Object.class);
    assertNotNull(f1);
    RuntimeTypeAdapterFactory<Object> f2 = RuntimeTypeAdapterFactory.of(Object.class, "type");
    assertNotNull(f2);
  }

  @Test
    @Timeout(8000)
  public void testRecognizeSubtypes_returnsSameInstanceAndSetsFlag() throws Exception {
    RuntimeTypeAdapterFactory<Object> f = RuntimeTypeAdapterFactory.of(Object.class, "type", false);
    RuntimeTypeAdapterFactory<Object> returned = f.recognizeSubtypes();
    assertSame(f, returned);

    Field field = RuntimeTypeAdapterFactory.class.getDeclaredField("recognizeSubtypes");
    field.setAccessible(true);
    assertTrue(field.getBoolean(f));
  }

  @Test
    @Timeout(8000)
  public void testRegisterSubtype_withLabelAndWithoutLabel() throws Exception {
    RuntimeTypeAdapterFactory<Object> f = RuntimeTypeAdapterFactory.of(Object.class, "type", false);

    class SubtypeA {}
    class SubtypeB {}

    RuntimeTypeAdapterFactory<Object> returned1 = f.registerSubtype(SubtypeA.class, "labelA");
    assertSame(f, returned1);

    RuntimeTypeAdapterFactory<Object> returned2 = f.registerSubtype(SubtypeB.class);
    assertSame(f, returned2);

    Field labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
    labelToSubtypeField.setAccessible(true);
    Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(f);
    assertEquals(SubtypeA.class, labelToSubtype.get("labelA"));
    assertEquals(SubtypeB.class, labelToSubtype.get("SubtypeB"));

    Field subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
    subtypeToLabelField.setAccessible(true);
    Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(f);
    assertEquals("labelA", subtypeToLabel.get(SubtypeA.class));
    assertEquals("SubtypeB", subtypeToLabel.get(SubtypeB.class));
  }

  @Test
    @Timeout(8000)
  public void testCreate_withUnregisteredSubtype_returnsNull() {
    RuntimeTypeAdapterFactory<Object> f = RuntimeTypeAdapterFactory.of(Object.class, "type", false);
    Gson gson = new Gson();
    TypeToken<String> stringType = TypeToken.get(String.class);

    TypeAdapter<String> adapter = f.create(gson, stringType);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testCreate_withRegisteredSubtype_andMaintainTypeFalse() throws IOException {
    class Base {}
    class Sub extends Base {}

    RuntimeTypeAdapterFactory<Base> f = RuntimeTypeAdapterFactory.of(Base.class, "type", false);
    f.registerSubtype(Sub.class, "sub");

    Gson gson = new Gson();
    TypeToken<Base> baseType = TypeToken.get(Base.class);

    TypeAdapter<Base> adapter = f.create(gson, baseType);
    assertNotNull(adapter);

    Base instance = new Sub();

    JsonWriter jsonWriter = mock(JsonWriter.class);
    adapter.write(jsonWriter, instance);
    verify(jsonWriter, atLeastOnce()).beginObject();
    verify(jsonWriter, atLeastOnce()).endObject();

    // Create JSON with an unregistered label to force JsonParseException
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("type", "unknownLabel");
    JsonReader jsonReader = new JsonReader(new java.io.StringReader(jsonObject.toString()));

    assertThrows(JsonParseException.class, () -> adapter.read(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testCreate_withMaintainTypeTrue_andRecognizeSubtypes() throws IOException, NoSuchFieldException, IllegalAccessException {
    class Base {}
    class Sub extends Base {}

    RuntimeTypeAdapterFactory<Base> f = RuntimeTypeAdapterFactory.of(Base.class, "type", true);
    RuntimeTypeAdapterFactory<Base> returned = f.recognizeSubtypes();
    assertSame(f, returned);

    // Verify that recognizeSubtypes field is true:
    Field recognizeSubtypesField = RuntimeTypeAdapterFactory.class.getDeclaredField("recognizeSubtypes");
    recognizeSubtypesField.setAccessible(true);
    assertTrue(recognizeSubtypesField.getBoolean(f));

    f.registerSubtype(Sub.class, "sub");

    Gson gson = new Gson();
    TypeToken<Base> baseType = TypeToken.get(Base.class);

    TypeAdapter<Base> adapter = f.create(gson, baseType);
    assertNotNull(adapter);

    Base instance = new Sub();

    // Use Gson to serialize and deserialize to test adapter behavior
    JsonElement jsonElement = adapter.toJsonTree(instance);
    assertTrue(jsonElement.isJsonObject());
    JsonObject obj = jsonElement.getAsJsonObject();
    assertTrue(obj.has("type"));
    assertEquals("sub", obj.get("type").getAsString());

    Base deserialized = adapter.fromJsonTree(jsonElement);
    assertNotNull(deserialized);
  }
}
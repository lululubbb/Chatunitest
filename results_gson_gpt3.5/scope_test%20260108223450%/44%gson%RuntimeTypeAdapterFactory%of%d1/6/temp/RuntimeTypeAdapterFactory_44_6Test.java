package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

class RuntimeTypeAdapterFactory_44_6Test {

  @Test
    @Timeout(8000)
  void testOf_withBaseTypeAndTypeFieldName() throws Exception {
    Class<String> baseType = String.class;
    String typeFieldName = "type";

    RuntimeTypeAdapterFactory<String> factory = RuntimeTypeAdapterFactory.of(baseType, typeFieldName);

    assertNotNull(factory);

    // Use reflection to access private fields to verify constructor set them correctly
    Class<?> clazz = factory.getClass();

    java.lang.reflect.Field baseTypeField = clazz.getDeclaredField("baseType");
    baseTypeField.setAccessible(true);
    assertEquals(baseType, baseTypeField.get(factory));

    java.lang.reflect.Field typeFieldNameField = clazz.getDeclaredField("typeFieldName");
    typeFieldNameField.setAccessible(true);
    assertEquals(typeFieldName, typeFieldNameField.get(factory));

    java.lang.reflect.Field maintainTypeField = clazz.getDeclaredField("maintainType");
    maintainTypeField.setAccessible(true);
    assertEquals(false, maintainTypeField.get(factory));
  }
}
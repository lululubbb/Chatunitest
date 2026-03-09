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

class RuntimeTypeAdapterFactory_44_4Test {

  @Test
    @Timeout(8000)
  void testOf_withBaseTypeAndTypeFieldName() {
    Class<String> baseType = String.class;
    String typeFieldName = "type";

    RuntimeTypeAdapterFactory<String> factory = RuntimeTypeAdapterFactory.of(baseType, typeFieldName);

    assertNotNull(factory);
    // Using reflection to verify private fields
    try {
      java.lang.reflect.Field baseTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("baseType");
      baseTypeField.setAccessible(true);
      Object baseTypeValue = baseTypeField.get(factory);
      assertEquals(baseType, baseTypeValue);

      java.lang.reflect.Field typeFieldNameField = RuntimeTypeAdapterFactory.class.getDeclaredField("typeFieldName");
      typeFieldNameField.setAccessible(true);
      Object typeFieldNameValue = typeFieldNameField.get(factory);
      assertEquals(typeFieldName, typeFieldNameValue);

      java.lang.reflect.Field maintainTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("maintainType");
      maintainTypeField.setAccessible(true);
      Object maintainTypeValue = maintainTypeField.get(factory);
      assertEquals(false, maintainTypeValue);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}
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

class RuntimeTypeAdapterFactory_44_2Test {

  @Test
    @Timeout(8000)
  void of_withBaseTypeAndTypeFieldName_createsInstance() {
    Class<String> baseType = String.class;
    String typeFieldName = "type";

    RuntimeTypeAdapterFactory<String> factory = RuntimeTypeAdapterFactory.of(baseType, typeFieldName);

    assertNotNull(factory);
    // Use reflection to verify private fields
    try {
      var baseTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("baseType");
      baseTypeField.setAccessible(true);
      var actualBaseType = baseTypeField.get(factory);
      assertEquals(baseType, actualBaseType);

      var typeFieldNameField = RuntimeTypeAdapterFactory.class.getDeclaredField("typeFieldName");
      typeFieldNameField.setAccessible(true);
      var actualTypeFieldName = typeFieldNameField.get(factory);
      assertEquals(typeFieldName, actualTypeFieldName);

      var maintainTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("maintainType");
      maintainTypeField.setAccessible(true);
      var actualMaintainType = maintainTypeField.get(factory);
      assertEquals(false, actualMaintainType);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failure: " + e.getMessage());
    }
  }
}
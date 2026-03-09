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

class RuntimeTypeAdapterFactory_43_5Test {

  @Test
    @Timeout(8000)
  void testOfWithAllParameters() {
    Class<String> baseType = String.class;
    String typeFieldName = "typeField";
    boolean maintainType = true;

    RuntimeTypeAdapterFactory<String> factory = RuntimeTypeAdapterFactory.of(baseType, typeFieldName, maintainType);

    assertNotNull(factory);
    // Use reflection to verify private fields
    try {
      var baseTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("baseType");
      baseTypeField.setAccessible(true);
      Object baseTypeValue = baseTypeField.get(factory);
      assertEquals(baseType, baseTypeValue);

      var typeFieldNameField = RuntimeTypeAdapterFactory.class.getDeclaredField("typeFieldName");
      typeFieldNameField.setAccessible(true);
      Object typeFieldNameValue = typeFieldNameField.get(factory);
      assertEquals(typeFieldName, typeFieldNameValue);

      var maintainTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("maintainType");
      maintainTypeField.setAccessible(true);
      Object maintainTypeValue = maintainTypeField.get(factory);
      assertEquals(maintainType, maintainTypeValue);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}
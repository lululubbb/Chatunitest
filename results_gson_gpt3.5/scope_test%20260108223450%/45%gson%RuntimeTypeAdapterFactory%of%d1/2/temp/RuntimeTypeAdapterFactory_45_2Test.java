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

class RuntimeTypeAdapterFactory_45_2Test {

  @Test
    @Timeout(8000)
  void testOf_withBaseType_createsInstanceWithDefaults() {
    Class<Number> baseType = Number.class;
    RuntimeTypeAdapterFactory<Number> factory = RuntimeTypeAdapterFactory.of(baseType);

    assertNotNull(factory);
    // Using reflection to verify private fields
    try {
      var baseTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("baseType");
      baseTypeField.setAccessible(true);
      Object baseTypeValue = baseTypeField.get(factory);
      assertEquals(baseType, baseTypeValue);

      var typeFieldNameField = RuntimeTypeAdapterFactory.class.getDeclaredField("typeFieldName");
      typeFieldNameField.setAccessible(true);
      Object typeFieldNameValue = typeFieldNameField.get(factory);
      assertEquals("type", typeFieldNameValue);

      var maintainTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("maintainType");
      maintainTypeField.setAccessible(true);
      boolean maintainTypeValue = maintainTypeField.getBoolean(factory);
      assertFalse(maintainTypeValue);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failure: " + e.getMessage());
    }
  }
}
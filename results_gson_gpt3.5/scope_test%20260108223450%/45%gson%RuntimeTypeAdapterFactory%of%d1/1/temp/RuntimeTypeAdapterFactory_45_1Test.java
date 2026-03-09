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

public class RuntimeTypeAdapterFactory_45_1Test {

  @Test
    @Timeout(8000)
  void testOfWithBaseType() {
    RuntimeTypeAdapterFactory<Object> factory = RuntimeTypeAdapterFactory.of(Object.class);
    assertNotNull(factory);
    // Using reflection to verify private fields
    try {
      var baseTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("baseType");
      baseTypeField.setAccessible(true);
      assertEquals(Object.class, baseTypeField.get(factory));

      var typeFieldNameField = RuntimeTypeAdapterFactory.class.getDeclaredField("typeFieldName");
      typeFieldNameField.setAccessible(true);
      assertEquals("type", typeFieldNameField.get(factory));

      var maintainTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("maintainType");
      maintainTypeField.setAccessible(true);
      assertFalse(maintainTypeField.getBoolean(factory));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection access failed: " + e.getMessage());
    }
  }
}
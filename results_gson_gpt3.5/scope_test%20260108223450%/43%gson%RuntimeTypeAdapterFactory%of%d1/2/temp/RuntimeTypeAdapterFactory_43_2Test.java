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

public class RuntimeTypeAdapterFactory_43_2Test {

  @Test
    @Timeout(8000)
  public void testOf_withBaseTypeAndTypeFieldNameAndMaintainType() {
    Class<String> baseType = String.class;
    String typeFieldName = "typeField";
    boolean maintainType = true;

    RuntimeTypeAdapterFactory<String> factory =
        RuntimeTypeAdapterFactory.of(baseType, typeFieldName, maintainType);

    assertNotNull(factory);
    // Using reflection to verify private fields
    try {
      var baseTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("baseType");
      baseTypeField.setAccessible(true);
      assertEquals(baseType, baseTypeField.get(factory));

      var typeFieldNameField = RuntimeTypeAdapterFactory.class.getDeclaredField("typeFieldName");
      typeFieldNameField.setAccessible(true);
      assertEquals(typeFieldName, typeFieldNameField.get(factory));

      var maintainTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("maintainType");
      maintainTypeField.setAccessible(true);
      assertEquals(maintainType, maintainTypeField.getBoolean(factory));

      var recognizeSubtypesField = RuntimeTypeAdapterFactory.class.getDeclaredField("recognizeSubtypes");
      recognizeSubtypesField.setAccessible(true);
      assertFalse(recognizeSubtypesField.getBoolean(factory));

      var labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
      labelToSubtypeField.setAccessible(true);
      assertTrue(((java.util.Map<?, ?>) labelToSubtypeField.get(factory)).isEmpty());

      var subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
      subtypeToLabelField.setAccessible(true);
      assertTrue(((java.util.Map<?, ?>) subtypeToLabelField.get(factory)).isEmpty());

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}
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

public class RuntimeTypeAdapterFactory_43_1Test {

  @Test
    @Timeout(8000)
  public void testOf_WithBaseTypeAndTypeFieldNameAndMaintainType() {
    Class<String> baseType = String.class;
    String typeFieldName = "typeField";
    boolean maintainType = true;

    RuntimeTypeAdapterFactory<String> factory = RuntimeTypeAdapterFactory.of(baseType, typeFieldName, maintainType);

    assertNotNull(factory);
    // Using reflection to check private fields
    try {
      var baseTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("baseType");
      baseTypeField.setAccessible(true);
      var baseTypeValue = baseTypeField.get(factory);
      assertEquals(baseType, baseTypeValue);

      var typeFieldNameField = RuntimeTypeAdapterFactory.class.getDeclaredField("typeFieldName");
      typeFieldNameField.setAccessible(true);
      var typeFieldNameValue = typeFieldNameField.get(factory);
      assertEquals(typeFieldName, typeFieldNameValue);

      var maintainTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("maintainType");
      maintainTypeField.setAccessible(true);
      var maintainTypeValue = maintainTypeField.get(factory);
      assertEquals(maintainType, maintainTypeValue);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testOf_WithBaseTypeAndTypeFieldName() {
    Class<Integer> baseType = Integer.class;
    String typeFieldName = "myType";

    RuntimeTypeAdapterFactory<Integer> factory = RuntimeTypeAdapterFactory.of(baseType, typeFieldName);

    assertNotNull(factory);
    try {
      var baseTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("baseType");
      baseTypeField.setAccessible(true);
      assertEquals(baseType, baseTypeField.get(factory));

      var typeFieldNameField = RuntimeTypeAdapterFactory.class.getDeclaredField("typeFieldName");
      typeFieldNameField.setAccessible(true);
      assertEquals(typeFieldName, typeFieldNameField.get(factory));

      var maintainTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("maintainType");
      maintainTypeField.setAccessible(true);
      assertFalse((Boolean) maintainTypeField.get(factory));

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testOf_WithBaseTypeOnly() {
    Class<Double> baseType = Double.class;

    RuntimeTypeAdapterFactory<Double> factory = RuntimeTypeAdapterFactory.of(baseType);

    assertNotNull(factory);
    try {
      var baseTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("baseType");
      baseTypeField.setAccessible(true);
      assertEquals(baseType, baseTypeField.get(factory));

      var typeFieldNameField = RuntimeTypeAdapterFactory.class.getDeclaredField("typeFieldName");
      typeFieldNameField.setAccessible(true);
      assertEquals("type", typeFieldNameField.get(factory));

      var maintainTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("maintainType");
      maintainTypeField.setAccessible(true);
      assertFalse((Boolean) maintainTypeField.get(factory));

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}